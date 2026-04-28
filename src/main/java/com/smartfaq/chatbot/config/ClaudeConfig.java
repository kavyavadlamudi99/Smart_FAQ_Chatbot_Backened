package com.smartfaq.chatbot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Claude API Configuration Properties
 * Binds Claude API related configuration from application.yml
 */
@Configuration
@ConfigurationProperties(prefix = "claude.api")
@Getter
@Setter
public class ClaudeConfig {
    
    private String key;
    private String baseUrl;
    private String model;
    private Integer maxTokens;
    private Double temperature;
}
