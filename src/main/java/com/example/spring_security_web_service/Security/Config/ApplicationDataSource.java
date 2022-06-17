package com.example.spring_security_web_service.Security.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLTimeoutException;

@Configuration("ApplicationConfig")
@PropertySource("classpath:mysqldb.properties")
public class ApplicationDataSource {
    private static final Logger log = LoggerFactory.getLogger(ApplicationDataSource.class);

    @Autowired
    private Environment env;
    /**
     * <p>Gets {@linkplain DataSource} data from environmental variables.</p>
     * @return the {@linkplain DataSource}
     */
    @Bean
    public DataSource getDataSource(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("mysql.driver"));
        dataSource.setUrl(env.getProperty("mysql.jdbcUrl"));
        dataSource.setUsername(env.getProperty("mysql.username"));
        dataSource.setPassword(env.getProperty("mysql.password"));
        log.info("Datasource: {}", dataSource);
        return dataSource;
    }
}
