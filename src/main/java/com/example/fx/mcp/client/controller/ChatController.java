package com.example.fx.mcp.client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ChatController {
  private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);
  private final ChatClient chatClient;

  public ChatController(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  @GetMapping("/ai/fx")
  public String fxChat(@RequestParam(value = "query") String userQuery) {
    String systemMessage = """
            You are an FX Rate Subscription Manager AI. ...
            """;

    PromptTemplate promptTemplate = new PromptTemplate(systemMessage + "\nUser query: {query}");
    Prompt prompt = new Prompt(promptTemplate.createMessage(Map.of("query", userQuery)));

    String response = chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getText();

    LOGGER.info("LLM Response: {}", response);
    return response;
  }
}
