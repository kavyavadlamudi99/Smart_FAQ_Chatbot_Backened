# Project Structure

## Overview
This document describes the package and folder structure of the Smart FAQ Chatbot Backend application.

## Directory Structure

```
Smart_FAQ_Chatbot_Backened/
│
├── docs/                           # Documentation files
│   └── DATABASE_SCHEMA.md         # Database ER diagram and schema documentation
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── faq/
│   │   │           └── chatbot/
│   │   │               ├── SmartFaqChatbotApplication.java  # Main Spring Boot application
│   │   │               │
│   │   │               ├── controller/    # REST API Controllers
│   │   │               │   └── package-info.java
│   │   │               │
│   │   │               ├── service/       # Business Logic Layer
│   │   │               │   └── package-info.java
│   │   │               │
│   │   │               ├── repository/    # Data Access Layer (JPA Repositories)
│   │   │               │   ├── AdminUserRepository.java
│   │   │               │   ├── FaqDocumentRepository.java
│   │   │               │   ├── FaqChunkRepository.java
│   │   │               │   ├── ChatSessionRepository.java
│   │   │               │   ├── ChatMessageRepository.java
│   │   │               │   └── package-info.java
│   │   │               │
│   │   │               ├── entity/        # JPA Entities (Database Models)
│   │   │               │   ├── AdminUser.java
│   │   │               │   ├── FaqDocument.java
│   │   │               │   ├── FaqChunk.java
│   │   │               │   ├── ChatSession.java
│   │   │               │   ├── ChatMessage.java
│   │   │               │   └── package-info.java
│   │   │               │
│   │   │               ├── dto/           # Data Transfer Objects
│   │   │               │   └── package-info.java
│   │   │               │
│   │   │               └── config/        # Spring Configuration Classes
│   │   │                   ├── CorsConfig.java
│   │   │                   ├── ClaudeConfig.java
│   │   │                   ├── AppConfig.java
│   │   │                   └── package-info.java
│   │   │
│   │   └── resources/
│   │       ├── application.yml                    # Main configuration file
│   │       └── application-local.yml.template     # Local environment template
│   │
│   └── test/
│       └── java/
│           └── com/
│               └── faq/
│                   └── chatbot/
│                       └── (test files will go here)
│
├── logs/                           # Application log files (generated at runtime)
├── target/                         # Maven build output (gitignored)
├── .gitignore                      # Git ignore rules
├── pom.xml                         # Maven project configuration
└── README.md                       # Project documentation
```

## Package Descriptions

### 1. `com.faq.chatbot.controller`
**Purpose**: REST API endpoint definitions

**Responsibilities**:
- Handle HTTP requests and responses
- Input validation
- Route requests to appropriate services
- Return DTOs to clients

**Future Classes**:
- `AdminController.java` - Admin authentication and management
- `FaqDocumentController.java` - Document upload and management
- `ChatController.java` - Chat interaction endpoints

---

### 2. `com.faq.chatbot.service`
**Purpose**: Business logic implementation

**Responsibilities**:
- Implement core business rules
- Orchestrate between repositories and external APIs
- Transaction management
- Data transformation between entities and DTOs

**Future Classes**:
- `AdminUserService.java` - Admin user management
- `FaqDocumentService.java` - Document processing logic
- `ChatService.java` - Chat session and message handling
- `ClaudeApiService.java` - Claude API integration
- `EmbeddingService.java` - Text embedding generation

---

### 3. `com.faq.chatbot.repository`
**Purpose**: Database access layer

**Responsibilities**:
- CRUD operations on entities
- Custom queries
- Database interaction abstraction

**Current Classes**:
- `AdminUserRepository.java` - AdminUser data access
- `FaqDocumentRepository.java` - FaqDocument data access
- `FaqChunkRepository.java` - FaqChunk data access
- `ChatSessionRepository.java` - ChatSession data access
- `ChatMessageRepository.java` - ChatMessage data access

---

### 4. `com.faq.chatbot.entity`
**Purpose**: JPA entity definitions (database models)

**Responsibilities**:
- Map Java classes to database tables
- Define relationships between entities
- Validation constraints

**Current Classes**:
- `AdminUser.java` - Admin user entity
- `FaqDocument.java` - FAQ document entity
- `FaqChunk.java` - Document chunk entity
- `ChatSession.java` - Chat session entity
- `ChatMessage.java` - Chat message entity

---

### 5. `com.faq.chatbot.dto`
**Purpose**: Data Transfer Objects

