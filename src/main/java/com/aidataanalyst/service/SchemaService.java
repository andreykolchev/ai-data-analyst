package com.aidataanalyst.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;

@Service
public class SchemaService {

    private final JdbcTemplate jdbcTemplate;
    private String cachedSchema;

    public SchemaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void init() {
        this.cachedSchema = loadSchemaDescription();
    }

    public String getSchemaDescription() {
        return cachedSchema;
    }

    private String loadSchemaDescription() {

        Map<String, List<ColumnInfo>> tables = loadColumns();
        Map<String, List<ForeignKey>> foreignKeys = loadForeignKeys();

        StringBuilder sb = new StringBuilder();
        sb.append("Database schema description:\n\n");

        for (var entry : tables.entrySet()) {
            String table = entry.getKey();
            sb.append("Table: ").append(table).append("\n");
            sb.append("Columns:\n");

            for (ColumnInfo col : entry.getValue()) {
                sb.append("  - ").append(col.columnName)
                        .append(" (").append(col.dataType).append(")");

                if (col.isNullable.equals("NO")) {
                    sb.append(" NOT NULL");
                }
                sb.append("\n");
            }

            if (foreignKeys.containsKey(table)) {
                sb.append("Foreign Keys:\n");
                for (ForeignKey fk : foreignKeys.get(table)) {
                    sb.append("  - ").append(fk.columnName)
                            .append(" -> ").append(fk.foreignTable)
                            .append(".").append(fk.foreignColumn)
                            .append("\n");
                }
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    private Map<String, List<ColumnInfo>> loadColumns() {
        String sql = """
                SELECT table_name, column_name, data_type, is_nullable
                FROM information_schema.columns
                WHERE table_schema = 'public'
                ORDER BY table_name, ordinal_position
                """;

        return jdbcTemplate.query(sql, rs -> {
            Map<String, List<ColumnInfo>> result = new LinkedHashMap<>();

            while (rs.next()) {
                String table = rs.getString("table_name");
                String column = rs.getString("column_name");
                String type = rs.getString("data_type");
                String nullable = rs.getString("is_nullable");

                result.computeIfAbsent(table, k -> new ArrayList<>())
                        .add(new ColumnInfo(column, type, nullable));
            }
            return result;
        });
    }

    private Map<String, List<ForeignKey>> loadForeignKeys() {
        String sql = """
                SELECT
                    tc.table_name AS table_name,
                    kcu.column_name AS column_name,
                    ccu.table_name AS foreign_table,
                    ccu.column_name AS foreign_column
                FROM information_schema.table_constraints AS tc
                JOIN information_schema.key_column_usage AS kcu
                    ON tc.constraint_name = kcu.constraint_name
                JOIN information_schema.constraint_column_usage AS ccu
                    ON ccu.constraint_name = tc.constraint_name
                WHERE tc.constraint_type = 'FOREIGN KEY'
                  AND tc.table_schema = 'public'
                ORDER BY tc.table_name
                """;

        return jdbcTemplate.query(sql, rs -> {
            Map<String, List<ForeignKey>> result = new LinkedHashMap<>();

            while (rs.next()) {
                var fk = new ForeignKey(
                        rs.getString("column_name"),
                        rs.getString("foreign_table"),
                        rs.getString("foreign_column")
                );
                result.computeIfAbsent(rs.getString("table_name"), k -> new ArrayList<>())
                        .add(fk);
            }

            return result;
        });
    }

    private record ColumnInfo(String columnName, String dataType, String isNullable) {}
    private record ForeignKey(String columnName, String foreignTable, String foreignColumn) {}
}
