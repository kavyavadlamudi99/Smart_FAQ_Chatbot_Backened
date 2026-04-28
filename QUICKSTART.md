# Smart FAQ Chatbot Backend - Setup Complete

## Project Overview
Spring Boot backend for a Smart FAQ Chatbot SaaS product with AI-powered Claude integration.

## Configuration

### Technology Stack
- **Java**: 17
- **Spring Boot**: 3.2.5
- **Spring Web**: For REST APIs
- **Spring Data JPA**: For database operations
- **Spring Security**: For security
- **MySQL Driver**: For database connectivity
- **Lombok**: For reducing boilerplate code
- **Validation**: For bean validation

### Base Package
`com.smartfaq.chatbot`

### Project Structure
```
src/main/java/com/smartfaq/chatbot/
├── SmartFaqChatbotApplication.java      # Main application entry point
├── config/
│   ├── AppConfig.java                   # Application configuration
│   ├── ClaudeConfig.java                # Claude API configuration
│   ├── CorsConfig.java                  # CORS configuration
│   └── SecurityConfig.java              # Spring Security configuration
├── controller/
│   └── HealthCheckController.java       # Health check endpoint
└── dto/
    └── response/
        └── HealthCheckResponse.java     # Health check response DTO
```

## API Endpoints

### Health Check
- **Endpoint**: `GET /api/health`
- **Authentication**: Not required (public access)
- **Response**:
```json
{
  "status": "UP",
  "message": "Smart FAQ Chatbot backend is running"
}
```

## Database Configuration
The application is configured to use MySQL with the following environment variables:
- `DB_USERNAME`: Database username (default: root)
- `DB_PASSWORD`: Database password (default: password)
- `CLAUDE_API_KEY`: Your Claude API key

### Database Details
- **URL**: `jdbc:mysql://localhost:3306/faq_chatbot`
- **Driver**: MySQL Connector/J
- **Hibernate DDL**: Update mode (creates/updates tables automatically)

## Spring Security Configuration
The security configuration allows:
- Public access to `/api/health` endpoint
- All other endpoints require authentication
- CSRF protection is disabled for API endpoints
- HTTP Basic authentication is enabled

## Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL Server running locally
- Environment variables set for API keys

### Build
```bash
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

### Development with IDE
1. Import the project into your IDE (IntelliJ IDEA, Eclipse, VS Code)
2. Ensure Java 17 is selected as the project SDK
3. Run `SmartFaqChatbotApplication.java` as a Spring Boot application

## Configuration Files
- `application.yml`: Main application configuration
- `application-local.yml.template`: Template for local development overrides

## Next Steps
1. Configure your MySQL database connection
2. Set up your Claude API key
3. Implement additional controllers and services as needed
4. Add more API endpoints for FAQ management and chat functionality

## Security Considerations
- Spring Security is configured to require authentication for all endpoints except `/api/health`
- CORS is configured to allow requests from localhost:3000 and localhost:4200 by default
- Configure CORS settings via `app.config.cors` properties in `application.yml`
- API key for Claude is managed via environment variables
