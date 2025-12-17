package com.aidataanalyst.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AIService {
    private final Client geminiClient;
    private final RestTemplate restTemplate;

    public AIService(Client geminiClient, RestTemplate restTemplate) {
        this.geminiClient = geminiClient;
        this.restTemplate = restTemplate;
    }

    public String geminiGenerateContent(String prompt) {
        GenerateContentConfig config = GenerateContentConfig.builder().build();
        GenerateContentResponse geminiResp = geminiClient.models.generateContent("gemini-2.5-flash", prompt, config);
        return geminiResp.candidates().orElseThrow().getFirst().content().orElseThrow().text();
    }

    public String ollamaGenerateContent(String prompt) {
        String url = "http://localhost:11434/api/generate";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        GenerateRequest body = new GenerateRequest("qwen2.5-coder:3b", prompt, false);
        GenerateResponse resp = restTemplate.postForObject(url, new HttpEntity<>(body, headers), GenerateResponse.class);
        return resp != null ? resp.response : null;
    }

    public record GenerateRequest(String model, String prompt, boolean stream) {
    }

    public record GenerateResponse(String response) {
    }
}
