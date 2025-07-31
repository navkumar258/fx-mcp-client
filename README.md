# FX MCP Client

A Spring Boot application that serves as an MCP (Model Context Protocol) client for FX rate subscription management. This client integrates with AI models to provide natural language processing capabilities for managing foreign exchange subscriptions through a conversational interface.

## �� Features

### Core Functionality
- **MCP Client Integration**: Model Context Protocol client for AI tool interactions
- **Spring AI Integration**: Natural language processing via Ollama for subscription management
- **SSE (Server-Sent Events) Support**: Real-time communication with MCP server
- **SSL/TLS Configuration**: Configurable SSL bypass for development environments
- **RESTful API**: Chat endpoint for AI-powered FX subscription management

### Technical Features
- **Spring Boot 3.5.4**: Latest Spring Boot version with Java 21
- **Spring AI**: AI model integration with multiple providers (Ollama, Anthropic, OpenAI)
- **MCP Protocol**: Model Context Protocol client implementation
- **HTTP Client**: Customizable HTTP client with SSL configuration
- **Configuration Management**: Externalized configuration with conditional bean creation

## �� Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Web Client    │    │   Mobile App    │    │   AI Chat Bot   │
└─────────┬───────┘    └─────────┬───────┘    └─────────┬───────┘
           │                      │                      │
           └──────────────────────┼──────────────────────┘
                                  │
                     ┌─────────────▼─────────────┐
                     │     FX MCP Client         │
                     │                           │
                     │  ┌─────────────────────┐  │
                     │  │   Chat Controller   │  │
                     │  └─────────┬───────────┘  │
                     │            │              │
                     │  ┌─────────▼───────────┐  │
                     │  │   Spring AI Config  │  │
                     │  └─────────┬───────────┘  │
                     │            │              │
                     │  ┌─────────▼───────────┐  │
                     │  │   MCP Sync Client   │  │
                     │  └─────────┬───────────┘  │
                     └────────────┼──────────────┘
                                  │
                     ┌─────────────▼─────────────┐
                     │   MCP Server (SSE)       │
                     │   (FX Subscription Svc)  │
                     └───────────────────────────┘
```

## 📝 Prerequisites

- Java 21+
- Gradle 8.0+
- Ollama (for local AI model)
- FX Subscription Service (MCP Server) running

## 🛠️ Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd fx-mcp-client
```

### 2. Build the Application
```bash
./gradlew build
```

### 3. Start Ollama (for AI features)
```bash
# Install Ollama first, then run:
ollama run qwen3
```

### 4. Start FX Subscription Service (MCP Server)
```bash
# Ensure the FX Subscription Service is running on https://localhost:8443
# This serves as the MCP server for this client
```

### 5. Run the Application
```bash
./gradlew bootRun
```

The application will start on `http://localhost:8081`

## ⚙️ Configuration

### Environment Variables
```bash
# Application Configuration
spring.application.name=fx-mcp-client
server.port=8081

# AI Model Configuration
spring.ai.model.chat=ollama
spring.ai.model.embedding=none
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.options.model=qwen3

# MCP Server Configuration
fx.mcp.server.base-url=https://localhost:8443
fx.mcp.server.sse-endpoint=/sse
fx.mcp.server.bypass-ssl=true

# Alternative AI Providers
# Anthropic Configuration
#spring.ai.anthropic.api-key=${ANTHROPIC_API_KEY}

# OpenAI Configuration
#spring.ai.openai.api-key=${OPENAI_API_KEY}
#spring.ai.openai.base-url=http://localhost:12434/engines
#spring.ai.openai.chat.options.model=ai/gemma3
```

### SSL Configuration
The application supports conditional SSL bypass for development environments:

- **Development**: Set `fx.mcp.server.bypass-ssl=true` to bypass SSL verification
- **Production**: Set `fx.mcp.server.bypass-ssl=false` for proper SSL verification

## 📄 API Documentation

### AI Chat Endpoint

#### Natural Language FX Subscription Management
```http
GET /api/v1/ai/fx?query=Create a subscription for GBP/USD above 1.25 with email notifications
```

