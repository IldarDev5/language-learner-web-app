package ru.ildar.languagelearner.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.ildar.languagelearner.config.WebConfig;
import ru.ildar.languagelearner.service.AppUserService;

import static org.mockito.Mockito.mock;

@Configuration
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@ComponentScan(basePackageClasses = {WebConfig.class},
    basePackages = "ru.ildar.languagelearner.controller")
public class TestConfig
{
    @Bean
    public AppUserService appUserService()
    {
        return mock(AppUserService.class);
    }

    @Bean
    public UserDetailsService userDetailsService()
    {
        return mock(UserDetailsService.class);
    }
}
