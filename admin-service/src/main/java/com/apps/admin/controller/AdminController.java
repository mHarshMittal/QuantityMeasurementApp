package com.apps.admin.controller;

import com.apps.admin.client.AuthClient;
import com.apps.admin.client.HistoryAdminClient;
import com.apps.admin.dto.HistoryRecord;
import com.apps.admin.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AuthClient authClient;
    private final HistoryAdminClient historyAdminClient;

    public AdminController(AuthClient authClient, HistoryAdminClient historyAdminClient) {
        this.authClient = authClient;
        this.historyAdminClient = historyAdminClient;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(authClient.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(authClient.getUserById(id));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        authClient.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/history")
    public ResponseEntity<List<HistoryRecord>> getAllHistory() {
        return ResponseEntity.ok(historyAdminClient.getAllHistory());
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        List<HistoryRecord> all = historyAdminClient.getAllHistory();
        stats.put("totalOperations", all != null ? all.size() : 0);

        String[] ops = {"COMPARE", "CONVERT", "ADD", "SUBTRACT", "MULTIPLY", "DIVIDE"};
        Map<String, Long> opCounts = new HashMap<>();
        for (String op : ops) {
            try {
                opCounts.put(op, historyAdminClient.getOperationCount(op));
            } catch (Exception e) {
                opCounts.put(op, 0L);
            }
        }
        stats.put("operationCounts", opCounts);

        long errorCount = all != null ? all.stream().filter(HistoryRecord::isError).count() : 0;
        stats.put("errorCount", errorCount);

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "admin-service",
            "port", "8082"
        ));
    }
}
