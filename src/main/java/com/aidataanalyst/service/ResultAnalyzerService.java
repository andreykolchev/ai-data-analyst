package com.aidataanalyst.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ResultAnalyzerService {

    private final Client geminiClient;

    public ResultAnalyzerService(Client geminiClient) {
        this.geminiClient = geminiClient;
    }

    public String analyze(String sql, List<Map<String, Object>> data) {

        String prompt = """
                You are an expert data analyst.
                Explain the result set in simple analytical language.
                Highlight trends, anomalies and conclusions.

                SQL: %s

                Data: %s

                Analysis:
                """.formatted(sql, data);

        GenerateContentConfig config = GenerateContentConfig.builder().build();

        GenerateContentResponse geminiResp = geminiClient.models.generateContent("gemini-2.5-flash", prompt, config);

        return geminiResp.candidates().orElseThrow().getFirst().content().orElseThrow().text();

    }
}