**Responsibilities**:
- API request/response structures
- Decouple internal entities from external API contracts
- Input validation annotations

**Future Classes**:
- Request DTOs:
  - `AdminLoginRequest.java`
  - `DocumentUploadRequest.java`
  - `ChatMessageRequest.java`
- Response DTOs:
  - `AdminResponse.java`
  - `DocumentResponse.java`
  - `ChatResponse.java`
- Common DTOs:
  - `ApiResponse.java`
  - `ErrorResponse.java`

---

### 6. `com.faq.chatbot.config`
**Purpose**: Spring framework configuration

**Responsibilities**:
- Bean definitions
- Application settings
- External service configuration

**Current Classes**:
- `CorsConfig.java` - CORS settings
- `ClaudeConfig.java` - Claude API configuration
- `AppConfig.java` - Application-specific settings

**Future Classes**:
- `SecurityConfig.java` - Security configuration
- `RestTemplateConfig.java` - HTTP client configuration
- `SwaggerConfig.java` - API documentation

---

## Configuration Files

### application.yml
Main configuration file containing:
- Database connection settings
- JPA/Hibernate configuration
- Claude API settings
- CORS configuration
- File upload settings
- Logging configuration
- Server port settings

### application-local.yml.template
Template for local development environment configuration:
- Copy to `application-local.yml`
- Customize with your local settings
- Never commit `application-local.yml` (gitignored)

---

## Database Schema
Refer to [DATABASE_SCHEMA.md](DATABASE_SCHEMA.md) for detailed ER diagram and entity relationships.

---

## Development Workflow

### Adding a New Feature

1. **Define Entity** (if needed)
   - Create entity class in `entity/` package
   - Add JPA annotations
   - Define relationships

2. **Create Repository**
   - Create repository interface in `repository/` package
   - Extend `JpaRepository`
   - Add custom queries if needed

3. **Define DTOs**
   - Create request/response DTOs in `dto/` package
   - Add validation annotations

4. **Implement Service**
   - Create service class in `service/` package
   - Annotate with `@Service`
   - Implement business logic

5. **Create Controller**
   - Create controller class in `controller/` package
   - Annotate with `@RestController` and `@RequestMapping`
   - Define endpoints

6. **Write Tests**
   - Create test classes in `src/test/java/`
   - Write unit tests for services
   - Write integration tests for controllers

---

## Testing Structure

```
src/test/java/com/faq/chatbot/
├── controller/
│   └── *ControllerTest.java
├── service/
│   └── *ServiceTest.java
├── repository/
│   └── *RepositoryTest.java
└── integration/
    └── *IntegrationTest.java
```

---

## Build and Run

### Build Project
```bash
mvn clean install
```

### Run Application
```bash
mvn spring-boot:run
```

### Run with Specific Profile
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Run Tests
```bash
mvn test
```

---

## Dependencies

Current dependencies (from pom.xml):
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- MySQL Connector/J
- Spring Boot Starter Validation
- Lombok
- Spring Boot DevTools
- Spring Boot Starter Test

---

## Naming Conventions

### Packages
- All lowercase
- Use plural for collections (controllers, services)

### Classes
- PascalCase
- Controller classes: end with `Controller`
- Service classes: end with `Service`
- Repository classes: end with `Repository`
- Entity classes: singular nouns
- DTO classes: end with `Request` or `Response`

### Methods
- camelCase
- Repository methods: follow Spring Data naming conventions
- Service methods: describe business action
- Controller methods: HTTP verb + resource name

### Variables
- camelCase
- Descriptive names
- Avoid abbreviations

---

## Code Style Guidelines

1. **Use Lombok** for reducing boilerplate
   - `@Data`, `@Getter`, `@Setter`, `@Builder`, etc.

2. **Document public APIs** with Javadoc
   - Class-level documentation
   - Complex method documentation

3. **Validate inputs** at controller level
   - Use `@Valid` annotation
   - Custom validators when needed

4. **Handle exceptions** properly
   - Use `@ControllerAdvice` for global exception handling
   - Return meaningful error messages

5. **Log appropriately**
   - Use SLF4J logger
   - INFO for important business events
   - DEBUG for detailed troubleshooting
   - ERROR for exceptions

---

## Next Steps (Day 2+)

1. Implement service layer
2. Create controller endpoints
3. Add security (Spring Security + JWT)
4. Implement file upload functionality
5. Integrate Claude API
6. Add exception handling
7. Write unit tests
8. Create API documentation (Swagger)
9. Set up Docker configuration
10. Deploy to production environment
