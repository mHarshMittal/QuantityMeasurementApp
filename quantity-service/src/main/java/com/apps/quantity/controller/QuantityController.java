package com.apps.quantity.controller;

import com.apps.quantity.client.HistoryClient;
import com.apps.quantity.dto.HistoryRecord;
import com.apps.quantity.dto.QuantityDTO;
import com.apps.quantity.dto.QuantityInputDTO;
import com.apps.quantity.exception.QuantityException;
import com.apps.quantity.unit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/quantities")
public class QuantityController {

    @Autowired
    private HistoryClient historyClient;

    private IMeasurable getUnit(String measurementType, String unit) {
        return switch (measurementType.toUpperCase()) {
            case "LENGTH" -> LengthUnit.valueOf(unit.toUpperCase());
            case "WEIGHT" -> WeightUnit.valueOf(unit.toUpperCase());
            case "VOLUME" -> VolumeUnit.valueOf(unit.toUpperCase());
            case "TEMPERATURE" -> TemperatureUnit.valueOf(unit.toUpperCase());
            default -> throw new QuantityException("Invalid measurement type: " + measurementType);
        };
    }

    private HistoryRecord buildBase(Long userId, String email) {
        HistoryRecord r = new HistoryRecord();
        r.setUserId(userId);
        r.setUserEmail(email);
        r.setCreatedAt(LocalDateTime.now());
        return r;
    }

    private void setInput(HistoryRecord r, QuantityInputDTO input) {
        QuantityDTO q1 = input.getThisQuantityDTO();
        QuantityDTO q2 = input.getThatQuantityDTO();
        r.setThisValue(q1.getValue());
        r.setThisUnit(q1.getUnit());
        r.setThisMeasurementType(q1.getMeasurementType());
        if (q2 != null) {
            r.setThatValue(q2.getValue());
            r.setThatUnit(q2.getUnit());
            r.setThatMeasurementType(q2.getMeasurementType());
        }
    }

    @PostMapping("/compare/{userId}")
    public ResponseEntity<HistoryRecord> compare(@PathVariable Long userId,
                                                  @RequestBody QuantityInputDTO input,
                                                  Authentication auth) {
        HistoryRecord r = buildBase(userId, auth != null ? auth.getName() : null);
        r.setOperation("COMPARE");
        try {
            QuantityDTO q1 = input.getThisQuantityDTO();
            QuantityDTO q2 = input.getThatQuantityDTO();
            IMeasurable u1 = getUnit(q1.getMeasurementType(), q1.getUnit());
            IMeasurable u2 = getUnit(q2.getMeasurementType(), q2.getUnit());
            double base1 = u1.convertToBaseUnit(q1.getValue());
            double base2 = u2.convertToBaseUnit(q2.getValue());
            setInput(r, input);
            r.setResultString(String.valueOf(base1 == base2));
        } catch (Exception e) {
            r.setError(true); r.setErrorMessage(e.getMessage());
        }
        return ResponseEntity.ok(historyClient.save(r));
    }

    @PostMapping("/convert/{userId}")
    public ResponseEntity<HistoryRecord> convert(@PathVariable Long userId,
                                                  @RequestBody QuantityInputDTO input,
                                                  Authentication auth) {
        HistoryRecord r = buildBase(userId, auth != null ? auth.getName() : null);
        r.setOperation("CONVERT");
        try {
            QuantityDTO q = input.getThisQuantityDTO();
            QuantityDTO target = input.getThatQuantityDTO();
            IMeasurable fromUnit = getUnit(q.getMeasurementType(), q.getUnit());
            IMeasurable toUnit = getUnit(q.getMeasurementType(), target.getUnit());
            double base = fromUnit.convertToBaseUnit(q.getValue());
            double result = toUnit.convertFromBaseUnit(base);
            r.setThisValue(q.getValue()); r.setThisUnit(q.getUnit()); r.setThisMeasurementType(q.getMeasurementType());
            r.setResultValue(result); r.setResultUnit(target.getUnit());
        } catch (Exception e) {
            r.setError(true); r.setErrorMessage(e.getMessage());
        }
        return ResponseEntity.ok(historyClient.save(r));
    }

