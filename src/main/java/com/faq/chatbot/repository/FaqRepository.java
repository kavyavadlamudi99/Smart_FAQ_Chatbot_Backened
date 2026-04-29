package com.faq.chatbot.repository;

import com.faq.chatbot.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for FAQ entity
 */
@Repository
public interface FaqRepository extends JpaRepository<Faq, Long> {
    
    List<Faq> findByIsActiveTrue();
    
    List<Faq> findByCategoryAndIsActiveTrue(String category);
}
