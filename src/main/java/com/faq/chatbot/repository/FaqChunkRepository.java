package com.faq.chatbot.repository;

import com.faq.chatbot.entity.FaqChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for FaqChunk entity
 */
@Repository
public interface FaqChunkRepository extends JpaRepository<FaqChunk, Long> {
    
    List<FaqChunk> findByDocumentId(Long documentId);
    
    List<FaqChunk> findByDocumentIdAndActive(Long documentId, Boolean active);
    
    @Query("SELECT c FROM FaqChunk c WHERE c.document.id = :documentId ORDER BY c.chunkIndex ASC")
    List<FaqChunk> findByDocumentIdOrderByChunkIndex(Long documentId);
    
    @Query("SELECT c FROM FaqChunk c WHERE c.active = true AND c.embedding IS NOT NULL")
    List<FaqChunk> findAllActiveWithEmbeddings();
    
    void deleteByDocumentId(Long documentId);
}
