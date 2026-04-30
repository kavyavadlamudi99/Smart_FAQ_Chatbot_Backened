package com.faq.chatbot.repository;

import com.faq.chatbot.entity.FaqDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for FaqDocument entity
 */
@Repository
public interface FaqDocumentRepository extends JpaRepository<FaqDocument, Long> {
    
    List<FaqDocument> findByUploadedById(Long uploadedById);
}
