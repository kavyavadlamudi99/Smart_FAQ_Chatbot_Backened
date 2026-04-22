package com.faq.chatbot.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * FaqChunk Entity
 * Represents a chunk of text from an FAQ document for RAG (Retrieval-Augmented Generation)
 */
@Entity
@Table(name = "faq_chunks", indexes = {
    @Index(name = "idx_document_id", columnList = "document_id"),
    @Index(name = "idx_chunk_index", columnList = "chunk_index")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaqChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private FaqDocument document;

    @Column(nullable = false)
    private Integer chunkIndex;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer contentLength;

    @Column(columnDefinition = "TEXT")
    private String embedding;

    @Column(length = 500)
    private String metadata;

    @Column(nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime embeddingGeneratedAt;
}
