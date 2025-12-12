package com.aidataanalyst.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.stereotype.Service;

@Service
public class SqlGeneratorService {

    private final Client geminiClient;

    public SqlGeneratorService(Client geminiClient) {
        this.geminiClient = geminiClient;
    }

    public String generateSql(String question, String schema) {

        String prompt = """
                You are an expert SQL generator.
                You must only produce PostgreSQL SELECT statements with LIMIT.
                Never use UPDATE, DELETE, INSERT or DDL.
                
                Schema:
                %s
                
                Question: %s
                
                SQL:
                """.formatted(schema, question);

        GenerateContentConfig config = GenerateContentConfig.builder().build();

        GenerateContentResponse geminiResp = geminiClient.models.generateContent("gemini-2.5-flash", prompt, config);

        return trim(geminiResp.candidates().orElseThrow().getFirst().content().orElseThrow().text());
        
        
    }
    
    private String trim(String generatedSql){
        if (generatedSql == null) return "";
        return generatedSql
                .replace("\r\n", "\n")
                .replace("```sql", "")
                .replace("```", "")
                .trim();
    }
}