# Database Schema - Entity Relationship Documentation

## Overview
This document describes the database schema for the Smart FAQ Chatbot Backend system. The system consists of 5 core entities that manage admin users, FAQ documents, document chunks for RAG, chat sessions, and chat messages.

---

## Entity Relationship Diagram (ER)

```
┌─────────────────┐
│   AdminUser     │
│─────────────────│
│ id (PK)         │
│ username        │
│ email           │
│ password        │
│ fullName        │
│ role            │
│ active          │
│ profilePicture  │
│ createdAt       │
│ updatedAt       │
│ lastLoginAt     │
└─────────────────┘
        │
        │ 1:N (uploads)
        │
        ▼
┌─────────────────┐         1:N          ┌─────────────────┐
│  FaqDocument    │◄────────────────────►│   FaqChunk      │
│─────────────────│                      │─────────────────│
│ id (PK)         │                      │ id (PK)         │
│ fileName        │                      │ document_id(FK) │
│ filePath        │                      │ chunkIndex      │
│ fileType        │                      │ content         │
│ fileSize        │                      │ contentLength   │
│ title           │                      │ embedding       │
│ description     │                      │ metadata        │
│ status          │                      │ active          │
│ active          │                      │ createdAt       │
│ uploaded_by_id  │                      │ embeddingGenAt  │
│ createdAt       │                      └─────────────────┘
│ updatedAt       │
│ processedAt     │
│ processingError │
└─────────────────┘


┌─────────────────┐         1:N          ┌─────────────────┐
│  ChatSession    │◄────────────────────►│  ChatMessage    │
│─────────────────│                      │─────────────────│
│ id (PK)         │                      │ id (PK)         │
│ sessionId       │                      │ session_id (FK) │
│ userName        │                      │ role            │
│ userEmail       │                      │ content         │
│ ipAddress       │                      │ messageIndex    │
│ userAgent       │                      │ tokenCount      │
│ status          │                      │ contextDocs     │
│ createdAt       │                      │ relevanceScore  │
│ updatedAt       │                      │ isError         │
│ lastMessageAt   │                      │ errorMessage    │
│ closedAt        │                      │ createdAt       │
│ messageCount    │                      │ responseTimeMs  │
└─────────────────┘                      └─────────────────┘
```

---

## Entity Details

### 1. AdminUser
**Purpose**: Manages administrator accounts who can upload and manage FAQ documents.

**Fields**:
- `id` (BIGINT, PK, AUTO_INCREMENT): Primary key
- `username` (VARCHAR(100), UNIQUE, NOT NULL): Unique username for login
- `email` (VARCHAR(150), UNIQUE, NOT NULL): Admin email address
- `password` (VARCHAR(255), NOT NULL): Encrypted password
- `fullName` (VARCHAR(100)): Admin's full name
- `role` (ENUM, NOT NULL): User role - SUPER_ADMIN, ADMIN, MODERATOR
- `active` (BOOLEAN, DEFAULT true): Account active status
- `profilePicture` (VARCHAR(255)): Profile picture URL
- `createdAt` (TIMESTAMP): Record creation timestamp
- `updatedAt` (TIMESTAMP): Last update timestamp
- `lastLoginAt` (TIMESTAMP): Last login timestamp

**Relationships**:
- One-to-Many with FaqDocument (one admin can upload many documents)

---

### 2. FaqDocument
**Purpose**: Stores uploaded FAQ documents and their metadata.

**Fields**:
- `id` (BIGINT, PK, AUTO_INCREMENT): Primary key
- `fileName` (VARCHAR(255), NOT NULL): Original file name
- `filePath` (VARCHAR(500), NOT NULL): Storage path
- `fileType` (VARCHAR(50), NOT NULL): File extension (pdf, txt, doc, docx)
- `fileSize` (BIGINT, NOT NULL): File size in bytes
- `title` (VARCHAR(255)): Document title
- `description` (TEXT): Document description
- `status` (ENUM, NOT NULL): UPLOADING, PROCESSING, PROCESSED, FAILED, ARCHIVED
- `active` (BOOLEAN, DEFAULT true): Document active status
- `uploaded_by_id` (BIGINT, FK, NOT NULL): Reference to AdminUser
- `createdAt` (TIMESTAMP): Upload timestamp
- `updatedAt` (TIMESTAMP): Last update timestamp
- `processedAt` (TIMESTAMP): Processing completion timestamp
- `processingError` (TEXT): Error message if processing failed

