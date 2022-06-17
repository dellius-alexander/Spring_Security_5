package com.example.spring_security_web_service.Security.Custom;


import com.example.spring_security_web_service.Security.Secret.Secret;
import com.example.spring_security_web_service.User.User;
import com.example.spring_security_web_service.User.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.security.auth.Subject;
import java.util.Collection;
import java.util.Objects;

@Configuration("CustomAuthenticationProvider")
public class CustomAuthenticationProvider implements AuthenticationProvider, Authentication {
    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationProvider.class);
    private final UserService userService;
    private Authentication authentication = null;
    private User user;
    private boolean isAuthenticated = false;
    private Collection<GrantedAuthority> authorities;
    @Autowired
    public CustomAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    public CustomAuthenticationProvider(UserService userService, Authentication authentication, User user, boolean isAuthenticated, Collection<GrantedAuthority> authorities) {
        this.userService = userService;
        this.authentication = authentication;
        this.user = user;
        this.isAuthenticated = isAuthenticated;
        this.authorities = authorities;
    }

    /**
     * Performs authentication with the same contract as
     * {@link AuthenticationManager#authenticate(Authentication)}
     * .
     *
     * @param authentication the authentication request object.
     * @return a fully authenticated object including credentials. May return
     * <code>null</code> if the <code>AuthenticationProvider</code> is unable to support
     * authentication of the passed <code>Authentication</code> object. In such a case,
     * the next <code>AuthenticationProvider</code> that supports the presented
     * <code>Authentication</code> class will be tried.
     * @throws AuthenticationException if authentication fails.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Secret password = null;
        try{
            String username = authentication.getName();
            password = (Secret) authentication.getCredentials();
            user = (User) userService.getByUsername(username).getBody();

            if (user != null &&
                    user.getName().equals(username) ||
                    user.getUsername().equals(username) &&
                    Secret.matches(user.getPasswordClass(), password)) {
                log.info("\n{\nusername: {},\npassword: {}\n}\n",username, password);
                log.info("USER: {}",user);

                this.authentication = new Authentication() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        return user.getAuthorities();
                    }

                    @Override
                    public Object getCredentials() {
                        return user.getPassword();
                    }

                    @Override
                    public Object getDetails() {
                        return user;
                    }

                    @Override
                    public Object getPrincipal() {
                        return user.getUsername();
                    }

                    @Override
                    public boolean isAuthenticated() {
                        return true;
                    }

                    @Override
                    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
                        log.info("isAuthenticated: {}", isAuthenticated);}
                    @Override
                    public String getName() {
                        return user.getName();
                    }
                };
                SecurityContextHolder.setContext(new CustomSecurityContext(this.authentication));
            }
            else {
                throw new BadCredentialsException("Authentication failed for username: " + username);
            }
        }catch (Exception e){
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return this.authentication;
    }

    /**
     * Returns <code>true</code> if this <Code>AuthenticationProvider</code> supports the
     * indicated <Code>Authentication</code> object.
     * <p>
     * Returning <code>true</code> does not guarantee an
     * <code>AuthenticationProvider</code> will be able to authenticate the presented
     * instance of the <code>Authentication</code> class. It simply indicates it can
     * support closer evaluation of it. An <code>AuthenticationProvider</code> can still
     * return <code>null</code> from the {@link #authenticate(Authentication)} method to
     * indicate another <code>AuthenticationProvider</code> should be tried.
     * </p>
     * <p>
     * Selection of an <code>AuthenticationProvider</code> capable of performing
     * authentication is conducted at runtime the <code>ProviderManager</code>.
     * </p>
     *
     * @param authentication
     * @return <code>true</code> if the implementation can more closely evaluate the
     * <code>Authentication</code> class presented
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    /**
     * Set by an <code>AuthenticationManager</code> to indicate the authorities that the
     * principal has been granted. Note that classes should not rely on this value as
     * being valid unless it has been set by a trusted <code>AuthenticationManager</code>.
     * <p>
     * Implementations should ensure that modifications to the returned collection array
     * do not affect the state of the Authentication object, or use an unmodifiable
     * instance.
     * </p>
     *
     * @return the authorities granted to the principal, or an empty collection if the
     * token has not been authenticated. Never null.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    /**
     * The credentials that prove the principal is correct. This is usually a password,
     * but could be anything relevant to the <code>AuthenticationManager</code>. Callers
     * are expected to populate the credentials.
     *
     * @return the credentials that prove the identity of the <code>Principal</code>
     */
    @Override
    public Object getCredentials() {
        return user.getPassword();
    }

    /**
     * Stores additional details about the authentication request. These might be an IP
     * address, certificate serial number etc.
     *
     * @return additional details about the authentication request, or <code>null</code>
     * if not used
     */
    @Override
    public Object getDetails() {
        return user;
    }

    /**
     * The identity of the principal being authenticated. In the case of an authentication
     * request with username and password, this would be the username. Callers are
     * expected to populate the principal for an authentication request.
     * <p>
     * The <tt>AuthenticationManager</tt> implementation will often return an
     * <tt>Authentication</tt> containing richer information as the principal for use by
     * the application. Many of the authentication providers will create a
     * {@code UserDetails} object as the principal.
     *
     * @return the <code>Principal</code> being authenticated or the authenticated
     * principal after authentication.
     */
    @Override
    public Object getPrincipal() {
        return user.getUsername();
    }

    /**
     * Used to indicate to {@code AbstractSecurityInterceptor} whether it should present
     * the authentication token to the <code>AuthenticationManager</code>. Typically an
     * <code>AuthenticationManager</code> (or, more often, one of its
     * <code>AuthenticationProvider</code>s) will return an immutable authentication token
     * after successful authentication, in which case that token can safely return
     * <code>true</code> to this method. Returning <code>true</code> will improve
     * performance, as calling the <code>AuthenticationManager</code> for every request
     * will no longer be necessary.
     * <p>
     * For security reasons, implementations of this interface should be very careful
     * about returning <code>true</code> from this method unless they are either
     * immutable, or have some way of ensuring the properties have not been changed since
     * original creation.
     *
     * @return true if the token has been authenticated and the
     * <code>AbstractSecurityInterceptor</code> does not need to present the token to the
     * <code>AuthenticationManager</code> again for re-authentication.
     */
    @Override
    public boolean isAuthenticated() {
        return this.isAuthenticated;
    }

    /**
     * See {@link #isAuthenticated()} for a full description.
     * <p>
     * Implementations should <b>always</b> allow this method to be called with a
     * <code>false</code> parameter, as this is used by various classes to specify the
     * authentication token should not be trusted. If an implementation wishes to reject
     * an invocation with a <code>true</code> parameter (which would indicate the
     * authentication token is trusted - a potential security risk) the implementation
     * should throw an {@link IllegalArgumentException}.
     *
     * @param isAuthenticated <code>true</code> if the token should be trusted (which may
     *                        result in an exception) or <code>false</code> if the token should not be trusted
     * @throws IllegalArgumentException if an attempt to make the authentication token
     *                                  trusted (by passing <code>true</code> as the argument) is rejected due to the
     *                                  implementation being immutable or implementing its own alternative approach to
     *                                  {@link #isAuthenticated()}
     */
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    /**
     * Returns the name of this principal.
     *
     * @return the name of this principal.
     */
    @Override
    public String getName() {
        return user.getName();
    }

    /**
     * Returns true if the specified subject is implied by this principal.
     *
     * @param subject the {@code Subject}
     * @return true if {@code subject} is non-null and is
     * implied by this principal, or false otherwise.
     * @implSpec The default implementation of this method returns true if
     * {@code subject} is non-null and contains at least one principal that
     * is equal to this principal.
     *
     * <p>Subclasses may override this with a different implementation, if
     * necessary.
     * @since 1.8
     */
    @Override
    public boolean implies(Subject subject) {
        return Authentication.super.implies(subject);
    }

    public UserService getUserService() {
        return this.userService;
    }

    public Authentication getAuthentication() {
        return this.authentication;
    }

    public User getUser() {
        return this.user;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CustomAuthenticationProvider)) return false;
        final CustomAuthenticationProvider other = (CustomAuthenticationProvider) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$userService = this.getUserService();
        final Object other$userService = other.getUserService();
        if (!Objects.equals(this$userService, other$userService))
            return false;
        final Object this$authentication = this.getAuthentication();
        final Object other$authentication = other.getAuthentication();
        if (!Objects.equals(this$authentication, other$authentication))
            return false;
        final Object this$user = this.getUser();
        final Object other$user = other.getUser();
        if (!Objects.equals(this$user, other$user)) return false;
        if (this.isAuthenticated() != other.isAuthenticated()) return false;
        final Object this$authorities = this.getAuthorities();
        final Object other$authorities = other.getAuthorities();
        if (!Objects.equals(this$authorities, other$authorities))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CustomAuthenticationProvider;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $userService = this.getUserService();
        result = result * PRIME + ($userService == null ? 43 : $userService.hashCode());
        final Object $authentication = this.getAuthentication();
        result = result * PRIME + ($authentication == null ? 43 : $authentication.hashCode());
        final Object $user = this.getUser();
        result = result * PRIME + ($user == null ? 43 : $user.hashCode());
        result = result * PRIME + (this.isAuthenticated() ? 79 : 97);
        final Object $authorities = this.getAuthorities();
        result = result * PRIME + ($authorities == null ? 43 : $authorities.hashCode());
        return result;
    }

    public String toString() {
        return  "{" +
                "\n\t\"userService\":" + this.getUserService() + "," +
                "\n\t\"authentication\":" + this.getAuthentication() + "," +
                "\n\t\"user\":" + this.getUser() + "," +
                "\n\t\"isAuthenticated\":" + this.isAuthenticated() + "," +
                "\n\t\"authorities\":" + this.getAuthorities() +
                "\n}";
    }
}
