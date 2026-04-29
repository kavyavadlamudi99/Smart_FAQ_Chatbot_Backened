package com.faq.chatbot.dto;

import lombok.*;

/**
 * DTO for authentication response
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    
    private String message;
    
    private String token;
    
    private String adminName;
    
    private String companyName;
}
