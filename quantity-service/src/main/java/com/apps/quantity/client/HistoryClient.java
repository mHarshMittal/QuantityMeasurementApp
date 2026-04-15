package com.apps.quantity.client;

import com.apps.quantity.dto.HistoryRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HistoryClient {

    private final RestTemplate restTemplate;
    private final String historyServiceUrl;

    public HistoryClient(@Value("${history.service.url}") String historyServiceUrl) {
        this.restTemplate = new RestTemplate();
        this.historyServiceUrl = historyServiceUrl;
    }

    public HistoryRecord save(HistoryRecord record) {
        try {
            ResponseEntity<HistoryRecord> response = restTemplate.postForEntity(
                    historyServiceUrl + "/api/v1/history/save",
                    record,
                    HistoryRecord.class
            );
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Failed to save history: " + e.getMessage());
            return record;
        }
    }
}