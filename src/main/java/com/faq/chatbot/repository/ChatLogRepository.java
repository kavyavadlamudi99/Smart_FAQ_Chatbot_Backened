package com.faq.chatbot.repository;

import com.faq.chatbot.entity.ChatLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for ChatLog entity
 */
@Repository
public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {
    
    /**
     * Get the latest 100 chat logs ordered by creation date descending
     */
    List<ChatLog> findTop100ByOrderByCreatedAtDesc();
    
    /**
     * Search chat logs by keyword in userQuestion or botAnswer
     */
    @Query("SELECT cl FROM ChatLog cl WHERE " +
           "LOWER(cl.userQuestion) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(cl.botAnswer) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "ORDER BY cl.createdAt DESC")
    List<ChatLog> searchByKeyword(String keyword);
    
    /**
     * Get all chat logs that need review (needsReview = true)
     */
    List<ChatLog> findByNeedsReviewTrueOrderByCreatedAtDesc();
}
