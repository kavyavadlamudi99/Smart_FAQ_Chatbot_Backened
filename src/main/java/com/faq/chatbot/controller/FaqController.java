package com.faq.chatbot.controller;

import com.faq.chatbot.dto.FaqCreateRequest;
import com.faq.chatbot.dto.FaqResponse;
import com.faq.chatbot.dto.FaqUpdateRequest;
import com.faq.chatbot.service.FaqService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for FAQ operations
 */
@RestController
@RequestMapping("/api/faqs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FaqController {
    
    private final FaqService faqService;
    
    public FaqController(FaqService faqService) {
        this.faqService = faqService;
    }
    
    /**
     * Get all active FAQs
     * 
     * @return List of all active FAQs
     */
    @GetMapping
    public ResponseEntity<List<FaqResponse>> getAllFaqs(
            @RequestParam(value = "category", required = false) String category) {
        try {
            List<FaqResponse> faqs;
            if (category != null && !category.isEmpty()) {
                faqs = faqService.getFaqsByCategory(category);
            } else {
                faqs = faqService.getAllFaqs();
            }
            return ResponseEntity.ok(faqs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get a specific FAQ by ID
     * 
     * @param id The FAQ ID
     * @return FAQ response
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getFaqById(@PathVariable Long id) {
        try {
            FaqResponse faq = faqService.getFaqById(id);
            return ResponseEntity.ok(faq);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Create a new FAQ manually
     * 
     * @param request The create request with question, answer, and category
     * @return Created FAQ response
     */
    @PostMapping
    public ResponseEntity<?> createFaq(@RequestBody FaqCreateRequest request) {
        try {
            // For manual creation, use a default documentId of 0 or null
            FaqResponse faq = faqService.createFaq(request, 0L);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "FAQ created successfully");
            response.put("faqId", faq.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Update an existing FAQ
     * 
     * @param id The FAQ ID to update
     * @param request The update request
     * @return Update confirmation message
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFaq(@PathVariable Long id, @RequestBody FaqUpdateRequest request) {
        try {
            faqService.updateFaq(id, request);
            return ResponseEntity.ok(createSuccessResponse("FAQ updated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Soft delete an FAQ by marking it as inactive
     * 
     * @param id The FAQ ID to delete
     * @return Delete confirmation message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFaq(@PathVariable Long id) {
        try {
            faqService.deleteFaq(id);
            return ResponseEntity.ok(createSuccessResponse("FAQ deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Helper method to create a success response map
     * 
     * @param message The success message
     * @return Response map
     */
    private Map<String, String> createSuccessResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return response;
    }
    
    /**
     * Helper method to create an error response map
     * 
     * @param error The error message
     * @return Response map
     */
    private Map<String, String> createErrorResponse(String error) {
        Map<String, String> response = new HashMap<>();
        response.put("error", error);
        return response;
    }
}
