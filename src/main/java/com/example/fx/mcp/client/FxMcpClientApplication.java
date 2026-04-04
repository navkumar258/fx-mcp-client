package com.example.fx.mcp.client;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientStreamableHttpTransport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.net.ssl.*;
import java.net.http.HttpClient;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

@SpringBootApplication
public class FxMcpClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(FxMcpClientApplication.class, args);
  }

  @Bean
  public McpSyncClient mcpSyncClient() throws Exception {
    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, new TrustManager[]{
            new X509TrustManager() {
              public void checkClientTrusted(X509Certificate[] chain, String authType) {}
              public void checkServerTrusted(X509Certificate[] chain, String authType) {}
              public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
            }
    }, new SecureRandom());

    HttpClient.Builder httpClientBuilder = HttpClient.newBuilder()
            .sslContext(sslContext);

    HttpClientStreamableHttpTransport transport = HttpClientStreamableHttpTransport
            .builder("https://localhost:8443")
            .clientBuilder(httpClientBuilder)
            .build();

        return McpClient.sync(transport).build();
  }
}
