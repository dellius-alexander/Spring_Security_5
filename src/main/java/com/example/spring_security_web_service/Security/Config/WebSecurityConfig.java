package com.example.spring_security_web_service.Security.Config;

import com.example.spring_security_web_service.Security.Custom.CustomAuthenticationProvider;
import com.example.spring_security_web_service.Security.Custom.CustomPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.saml2.Saml2RelyingPartyAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.saml2.RelyingPartyRegistrationsBeanDefinitionParser;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.sql.DataSource;

import java.util.Collection;

import static org.springframework.security.extensions.saml2.config.SAMLConfigurer.saml;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true)
@ComponentScan(basePackages = {
        "com.example.spring_security_web_service",
})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Autowired
    CustomAuthenticationProvider customAuthenticationProvider;
    @Autowired
    CustomPasswordEncoder passwordEncoder;
    @Autowired
    ApplicationDataSource datasource;

    RelyingPartyRegistration relyingPartyRegistration;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-ui/**",
            "/v3/api-docs",
            "/webjars/**",
            "/swagger-ui",

    };

    private static final String[] URI_LIST = new String[] {
            "/**",
            "/saml/**",
            "/error",
            "/index"
    };

    @Value("${security.saml2.metadata-url}")
    String metadataUrl;

    /**
     * Override this method to configure the {@link HttpSecurity}. Typically subclasses
     * should not invoke this method by calling super as it may override their
     * configuration. The default configuration is:
     *
     * <pre>
     * http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
     * </pre>
     *
     * Any endpoint that requires defense against common vulnerabilities can be specified
     * here, including public ones. See {@link HttpSecurity#authorizeRequests} and the
     * `permitAll()` authorization rule for more details on public endpoints.
     * @param http the {@link HttpSecurity} to modify
     * @throws Exception if an error occurs
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("\nInitializing Http handler...");
//        http.authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll();
        // Apply options to our http client:
        http
                    .authorizeRequests()
                    .antMatchers(URI_LIST)
                    .permitAll()
                    .anyRequest()
                    .authenticated()
                .and()
                    .saml2Login()
                .and()
                    .apply(saml())
                    .serviceProvider()
                    .keyStore()
                    .storeFilePath("saml/keystore.jks")
                    .password("secret")
                    .keyname("spring")
                    .keyPassword("secret")
                .and()
                    .protocol("https")
                    .hostname("localhost:8443")
                    .basePath("/")
                .and()
                    .identityProvider()
                    .metadataFilePath("classpath:saml/saml.xml")
                    .discoveryEnabled(true)
                .and();

//        http
//                .sessionManagement(
//                        session -> session
//                                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
//                                .invalidSessionUrl("/error")
//                                .maximumSessions(1))
//                .sessionManagement().sessionFixation().migrateSession()
//                .and()
//                .logout(
//                        logout -> logout
//                                .deleteCookies("JSESSIONID*"))
//                .exceptionHandling(
//                        exceptionHandling -> exceptionHandling
//                                .accessDeniedPage("/error"));

        // Cross-Site Request Forgery (CSRF) is an attack that
        // forces an end user to execute unwanted actions on a
        // web application in which they’re currently authenticated.
        // http.csrf().disable();
    }
    /**
     * Global Configuration.
     * Used by the default implementation of authenticationManager() to attempt to obtain an AuthenticationManager. If overridden, the AuthenticationManagerBuilder should be used to specify the AuthenticationManager.
     * The authenticationManagerBean() method can be used to expose the resulting AuthenticationManager as a Bean. The userDetailsServiceBean() can be used to expose the last populated UserDetailsService that is created with the AuthenticationManagerBuilder as a Bean. The UserDetailsService will also automatically be populated on HttpSecurity.getSharedObject(Class) for use with other SecurityContextConfigurer (i.e. RememberMeConfigurer )
     * For example, the following configuration could be used to register in memory authentication that exposes an in memory UserDetailsService:
     *              @Override
     *       protected void configure(AuthenticationManagerBuilder auth) {
     * 	   	auth
     * 	   	// enable in memory based authentication with a user named
     * 	   	// "user" and "admin"
     * 	   	.inMemoryAuthentication().withUser("user").password("password").roles("USER").and()
     * 	   			.withUser("admin").password("password").roles("USER", "ADMIN");
     *       }
     *
     * 	   // Expose the UserDetailsService as a Bean
     *       @Bean
     *       @Override
     *       public UserDetailsService userDetailsServiceBean() throws Exception {
     * 	   	return super.userDetailsServiceBean();
     *       }
     *
     * @param  auth – the AuthenticationManagerBuilder to use
     * @throws Exception
     */
    @Autowired
    public void configGlobalAuthManager(AuthenticationManagerBuilder auth)
            throws Exception {
        // Custom authentication provider - Order 1
        auth.authenticationProvider(customAuthenticationProvider);
        // Custom authentication provider - Order 2
        auth.jdbcAuthentication()
                .dataSource(dataSource())
                    .usersByUsernameQuery("select email, password from user where email = ?")
                        .authoritiesByUsernameQuery("select r.name\n" +
                                "from role r join user_roles ur on r.role_id = ur.role_id\n" +
                                "where ur.user_id in (select u.user_id from user u where u.email = ?)")
                            .passwordEncoder(passwordEncoder);
        // Built-in authentication provider - Order 3
        auth.inMemoryAuthentication()
                .withUser("john@gmail.com")
                // {noop} makes sure that the password encoder does not apply any encryption algorithms to the password
                .password("{noop}password")
                .credentialsExpired(false)
                .accountExpired(false)
                .accountLocked(false)
                .roles("ADMIN")
            .and()
                .withUser("admin@gmail.com")
                .password("{noop}password")
                .credentialsExpired(false)
                .accountExpired(false)
                .accountLocked(false)
                .roles("USER");
    }
    /**
     * Used by the default implementation of authenticationManager() to attempt to obtain an AuthenticationManager. If overridden, the AuthenticationManagerBuilder should be used to specify the AuthenticationManager.
     * The authenticationManagerBean() method can be used to expose the resulting AuthenticationManager as a Bean. The userDetailsServiceBean() can be used to expose the last populated UserDetailsService that is created with the AuthenticationManagerBuilder as a Bean. The UserDetailsService will also automatically be populated on HttpSecurity.getSharedObject(Class) for use with other SecurityContextConfigurer (i.e. RememberMeConfigurer )
     * For example, the following configuration could be used to register in memory authentication that exposes an in memory UserDetailsService:
     *              @Override
     *       protected void configure(AuthenticationManagerBuilder auth) {
     * 	   	auth
     * 	   	// enable in memory based authentication with a user named
     * 	   	// "user" and "admin"
     * 	   	.inMemoryAuthentication().withUser("user").password("password").roles("USER").and()
     * 	   			.withUser("admin").password("password").roles("USER", "ADMIN");
     *       }
     *
     * 	   // Expose the UserDetailsService as a Bean
     *       @Bean
     *       @Override
     *       public UserDetailsService userDetailsServiceBean() throws Exception {
     * 	   	return super.userDetailsServiceBean();
     *       }
     *
     * @param  auth – the {@link AuthenticationManagerBuilder} to use
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        // Custom authentication provider - Order 1
        auth.authenticationProvider(customAuthenticationProvider);
        // Custom authentication provider - Order 2
        auth.jdbcAuthentication()
            .dataSource(dataSource())
            .usersByUsernameQuery("select email, password from user where email = ?")
            .authoritiesByUsernameQuery("select r.name\n" +
                    "from role r join user_roles ur on r.role_id = ur.role_id\n" +
                    "where ur.user_id in (select u.user_id from user u where u.email = ?)")
            .passwordEncoder(passwordEncoder);
        // Built-in authentication provider - Order 3
        auth.inMemoryAuthentication()
                .withUser("admin")
                // {noop} makes sure that the password encoder does not apply any encryption algorithms to the password
                .password("{noop}password")
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password("{noop}password")
                .credentialsExpired(false)
                .accountExpired(false)
                .accountLocked(false)
                .roles("USER");
    }

    /**
     * Filter chain to configure session management, logout and
     * error handling.
     * @param http {@link HttpSecurity}
     * @return {@link SecurityFilterChain}
     */
    @Bean(name = "springSecurityFilterChain")
    public SecurityFilterChain filterChain(HttpSecurity http){
        try{
            http
                    .sessionManagement(
                            session -> session
                                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                                    .invalidSessionUrl("/saml/SSO/error")
                                    .maximumSessions(1))
                    .sessionManagement().sessionFixation().migrateSession()
                .and()
                    .logout(
                            logout -> logout
                                    .deleteCookies("JSESSIONID"))
                    .exceptionHandling(
                            exceptionHandling -> exceptionHandling
                                    .accessDeniedPage("/saml/SSO/error"));
            return http.getSharedObject(SecurityFilterChain.class);
        } catch (Exception e){
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return null;
    }

//    @Bean(name = "CustomAuthenticationProvider")
//    public AuthenticationProvider authenticationProvider(){
//        return customAuthenticationProvider;
//    }

//    @Bean(name = "ApplicationDataSource")
    public DataSource dataSource(){
        return datasource.getDataSource();
    }

    @Bean
    public PersistentTokenRepository tokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource());
        return jdbcTokenRepository;
    }


    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}