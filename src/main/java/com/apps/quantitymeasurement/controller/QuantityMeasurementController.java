package com.apps.quantitymeasurement.controller;

import com.apps.quantitymeasurement.dto.QuantityInputDTO;
import com.apps.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.apps.quantitymeasurement.service.IQuantityMeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quantities")
public class QuantityMeasurementController {

    @Autowired
    private IQuantityMeasurementService service;

    @PostMapping("/compare/{userId}")
    public ResponseEntity<QuantityMeasurementEntity> compare(@PathVariable Long userId,
                                                             @RequestBody QuantityInputDTO input) {
        return ResponseEntity.ok(service.compare(userId, input));
    }

    @PostMapping("/convert/{userId}")
    public ResponseEntity<QuantityMeasurementEntity> convert(@PathVariable Long userId,
                                                             @RequestBody QuantityInputDTO input) {
        return ResponseEntity.ok(service.convert(userId, input));
    }

    @PostMapping("/add/{userId}")
    public ResponseEntity<QuantityMeasurementEntity> add(@PathVariable Long userId,
                                                         @RequestBody QuantityInputDTO input) {
        return ResponseEntity.ok(service.add(userId, input));
    }

    @PostMapping("/subtract/{userId}")
    public ResponseEntity<QuantityMeasurementEntity> subtract(@PathVariable Long userId,
                                                              @RequestBody QuantityInputDTO input) {
        return ResponseEntity.ok(service.subtract(userId, input));
    }

    @PostMapping("/divide/{userId}")
    public ResponseEntity<QuantityMeasurementEntity> divide(@PathVariable Long userId,
                                                            @RequestBody QuantityInputDTO input) {
        return ResponseEntity.ok(service.divide(userId, input));
    }

    @GetMapping("/history")
    public ResponseEntity<List<QuantityMeasurementEntity>> getHistory() {
        return ResponseEntity.ok(service.getHistory());
    }

    @GetMapping("/history/{operation}")
    public ResponseEntity<List<QuantityMeasurementEntity>> getHistoryByOperation(@PathVariable String operation) {
        return ResponseEntity.ok(service.getHistoryByOperation(operation));
    }

    @GetMapping("/count/{operation}")
    public ResponseEntity<Long> getOperationCount(@PathVariable String operation) {
        return ResponseEntity.ok(service.getOperationCount(operation));
    }
}