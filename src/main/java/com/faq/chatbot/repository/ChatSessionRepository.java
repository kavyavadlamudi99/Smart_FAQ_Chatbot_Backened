package com.faq.chatbot.repository;

import com.faq.chatbot.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ChatSession entity
 */
@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    
    Optional<ChatSession> findBySessionId(String sessionId);
    
    List<ChatSession> findByStatus(ChatSession.SessionStatus status);
    
    List<ChatSession> findByUserEmail(String userEmail);
    
    @Query("SELECT s FROM ChatSession s WHERE s.status = 'ACTIVE' AND s.lastMessageAt < :timeoutThreshold")
    List<ChatSession> findExpiredSessions(LocalDateTime timeoutThreshold);
    
    @Query("SELECT s FROM ChatSession s WHERE s.createdAt BETWEEN :startDate AND :endDate ORDER BY s.createdAt DESC")
    List<ChatSession> findSessionsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT COUNT(s) FROM ChatSession s WHERE s.status = 'ACTIVE'")
    Long countActiveSessions();
}
