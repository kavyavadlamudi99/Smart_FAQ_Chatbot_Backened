# Quick Setup Guide

This guide will help you set up the Smart FAQ Chatbot Backend in under 10 minutes.

## Prerequisites Checklist

Before starting, ensure you have:
- [ ] Java 17 or higher installed
- [ ] Maven 3.6+ installed
- [ ] MySQL 8.0+ installed and running
- [ ] Claude API key from Anthropic ([Get one here](https://console.anthropic.com/))
- [ ] Git installed

## Step-by-Step Setup

### Step 1: Clone and Navigate
```bash
git clone <repository-url>
cd Smart_FAQ_Chatbot_Backened
```

### Step 2: Database Setup

#### Option A: Using SQL Script (Recommended)
```bash
# Login to MySQL
mysql -u root -p

# Run the schema script
mysql -u root -p < database/schema.sql
```

#### Option B: Let Spring Boot Create Tables
Create an empty database and Spring Boot will auto-create tables:
```sql
CREATE DATABASE faq_chatbot;
```

### Step 3: Configure Application

Create your local configuration file:
```bash
cp src/main/resources/application-local.yml.template src/main/resources/application-local.yml
```

Edit `src/main/resources/application-local.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/faq_chatbot
    username: root           # Your MySQL username
    password: yourpassword   # Your MySQL password

claude:
  api:
    key: sk-ant-api...      # Your Claude API key
```

### Step 4: Build the Project
```bash
mvn clean install
```

### Step 5: Run the Application
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Step 6: Verify Installation

The application should start successfully. You'll see:
```
Started SmartFaqChatbotApplication in X.XXX seconds
```

Access the application at: `http://localhost:8080`

## Environment Variables Method (Alternative)

Instead of creating `application-local.yml`, you can use environment variables:

### macOS/Linux:
```bash
export DB_USERNAME="root"
export DB_PASSWORD="yourpassword"
export CLAUDE_API_KEY="sk-ant-api..."
export SERVER_PORT=8080

mvn spring-boot:run
```

### Windows (Command Prompt):
```cmd
set DB_USERNAME=root
set DB_PASSWORD=yourpassword
set CLAUDE_API_KEY=sk-ant-api...
set SERVER_PORT=8080

mvn spring-boot:run
```

### Windows (PowerShell):
```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="yourpassword"
$env:CLAUDE_API_KEY="sk-ant-api..."
$env:SERVER_PORT=8080

mvn spring-boot:run
```

## Verify Setup

### 1. Check Database Connection
Look for this in the logs:
```
HikariPool-1 - Start completed.
```

### 2. Check Tables Created
```sql
USE faq_chatbot;
SHOW TABLES;
```

You should see:
- admin_users
- faq_documents
- faq_chunks
- chat_sessions
- chat_messages

### 3. Test Default Admin Account
If you used the SQL script, a default admin account is created:
- Username: `admin`
- Password: `admin123`

**⚠️ IMPORTANT**: Change this password immediately in production!

## Common Issues and Solutions

### Issue 1: "Access denied for user"
**Solution**: Check your MySQL username and password in `application-local.yml`

### Issue 2: "Unknown database 'faq_chatbot'"
**Solution**: Create the database:
```sql
CREATE DATABASE faq_chatbot;
```

### Issue 3: "Communications link failure"
**Solution**: 
1. Ensure MySQL is running: `sudo systemctl status mysql` (Linux) or check Activity Monitor/Task Manager
2. Check MySQL port (default: 3306)
3. Verify connection string in configuration

### Issue 4: "Table 'faq_chatbot.admin_users' doesn't exist"
**Solution**: 
1. Check `spring.jpa.hibernate.ddl-auto` is set to `update` in application.yml
2. Or run the `database/schema.sql` script manually

### Issue 5: Maven build fails
**Solution**: 
1. Check Java version: `java -version` (should be 17+)
2. Check Maven version: `mvn -version` (should be 3.6+)
3. Clear Maven cache: `mvn clean`

### Issue 6: Port 8080 already in use
**Solution**: Change the port in `application.yml`:
```yaml
server:
  port: 8081
```

## Next Steps

After successful setup:

1. **Explore the Code**
   - Check out the entity classes in `src/main/java/com/faq/chatbot/entity/`
   - Review the database schema in `docs/DATABASE_SCHEMA.md`
   - Understand the project structure in `docs/PROJECT_STRUCTURE.md`

2. **Development Tasks**
   - Implement service layer (Day 2)
   - Create REST controllers (Day 2)
   - Add security (Day 2)
   - Integrate Claude API (Day 3)

3. **Testing**
   - Access logs at `logs/application.log`
   - Check database records with MySQL client
   - Use Postman or curl to test APIs (once implemented)

## IDE Setup

### IntelliJ IDEA
1. File → Open → Select project folder
2. Maven projects will be auto-detected
3. Enable Lombok plugin: File → Settings → Plugins → Search "Lombok"
4. Enable annotation processing: Settings → Build → Compiler → Annotation Processors

### Eclipse
1. File → Import → Maven → Existing Maven Projects
2. Install Lombok: 
   - Download `lombok.jar` from [projectlombok.org](https://projectlombok.org/)
   - Run: `java -jar lombok.jar`
   - Select Eclipse installation directory

### VS Code
1. Install extensions:
   - Extension Pack for Java
   - Spring Boot Extension Pack
   - Lombok Annotations Support
2. Open project folder
3. Trust workspace when prompted

## Production Deployment Checklist

Before deploying to production:

- [ ] Change default admin password
- [ ] Use strong MySQL password
- [ ] Secure Claude API key (use secrets manager)
- [ ] Set `spring.jpa.hibernate.ddl-auto=validate` (not `update`)
- [ ] Configure proper CORS origins
- [ ] Enable HTTPS/SSL
- [ ] Set up database backups
- [ ] Configure logging to file/service
- [ ] Set up monitoring (health checks)
- [ ] Review security settings
- [ ] Configure rate limiting
- [ ] Set up firewall rules

## Getting Help

- **Documentation**: Check the `docs/` folder
- **Issues**: Create an issue on GitHub
- **Questions**: Contact the development team

## Quick Reference Commands

```bash
# Build
mvn clean install

# Run
mvn spring-boot:run

# Run with profile
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Run tests
mvn test

# Package for production
mvn clean package -DskipTests

# Run packaged JAR
java -jar target/chatbot-0.0.1-SNAPSHOT.jar

# Check Java version
java -version

# Check Maven version
mvn -version

# Clean build artifacts
mvn clean
```

---

## ✅ Setup Complete!

You're now ready to start developing the Smart FAQ Chatbot Backend. Happy coding! 🚀
