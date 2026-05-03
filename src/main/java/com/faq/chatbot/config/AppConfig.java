package com.faq.chatbot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

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
    
    /**
     * BCryptPasswordEncoder bean for password hashing
     *
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
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
