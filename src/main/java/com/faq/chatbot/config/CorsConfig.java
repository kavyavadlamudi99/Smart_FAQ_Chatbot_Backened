package com.faq.chatbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS Configuration
 * Configures Cross-Origin Resource Sharing settings for the application
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${app.config.cors.allowed-origins}")
    private String allowedOrigins;

    @Value("${app.config.cors.allowed-methods}")
    private String allowedMethods;

    @Value("${app.config.cors.allowed-headers}")
    private String allowedHeaders;

    @Value("${app.config.cors.exposed-headers}")
    private String exposedHeaders;

    @Value("${app.config.cors.allow-credentials}")
    private boolean allowCredentials;

    @Value("${app.config.cors.max-age}")
    private long maxAge;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins.split(","))
                .allowedMethods(allowedMethods.split(","))
                .allowedHeaders(allowedHeaders.split(","))
                .exposedHeaders(exposedHeaders.split(","))
                .allowCredentials(allowCredentials)
                .maxAge(maxAge);
    }
}
