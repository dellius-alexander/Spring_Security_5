package com.example.spring_security_web_service.Security.Custom;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
@Configuration("CustomAuthenticationFilter")
@Component
public class CustomAuthenticationFilter extends AuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private AuthenticationConverter authenticationConverter;
    private AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver;



    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationConverter authenticationConverter) {
        super(authenticationManager, authenticationConverter);
        this.authenticationManager = authenticationManager;
        this.authenticationConverter = authenticationConverter;
    }
    @Autowired
    public CustomAuthenticationFilter(AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver, AuthenticationConverter authenticationConverter) {
        super(authenticationManagerResolver, authenticationConverter);
        this.authenticationManagerResolver = authenticationManagerResolver;
        this.authenticationConverter = authenticationConverter;
    }

    @Override
    public String toString(){
        return super.toString();
    }
}
