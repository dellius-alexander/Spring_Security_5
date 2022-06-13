package com.example.Spring_Security_5.Security.Custom;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;

public class CustomSecurityContextStrategy implements SecurityContextHolderStrategy {
    private CustomSecurityContext customSecurityContext;
    /**
     * Clears the current context.
     */
    @Override
    public void clearContext() {
    }

    /**
     * Obtains the current context.
     *
     * @return a context (never <code>null</code> - create a default implementation if
     * necessary)
     */
    @Override
    public SecurityContext getContext() {
        return this.customSecurityContext;
    }

    /**
     * Sets the current context.
     *
     * @param context to the new argument (should never be <code>null</code>, although
     *                implementations must check if <code>null</code> has been passed and throw an
     *                <code>IllegalArgumentException</code> in such cases)
     */
    @Override
    public void setContext(SecurityContext context) {
        this.customSecurityContext = (CustomSecurityContext) context;
    }

    /**
     * Creates a new, empty context implementation, for use by
     * <tt>SecurityContextRepository</tt> implementations, when creating a new context for
     * the first time.
     *
     * @return the empty context.
     */
    @Override
    public SecurityContext createEmptyContext() {
        return new CustomSecurityContext();
    }
}