**Example Queries:**
- "Create a subscription for EUR/USD below 1.10"
- "Update subscription 123 to threshold 1.30"
- "Delete subscription 456"
- "Show all my subscriptions"

**Response Format:**
```json
{
  "content": "I've successfully created a subscription for GBP/USD above 1.25 with email notifications.",
  "metadata": {
    "tool_calls": [
      {
        "tool": "createFxSubscription",
        "parameters": {
          "userId": "user123",
          "currencyPair": "GBP/USD",
          "thresholdValue": 1.25,
          "notificationMethod": "email"
        }
      }
    ]
  }
}
```

## 🔧 Development Configuration

### SSL Bypass for Development
The application includes conditional bean creation for SSL bypass:

```java
@Bean
@ConditionalOnProperty(name = "fx.mcp.server.bypass-ssl", havingValue = "true")
McpSyncClient mcpSyncClientWithSslBypass(...)

@Bean
@ConditionalOnProperty(name = "fx.mcp.server.bypass-ssl", havingValue = "false", matchIfMissing = true)
McpSyncClient mcpSyncClient(...)
```

### MCP Tool Integration
The client integrates with the following MCP tools:
- `createFxSubscription` - Create new FX rate subscriptions
- `updateFxSubscription` - Update existing subscriptions
- `deleteFxSubscription` - Delete subscriptions
- `getFxSubscriptionsForUser` - Retrieve user subscriptions

## 🤖 AI Integration

### Features
- Natural language subscription management
- Conversational interface for FX operations
- Automated parameter extraction from user queries
- Error handling and validation
- Tool-based AI interactions via MCP

### Available AI Models
- **Ollama**: Local AI model (default: qwen3)
- **Anthropic**: Claude models (requires API key)
- **OpenAI**: GPT models (requires API key)

### System Prompt
The AI is configured with a comprehensive system prompt that:
- Defines the AI's role as an FX Rate Subscription Manager
- Provides guidelines for tool usage
- Ensures proper parameter extraction
- Handles error scenarios gracefully

## 🔐 Security

### SSL/TLS Configuration
- Configurable SSL bypass for development
- Custom SSL context for development environments
- Production-ready SSL verification

### Network Security
- HTTPS communication with MCP server
- Configurable trust managers for development
- Secure SSE endpoint communication

## 📊 Monitoring

### Health Endpoints
- `GET /actuator/health` - Application health status
- `GET /actuator/info` - Application information

### Logging
- Spring Boot default logging
- AI interaction logging via SimpleLoggerAdvisor
- MCP client interaction logging

## 🧪 Testing

### Test Coverage
- Application context loading tests
- Basic functionality verification

### Running Tests
```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests FxMcpClientApplicationTests
```

## 🚀 Deployment

### Docker
```bash
# Build Docker image
docker build -t fx-mcp-client .

# Run container
docker run -p 8081:8081 fx-mcp-client
```

### Docker Compose
```yaml
version: '3.8'
services:
  fx-mcp-client:
    build: .
    ports:
      - "8081:8081"
    environment:
      - fx.mcp.server.base-url=https://fx-subscription-service:8443
      - fx.mcp.server.bypass-ssl=false
    depends_on:
      - fx-subscription-service
```

## 🔧 Development

### Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/example/fx/mcp/client/
│   │       ├── controller/     # REST controllers
│   │       ├── config/         # Configuration classes
│   │       └── FxMcpClientApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/                   # Test classes
```

### Key Components
- **FxMcpClientApplication**: Main application class with MCP client configuration
- **SpringAiConfig**: Spring AI configuration with MCP tool integration
- **ChatController**: REST endpoint for AI chat interactions

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Check the documentation
- Review existing issues and discussions

##  Version History

- **v1.0.0** - Initial release with MCP client and AI integration
- **v1.1.0** - Added SSL bypass configuration for development
- **v1.2.0** - Enhanced AI prompt and tool integration

---

**Note**: This client is designed to work with the FX Subscription Service as the MCP server. Ensure the server is running and properly configured before using this client.