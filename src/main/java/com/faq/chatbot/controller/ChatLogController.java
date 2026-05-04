package com.faq.chatbot.controller;

import com.faq.chatbot.dto.ChatLogResponse;
import com.faq.chatbot.service.ChatLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for ChatLog operations
 * Provides endpoints to retrieve and search chat logs
 */
@RestController
@RequestMapping("/api/chat-logs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChatLogController {
    
    private final ChatLogService chatLogService;
    
    public ChatLogController(ChatLogService chatLogService) {
        this.chatLogService = chatLogService;
    }
    
    /**
     * Get the latest 100 chat logs
     * 
     * @return List of latest 100 chat logs
     */
    @GetMapping
    public ResponseEntity<?> getLatestChatLogs() {
        try {
            List<ChatLogResponse> chatLogs = chatLogService.getLatestChatLogs();
            return ResponseEntity.ok(chatLogs);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve chat logs");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Search chat logs by keyword
     * Searches in both userQuestion and botAnswer fields
     * 
     * @param keyword The keyword to search for
     * @return List of chat logs matching the keyword
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchChatLogs(
            @RequestParam(value = "keyword", required = false) String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Keyword parameter is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
            List<ChatLogResponse> results = chatLogService.searchChatLogs(keyword);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to search chat logs");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Get all unanswered chat logs
     * Returns chat logs where the bot response indicates insufficient information
     * 
     * @return List of chat logs marked for review
     */
    @GetMapping("/unanswered")
    public ResponseEntity<?> getUnansweredChatLogs() {
        try {
            List<ChatLogResponse> unansweredLogs = chatLogService.getUnansweredChatLogs();
            return ResponseEntity.ok(unansweredLogs);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve unanswered chat logs");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
