package com.UnitConversion.UnitConversion.controller;

import com.UnitConversion.UnitConversion.model.OperationType;
import com.UnitConversion.UnitConversion.model.QuantityInputDTO;
import com.UnitConversion.UnitConversion.model.QuantityMeasurementDTO;
import com.UnitConversion.UnitConversion.service.IQuantityMeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quantities")
@Tag(name = "Quantity Measurements", description = "REST API for quantity measurement operations")
public class QuantityMeasurementController {

    private final IQuantityMeasurementService service;

    public QuantityMeasurementController(IQuantityMeasurementService service) {
        this.service = service;
    }

    @PostMapping("/compare")
    @Operation(summary = "Compare two quantities")
    public QuantityMeasurementDTO compare(@Valid @RequestBody QuantityInputDTO input) {
        return service.compare(input.getThisQuantityDTO(), input.getThatQuantityDTO());
    }

    @PostMapping("/convert")
    @Operation(summary = "Convert a quantity to another unit")
    public QuantityMeasurementDTO convert(
            @Valid @RequestBody QuantityInputDTO input
    ) {
        return service.convert(input.getThisQuantityDTO(), input.getThatQuantityDTO().getUnit());
    }

    @PostMapping("/add")
    @Operation(summary = "Add two quantities")
    public QuantityMeasurementDTO add(@Valid @RequestBody QuantityInputDTO input) {
        return service.add(input.getThisQuantityDTO(), input.getThatQuantityDTO());
    }

    @GetMapping("/history/operation/{operation}")
    @Operation(summary = "Get history by operation type")
    public List<QuantityMeasurementDTO> historyByOperation(@PathVariable OperationType operation) {
        return service.getHistoryByOperation(operation);
    }

    @GetMapping("/history/type/{measurementType}")
    @Operation(summary = "Get history by measurement type")
    public List<QuantityMeasurementDTO> historyByType(@PathVariable String measurementType) {
        return service.getHistoryByMeasurementType(measurementType);
    }

    @GetMapping("/count/{operation}")
    @Operation(summary = "Get successful operation count")
    public long count(@PathVariable OperationType operation) {
        return service.getSuccessfulOperationCount(operation);
    }

    @GetMapping("/history/errored")
    @Operation(summary = "Get history of errored operations")
    public List<QuantityMeasurementDTO> errored() {
        return service.getErroredHistory();
    }
}