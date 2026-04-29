package com.faq.chatbot.dto;

import lombok.*;

/**
 * DTO for admin registration request
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminRegisterRequest {
    
    private String name;
    
    private String email;
    
    private String password;
    
    private String companyName;
}
