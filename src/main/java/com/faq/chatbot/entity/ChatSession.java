package com.faq.chatbot.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ChatSession Entity
 * Represents a chat conversation session with a user
 */
@Entity
@Table(name = "chat_sessions", indexes = {
    @Index(name = "idx_session_id", columnList = "session_id"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String sessionId;

    @Column(length = 100)
    private String userName;

    @Column(length = 150)
    private String userEmail;

    @Column(length = 45)
    private String ipAddress;

    @Column(length = 500)
    private String userAgent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SessionStatus status = SessionStatus.ACTIVE;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    @Builder.Default
    private List<ChatMessage> messages = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime lastMessageAt;

    @Column
    private LocalDateTime closedAt;

    @Column(nullable = false)
    private Integer messageCount = 0;

    public enum SessionStatus {
        ACTIVE,
        CLOSED,
        EXPIRED,
        ARCHIVED
    }

    // Helper methods
    public void addMessage(ChatMessage message) {
        messages.add(message);
        message.setSession(this);
        this.messageCount = messages.size();
        this.lastMessageAt = LocalDateTime.now();
    }

    public void removeMessage(ChatMessage message) {
        messages.remove(message);
        message.setSession(null);
        this.messageCount = messages.size();
    }
}
