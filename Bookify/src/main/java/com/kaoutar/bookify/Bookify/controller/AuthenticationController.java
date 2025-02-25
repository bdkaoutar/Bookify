package com.kaoutar.bookify.Bookify.controller;


import com.kaoutar.bookify.Bookify.dto.VerifyUserDto;
import com.kaoutar.bookify.Bookify.dto.loginUserDto;
import com.kaoutar.bookify.Bookify.dto.registerUserDto;
import com.kaoutar.bookify.Bookify.model.User;
import com.kaoutar.bookify.Bookify.responses.LoginResponse;
import com.kaoutar.bookify.Bookify.service.AuthenticationService;
import com.kaoutar.bookify.Bookify.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/auth")
@RestController
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(
            JwtService jwtService,
            AuthenticationService authenticationService
    ) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody registerUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> authenticate(@RequestBody loginUserDto loginUserDto) {
//        User authenticatedUser = authenticationService.authenticate(loginUserDto);
//        String jwtToken = jwtService.generateToken(authenticatedUser);
//        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
//        return ResponseEntity.ok(loginResponse);
//    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody loginUserDto loginUserDto) {
        try {
            // Authenticate user
            User authenticatedUser = authenticationService.authenticate(loginUserDto);

            // Generate JWT token
            String jwtToken = jwtService.generateToken(authenticatedUser);

            // Return JWT token along with expiration time
            LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());

            return ResponseEntity.ok(loginResponse); // Return 200 OK with the response body

        } catch (Exception e) {
            // Log the error and return a 403 with an error message
            System.out.println("Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }




    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto verifyUserDto) {
        try {
            authenticationService.verifyUser(verifyUserDto);
            return ResponseEntity.ok("Account verified successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/test-auth")
    public ResponseEntity<String> testAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Authenticated User: " + auth.getName() +
                ", Roles: " + auth.getAuthorities());
    }

}
