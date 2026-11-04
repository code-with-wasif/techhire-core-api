package io.github.codewithwasif.techhire.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.google.genai.GoogleGenAiEmbeddingConnectionDetails;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }

    @Bean
    @Primary
    public EmbeddingModel embeddingModel(@Qualifier("googleGenAiTextEmbedding") EmbeddingModel googleGenAiTextEmbedding) {
        return googleGenAiTextEmbedding;
    }

    @Bean
    public GoogleGenAiEmbeddingConnectionDetails googleGenAiEmbeddingConnectionDetails(
            @Value("${spring.ai.google.genai.api-key:${GEMINI_API_KEY}}") String apiKey) {
        return GoogleGenAiEmbeddingConnectionDetails.builder()
                .apiKey(apiKey)
                .build();
    }
}