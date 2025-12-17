package com.aidataanalyst.service;

import org.springframework.stereotype.Service;

@Service
public class SqlGeneratorService {

    private final AIService aiService;

    public SqlGeneratorService(AIService aiService) {
        this.aiService = aiService;
    }

    public String generateSql(String question, String schema) {

        String prompt = """
                You are an expert SQL generator.
                You must only produce PostgreSQL SELECT statements with LIMIT.
                Never use UPDATE, DELETE, INSERT or DDL.
                Do not explain..
                
                Schema:
                %s
                
                Question: %s
                
                SQL:
                """.formatted(schema, question);

        String aiResp = aiService.ollamaGenerateContent(prompt);
        return extractSqlFromResponse(aiResp);
    }

    private String extractSqlFromResponse(String raw) {
        if (raw == null) return "";
        String normalized = raw.replace("\r\n", "\n");
        // First, try fenced SQL block: ```sql ... ``` (case-insensitive)
        java.util.regex.Pattern pSql = java.util.regex.Pattern.compile("(?is)```sql\\s*\\R([\\s\\S]*?)\\R?```", java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher mSql = pSql.matcher(normalized);
        if (mSql.find()) {
            return mSql.group(1).trim();
        }
        return "";
    }
}