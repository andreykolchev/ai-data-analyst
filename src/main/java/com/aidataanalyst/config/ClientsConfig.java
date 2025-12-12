package com.aidataanalyst.config;

import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientsConfig {

    @Value("${gemini.api.key:}")
    private String apiKey;

    @Bean
    public Client geminiClient() {
        return Client.builder().apiKey(apiKey).build();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}