package com.ecommerce.ECommerceAPI.api.controller.auth;

import com.ecommerce.ECommerceAPI.api.model.LoginBody;
import com.ecommerce.ECommerceAPI.api.model.LoginResponse;
import com.ecommerce.ECommerceAPI.api.model.PasswordResetBody;
import com.ecommerce.ECommerceAPI.api.model.RegistrationBody;
import com.ecommerce.ECommerceAPI.exception.EmailFailureException;
import com.ecommerce.ECommerceAPI.exception.EmailNotFoundException;
import com.ecommerce.ECommerceAPI.exception.UserAlreadyExistsException;
import com.ecommerce.ECommerceAPI.exception.UserNotVerifiedException;
import com.ecommerce.ECommerceAPI.model.LocalUser;
import com.ecommerce.ECommerceAPI.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Post mapping to handle registering users
     * @param registrationBody The registration information
     * @return Response to front end
     */
    @PostMapping("/register")
    public ResponseEntity registerUser(@Valid @RequestBody RegistrationBody registrationBody){
        try {
            userService.registerUser(registrationBody);
            return ResponseEntity.ok().build();
        }catch (UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (EmailFailureException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Post Mapping to handle user logins to provide authentication token
     * @param loginBody The login information
     * @return The authentication token if login is correct
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginBody loginBody){
        String jwt = null;
        try {
            jwt = userService.loginUser(loginBody);
        } catch (UserNotVerifiedException e) {
            LoginResponse response = new LoginResponse();
            response.setSuccess(false);
            String reason = "USER_NOT_VERIFIED";
            if (e.isNewEmailSent()){
                reason += "_EMAIL_RESEND";
            }
            response.setFailureReason(reason);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (EmailFailureException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        if (jwt == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }else{
            LoginResponse response = new LoginResponse();
            response.setJwt(jwt);
            response.setSuccess(true);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity verifyEmail(@RequestParam String token){
        if (userService.verifyUser(token)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/me")
    public LocalUser getLoggedInUserProfile(@AuthenticationPrincipal LocalUser user){
        return user;
    }

    //OK
    @PatchMapping("/me")
    public LocalUser updateLoggedProfile(@AuthenticationPrincipal LocalUser user,
                                              @RequestBody Map<String, String> json){
        return userService.updateName(user.getId(), json.get("firstName"), json.get("lastName"));
    }

    @PostMapping("/forgot")
    public ResponseEntity forgotPassword(@RequestParam String email) {
        try{
            userService.forgotPassword(email);
            return ResponseEntity.ok().build();
        }catch(EmailNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch(EmailFailureException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/reset")
    public ResponseEntity resetPassword(@Valid @RequestBody PasswordResetBody body){
        userService.resetPassword(body);
        return ResponseEntity.ok().build();
    }
}
