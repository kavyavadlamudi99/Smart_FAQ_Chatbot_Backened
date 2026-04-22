package com.faq.chatbot.repository;

import com.faq.chatbot.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for ChatMessage entity
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    List<ChatMessage> findBySessionId(Long sessionId);
    
    List<ChatMessage> findBySessionIdOrderByMessageIndexAsc(Long sessionId);
    
    @Query("SELECT m FROM ChatMessage m WHERE m.session.id = :sessionId ORDER BY m.messageIndex DESC")
    List<ChatMessage> findRecentMessagesBySessionId(Long sessionId);
    
    @Query("SELECT m FROM ChatMessage m WHERE m.createdAt BETWEEN :startDate AND :endDate")
    List<ChatMessage> findMessagesBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.session.id = :sessionId")
    Long countMessagesBySessionId(Long sessionId);
    
    void deleteBySessionId(Long sessionId);
}
