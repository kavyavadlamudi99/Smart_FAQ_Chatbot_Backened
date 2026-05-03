package com.faq.chatbot.service;

import com.faq.chatbot.config.ClaudeConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClaudeService {

    private static final String SYSTEM_INSTRUCTION =
            "You are a SaaS product support chatbot. Answer only using the provided FAQ context. " +
            "If the answer is not present in the FAQ context, say: " +
            "I don't have enough information from the uploaded FAQ. Please contact support.";

    private final ClaudeConfig claudeConfig;
    private final RestTemplate restTemplate;

    public String generateAnswer(String userQuestion, String faqContext) {
        String userContent = "FAQ Context:\n" + faqContext + "\n\nUser Question: " + userQuestion;

        Map<String, Object> requestBody = Map.of(
                "model", claudeConfig.getModel(),
                "max_tokens", claudeConfig.getMaxTokens() != null ? claudeConfig.getMaxTokens() : 1024,
                "system", SYSTEM_INSTRUCTION,
                "messages", List.of(
                        Map.of("role", "user", "content", userContent)
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", claudeConfig.getKey());
        headers.set("anthropic-version", "2023-06-01");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        String apiUrl = claudeConfig.getUrl();
        if (apiUrl == null || apiUrl.isBlank()) {
            apiUrl = "https://api.anthropic.com/v1/messages";
        }

        log.debug("Sending request to Claude API: {}", apiUrl);

        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.postForObject(apiUrl, request, Map.class);

        if (response == null) {
            throw new RuntimeException("Empty response from Claude API");
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> content = (List<Map<String, Object>>) response.get("content");
        if (content == null || content.isEmpty()) {
            throw new RuntimeException("No content in Claude API response");
        }

        return (String) content.get(0).get("text");
    }
}
