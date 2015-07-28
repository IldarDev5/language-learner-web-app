package ru.ildar.languagelearner;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
@EntityScan(basePackages = "ru.ildar.languagelearner.database.domain")
@EnableJpaRepositories(basePackages = "ru.ildar.languagelearner.database.dao")
public class Application extends SpringBootServletInitializer
{
}
