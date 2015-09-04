package ru.ildar.languagelearner.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig
{
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Bean
    public DataSource dataSource()
    {
        BasicDataSource dataSource = new BasicDataSource();

        if(url.equals("OPENSHIFT_POSTGRESQL_DB_URL"))
        {
            url = System.getenv("OPENSHIFT_POSTGRESQL_DB_URL");
        }
        if(username.equals("OPENSHIFT_POSTGRESQL_DB_USERNAME"))
        {
            username = System.getenv("OPENSHIFT_POSTGRESQL_DB_USERNAME");
        }
        if(password.equals("OPENSHIFT_POSTGRESQL_DB_PASSWORD"))
        {
            password = System.getenv("OPENSHIFT_POSTGRESQL_DB_PASSWORD");
        }

        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }
}
