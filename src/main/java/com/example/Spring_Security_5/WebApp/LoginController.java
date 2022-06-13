package com.example.Spring_Security_5.WebApp;

import com.example.Spring_Security_5.Security.Custom.CustomAuthenticationProvider;
import com.example.Spring_Security_5.Security.Custom.CustomSecurityContext;
import com.example.Spring_Security_5.Security.UserDetails.UserLoginDetails;
import com.example.Spring_Security_5.User.User;
import com.example.Spring_Security_5.Utility.HierarchicalCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Controller(value = "LoginController")
@RequestMapping( value = {"/"}, path = {"/"})
public class LoginController<T extends Map<String, Object>> implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    // payLoad should be used to manage DOM objects passed between server and client
    protected T payLoad = (T) new HashMap<String, Object>();
    // used to clean the payLoad of null objects
    private HierarchicalCheck hck = new HierarchicalCheck();

    private final CustomAuthenticationProvider customAuthenticationProvider;
    @Autowired
    public LoginController(CustomAuthenticationProvider customAuthenticationProvider) {
        this.customAuthenticationProvider = customAuthenticationProvider;
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
            BindingResult result

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
            Model model
    ){
        try{
            payLoad.putAll(hck.hierarchicalCheck(model.asMap()));
            log.info(model.toString());
            UserLoginDetails userLoginDetails = (UserLoginDetails) payLoad.get("userLoginDetails");
            customAuthenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDetails.getUsername(),
                        userLoginDetails.getPasswordClass(),
                        userLoginDetails.getAuthorities()
                )
            );
            payLoad.put("userLoginDetails", userLoginDetails);
            // merge our attributes
            model.mergeAttributes(payLoad);
            log.info(payLoad.toString());
            log.info(model.toString());
            log.info("Security Context Holder Details: {}", SecurityContextHolder.getContext().getAuthentication().getDetails().toString());
        } catch (Exception e){
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
            return "redirect:login";
        }
        return "index";
    }

}
