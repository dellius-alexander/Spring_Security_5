package com.example.spring_security_web_service.WebApp;

import com.example.spring_security_web_service.Security.Custom.CustomAuthenticationProvider;
import com.example.spring_security_web_service.Security.Secret.Secret;
import com.example.spring_security_web_service.Security.UserDetails.UserLoginDetails;
import com.example.spring_security_web_service.User.User;
import com.example.spring_security_web_service.Utility.HierarchicalCheck;
import org.apache.commons.httpclient.auth.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.Serializable;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Controller(value = "LoginController")
public class RootController<T extends Map<String, Object>> implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(RootController.class);
    // payLoad should be used to manage DOM objects passed between server and client
    protected T payLoad = (T) new HashMap<String, Object>();
    // used to clean the payLoad of null objects
    private HierarchicalCheck hck = new HierarchicalCheck();

    private final CustomAuthenticationProvider customAuthenticationProvider;
    @Autowired
    public RootController(CustomAuthenticationProvider customAuthenticationProvider) {
        this.customAuthenticationProvider = customAuthenticationProvider;
    }
    @GetMapping("/")
    public String home(Model model, Principal principal) {
        if(principal != null)
            model.addAttribute("msg", "Welcome " + principal.getName());
        return "index";
    }
    /**
     * Login page
     * @param request
     * @param response
     * @param model
     * @return
     */
    @GetMapping(
            value = {"login"})
    public String login(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        try{
            // clear out payLoad at login
            // reset our payLoad object
//            payLoad.clear();
            // cleanse the model of null objects
            payLoad.putAll(hck.hierarchicalCheck(model.asMap()));
            // reset our security context
//            SecurityContextHolder.getContext().setAuthentication(null);
            /*************************************************************
             * https://datatracker.ietf.org/doc/html/rfc7234#section-5.4
             * ***********************************************************
             * Write the response headers for login. The "Pragma" header
             * field allows backwards compatibility with HTTP/1.0 caches,
             * so that clients can specify a "no-cache" request that they
             * will understand (as Cache-Control was not defined until
             * HTTP/1.1).  When the Cache-Control header field is also
             * present and understood in a request, Pragma is ignored.
             ************************************************************/
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Private","no-cache");
            response.setDateHeader("Expires", 0); // no expiration
            log.info(request.toString());
            payLoad.put("userLoginDetails", new UserLoginDetails());
            // merge our attributes
            model.mergeAttributes(payLoad);
            log.info("\nPayload: {}\n", payLoad);
        } catch (Exception e){
            SecurityContextHolder.getContext().setAuthentication(null);
            log.error(e.getMessage());
            e.printStackTrace();
            return "redirect:login";

        }
        return "login";
    }
    /**
     * User login form verification
     * @param userLoginAttempt
     * @param result
     * @param model
     * @return
     */
    @PostMapping(
            value = {"index"})
    public String postIndex(
            @ModelAttribute("userLoginDetails")
            @Valid UserLoginDetails userLoginAttempt,
            Model model,
            BindingResult result,
            HttpServletRequest request,
            HttpServletResponse response

    ) {
        try{
            payLoad.putAll(hck.hierarchicalCheck(model.asMap()));
            log.info(payLoad.toString());
            log.info(userLoginAttempt.toString());
            log.info(model.toString());
            if (result.hasErrors()){
                return "redirect:login";
            }
            else if (userLoginAttempt.getUsername().isEmpty()){
                return "redirect:login";
            }
            customAuthenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginAttempt.getUsername(),
                            userLoginAttempt.getPasswordClass(),
                            userLoginAttempt.getAuthorities()
                            )
            );
            payLoad.put("userLoginDetails", new UserLoginDetails( (User) SecurityContextHolder.getContext().getAuthentication().getDetails()));
            model.addAttribute("userLoginDetails", (UserLoginDetails) payLoad.get("userLoginDetails"));
            response.addCookie(
                    new Cookie("JSESSIONID_"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
                    new Secret(String.valueOf(SecurityContextHolder.getContext().getAuthentication().hashCode())).getPasswordToString()));
            // merge our attributes
            model.mergeAttributes(payLoad);
            log.info(payLoad.toString());
            log.info(model.toString());
            log.info("Security Context Holder Details: {}", SecurityContextHolder.getContext().getAuthentication().getDetails().toString());
        } catch (Exception e) {
            log.error("Failure in autoLogin. Redirection back to /login. \n{}", e.getMessage());
            e.printStackTrace();
            return "redirect:login";
        }
        return "index";
    }


    @GetMapping(
            value = "index"
    )
    public String getIndex(
            Authentication auth,
            Model model
    ){
        UserLoginDetails userLoginDetails =  null;
        try{
            payLoad.putAll(hck.hierarchicalCheck(model.asMap()));
            log.info(model.toString());
            log.info("Authentication: {}",auth.getDetails().toString());
            if (auth.isAuthenticated()){
                userLoginDetails = new UserLoginDetails((User) auth.getDetails());
                payLoad.put("userLoginDetails", userLoginDetails);
//                customAuthenticationProvider.authenticate(
//                        new UsernamePasswordAuthenticationToken(
//                                userLoginDetails.getUsername(),
//                                userLoginDetails.getPasswordClass(),
//                                userLoginDetails.getAuthorities()
//                        )
//                );
            }
            else {
                throw new AuthenticationException("User not authenticated. ");
            }
            // merge our attributes
            model.mergeAttributes(payLoad);
            log.info(payLoad.toString());
            log.info(model.toString());
            log.info("Security Context Holder Details: {}",
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getDetails()
                            .toString());
        } catch (Exception e){
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
            return "redirect:login";
        }
        return "index";
    }
    /**
     * Login-error
     * @param throwable
     * @param model
     * @return error
     */
    @ExceptionHandler(Throwable.class)
    @GetMapping(
            value = {"error"})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(
            final Throwable throwable,
            final Model model) {
        log.error("Exception during execution of LendIT Book Kiosk application", throwable);
        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
        model.addAttribute("errorMessage", errorMessage);
        throwable.printStackTrace();
        return "error";
    }

}
