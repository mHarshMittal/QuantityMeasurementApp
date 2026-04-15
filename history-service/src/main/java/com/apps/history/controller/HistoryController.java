package com.apps.history.controller;

import com.apps.history.entity.QuantityMeasurementEntity;
import com.apps.history.repository.HistoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/history")
public class HistoryController {

    private final HistoryRepository repository;

    public HistoryController(HistoryRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/save")
    public ResponseEntity<QuantityMeasurementEntity> save(@RequestBody QuantityMeasurementEntity entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @GetMapping
    public ResponseEntity<List<QuantityMeasurementEntity>> getAll(
            @RequestParam(required = false) String email) {
        if (email != null && !email.isEmpty()) {
            return ResponseEntity.ok(repository.findByUserEmailOrderByCreatedAtDesc(email));
        }
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/operation/{operation}")
    public ResponseEntity<List<QuantityMeasurementEntity>> getByOperation(
            @PathVariable String operation,
            @RequestParam(required = false) String email) {
        if (email != null && !email.isEmpty()) {
            return ResponseEntity.ok(repository.findByUserEmailAndOperationOrderByCreatedAtDesc(email, operation.toUpperCase()));
        }
        return ResponseEntity.ok(repository.findByOperation(operation.toUpperCase()));
    }

    @GetMapping("/count/{operation}")
    public ResponseEntity<Long> count(
            @PathVariable String operation,
            @RequestParam(required = false) String email) {
        if (email != null && !email.isEmpty()) {
            return ResponseEntity.ok(repository.countByUserEmailAndOperationAndErrorFalse(email, operation.toUpperCase()));
        }
        return ResponseEntity.ok(repository.countByOperationAndErrorFalse(operation.toUpperCase()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll(@RequestParam(required = false) String email) {
        if (email != null && !email.isEmpty()) {
            repository.deleteByUserEmail(email);
        } else {
            repository.deleteAll();
        }
        return ResponseEntity.noContent().build();
    }
}
