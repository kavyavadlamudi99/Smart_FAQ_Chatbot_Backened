package com.faq.chatbot.repository;

import com.faq.chatbot.entity.FaqDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for FaqDocument entity
 */
@Repository
public interface FaqDocumentRepository extends JpaRepository<FaqDocument, Long> {
    
    List<FaqDocument> findByActive(Boolean active);
    
    List<FaqDocument> findByStatus(FaqDocument.DocumentStatus status);
    
    List<FaqDocument> findByUploadedById(Long uploadedById);
    
    @Query("SELECT d FROM FaqDocument d WHERE d.active = true AND d.status = 'PROCESSED'")
    List<FaqDocument> findAllActiveProcessedDocuments();
    
    @Query("SELECT COUNT(d) FROM FaqDocument d WHERE d.active = true")
    Long countActiveDocuments();
}
