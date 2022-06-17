package com.example.spring_security_web_service.Security.Config;

import com.example.spring_security_web_service.spring_security_web_service;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;

@Configuration("ServletInitializer")
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(spring_security_web_service.class);
    }

}