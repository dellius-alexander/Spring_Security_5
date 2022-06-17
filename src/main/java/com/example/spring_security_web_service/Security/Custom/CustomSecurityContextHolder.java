package com.example.spring_security_web_service.Security.Custom;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration("CustomSecurityContextHolder")
public class CustomSecurityContextHolder extends SecurityContextHolder {
    // nothing to do
}
