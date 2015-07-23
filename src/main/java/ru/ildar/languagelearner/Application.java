package ru.ildar.languagelearner;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class Application extends SpringBootServletInitializer
{
}
