package com.example.Spring_Security_5.Security.Custom;

import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

@Data
public class CustomSecurityContext implements SecurityContext {
    private Authentication auth;

    public CustomSecurityContext(){this.auth = null;}
    public CustomSecurityContext(Authentication authentication) { this.auth = authentication;}

    /**
     * Obtains the currently authenticated principal, or an authentication request token.
     *
     * @return the <code>Authentication</code> or <code>null</code> if no authentication
     * information is available
     */
    @Override
    public Authentication getAuthentication() {
        return this.auth;
    }

    /**
     * Changes the currently authenticated principal, or removes the authentication
     * information.
     *
     * @param authentication the new <code>Authentication</code> token, or
     *                       <code>null</code> if no further authentication information should be stored
     */
    @Override
    public void setAuthentication(Authentication authentication) {
        this.auth = authentication;
    }
}
