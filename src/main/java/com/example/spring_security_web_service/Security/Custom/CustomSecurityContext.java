package com.example.spring_security_web_service.Security.Custom;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import java.util.Objects;

@Configuration("CustomSecurityContext")
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

    public Authentication getAuth() {
        return this.auth;
    }

    public void setAuth(Authentication auth) {
        this.auth = auth;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CustomSecurityContext)) return false;
        final CustomSecurityContext other = (CustomSecurityContext) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$auth = this.getAuth();
        final Object other$auth = other.getAuth();
        if (!Objects.equals(this$auth, other$auth)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CustomSecurityContext;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $auth = this.getAuth();
        result = result * PRIME + ($auth == null ? 43 : $auth.hashCode());
        return result;
    }

    public String toString() {
        return "CustomSecurityContext{\"auth\":" + this.getAuth() + "}";
    }
}
