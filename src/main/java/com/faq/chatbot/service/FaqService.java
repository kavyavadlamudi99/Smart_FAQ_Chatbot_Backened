package com.faq.chatbot.service;

import com.faq.chatbot.dto.FaqCreateRequest;
import com.faq.chatbot.dto.FaqExtractedRecord;
import com.faq.chatbot.dto.FaqResponse;
import com.faq.chatbot.dto.FaqUpdateRequest;
import com.faq.chatbot.entity.Faq;
import com.faq.chatbot.repository.FaqRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for FAQ operations
 */
@Service
public class FaqService {
    
    private final FaqRepository faqRepository;
    
    public FaqService(FaqRepository faqRepository) {
        this.faqRepository = faqRepository;
    }
    
    /**
     * Get all active FAQs
     * 
     * @return List of all active FAQs
     */
    public List<FaqResponse> getAllFaqs() {
        List<Faq> faqs = faqRepository.findByIsActiveTrue();
        return faqs.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get FAQs by category
     * 
     * @param category The category to filter by
     * @return List of FAQs in the specified category
     */
    public List<FaqResponse> getFaqsByCategory(String category) {
        List<Faq> faqs = faqRepository.findByCategoryAndIsActiveTrue(category);
        return faqs.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get a FAQ by ID
     * 
     * @param id The FAQ ID
     * @return FAQ response
     * @throws IllegalArgumentException if FAQ not found
     */
    public FaqResponse getFaqById(Long id) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FAQ not found with ID: " + id));
        return mapToResponse(faq);
    }
    
    /**
     * Create a new FAQ
     * 
     * @param request The create request with question, answer, and category
     * @param documentId The ID of the associated document
     * @return Created FAQ response with ID
     */
    public FaqResponse createFaq(FaqCreateRequest request, Long documentId) {
        if (request.getQuestion() == null || request.getQuestion().trim().isEmpty()) {
            throw new IllegalArgumentException("Question cannot be empty");
        }
        
        if (request.getAnswer() == null || request.getAnswer().trim().isEmpty()) {
            throw new IllegalArgumentException("Answer cannot be empty");
        }
        
        Faq faq = Faq.builder()
                .question(request.getQuestion())
                .answer(request.getAnswer())
                .category(request.getCategory() != null ? request.getCategory() : "General")
                .documentId(documentId)
                .isActive(true)
                .build();
        
        Faq savedFaq = faqRepository.save(faq);
        return mapToResponse(savedFaq);
    }
    
    /**
     * Create multiple FAQs from extracted records
     * 
     * @param records List of extracted FAQ records
     * @param documentId The ID of the associated document
     * @return List of created FAQ responses
     */
    public List<FaqResponse> createFaqsFromExtracted(List<FaqExtractedRecord> records, Long documentId) {
        return records.stream()
                .map(record -> {
                    Faq faq = Faq.builder()
                            .question(record.getQuestion())
                            .answer(record.getAnswer())
                            .category(record.getCategory() != null ? record.getCategory() : "General")
                            .documentId(documentId)
                            .isActive(true)
                            .build();
                    return faqRepository.save(faq);
                })
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Update an existing FAQ
     * 
     * @param id The FAQ ID to update
     * @param request The update request
     * @return Updated FAQ response
     * @throws IllegalArgumentException if FAQ not found
     */
    public FaqResponse updateFaq(Long id, FaqUpdateRequest request) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FAQ not found with ID: " + id));
        
        if (request.getQuestion() != null && !request.getQuestion().trim().isEmpty()) {
            faq.setQuestion(request.getQuestion());
        }
        
        if (request.getAnswer() != null && !request.getAnswer().trim().isEmpty()) {
            faq.setAnswer(request.getAnswer());
        }
        
        if (request.getCategory() != null) {
            faq.setCategory(request.getCategory());
        }
        
        if (request.getIsActive() != null) {
            faq.setIsActive(request.getIsActive());
        }
        
        Faq updatedFaq = faqRepository.save(faq);
        return mapToResponse(updatedFaq);
    }
    
    /**
     * Soft delete an FAQ by marking it as inactive
     * 
     * @param id The FAQ ID to delete
     * @throws IllegalArgumentException if FAQ not found
     */
    public void deleteFaq(Long id) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FAQ not found with ID: " + id));
        
        faq.setIsActive(false);
        faqRepository.save(faq);
    }
    
    /**
     * Map Faq entity to FaqResponse DTO
     * 
     * @param faq The Faq entity
     * @return FaqResponse DTO
     */
    private FaqResponse mapToResponse(Faq faq) {
        return FaqResponse.builder()
                .id(faq.getId())
                .question(faq.getQuestion())
                .answer(faq.getAnswer())
                .category(faq.getCategory())
                .isActive(faq.getIsActive())
                .build();
    }
}
