package com.example.Spring_Security_5.Security.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.stereotype.Component;

@Configuration("WebApplicationInitializer")
@Component
public class WebApplicationInitializer
        extends AbstractSecurityWebApplicationInitializer {
    // Nothing to be added here
}