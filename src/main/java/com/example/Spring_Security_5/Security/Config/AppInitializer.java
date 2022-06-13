package com.example.Spring_Security_5.Security.Config;

import com.example.Spring_Security_5.Security.Custom.*;
import com.example.Spring_Security_5.Security.Secret.Secret;
import com.example.Spring_Security_5.Security.Custom.CustomUserDetailsService;
import com.example.Spring_Security_5.Security.UserDetails.UserLoginDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.springframework.context.annotation.Configuration;

@Configuration("AppInitializer")
@Component
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
                ApplicationConfig.class,
                SpringSecurityInitializer.class,
                WebApplicationInitializer.class,
                WebSecurityConfig.class,
                CustomApplicationContext.class,
                CustomAuthenticationFilter.class,
                CustomAuthenticationProvider.class,
                CustomPasswordEncoder.class,
                CustomSecurityContext.class,
                CustomSecurityContextHolder.class,
                CustomSecurityContextStrategy.class,
                CustomUserDetailsService.class,
                Secret.class,
                UserLoginDetails.class
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
        return new String[]{"/"};
    }

}