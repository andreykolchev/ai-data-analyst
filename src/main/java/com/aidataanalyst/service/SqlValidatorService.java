package com.aidataanalyst.service;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.Select;
import org.springframework.stereotype.Service;

@Service
public class SqlValidatorService {

    public void validate(String sql) {
        try {
            var stmt = CCJSqlParserUtil.parse(sql);

            if (!(stmt instanceof Select)) {
                throw new RuntimeException("Only SELECT queries allowed");
            }

            if (!sql.toLowerCase().contains("limit")) {
                throw new RuntimeException("LIMIT clause is required");
            }

        } catch (Exception e) {
            throw new RuntimeException("Invalid SQL: " + e.getMessage());
        }
    }
}
