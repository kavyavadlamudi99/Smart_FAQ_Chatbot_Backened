package com.faq.chatbot.dto;

import lombok.*;

/**
 * DTO for admin login request
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminLoginRequest {
    
    private String email;
    
    private String password;
}
