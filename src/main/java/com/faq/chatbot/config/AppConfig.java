package com.faq.chatbot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Application Configuration Properties
 * Binds application-specific configuration from application.yml
 */
@Configuration
@ConfigurationProperties(prefix = "app.config")
@Getter
@Setter
public class AppConfig {
    
    private Upload upload = new Upload();
    private Chat chat = new Chat();
    
    @Getter
    @Setter
    public static class Upload {
        private String maxFileSize;
        private String maxRequestSize;
        private String allowedExtensions;
    }
    
    @Getter
    @Setter
    public static class Chat {
        private Integer maxHistoryMessages;
        private Integer sessionTimeoutMinutes;
    }
}
