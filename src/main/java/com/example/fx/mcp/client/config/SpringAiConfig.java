package com.example.fx.mcp.client.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class SpringAiConfig {

  @Bean
  public SimpleVectorStore vectorStore(EmbeddingModel embeddingModel) {
    SimpleVectorStore store = SimpleVectorStore.builder(embeddingModel).build();
    File storageFile = new File("src/main/resources/vector_store.json");

    // Load existing data if the file exists
    if (storageFile.exists()) {
      store.load(storageFile);
    }

    return store;
  }

  @Bean
  public ChatClient chatClient(ChatModel chatModel, ToolCallbackProvider tools, VectorStore vectorStore) {
    return ChatClient.builder(chatModel)
            .defaultAdvisors(
                    QuestionAnswerAdvisor.builder(vectorStore)
                            .searchRequest(SearchRequest.builder()
                                    .topK(4)
                                    .build())
                            .build(),
                    new SimpleLoggerAdvisor())
            .defaultToolCallbacks(tools)
            .build();
  }
}
