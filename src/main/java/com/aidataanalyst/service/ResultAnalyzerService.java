package com.aidataanalyst.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ResultAnalyzerService {

    private final AIService aiService;

    public ResultAnalyzerService(AIService aiService) {
        this.aiService = aiService;
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

        return aiService.ollamaGenerateContent(prompt);
    }
}
