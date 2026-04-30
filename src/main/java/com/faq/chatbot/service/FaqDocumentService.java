package com.faq.chatbot.service;

import com.faq.chatbot.dto.FaqDocumentDetailResponse;
import com.faq.chatbot.dto.FaqDocumentResponse;
import com.faq.chatbot.dto.FaqDocumentUploadResponse;
import com.faq.chatbot.entity.FaqDocument;
import com.faq.chatbot.repository.FaqDocumentRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for FAQ document operations
 */
@Service
public class FaqDocumentService {
    
    private final FaqDocumentRepository faqDocumentRepository;
    private static final List<String> ALLOWED_FILE_TYPES = Arrays.asList(".txt", ".pdf");
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    
    public FaqDocumentService(FaqDocumentRepository faqDocumentRepository) {
        this.faqDocumentRepository = faqDocumentRepository;
    }
    
    /**
     * Upload a FAQ document file
     * 
     * @param file The multipart file to upload
     * @param uploadedBy The ID of the user uploading the document
     * @return Upload response with document details
     * @throws IllegalArgumentException if file validation fails
     * @throws IOException if file reading fails
     */
    public FaqDocumentUploadResponse uploadDocument(MultipartFile file, Long uploadedBy) 
            throws IOException {
        
        // Validate file
        validateFile(file);
        
        // Extract file extension
        String fileName = file.getOriginalFilename();
        String fileExtension = getFileExtension(fileName);
        
        // Read file content as string
        String content = readFileContent(file, fileExtension);
        
        // Create and save FaqDocument entity
        FaqDocument faqDocument = FaqDocument.builder()
                .fileName(fileName)
                .content(content)
                .uploadedBy(uploadedBy)
                .build();
        
        FaqDocument savedDocument = faqDocumentRepository.save(faqDocument);
        
        // Build response
        return FaqDocumentUploadResponse.builder()
                .documentId(savedDocument.getId())
                .fileName(savedDocument.getFileName())
                .message("FAQ document uploaded successfully")
                .build();
    }
    
    /**
     * Validate uploaded file
     * 
     * @param file The file to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty or not provided");
        }
        
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size of 10MB");
        }
        
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File name is invalid");
        }
        
        String fileExtension = getFileExtension(fileName);
        if (!ALLOWED_FILE_TYPES.stream().anyMatch(ext -> ext.equalsIgnoreCase(fileExtension))) {
            throw new IllegalArgumentException("Only TXT and PDF files are supported");
        }
    }
    
    /**
     * Get file extension from file name
     * 
     * @param fileName The file name
     * @return File extension including the dot (e.g., ".txt")
     */
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex);
        }
        return "";
    }
    
    /**
     * Read file content as string based on file type
     * 
     * @param file The file to read
     * @param fileExtension The file extension
     * @return File content as string
     * @throws IOException if file reading fails
     */
    private String readFileContent(MultipartFile file, String fileExtension) throws IOException {
        if (fileExtension.equalsIgnoreCase(".txt")) {
            // For .txt files, read as plain text
            return new String(file.getBytes(), StandardCharsets.UTF_8);
        } else if (fileExtension.equalsIgnoreCase(".pdf")) {
            // For .pdf files, extract text using PDFBox
            return extractTextFromPdf(file);
        }
        
        throw new IllegalArgumentException("Only TXT and PDF files are supported");
    }
    
    /**
     * Extract text from PDF file using PDFBox
     * 
     * @param file The PDF file
     * @return Extracted text content
     * @throws IOException if PDF parsing fails
     */
    private String extractTextFromPdf(MultipartFile file) throws IOException {
        byte[] pdfData = file.getBytes();
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfData))) {
            if (document.isEncrypted()) {
                throw new IOException("Cannot extract text from encrypted PDF");
            }
            
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
    
    /**
     * Get all FAQ documents
     * 
     * @return List of FAQ documents with id, fileName, and createdAt
     */
    public List<FaqDocumentResponse> getAllDocuments() {
        List<FaqDocument> documents = faqDocumentRepository.findAll();
        return documents.stream()
                .map(doc -> FaqDocumentResponse.builder()
                        .id(doc.getId())
                        .fileName(doc.getFileName())
                        .createdAt(doc.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
    
    /**
     * Get a specific FAQ document by ID
     * 
     * @param id The document ID
     * @return FAQ document detail with full content
     * @throws IllegalArgumentException if document not found
     */
    public FaqDocumentDetailResponse getDocumentById(Long id) {
        FaqDocument document = faqDocumentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document not found with ID: " + id));
        
        return FaqDocumentDetailResponse.builder()
                .id(document.getId())
                .fileName(document.getFileName())
                .content(document.getContent())
                .build();
    }
}
