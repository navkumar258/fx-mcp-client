package com.example.fx.mcp.client;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@SpringBootApplication
public class FxMcpClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(FxMcpClientApplication.class, args);
  }

  @Bean
  @ConditionalOnProperty(name = "fx.mcp.server.bypass-ssl", havingValue = "true")
  McpSyncClient mcpSyncClientWithSslBypass(
          @Value("${fx.mcp.server.base-url}") String baseUrl,
          @Value("${fx.mcp.server.sse-endpoint}") String sseEndpoint
  ) {
    McpSyncClient mcpClient = McpClient
            .sync(HttpClientSseClientTransport
                    .builder(baseUrl)
                    .customizeClient(clientCustomizer ->
                    {
                      try {
                        TrustManager[] trustAllCerts = new TrustManager[]
                                {
                                        new X509ExtendedTrustManager() {
                                          public X509Certificate[] getAcceptedIssuers() {
                                            return null;
                                          }

                                          public void checkClientTrusted(
                                                  final X509Certificate[] a_certificates,
                                                  final String a_auth_type) {
                                          }

                                          public void checkServerTrusted(
                                                  final X509Certificate[] a_certificates,
                                                  final String a_auth_type) {
                                          }

                                          public void checkClientTrusted(
                                                  final X509Certificate[] a_certificates,
                                                  final String a_auth_type,
                                                  final Socket a_socket) {
                                          }

                                          public void checkServerTrusted(
                                                  final X509Certificate[] a_certificates,
                                                  final String a_auth_type,
                                                  final Socket a_socket) {
                                          }

                                          public void checkClientTrusted(
                                                  final X509Certificate[] a_certificates,
                                                  final String a_auth_type,
                                                  final SSLEngine a_engine) {
                                          }

                                          public void checkServerTrusted(
                                                  final X509Certificate[] a_certificates,
                                                  final String a_auth_type,
                                                  final SSLEngine a_engine) {
                                          }
                                        }
                                };

                        SSLContext sslContext = SSLContext.getInstance("TLS");
                        sslContext.init(null, trustAllCerts, null);
                        SSLContext.setDefault(sslContext);

                        clientCustomizer.sslContext(sslContext);
                      } catch (KeyManagementException | NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                      }
                    })
                    .sseEndpoint(sseEndpoint)
                    .build())
            .build();

    mcpClient.initialize();
    return mcpClient;
  }

  @Bean
  @ConditionalOnProperty(name = "fx.mcp.server.bypass-ssl", havingValue = "false", matchIfMissing = true)
  McpSyncClient mcpSyncClient(
          @Value("${fx.mcp.server.base-url}") String baseUrl,
          @Value("${fx.mcp.server.sse-endpoint}") String sseEndpoint
  ) {
    McpSyncClient mcpClient = McpClient
            .sync(HttpClientSseClientTransport
                    .builder(baseUrl)
                    .sseEndpoint(sseEndpoint)
                    .build())
            .build();

    mcpClient.initialize();
    return mcpClient;
  }
}
