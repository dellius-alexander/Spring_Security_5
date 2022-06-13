package com.example.Spring_Security_5.Security.Custom;

import com.example.Spring_Security_5.Security.UserDetails.UserLoginDetails;
import com.example.Spring_Security_5.User.User;
import com.example.Spring_Security_5.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service(value = "CustomUserDetailsService")
@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final static Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
    @Autowired
    private UserService userService;
    public CustomUserDetailsService(){}
    public CustomUserDetailsService(String username){loadUserByUsername(username);}
    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     * GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        short ENABLED = 1;
        log.info("\nUSERNAME: {}\n",username);
        User user = userService.getByEmail(username);
        if (user == null){
                throw  new UsernameNotFoundException(
                        String.format("\nUser with USERNAME: %s could not be found.\n",username));

        }
        log.info("\nUSER RETRIEVED: {}\n",user);
        return (UserDetails) new UserLoginDetails(user);
    }

}