**Relationships**:
- Many-to-One with AdminUser (many documents are uploaded by one admin)
- One-to-Many with FaqChunk (one document is split into many chunks)

**Cascade Behavior**:
- When a document is deleted, all associated chunks are deleted (CASCADE)

---

### 3. FaqChunk
**Purpose**: Stores text chunks from FAQ documents for RAG (Retrieval-Augmented Generation).

**Fields**:
- `id` (BIGINT, PK, AUTO_INCREMENT): Primary key
- `document_id` (BIGINT, FK, NOT NULL): Reference to FaqDocument
- `chunkIndex` (INT, NOT NULL): Sequential order of chunk in document
- `content` (TEXT, NOT NULL): Actual text content of the chunk
- `contentLength` (INT, NOT NULL): Character count of content
- `embedding` (TEXT): Vector embedding for semantic search (JSON or serialized format)
- `metadata` (VARCHAR(500)): Additional chunk metadata (page number, section, etc.)
- `active` (BOOLEAN, DEFAULT true): Chunk active status
- `createdAt` (TIMESTAMP): Chunk creation timestamp
- `embeddingGeneratedAt` (TIMESTAMP): When embedding was generated

**Indexes**:
- `idx_document_id` on `document_id` (for fast retrieval by document)
- `idx_chunk_index` on `chunkIndex` (for ordered retrieval)

**Relationships**:
- Many-to-One with FaqDocument (many chunks belong to one document)

---

### 4. ChatSession
**Purpose**: Manages individual chat conversation sessions with end users.

**Fields**:
- `id` (BIGINT, PK, AUTO_INCREMENT): Primary key
- `sessionId` (VARCHAR(100), UNIQUE, NOT NULL): Unique session identifier (UUID)
- `userName` (VARCHAR(100)): Optional user name
- `userEmail` (VARCHAR(150)): Optional user email
- `ipAddress` (VARCHAR(45)): User's IP address (IPv4/IPv6)
- `userAgent` (VARCHAR(500)): Browser user agent string
- `status` (ENUM, NOT NULL): ACTIVE, CLOSED, EXPIRED, ARCHIVED
- `createdAt` (TIMESTAMP): Session start timestamp
- `updatedAt` (TIMESTAMP): Last update timestamp
- `lastMessageAt` (TIMESTAMP): Last message timestamp
- `closedAt` (TIMESTAMP): Session close timestamp
- `messageCount` (INT, DEFAULT 0): Total number of messages

**Indexes**:
- `idx_session_id` on `sessionId` (for fast session lookup)
- `idx_created_at` on `createdAt` (for time-based queries)

**Relationships**:
- One-to-Many with ChatMessage (one session contains many messages)

**Cascade Behavior**:
- When a session is deleted, all associated messages are deleted (CASCADE)

---

### 5. ChatMessage
**Purpose**: Stores individual messages within a chat session.

**Fields**:
- `id` (BIGINT, PK, AUTO_INCREMENT): Primary key
- `session_id` (BIGINT, FK, NOT NULL): Reference to ChatSession
- `role` (ENUM, NOT NULL): USER, ASSISTANT, SYSTEM
- `content` (TEXT, NOT NULL): Message text content
- `messageIndex` (INT, NOT NULL): Sequential order of message in session
- `tokenCount` (INT): Token count for API billing
- `contextDocuments` (VARCHAR(500)): IDs of FAQ chunks used for context
- `relevanceScore` (DOUBLE): Relevance score of retrieved context
- `isError` (BOOLEAN, DEFAULT false): Whether message is an error
- `errorMessage` (TEXT): Error details if applicable
- `createdAt` (TIMESTAMP): Message creation timestamp
- `responseTimeMs` (INT): Response generation time in milliseconds

