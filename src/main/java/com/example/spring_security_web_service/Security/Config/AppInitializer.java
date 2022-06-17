package com.example.spring_security_web_service.Security.Config;

import com.example.spring_security_web_service.Security.Custom.*;
import com.example.spring_security_web_service.Security.Custom.CustomUserDetailsService;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.springframework.context.annotation.Configuration;

@Configuration("AppInitializer")
public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     *
     * @return root configuration classes: [HibernateConfig.class,
     *                 WebSecurityConfig.class]
     */
    /**
     * Defines root configuration classes
     * @return list of configuration classes: AppDataSource.class,
     *                 WebSecurityConfig.class,
     *                 CustomAuthenticationProvider.class,
     *                 CustomAccessDenialHandler.class,
     *                 OpenApiConfig.class,
     *                 SpringSecurityInitializer.class,
     *                 WebApplicationInitializer.class,
     *                 CustomAuthenticationProvider.class,
     *                 CustomPasswordEncoder.class,
     *                 CustomUserDetailsService.class,
     *                 UserLoginDetails.class
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{
                ApplicationDataSource.class,
                CustomApplicationContext.class,
                CustomAuthenticationProvider.class,
                CustomPasswordEncoder.class,
                CustomSecurityContext.class,
                CustomSecurityContextHolder.class,
                CustomSecurityContextStrategy.class,
                CustomUserDetailsService.class,
                ProblemHttpConfigurer.class,
                SecurityProblemSupport.class,
                ServletInitializer.class,
                SpringSecurityInitializer.class,
                WebApplicationInitializer.class,
                WebSecurityConfig.class
        };
    }

    /**
     * Initialize servlet configuration files.  This includes but not
     * limited to: thymeleaf viewResolvers, Url paths and endpoints.
     * @return servlet configuration classes: WebMvcConfig.class
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{
                WebMvcConfig.class
        };
    }

    /**
     * Initialize root servlet mapping
     * @return "/"
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{
                "/",
                "/saml/SSO/"
        };
    }

}