package com.example.spring_security_web_service.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.example.spring_security_web_service.Security.UserDetails.UserLoginDetails;
import com.example.spring_security_web_service.User.Role.Role;
import com.example.spring_security_web_service.User.Role.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service Access Layer
 */
@Transactional
@Service(value = "UserService")
public class UserService implements UserDetailsService, Serializable {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    /**
     * Service access layer connects to Data access layer to retrieve students
     * @param userRepository user repository
     * @param roleRepository getRoleByRolename repository
     */
    @Autowired // needed to automatically connect to StudentRepository
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * Saves a single user
     * @param user
     * @return
     */
    public User saveUser(User user){
        log.info("Saving new user: {}", user);
        return userRepository.save(user);
    }
    /**
     * Adds getRoleByRolename to user.
     * @param username
     * @param roleName
     */
    public void addRoleToUser(String username, String roleName){
        // TODO: REMEMBER TO INCLUDE MORE VALIDATION CODE
        log.info("Adding UserRole: {} to USER: {}",roleName, username);
        Optional<User> userOptional = userRepository
                .findUserByName(username);
        Optional<Role> roleOptional = roleRepository.findRoleByName(roleName);
        // the user is not present in the User table
        if (userOptional.isEmpty()){
            throw new IllegalStateException("Username " + username + " does not exists.");
        }
        // the user has already been assigned this getRoleByRolename
        if (roleOptional.isPresent() && userOptional.get().getRoles().contains(roleOptional.get())){
            throw new IllegalStateException(" Role:" + roleName + " already assigned to Username: " + username  );
        }
        // the getRoleByRolename does not exist as a predefined getRoleByRolename
        if (roleOptional.isEmpty()){
            throw new IllegalStateException(roleName + " is not a Role defined for USERS.");
        }
        // check if user is assigned the getRoleByRolename
        for (Role role : userOptional.get().getRoles()) {
            if (role.equals(roleOptional.get())) {
                throw new IllegalStateException(" Role:" + roleName + " already assigned to Username: " + username  );
            }
        }
        // add getRoleByRolename to user
        userOptional.get().setRole(roleOptional.get());

        // update the database
        userRepository.save(userOptional.get());
        // userOptional.get().setRole(roleName);
        String response = "{\n\"response\":\"Successfully added Role\",\n" +
                "\"getRoleByRolename\":\"" + roleName + "\",\n" +
                "\"username\":\"" + username +
                "\n}";
        log.info(response);
    }
    // TODO: REMEMBER TO REMOVE ON PRODUCTION DEPLOY
    /**
     * Gets the user by username
     * @param username username
     */
    public User getUser(String username){
        log.info("Fetching USER: {}", username);
        User user = userRepository.findUserByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Unable to locate user.")
        );
        // return User or else return null
        return user;
    }
    // TODO: REMEMBER TO REMOVE ON PRODUCTION DEPLOY
    /**
     * Get all users
     * @return
     */
    public List<User> getUsers(){
        log.info("Fetching all USERS. FOR TESTING PURPOSES ONLY.......");
        List<User> users = userRepository.findAll();
        log.info("\nUsers Received from DB: {}\n",users.stream().toString());
        return users;
    }
    /**
     * Get <code>User</code> by username
     * @param username user username
     * @return User
     */
    public ResponseEntity<User> getByUsername(String username){
        try{
            return ResponseEntity.status(HttpStatus.ACCEPTED).body((User) List.of(userRepository.findUserByUsername(username)
                .orElseThrow(() ->
                    new IllegalStateException("User with username " + username +
                            " was not found."))));
        } catch (Exception e){
            log.error(e.getLocalizedMessage());
            log.error("Unable to locate user: {}",username);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body((User) List.of(e.getLocalizedMessage()));
        }

    }

    /**
     * Update a <code>User</code> account
     * @param user a <code>User</code>
     * @return <code>User</code>
     */
    public ResponseEntity<?> updateUser(User user){
        log.info("\nUser: {}\n", user.toString());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body( List.of(userRepository.save(user)));
    }
    /**
     * Loads UserDetails by username search
     * @param username user email
     * @return <code>UserDetails</code>
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("\nUSERNAME: {}\n", username);
        User user = userRepository.findUserByUsername(username)
            .orElseThrow(
                () -> new UsernameNotFoundException(
                    "<p>Unable to locate User: " + username + "</p>"
                )
            );
        return (UserDetails) new UserLoginDetails(user);
    }
    /**
     * Delete a <code>User</code> by user id
     * @param username the user id
     * @return the response status
     */
    public ResponseEntity<?> delete(String username){
        return ResponseEntity.ok().body(userRepository.deleteByUsername(username));
    }

    /**
     * Save all users to DB
     * @param users
     * @return http status code 200 ok or throws exception
     */
    @Async
    public ResponseEntity<List<?>> saveAll(List< User> users){
        try {
            List<User> notExist = new ArrayList<>();
            List<String> messages = new ArrayList<>(users.size());
            for (User user : users) { // check if user exists
                if (userRepository.findUserByUsername(user.getUsername()).isPresent()) {
                    messages.add("User " + user.getUsername() + " already exists.");
                }
                else { // add new user that not exists in db
                    notExist.add(user);
                }
            }
            if(notExist.size() == 0){ // throw exception if all users already exists
                throw new IllegalStateException(messages.stream().collect(Collectors.toList()).toString());
            }
            // save what remains
            return ResponseEntity.ok().body(List.of(messages,userRepository.saveAll(notExist)));
        } catch (Exception e){
            log.error("\n{}",e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(List.of(e.getLocalizedMessage()));
        }
    }
}
