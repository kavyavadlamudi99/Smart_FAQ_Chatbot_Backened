package com.faq.chatbot.repository;

import com.faq.chatbot.entity.ChatLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for ChatLog entity
 */
@Repository
public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {
    
    List<ChatLog> findTop100ByOrderByCreatedAtDesc();
}
