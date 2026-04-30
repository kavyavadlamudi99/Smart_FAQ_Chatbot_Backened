package com.faq.chatbot.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for FAQ document listing
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaqDocumentResponse {
    private Long id;
    private String fileName;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
