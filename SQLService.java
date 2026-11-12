
package com.tecnots.n12sql.service;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SQLService {

    private final JdbcTemplate jdbcTemplate;

    private static final Pattern SELECT_PATTERN =
        Pattern.compile("(?is)^\\s*SELECT\\b");

    private static final Pattern DANGEROUS_PATTERN =
        Pattern.compile(
            "(?is)\\b(INSERT|UPDATE|DELETE|DROP|ALTER|TRUNCATE|" +
            "CREATE|REPLACE|GRANT|REVOKE|CALL|EXEC|EXECUTE|" +
            "LOAD_FILE|SLEEP|BENCHMARK)\\b"
        );

    public SQLService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> executeQuery(String sql) {

        if (sql == null || sql.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "SQL query cannot be empty."
            );
        }

        String query = sql.trim();

        // Allow only SELECT statements
        if (!SELECT_PATTERN.matcher(query).find()) {
            throw new IllegalArgumentException(
                "Only SELECT queries are allowed."
            );
        }

        // Block multiple statements and SQL comments
        if (query.contains(";") ||
            query.contains("--") ||
            query.contains("/*") ||
            query.contains("*/") ||
            query.contains("#")) {

            throw new IllegalArgumentException(
                "Multiple statements and SQL comments are not allowed."
            );
        }

        // Block modification commands
        if (DANGEROUS_PATTERN.matcher(query).find()) {
            throw new IllegalArgumentException(
                "Modification queries are not allowed."
            );
        }

        // Block file export and locking operations
        String lowerQuery = query.toLowerCase();

        if (lowerQuery.contains("into outfile") ||
            lowerQuery.contains("into dumpfile") ||
            lowerQuery.contains("for update") ||
            lowerQuery.contains("lock in share mode")) {

            throw new IllegalArgumentException(
                "This SQL operation is not allowed."
            );
        }

        // Execute the validated query
        return jdbcTemplate.queryForList(query);
    }
}
