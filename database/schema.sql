-- Smart FAQ Chatbot Database Schema
-- This script creates the database and all required tables
-- Run this script before starting the application

-- Create database
CREATE DATABASE IF NOT EXISTS faq_chatbot
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE faq_chatbot;

-- Table: admin_users
-- Stores administrator user information
CREATE TABLE IF NOT EXISTS admin_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    role VARCHAR(20) NOT NULL DEFAULT 'ADMIN',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    profile_picture VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login_at TIMESTAMP NULL,
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table: faq_documents
-- Stores uploaded FAQ document metadata
CREATE TABLE IF NOT EXISTS faq_documents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    file_size BIGINT NOT NULL,
    title VARCHAR(255),
    description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PROCESSING',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    uploaded_by_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    processed_at TIMESTAMP NULL,
    processing_error TEXT,
    INDEX idx_uploaded_by (uploaded_by_id),
    INDEX idx_status (status),
    INDEX idx_active (active),
    INDEX idx_active_status (active, status),
    FOREIGN KEY (uploaded_by_id) REFERENCES admin_users(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table: faq_chunks
-- Stores document chunks for RAG
CREATE TABLE IF NOT EXISTS faq_chunks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    document_id BIGINT NOT NULL,
    chunk_index INT NOT NULL,
    content TEXT NOT NULL,
    content_length INT NOT NULL,
    embedding TEXT,
    metadata VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    embedding_generated_at TIMESTAMP NULL,
    INDEX idx_document_id (document_id),
    INDEX idx_chunk_index (chunk_index),
    INDEX idx_active (active),
    INDEX idx_document_chunk (document_id, chunk_index),
    FOREIGN KEY (document_id) REFERENCES faq_documents(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table: chat_sessions
-- Stores chat conversation sessions
CREATE TABLE IF NOT EXISTS chat_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id VARCHAR(100) NOT NULL UNIQUE,
    user_name VARCHAR(100),
    user_email VARCHAR(150),
    ip_address VARCHAR(45),
    user_agent VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_message_at TIMESTAMP NULL,
    closed_at TIMESTAMP NULL,
    message_count INT NOT NULL DEFAULT 0,
    INDEX idx_session_id (session_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    INDEX idx_last_message (last_message_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table: chat_messages
-- Stores individual chat messages
CREATE TABLE IF NOT EXISTS chat_messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT NOT NULL,
    role VARCHAR(20) NOT NULL,
    content TEXT NOT NULL,
    message_index INT NOT NULL,
    token_count INT,
    context_documents VARCHAR(500),
    relevance_score DOUBLE,
    is_error BOOLEAN NOT NULL DEFAULT FALSE,
    error_message TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    response_time_ms INT,
    INDEX idx_session_id (session_id),
    INDEX idx_created_at (created_at),
    INDEX idx_message_index (message_index),
    INDEX idx_session_message (session_id, message_index),
    FOREIGN KEY (session_id) REFERENCES chat_sessions(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert default super admin (password: admin123 - CHANGE THIS IN PRODUCTION!)
-- Note: Password should be bcrypt encrypted in production
-- This is a placeholder for initial setup
INSERT INTO admin_users (username, email, password, full_name, role, active)
VALUES (
    'admin',
    'admin@faqchatbot.com',
    '$2a$10$slYQMmLc0NVEXr.N.CxUe.EoqBh2yP5DhvhqGJGPmT.ItVHGQwO/W', -- bcrypt hash of "admin123"
    'System Administrator',
    'SUPER_ADMIN',
    TRUE
) ON DUPLICATE KEY UPDATE id=id;

-- Verification queries
-- Uncomment to run after schema creation

-- Show all tables
-- SHOW TABLES;

-- Verify table structures
-- DESCRIBE admin_users;
-- DESCRIBE faq_documents;
-- DESCRIBE faq_chunks;
-- DESCRIBE chat_sessions;
-- DESCRIBE chat_messages;

-- Check foreign key relationships
-- SELECT 
--     TABLE_NAME,
--     COLUMN_NAME,
--     CONSTRAINT_NAME,
--     REFERENCED_TABLE_NAME,
--     REFERENCED_COLUMN_NAME
-- FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
-- WHERE TABLE_SCHEMA = 'faq_chatbot'
-- AND REFERENCED_TABLE_NAME IS NOT NULL;

-- Database setup complete!
-- Next steps:
-- 1. Update application.yml with your database credentials
-- 2. Start the Spring Boot application
-- 3. Login with username: admin, password: admin123 (CHANGE THIS!)
