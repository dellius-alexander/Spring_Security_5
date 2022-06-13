package com.example.Spring_Security_5.Security.Custom;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import javax.servlet.http.HttpServletRequest;

public class CustomAuthenticationFilter extends AuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private AuthenticationConverter authenticationConverter;
    private AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver;


    @Autowired
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
