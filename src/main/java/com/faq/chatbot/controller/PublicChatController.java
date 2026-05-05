package com.faq.chatbot.controller;

import com.faq.chatbot.dto.ChatRequest;
import com.faq.chatbot.dto.ChatResponse;
import com.faq.chatbot.entity.ChatLog;
import com.faq.chatbot.repository.ChatLogRepository;
import com.faq.chatbot.service.ClaudeService;
import com.faq.chatbot.service.FaqContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Public chat endpoint used by the embedded widget.
 * No authentication required.
 */
@Slf4j
@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class PublicChatController {

    private final FaqContextService faqContextService;
    private final ClaudeService claudeService;
    private final ChatLogRepository chatLogRepository;

    /**
     * Answer a user question using FAQ context.
     * Accessible without authentication for embedded widget use.
     *
     * POST /api/public/chat
     */
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> publicChat(@RequestBody ChatRequest chatRequest) {
        String question = chatRequest.getQuestion();
        long startTime = System.currentTimeMillis();

        try {
            String faqContext = faqContextService.buildContextForQuestion(question);
            String answer = claudeService.generateAnswer(question, faqContext);

            long responseTimeMs = System.currentTimeMillis() - startTime;

            ChatLog chatLog = ChatLog.builder()
                    .userQuestion(question)
                    .botAnswer(answer)
                    .sourceType(ChatLog.SourceType.FAQ_CONTEXT)
                    .responseTimeMs(responseTimeMs)
                    .build();
            chatLogRepository.save(chatLog);

            return ResponseEntity.ok(ChatResponse.builder()
                    .answer(answer)
                    .sourceType("FAQ_CONTEXT")
                    .build());

        } catch (Exception e) {
            log.error("Public chat API failed for question: {}", question, e);

            long responseTimeMs = System.currentTimeMillis() - startTime;
            String fallbackAnswer = "Sorry, I am unable to answer right now. Please contact support.";

            ChatLog errorLog = ChatLog.builder()
                    .userQuestion(question)
                    .botAnswer(fallbackAnswer)
                    .sourceType(ChatLog.SourceType.ERROR)
                    .responseTimeMs(responseTimeMs)
                    .build();
            chatLogRepository.save(errorLog);

            return ResponseEntity.ok(ChatResponse.builder()
                    .answer(fallbackAnswer)
                    .sourceType("ERROR")
                    .build());
        }
    }
}
