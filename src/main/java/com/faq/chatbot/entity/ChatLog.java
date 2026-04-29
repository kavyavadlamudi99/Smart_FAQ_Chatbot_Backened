package com.faq.chatbot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ChatLog Entity
 * Stores chat interaction logs with bot responses and metadata
 */
@Entity
@Table(name = "chat_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String userQuestion;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String botAnswer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SourceType sourceType;

    @Column(nullable = false)
    private Long responseTimeMs;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum SourceType {
        FAQ_CONTEXT,
        FALLBACK,
        ERROR
    }
}
