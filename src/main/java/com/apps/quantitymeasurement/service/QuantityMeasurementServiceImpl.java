package com.apps.quantitymeasurement.service;

import com.apps.quantitymeasurement.dto.QuantityDTO;
import com.apps.quantitymeasurement.dto.QuantityInputDTO;
import com.apps.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.apps.quantitymeasurement.entity.User;
import com.apps.quantitymeasurement.exception.QuantityMeasurementException;
import com.apps.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.apps.quantitymeasurement.repository.UserRepository;
import com.apps.quantitymeasurement.unit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    @Autowired
    private QuantityMeasurementRepository repository;

    @Autowired
    private UserRepository userRepository;

    private IMeasurable getUnit(String measurementType, String unit) {
        return switch (measurementType.toUpperCase()) {
            case "LENGTH" -> LengthUnit.valueOf(unit.toUpperCase());
            case "WEIGHT" -> WeightUnit.valueOf(unit.toUpperCase());
            case "VOLUME" -> VolumeUnit.valueOf(unit.toUpperCase());
            case "TEMPERATURE" -> TemperatureUnit.valueOf(unit.toUpperCase());
            default -> throw new QuantityMeasurementException("Invalid measurement type: " + measurementType);
        };
    }

    private void setCommonFields(QuantityMeasurementEntity entity, QuantityInputDTO input) {
        QuantityDTO q1 = input.getThisQuantityDTO();
        QuantityDTO q2 = input.getThatQuantityDTO();

        entity.setThisValue(q1.getValue());
        entity.setThisUnit(q1.getUnit());
        entity.setThisMeasurementType(q1.getMeasurementType());

        if (q2 != null) {
            entity.setThatValue(q2.getValue());
            entity.setThatUnit(q2.getUnit());
            entity.setThatMeasurementType(q2.getMeasurementType());
        }
        entity.setCreatedAt(LocalDateTime.now());
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public QuantityMeasurementEntity compare(Long userId, QuantityInputDTO input) {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setUser(getUser(userId));
        try {
            QuantityDTO q1 = input.getThisQuantityDTO();
            QuantityDTO q2 = input.getThatQuantityDTO();

            IMeasurable unit1 = getUnit(q1.getMeasurementType(), q1.getUnit());
            IMeasurable unit2 = getUnit(q2.getMeasurementType(), q2.getUnit());

            double base1 = unit1.convertToBaseUnit(q1.getValue());
            double base2 = unit2.convertToBaseUnit(q2.getValue());

            setCommonFields(entity, input);
            entity.setOperation("COMPARE");
            entity.setResultString(String.valueOf(base1 == base2));
        } catch (Exception e) {
            entity.setError(true);
            entity.setErrorMessage(e.getMessage());
            entity.setOperation("COMPARE");
            entity.setCreatedAt(LocalDateTime.now());
        }
        return repository.save(entity);
    }

    @Override
    public QuantityMeasurementEntity convert(Long userId, QuantityInputDTO input) {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setUser(getUser(userId));
        try {
            QuantityDTO q = input.getThisQuantityDTO();
            QuantityDTO target = input.getThatQuantityDTO();

            IMeasurable fromUnit = getUnit(q.getMeasurementType(), q.getUnit());
            IMeasurable toUnit = getUnit(q.getMeasurementType(), target.getUnit());

            double base = fromUnit.convertToBaseUnit(q.getValue());
            double result = toUnit.convertFromBaseUnit(base);

            entity.setThisValue(q.getValue());
            entity.setThisUnit(q.getUnit());
            entity.setThisMeasurementType(q.getMeasurementType());
            entity.setOperation("CONVERT");
            entity.setResultValue(result);
            entity.setResultUnit(target.getUnit());
            entity.setCreatedAt(LocalDateTime.now());
        } catch (Exception e) {
            entity.setError(true);
            entity.setErrorMessage(e.getMessage());
            entity.setOperation("CONVERT");
            entity.setCreatedAt(LocalDateTime.now());
        }
        return repository.save(entity);
    }

    @Override
    public QuantityMeasurementEntity add(Long userId, QuantityInputDTO input) {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setUser(getUser(userId));
        try {
            QuantityDTO q1 = input.getThisQuantityDTO();
            QuantityDTO q2 = input.getThatQuantityDTO();

            IMeasurable unit1 = getUnit(q1.getMeasurementType(), q1.getUnit());
            IMeasurable unit2 = getUnit(q2.getMeasurementType(), q2.getUnit());

            if (!unit1.supportsArithmetic() || !unit2.supportsArithmetic()) {
                throw new QuantityMeasurementException("Arithmetic not supported for this unit");
            }

            double base1 = unit1.convertToBaseUnit(q1.getValue());
            double base2 = unit2.convertToBaseUnit(q2.getValue());
            double result = unit1.convertFromBaseUnit(base1 + base2);

            setCommonFields(entity, input);
            entity.setOperation("ADD");
            entity.setResultValue(result);
            entity.setResultUnit(q1.getUnit());
        } catch (Exception e) {
            entity.setError(true);
            entity.setErrorMessage(e.getMessage());
            entity.setOperation("ADD");
            entity.setCreatedAt(LocalDateTime.now());
        }
        return repository.save(entity);
    }

    @Override
    public QuantityMeasurementEntity subtract(Long userId, QuantityInputDTO input) {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setUser(getUser(userId));
        try {
            QuantityDTO q1 = input.getThisQuantityDTO();
            QuantityDTO q2 = input.getThatQuantityDTO();

            IMeasurable unit1 = getUnit(q1.getMeasurementType(), q1.getUnit());
            IMeasurable unit2 = getUnit(q2.getMeasurementType(), q2.getUnit());

            if (!unit1.supportsArithmetic() || !unit2.supportsArithmetic()) {
                throw new QuantityMeasurementException("Arithmetic not supported for this unit");
            }

            double base1 = unit1.convertToBaseUnit(q1.getValue());
            double base2 = unit2.convertToBaseUnit(q2.getValue());
            double result = unit1.convertFromBaseUnit(base1 - base2);

            setCommonFields(entity, input);
            entity.setOperation("SUBTRACT");
            entity.setResultValue(result);
            entity.setResultUnit(q1.getUnit());
        } catch (Exception e) {
            entity.setError(true);
            entity.setErrorMessage(e.getMessage());
            entity.setOperation("SUBTRACT");
            entity.setCreatedAt(LocalDateTime.now());
        }
        return repository.save(entity);
    }

    @Override
    public QuantityMeasurementEntity divide(Long userId, QuantityInputDTO input) {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setUser(getUser(userId));
        try {
            QuantityDTO q1 = input.getThisQuantityDTO();
            QuantityDTO q2 = input.getThatQuantityDTO();

            IMeasurable unit1 = getUnit(q1.getMeasurementType(), q1.getUnit());
            IMeasurable unit2 = getUnit(q2.getMeasurementType(), q2.getUnit());

            if (!unit1.supportsArithmetic() || !unit2.supportsArithmetic()) {
                throw new QuantityMeasurementException("Arithmetic not supported for this unit");
            }

            double base1 = unit1.convertToBaseUnit(q1.getValue());
            double base2 = unit2.convertToBaseUnit(q2.getValue());

            if (base2 == 0) {
                throw new QuantityMeasurementException("Cannot divide by zero");
            }

            setCommonFields(entity, input);
            entity.setOperation("DIVIDE");
            entity.setResultValue(base1 / base2);
            entity.setResultUnit("RATIO");
        } catch (Exception e) {
            entity.setError(true);
            entity.setErrorMessage(e.getMessage());
            entity.setOperation("DIVIDE");
            entity.setCreatedAt(LocalDateTime.now());
        }
        return repository.save(entity);
    }

    @Override
    public QuantityMeasurementEntity multiply(Long userId, QuantityInputDTO input) {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setUser(getUser(userId));
        try {
            QuantityDTO q1 = input.getThisQuantityDTO();
            QuantityDTO q2 = input.getThatQuantityDTO();

            IMeasurable unit1 = getUnit(q1.getMeasurementType(), q1.getUnit());

            if (!unit1.supportsArithmetic()) {
                throw new QuantityMeasurementException("Arithmetic not supported for this unit");
            }

            double base1 = unit1.convertToBaseUnit(q1.getValue());
            double scalar = q2.getValue(); // treat second quantity value as scalar multiplier
            double result = unit1.convertFromBaseUnit(base1 * scalar);

            setCommonFields(entity, input);
            entity.setOperation("MULTIPLY");
            entity.setResultValue(result);
            entity.setResultUnit(q1.getUnit());
        } catch (Exception e) {
            entity.setError(true);
            entity.setErrorMessage(e.getMessage());
            entity.setOperation("MULTIPLY");
            entity.setCreatedAt(LocalDateTime.now());
        }
        return repository.save(entity);
    }

    @Override
    public List<QuantityMeasurementEntity> getHistory() {
        return repository.findAll();
    }

    @Override
    public List<QuantityMeasurementEntity> getHistoryByEmail(String email) {
        return repository.findByUserEmail(email);
    }

    @Override
    public List<QuantityMeasurementEntity> getHistoryByOperation(String operation) {
        return repository.findByOperation(operation.toUpperCase());
    }

    @Override
    public long getOperationCount(String operation) {
        return repository.countByOperationAndErrorFalse(operation.toUpperCase());
    }

    @Override
    public void deleteRecord(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteAllByEmail(String email) {
        repository.deleteByUserEmail(email);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
