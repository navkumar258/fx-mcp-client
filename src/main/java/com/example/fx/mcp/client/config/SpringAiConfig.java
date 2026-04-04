package com.example.fx.mcp.client.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringAiConfig {

  @Bean
  public ChatClient chatClient(ChatModel chatModel, ToolCallbackProvider tools) {
    return ChatClient
            .builder(chatModel)
            .defaultAdvisors(new SimpleLoggerAdvisor())
            .defaultToolCallbacks(tools)
            .build();
  }
}
