package com.faq.chatbot.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO for ChatLog response
 * Contains chat log details for API responses
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatLogResponse {
    
    private Long id;
    private String userQuestion;
    private String botAnswer;
    private String sourceType;
    private Long responseTimeMs;
    private Boolean needsReview;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
