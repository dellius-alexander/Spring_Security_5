package com.example.spring_security_web_service.Security.Config;


import com.example.spring_security_web_service.Security.Custom.CustomApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;


@EnableWebMvc
@Configuration("WebMvcConfig")
@ComponentScan(basePackages = {
        "com.example.spring_security_web_service.User",
        "com.example.spring_security_web_service.WebApp",
        "com.example.spring_security_web_service.Security"
})
public class WebMvcConfig implements WebMvcConfigurer {
    private final static Logger log = LoggerFactory.getLogger(WebMvcConfig.class);

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry
                .jsp()
                .prefix("/WEB-INF/views/")
                .suffix(".jsp");
        log.info("\nViewResolverRegistry: {}",registry.toString());
    }

}