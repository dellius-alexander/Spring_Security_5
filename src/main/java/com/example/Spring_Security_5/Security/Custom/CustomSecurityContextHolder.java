package com.example.Spring_Security_5.Security.Custom;

import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
@Data
public class CustomSecurityContextHolder extends SecurityContextHolder {
    // nothing to do
}
