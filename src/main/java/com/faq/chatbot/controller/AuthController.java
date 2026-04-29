package com.faq.chatbot.controller;

import com.faq.chatbot.dto.AdminLoginRequest;
import com.faq.chatbot.dto.AdminRegisterRequest;
import com.faq.chatbot.dto.AuthResponse;
import com.faq.chatbot.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for admin authentication endpoints
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    
    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    /**
     * Register a new admin user
     *
     * @param request Admin registration request
     * @return Response with registration status
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AdminRegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(AuthResponse.builder()
                            .message(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AuthResponse.builder()
                            .message("Registration failed: " + e.getMessage())
                            .build());
        }
    }
    
    /**
     * Login admin user
     *
     * @param request Admin login request
     * @return Response with JWT token and admin details
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AdminLoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.builder()
                            .message(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AuthResponse.builder()
                            .message("Login failed: " + e.getMessage())
                            .build());
        }
    }
}
