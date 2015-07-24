package ru.ildar.languagelearner.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.extras.tiles2.dialect.TilesDialect;
import org.thymeleaf.extras.tiles2.spring4.web.configurer.ThymeleafTilesConfigurer;
import org.thymeleaf.extras.tiles2.spring4.web.view.FlowAjaxThymeleafTilesView;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.AjaxThymeleafViewResolver;

/**
 * Configuration class for Spring MVC */
@Configuration
@EntityScan(basePackages = "ru.ildar.languagelearner.database.domain")
@EnableJpaRepositories(basePackages = "ru.ildar.languagelearner.database.dao")
public class WebConfig extends WebMvcConfigurerAdapter
{
    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    /**
     * Thymeleaf view resolver that supports Spring WebFlow and Apache Tiles */
    @Bean
    public AjaxThymeleafViewResolver thymeleafViewResolver()
    {
        springTemplateEngine.addDialect(new TilesDialect());

        AjaxThymeleafViewResolver viewResolver = new AjaxThymeleafViewResolver();
        viewResolver.setViewClass(FlowAjaxThymeleafTilesView.class);
        viewResolver.setTemplateEngine(springTemplateEngine);
        return viewResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("/scripts/**").addResourceLocations("classpath:/js/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        registry.addViewController("/auth/login").setViewName("auth/login");
    }

    @Bean
    public ThymeleafTilesConfigurer thymeleafTilesConfigurer()
    {
        ThymeleafTilesConfigurer configurer = new ThymeleafTilesConfigurer();
        configurer.setDefinitions("classpath:tiles-defs.xml");
        return configurer;
    }
}
