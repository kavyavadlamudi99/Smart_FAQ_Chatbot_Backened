package com.smartfaq.chatbot.controller;

import com.smartfaq.chatbot.dto.response.HealthCheckResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Health Check Controller
 * Provides endpoints for monitoring the health of the Smart FAQ Chatbot backend
 */
@RestController
@RequestMapping("/api")
public class HealthCheckController {
    
    /**
     * Health check endpoint
     * 
     * @return HealthCheckResponse with status and message
     */
    @GetMapping("/health")
    public ResponseEntity<HealthCheckResponse> getHealth() {
        HealthCheckResponse response = HealthCheckResponse.builder()
                .status("UP")
                .message("Smart FAQ Chatbot backend is running")
                .build();
        
        return ResponseEntity.ok(response);
    }
}
