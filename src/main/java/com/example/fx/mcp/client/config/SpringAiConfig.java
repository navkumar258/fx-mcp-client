package com.example.fx.mcp.client.config;

import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringAiConfig {
  private final ChatModel chatModel;

  public SpringAiConfig(ChatModel chatModel) {
    this.chatModel = chatModel;
  }

  @Bean
  public ChatClient chatClient(McpSyncClient mcpSyncClient) {
    return ChatClient
            .builder(chatModel)
            .defaultAdvisors(new SimpleLoggerAdvisor())
            .defaultToolCallbacks(new SyncMcpToolCallbackProvider(mcpSyncClient))
            .build();
  }
}
