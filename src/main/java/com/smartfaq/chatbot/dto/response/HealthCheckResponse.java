package com.smartfaq.chatbot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Health Check Response DTO
 * Represents the status of the Smart FAQ Chatbot backend
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthCheckResponse {
    
    /**
     * Status of the application (UP, DOWN, DEGRADED)
     */
    private String status;
    
    /**
     * Descriptive message about the application status
     */
    private String message;
}
