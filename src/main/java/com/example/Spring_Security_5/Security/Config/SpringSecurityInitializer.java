package com.example.Spring_Security_5.Security.Config;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.stereotype.Component;

@Configuration("SpringSecurityInitializer")
@Component
/**
 * Spring security is implemented using DelegatingFilterProxy. To register it, with
 * spring container in Java configuration, you shall use AbstractSecurityWebApplicationInitializer.
 * Now when Spring detects the instance of this class during application startup, it will register
 * the DelegatingFilterProxy to use the springSecurityFilterChain before any other registered Filter.
 * It also registers a ContextLoaderListener.
 */
public class SpringSecurityInitializer extends AbstractSecurityWebApplicationInitializer {
    // no code needed
}