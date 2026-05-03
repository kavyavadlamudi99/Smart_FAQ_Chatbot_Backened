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

@Slf4j
@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class ChatController {

    private final FaqContextService faqContextService;
    private final ClaudeService claudeService;
    private final ChatLogRepository chatLogRepository;

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest chatRequest) {
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
            log.error("Claude API failed for question: {}", question, e);

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
