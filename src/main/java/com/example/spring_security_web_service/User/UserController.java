package com.example.spring_security_web_service.User;

import java.net.URI;
import java.util.List;

import com.example.spring_security_web_service.Security.UserDetails.UserLoginDetails;
import com.example.spring_security_web_service.User.Role.Role;
// LOGGING CLASSES
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Api Layer
 * <code style="color:orange;font-style:bold;">/user</code>
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }
    // TODO: REMEMBER TO KILL THIS METHOD DURING PRODUCTION

    /**
     * Get a list of All {@link User}s
     * @return a list of all users. {@link ResponseEntity<List<User>>}
     */
    @RequestMapping(
            value = {"/findAll", "findall"},
            method = RequestMethod.GET,
            produces = {"application/json"}
    )
    public ResponseEntity<List<User>> getUsers(){
        List<User> users = userService.getUsers();
        log.info("USERS: \n{}\n",users.toString());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .location(URI.create("/findAll")).body(users);
    }

    /**
     * Save a new {@link User}
     * @param user a new {@link User}
     * @return the saved user and response status code
     */
//            (
//            value = {"/save/user={user}","/save/{user}"},
////            method = RequestMethod.POST,
//            consumes = {"*/*"},
//            produces = {"application/json"},
//            params = {"user"},
//            name = "user")
    @PostMapping(
            value = {"/save/{user}"},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public ResponseEntity<User> saveUser(@RequestBody User user)
    {
        log.info("USER: \n{}\n", user.toString());
        return ResponseEntity.created(URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/user/save/*")
                            .toUriString())).body(userService.saveUser(user));
    }
    // TODO: Change visibility to protected
    /**
     * Assign {@link Role} to <code>User</code>
     * @param username the user id
     * @param role {@link Role}
     * @return the {@link User} and response status code
     */
    @RequestMapping(
            value = {"/assign-role/user_id={username}&role={role}"},
            method = RequestMethod.PATCH,
            consumes = {"application/json"},
            produces = {"application/json"})
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public ResponseEntity<?> saveRole(
            @PathVariable("username") String username,
            @PathVariable("role") @RequestBody Role role){

        log.info("\nUser Id: {}\nRole: {}\n", username, role.toString());
        if (username == null){
            throw new IllegalStateException("User id is NULL");
        }
        if (role == null){
            throw new IllegalStateException("Role is NULL");
        }
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/user/assign-role/*")
                        .toUriString());
        log.info("URI: \n{}\n",uri);
        User user = userService.getByUsername(username).getBody();
        user.setRole(role);
        return ResponseEntity.created(uri).body(userService.updateUser(user).getBody());
    }
    /**
     * Delete a <code>User</code> by <code>user_id</code>
     * @param username the user id
     * @Return the response status
     */
    @DeleteMapping(
            value = {"/delete/{user_id}"},
            produces = {"application/json"})
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> deleteUser(
            @PathVariable("username") String username){
        log.info("Delete RequestedMethod POST: StudentId => {}", username);
        return  ResponseEntity.ok().body(userService.delete(username));
    }

    /**
     * Verify <code>UserLoginDetails</code> form
     * @param form <code>UserLoginDetails</code> form
     * @return response status of POST request
     */
    @RequestMapping(
            value = {"/verify"},
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"},
            path = "/verify")
    @Secured("ROLE_PUBLIC")
    public ResponseEntity<?> verify(
            @PathVariable("role") @RequestBody UserLoginDetails form){
        log.info(form.toString());
         return ResponseEntity.ok().build();
    }

    @GetMapping("public")
    public ResponseEntity<String> publiclyAccessable(){
        return ResponseEntity.ok().body("Hello Public...");
    }
}