    @PostMapping("/add/{userId}")
    public ResponseEntity<HistoryRecord> add(@PathVariable Long userId,
                                              @RequestBody QuantityInputDTO input,
                                              Authentication auth) {
        HistoryRecord r = buildBase(userId, auth != null ? auth.getName() : null);
        r.setOperation("ADD");
        try {
            QuantityDTO q1 = input.getThisQuantityDTO();
            QuantityDTO q2 = input.getThatQuantityDTO();
            IMeasurable u1 = getUnit(q1.getMeasurementType(), q1.getUnit());
            IMeasurable u2 = getUnit(q2.getMeasurementType(), q2.getUnit());
            if (!u1.supportsArithmetic() || !u2.supportsArithmetic())
                throw new QuantityException("Arithmetic not supported for this unit");
            double result = u1.convertFromBaseUnit(u1.convertToBaseUnit(q1.getValue()) + u2.convertToBaseUnit(q2.getValue()));
            setInput(r, input);
            r.setResultValue(result); r.setResultUnit(q1.getUnit());
        } catch (Exception e) {
            r.setError(true); r.setErrorMessage(e.getMessage());
        }
        return ResponseEntity.ok(historyClient.save(r));
    }

    @PostMapping("/subtract/{userId}")
    public ResponseEntity<HistoryRecord> subtract(@PathVariable Long userId,
                                                   @RequestBody QuantityInputDTO input,
                                                   Authentication auth) {
        HistoryRecord r = buildBase(userId, auth != null ? auth.getName() : null);
        r.setOperation("SUBTRACT");
        try {
            QuantityDTO q1 = input.getThisQuantityDTO();
            QuantityDTO q2 = input.getThatQuantityDTO();
            IMeasurable u1 = getUnit(q1.getMeasurementType(), q1.getUnit());
            IMeasurable u2 = getUnit(q2.getMeasurementType(), q2.getUnit());
            if (!u1.supportsArithmetic() || !u2.supportsArithmetic())
                throw new QuantityException("Arithmetic not supported for this unit");
            double result = u1.convertFromBaseUnit(u1.convertToBaseUnit(q1.getValue()) - u2.convertToBaseUnit(q2.getValue()));
            setInput(r, input);
            r.setResultValue(result); r.setResultUnit(q1.getUnit());
        } catch (Exception e) {
            r.setError(true); r.setErrorMessage(e.getMessage());
        }
        return ResponseEntity.ok(historyClient.save(r));
    }

    @PostMapping("/multiply/{userId}")
    public ResponseEntity<HistoryRecord> multiply(@PathVariable Long userId,
                                                   @RequestBody QuantityInputDTO input,
                                                   Authentication auth) {
        HistoryRecord r = buildBase(userId, auth != null ? auth.getName() : null);
        r.setOperation("MULTIPLY");
        try {
            QuantityDTO q1 = input.getThisQuantityDTO();
            QuantityDTO q2 = input.getThatQuantityDTO();
            IMeasurable u1 = getUnit(q1.getMeasurementType(), q1.getUnit());
            if (!u1.supportsArithmetic())
                throw new QuantityException("Arithmetic not supported for this unit");
            double result = u1.convertFromBaseUnit(u1.convertToBaseUnit(q1.getValue()) * q2.getValue());
            setInput(r, input);
            r.setResultValue(result); r.setResultUnit(q1.getUnit());
        } catch (Exception e) {
            r.setError(true); r.setErrorMessage(e.getMessage());
        }
        return ResponseEntity.ok(historyClient.save(r));
    }

    @PostMapping("/divide/{userId}")
    public ResponseEntity<HistoryRecord> divide(@PathVariable Long userId,
                                                 @RequestBody QuantityInputDTO input,
                                                 Authentication auth) {
        HistoryRecord r = buildBase(userId, auth != null ? auth.getName() : null);
        r.setOperation("DIVIDE");
        try {
            QuantityDTO q1 = input.getThisQuantityDTO();
            QuantityDTO q2 = input.getThatQuantityDTO();
            IMeasurable u1 = getUnit(q1.getMeasurementType(), q1.getUnit());
            IMeasurable u2 = getUnit(q2.getMeasurementType(), q2.getUnit());
            if (!u1.supportsArithmetic() || !u2.supportsArithmetic())
                throw new QuantityException("Arithmetic not supported for this unit");
            double base1 = u1.convertToBaseUnit(q1.getValue());
            double base2 = u2.convertToBaseUnit(q2.getValue());
            if (base2 == 0) throw new QuantityException("Cannot divide by zero");
            setInput(r, input);
            r.setResultValue(base1 / base2); r.setResultUnit("RATIO");
        } catch (Exception e) {
            r.setError(true); r.setErrorMessage(e.getMessage());
        }
        return ResponseEntity.ok(historyClient.save(r));
    }
}
