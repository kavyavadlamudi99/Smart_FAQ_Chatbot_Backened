package com.faq.chatbot.controller;

import com.faq.chatbot.dto.FaqDocumentDetailResponse;
import com.faq.chatbot.dto.FaqDocumentResponse;
import com.faq.chatbot.dto.FaqDocumentUploadResponse;
import com.faq.chatbot.service.FaqDocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Controller for FAQ document operations
 */
@RestController
@RequestMapping("/api/faq-documents")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FaqDocumentController {
    
    private final FaqDocumentService faqDocumentService;
    
    public FaqDocumentController(FaqDocumentService faqDocumentService) {
        this.faqDocumentService = faqDocumentService;
    }
    
    /**
     * Get all FAQ documents
     * 
     * @return List of all FAQ documents with id, fileName, and createdAt
     */
    @GetMapping
    public ResponseEntity<List<FaqDocumentResponse>> getAllDocuments() {
        try {
            List<FaqDocumentResponse> documents = faqDocumentService.getAllDocuments();
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get a specific FAQ document by ID
     * 
     * @param id The document ID
     * @return FAQ document detail with id, fileName, and full content
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getDocumentById(@PathVariable Long id) {
        try {
            FaqDocumentDetailResponse document = faqDocumentService.getDocumentById(id);
            return ResponseEntity.ok(document);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Upload a FAQ document file
     * 
     * @param file The multipart file to upload (.txt or .pdf)
     * @param authentication The authentication context containing user information
     * @return Response with document ID, file name, and upload status
     */
    @PostMapping("/upload")
    public ResponseEntity<FaqDocumentUploadResponse> uploadDocument(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {
        
        try {
            // Extract user ID from authentication context
            // For now, using a default value - update this based on your auth implementation
            Long uploadedBy = extractUserIdFromAuthentication(authentication);
            
            FaqDocumentUploadResponse response = faqDocumentService.uploadDocument(file, uploadedBy);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(FaqDocumentUploadResponse.builder()
                            .error(e.getMessage())
                            .message("Upload failed")
                            .build());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(FaqDocumentUploadResponse.builder()
                            .error("Failed to read file content: " + e.getMessage())
                            .message("Upload failed")
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(FaqDocumentUploadResponse.builder()
                            .error("An unexpected error occurred: " + e.getMessage())
                            .message("Upload failed")
                            .build());
        }
    }
    
    /**
     * Extract user ID from authentication context
     * 
     * @param authentication The authentication object
     * @return User ID or default value if authentication is null
     */
    private Long extractUserIdFromAuthentication(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() != null) {
            // This implementation depends on how you store user info in the authentication token
            // Update this based on your JWT/authentication implementation
            String principal = authentication.getPrincipal().toString();
            // For now, return a default value
            // TODO: Update this to extract actual user ID from JWT token or authentication context
            try {
                return Long.parseLong(principal);
            } catch (NumberFormatException e) {
                return 1L; // Default user ID
            }
        }
        return 1L; // Default user ID
    }
}
