package com.example.fx.mcp.client;

import com.example.fx.mcp.client.exception.McpClientCreationException;
import com.example.fx.mcp.client.service.PdfIngestionService;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientStreamableHttpTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.http.HttpClient;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

@SpringBootApplication
public class FxMcpClientApplication {

	static void main(String[] args) {
		SpringApplication.run(FxMcpClientApplication.class, args);
	}

	@Bean
	public McpSyncClient mcpSyncClient() {
		try {
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[]{
					new X509TrustManager() {
						public void checkClientTrusted(X509Certificate[] chain, String authType) {
						}

						public void checkServerTrusted(X509Certificate[] chain, String authType) {
						}

						public X509Certificate[] getAcceptedIssuers() {
							return new X509Certificate[0];
						}
					}
			}, new SecureRandom());

			HttpClient.Builder httpClientBuilder = HttpClient.newBuilder()
					.sslContext(sslContext);

			HttpClientStreamableHttpTransport transport = HttpClientStreamableHttpTransport
					.builder("https://localhost:8443")
					.clientBuilder(httpClientBuilder)
					.build();

			return McpClient.sync(transport).build();
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			throw new McpClientCreationException("Failed to create McpSyncClient due to " + e.getMessage());
		}
	}

	@Bean
	@ConditionalOnBooleanProperty("spring.ai.pdf.analysis.enabled")
	CommandLineRunner ingestOnStartup(PdfIngestionService service,
	                                  @Value("classpath:data.pdf") Resource pdf) {
		return _ -> service.ingest(pdf);
	}
}
