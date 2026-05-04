package com.faq.chatbot.service;

import com.faq.chatbot.dto.ChatLogResponse;
import com.faq.chatbot.entity.ChatLog;
import com.faq.chatbot.repository.ChatLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for ChatLog operations
 * Handles retrieving and searching chat logs with unanswered query detection
 */
@Service
public class ChatLogService {
    
    private final ChatLogRepository chatLogRepository;
    private static final String INSUFFICIENT_INFO_MARKER = "I don't have enough information";
    
    public ChatLogService(ChatLogRepository chatLogRepository) {
        this.chatLogRepository = chatLogRepository;
    }
    
    /**
     * Get the latest 100 chat logs
     * 
     * @return List of latest 100 chat logs
     */
    public List<ChatLogResponse> getLatestChatLogs() {
        return chatLogRepository.findTop100ByOrderByCreatedAtDesc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Search chat logs by keyword
     * Searches both userQuestion and botAnswer fields
     * 
     * @param keyword The keyword to search for
     * @return List of chat logs matching the keyword
     */
    public List<ChatLogResponse> searchChatLogs(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }
        return chatLogRepository.searchByKeyword(keyword).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all chat logs that need review
     * These are logs where the bot answer indicates insufficient information
     * 
     * @return List of chat logs marked for review
     */
    public List<ChatLogResponse> getUnansweredChatLogs() {
        return chatLogRepository.findByNeedsReviewTrueOrderByCreatedAtDesc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Create and save a chat log
     * Automatically detects if the response needs review based on the bot answer
     * 
     * @param userQuestion The user's question
     * @param botAnswer The bot's answer
     * @param sourceType The source type of the answer
     * @param responseTimeMs The response time in milliseconds
     * @return The saved ChatLog entity
     */
    public ChatLog saveChatLog(String userQuestion, String botAnswer, 
                               ChatLog.SourceType sourceType, Long responseTimeMs) {
        Boolean needsReview = detectUnansweredQuery(botAnswer);
        
        ChatLog chatLog = ChatLog.builder()
                .userQuestion(userQuestion)
                .botAnswer(botAnswer)
                .sourceType(sourceType)
                .responseTimeMs(responseTimeMs)
                .needsReview(needsReview)
                .build();
        
        return chatLogRepository.save(chatLog);
    }
    
    /**
     * Detect if a chat response indicates an unanswered query
     * 
     * @param botAnswer The bot's answer text
     * @return true if the answer indicates insufficient information, false otherwise
     */
    public Boolean detectUnansweredQuery(String botAnswer) {
        return botAnswer != null && botAnswer.contains(INSUFFICIENT_INFO_MARKER);
    }
    
    /**
     * Map ChatLog entity to ChatLogResponse DTO
     * 
     * @param chatLog The ChatLog entity
     * @return ChatLogResponse DTO
     */
    private ChatLogResponse mapToResponse(ChatLog chatLog) {
        return ChatLogResponse.builder()
                .id(chatLog.getId())
                .userQuestion(chatLog.getUserQuestion())
                .botAnswer(chatLog.getBotAnswer())
                .sourceType(chatLog.getSourceType().toString())
                .responseTimeMs(chatLog.getResponseTimeMs())
                .needsReview(chatLog.getNeedsReview())
                .createdAt(chatLog.getCreatedAt())
                .build();
    }
}
