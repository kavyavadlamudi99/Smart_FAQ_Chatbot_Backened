package com.faq.chatbot.service;

import com.faq.chatbot.dto.AdminLoginRequest;
import com.faq.chatbot.dto.AdminRegisterRequest;
import com.faq.chatbot.dto.AuthResponse;
import com.faq.chatbot.entity.AdminUser;
import com.faq.chatbot.repository.AdminUserRepository;
import com.faq.chatbot.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for admin authentication operations
 */
@Service
public class AuthService {
    
    private final AdminUserRepository adminUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    public AuthService(AdminUserRepository adminUserRepository, 
                      BCryptPasswordEncoder passwordEncoder,
                      JwtUtil jwtUtil) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    
    /**
     * Register a new admin user
     *
     * @param request Admin registration request
     * @return Authentication response
     * @throws IllegalArgumentException if email already exists
     */
    public AuthResponse register(AdminRegisterRequest request) {
        // Check if email already exists
        if (adminUserRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        
        // Create new admin user
        AdminUser admin = AdminUser.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .companyName(request.getCompanyName())
                .build();
        
        adminUserRepository.save(admin);
        
        return AuthResponse.builder()
                .message("Admin registered successfully")
                .build();
    }
    
    /**
     * Login admin user
     *
     * @param request Admin login request
     * @return Authentication response with JWT token
     * @throws IllegalArgumentException if credentials are invalid
     */
    public AuthResponse login(AdminLoginRequest request) {
        // Find admin by email
        AdminUser admin = adminUserRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        
        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        
        // Generate JWT token
        String token = jwtUtil.generateToken(admin.getEmail(), admin.getId());
        
        return AuthResponse.builder()
                .token(token)
                .adminName(admin.getName())
                .companyName(admin.getCompanyName())
                .build();
    }
}
