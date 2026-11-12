package com.tecnots.n12sql.service;


import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class QueryHistoryService {

    private final JdbcTemplate jdbcTemplate;

    public QueryHistoryService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveHistory(String question, String sql, String status) {

        String query = """
            INSERT INTO query_history
            (question, generated_sql, execution_status)
            VALUES (?, ?, ?)
            """;

        jdbcTemplate.update(query, question, sql, status);
    }
    public List<Map<String, Object>> getHistory() {

        String query = """
            SELECT history_id, question, generated_sql,
                   execution_status, created_at
            FROM query_history
            ORDER BY created_at DESC
            """;

        return jdbcTemplate.queryForList(query);
    }
}