
package com.tecnots.n12sql.controlle;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tecnots.n12sql.service.AIService;
import com.tecnots.n12sql.service.SQLService;
import com.tecnots.n12sql.service.QueryHistoryService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class QueryController {

    private final AIService aiService;
    private final SQLService sqlService;
    private final QueryHistoryService historyService;

    public QueryController(
            AIService aiService,
            SQLService sqlService,
            QueryHistoryService historyService) {

        this.aiService = aiService;
        this.sqlService = sqlService;
        this.historyService = historyService;
    }

    // Step 1: Generate SQL without executing it
    @GetMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateSQL(
            @RequestParam String question) {

        if (question == null || question.trim().length() < 3) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "Please enter a meaningful question.")
            );
        }

        try {
            String sql = aiService.generateSQL(question);

            return ResponseEntity.ok(
                Map.of(
                    "question", question,
                    "sql", sql
                )
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "SQL generation failed: " + e.getMessage())
            );
        }
    }

    // Step 2: Execute the reviewed SQL
    @PostMapping("/execute")
    public ResponseEntity<Map<String, Object>> executeSQL(
            @RequestBody Map<String, String> request) {

        String question = request.getOrDefault("question", "");
        String sql = request.getOrDefault("sql", "");

        try {
            List<Map<String, Object>> result =
                    sqlService.executeQuery(sql);

            historyService.saveHistory(
                    question, sql, "SUCCESS"
            );

            return ResponseEntity.ok(
                Map.of(
                    "question", question,
                    "sql", sql,
                    "result", result,
                    "rowCount", result.size()
                )
            );

        } catch (IllegalArgumentException e) {

            saveFailedHistory(question, sql);

            return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
            );

        } catch (Exception e) {

            saveFailedHistory(question, sql);

            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR
            ).body(
                Map.of(
                    "error",
                    "Query execution failed. Please check your SQL and table/column names."
                )
            );
        }
    }

    private void saveFailedHistory(String question, String sql) {
        try {
            historyService.saveHistory(question, sql, "FAILED");
        } catch (Exception ignored) {
            // Preserve the original error.
        }
    }

    // Step 3: Retrieve query history
    @GetMapping("/history")
    public ResponseEntity<List<Map<String, Object>>> getHistory() {

        return ResponseEntity.ok(
                historyService.getHistory()
        );
    }
}
