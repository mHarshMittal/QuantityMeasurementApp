package com.apps.admin.client;

import com.apps.admin.dto.HistoryRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Component
public class HistoryAdminClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${history.service.url}")
    private String historyServiceUrl;

    public List<HistoryRecord> getAllHistory() {
        ResponseEntity<List<HistoryRecord>> response = restTemplate.exchange(
            historyServiceUrl + "/history",
            HttpMethod.GET, null,
            new ParameterizedTypeReference<List<HistoryRecord>>() {});
        return response.getBody();
    }

    public Long getOperationCount(String operation) {
        return restTemplate.getForObject(
            historyServiceUrl + "/history/count/" + operation, Long.class);
    }
}
