/*package com.ght666.aiTools.config;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

*//**
 * @author: 1012ght
 *//*
@Configuration
public class EmbeddingConfig {

    @Value("${spring.ai.ollama.base-url}")
    private String ollamaBaseUrl;

    @Value("${spring.ai.ollama.embedding.model}")
    private String embeddingModel;
    *//*@Bean
    public EmbeddingModel embeddingModel() {
        OpenAiApi openAiApi = new OpenAiApi(openAiBaseUrl, openAiApiKey);
        return new OpenAiEmbeddingModel(openAiApi);
    }*//*
    @Bean
    public EmbeddingModel embeddingModel() {
        OllamaApi ollamaApi = new OllamaApi(ollamaBaseUrl);
        return new OllamaEmbeddingModel(ollamaApi,);
    }
}*/
