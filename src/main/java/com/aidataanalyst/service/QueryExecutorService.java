package com.aidataanalyst.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class QueryExecutorService {

    private final JdbcTemplate jdbcTemplate;

    public QueryExecutorService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> execute(String sql) {
        return jdbcTemplate.queryForList(sql);
    }
}
