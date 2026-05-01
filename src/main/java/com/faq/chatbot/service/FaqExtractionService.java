package com.faq.chatbot.service;

import com.faq.chatbot.dto.FaqExtractedRecord;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for extracting FAQ question-answer pairs from document content
 */
@Service
public class FaqExtractionService {

    /**
     * Extract FAQ records from document content
     * Supports formats:
     * - Q: question \n A: answer
     * - Question: question \n Answer: answer
     * 
     * @param content The document content to extract FAQs from
     * @return List of extracted FAQ records
     */
    public List<FaqExtractedRecord> extractFaqRecords(String content) {
        List<FaqExtractedRecord> records = new ArrayList<>();
        
        if (content == null || content.trim().isEmpty()) {
            return records;
        }
        
        // Try pattern 1: Q: ... A: ...
        records.addAll(extractByPattern1(content));
        
        // Try pattern 2: Question: ... Answer: ...
        records.addAll(extractByPattern2(content));
        
        // Remove duplicates (same question and answer)
        return removeDuplicates(records);
    }
    
    /**
     * Extract FAQs using pattern: Q: question \n A: answer
     * 
     * @param content The document content
     * @return List of extracted FAQ records
     */
    private List<FaqExtractedRecord> extractByPattern1(String content) {
        List<FaqExtractedRecord> records = new ArrayList<>();
        
        // Pattern: Q: <anything until next Q: or A:> ... A: <anything until next Q:>
        // Case-insensitive matching
        Pattern pattern = Pattern.compile(
            "^\\s*Q[:]\\s*(.+?)\\s*$\\s*^\\s*A[:]\\s*(.+?)\\s*$",
            Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.DOTALL
        );
        
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String question = cleanText(matcher.group(1));
            String answer = cleanText(matcher.group(2));
            
            if (!question.isEmpty() && !answer.isEmpty()) {
                records.add(FaqExtractedRecord.builder()
                        .question(question)
                        .answer(answer)
                        .category("General")
                        .build());
            }
        }
        
        return records;
    }
    
    /**
     * Extract FAQs using pattern: Question: question \n Answer: answer
     * 
     * @param content The document content
     * @return List of extracted FAQ records
     */
    private List<FaqExtractedRecord> extractByPattern2(String content) {
        List<FaqExtractedRecord> records = new ArrayList<>();
        
        // Pattern: Question: <anything until Answer:> ... Answer: <anything until Question: or end>
        // Case-insensitive matching
        Pattern pattern = Pattern.compile(
            "^\\s*Question\\s*[:]\\s*(.+?)\\s*$\\s*^\\s*Answer\\s*[:]\\s*(.+?)\\s*$",
            Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.DOTALL
        );
        
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String question = cleanText(matcher.group(1));
            String answer = cleanText(matcher.group(2));
            
            if (!question.isEmpty() && !answer.isEmpty()) {
                records.add(FaqExtractedRecord.builder()
                        .question(question)
                        .answer(answer)
                        .category("General")
                        .build());
            }
        }
        
        return records;
    }
    
    /**
     * Clean extracted text by trimming and removing extra whitespace
     * 
     * @param text The text to clean
     * @return Cleaned text
     */
    private String cleanText(String text) {
        if (text == null) {
            return "";
        }
        // Trim and replace multiple spaces/newlines with single space
        return text.trim()
                   .replaceAll("\\s+", " ")
                   .replaceAll("\n+", " ")
                   .trim();
    }
    
    /**
     * Remove duplicate FAQ records based on question and answer
     * 
     * @param records The list of records
     * @return List with duplicates removed
     */
    private List<FaqExtractedRecord> removeDuplicates(List<FaqExtractedRecord> records) {
        List<FaqExtractedRecord> uniqueRecords = new ArrayList<>();
        
        for (FaqExtractedRecord record : records) {
            boolean isDuplicate = false;
            for (FaqExtractedRecord existing : uniqueRecords) {
                if (existing.getQuestion().equalsIgnoreCase(record.getQuestion()) &&
                    existing.getAnswer().equalsIgnoreCase(record.getAnswer())) {
                    isDuplicate = true;
                    break;
                }
            }
            
            if (!isDuplicate) {
                uniqueRecords.add(record);
            }
        }
        
        return uniqueRecords;
    }
}
