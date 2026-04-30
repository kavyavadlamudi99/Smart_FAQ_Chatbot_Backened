package com.faq.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Response DTO for FAQ document detail (includes full content)
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaqDocumentDetailResponse {
    private Long id;
    private String fileName;
    private String content;
}