**Indexes**:
- `idx_session_id` on `session_id` (for fast retrieval by session)
- `idx_created_at` on `createdAt` (for time-based queries)

**Relationships**:
- Many-to-One with ChatSession (many messages belong to one session)

---

## Relationship Summary

### Primary Relationships

1. **AdminUser → FaqDocument** (1:N)
   - One admin can upload multiple FAQ documents
   - Foreign Key: `FaqDocument.uploaded_by_id` → `AdminUser.id`
   - Delete Behavior: RESTRICT (cannot delete admin with documents)

2. **FaqDocument → FaqChunk** (1:N)
   - One document is split into multiple chunks
   - Foreign Key: `FaqChunk.document_id` → `FaqDocument.id`
   - Delete Behavior: CASCADE (deleting document deletes all chunks)

3. **ChatSession → ChatMessage** (1:N)
   - One session contains multiple messages
   - Foreign Key: `ChatMessage.session_id` → `ChatSession.id`
   - Delete Behavior: CASCADE (deleting session deletes all messages)

### Independent Entities
- ChatSession and ChatMessage are independent from AdminUser and FAQ entities
- This allows user conversations to continue even if documents are modified

---

## Database Indexes Strategy

### Performance-Critical Indexes

1. **AdminUser**
   - Unique index on `username`
   - Unique index on `email`

2. **FaqDocument**
   - Index on `uploaded_by_id` (FK)
   - Composite index on `(active, status)` for filtered queries

3. **FaqChunk**
   - Index on `document_id` (FK)
   - Index on `chunk_index` for ordered retrieval
   - Consider full-text index on `content` for text search

4. **ChatSession**
   - Unique index on `sessionId`
   - Index on `createdAt` for time-based queries
   - Index on `status` for active session queries

5. **ChatMessage**
   - Index on `session_id` (FK)
   - Index on `createdAt` for time-based queries
   - Composite index on `(session_id, messageIndex)` for ordered retrieval

---

## Data Flow

### FAQ Document Processing Flow
```
1. AdminUser uploads document → FaqDocument created (status: UPLOADING)
2. System processes file → FaqDocument status: PROCESSING
3. System chunks document → FaqChunk records created
4. System generates embeddings → FaqChunk.embedding populated
5. Processing complete → FaqDocument status: PROCESSED
```

### Chat Session Flow
```
1. User starts chat → ChatSession created (status: ACTIVE)
2. User sends message → ChatMessage created (role: USER)
3. System retrieves relevant FaqChunks
4. System calls Claude API with context
5. Assistant responds → ChatMessage created (role: ASSISTANT)
6. Repeat steps 2-5 for conversation
7. Session timeout/close → ChatSession status: CLOSED
```

---

## Scalability Considerations

1. **FaqChunk Table**
   - Expected to be the largest table
   - Consider partitioning by `document_id` for large datasets
   - Embedding storage may require BLOB or JSON columns

2. **ChatMessage Table**
   - High write frequency during active chats
   - Consider time-based partitioning (monthly/yearly)
   - Archive old messages to separate table

3. **Session Management**
   - Implement automatic session expiry (cronjob)
   - Archive closed sessions after retention period

4. **Embedding Storage**
   - Consider dedicated vector database (Pinecone, Weaviate) for production
   - Current TEXT storage suitable for POC/MVP

---

## Future Enhancements

1. **User Authentication**
   - Add separate EndUser entity for authenticated users
   - Link ChatSession to EndUser (optional FK)

2. **Feedback System**
   - Add ChatMessageFeedback entity (thumbs up/down)
   - Track message usefulness for model improvement

3. **Document Categories**
   - Add Category entity
   - Many-to-Many relationship with FaqDocument

4. **Analytics**
   - Add ChatAnalytics entity for aggregated metrics
   - Track popular queries, response times, satisfaction

---

## Notes

- All timestamps use UTC timezone
- Password fields are encrypted using bcrypt (implementation in service layer)
- Session IDs use UUID v4 format
- File paths are relative to configured storage directory
- Embedding format depends on embedding model (to be determined in implementation)
