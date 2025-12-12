package com.aidataanalyst.controller;

import com.aidataanalyst.service.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/query")
public class QueryController {

    private final SchemaService schemaService;
    private final SqlGeneratorService sqlGeneratorService;
    private final SqlValidatorService sqlValidatorService;
    private final QueryExecutorService queryExecutorService;
    private final ResultAnalyzerService resultAnalyzerService;

    public QueryController(
            SchemaService schemaService,
            SqlGeneratorService sqlGeneratorService,
            SqlValidatorService sqlValidatorService,
            QueryExecutorService queryExecutorService,
            ResultAnalyzerService resultAnalyzerService
    ) {
        this.schemaService = schemaService;
        this.sqlGeneratorService = sqlGeneratorService;
        this.sqlValidatorService = sqlValidatorService;
        this.queryExecutorService = queryExecutorService;
        this.resultAnalyzerService = resultAnalyzerService;
    }

    @PostMapping
    public QueryResponse query(@RequestBody QueryRequest request) {

        String schema = schemaService.getSchemaDescription();

        String sql = sqlGeneratorService.generateSql(request.question(), schema);

        sqlValidatorService.validate(sql);

        var data = queryExecutorService.execute(sql);

        String analysis = resultAnalyzerService.analyze(sql, data);

        return new QueryResponse(sql, data, analysis);
    }

    public record QueryRequest(String question) { }

    public record QueryResponse(String sql, List<Map<String, Object>> data, String analysis) { }
}
