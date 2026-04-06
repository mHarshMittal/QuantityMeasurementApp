package com.apps.quantitymeasurement.service;

import com.apps.quantitymeasurement.dto.QuantityInputDTO;
import com.apps.quantitymeasurement.entity.QuantityMeasurementEntity;

import java.util.List;

public interface IQuantityMeasurementService {
    QuantityMeasurementEntity compare(Long userId, QuantityInputDTO input);
    QuantityMeasurementEntity convert(Long userId, QuantityInputDTO input);
    QuantityMeasurementEntity add(Long userId, QuantityInputDTO input);
    QuantityMeasurementEntity subtract(Long userId, QuantityInputDTO input);
    QuantityMeasurementEntity divide(Long userId, QuantityInputDTO input);
    QuantityMeasurementEntity multiply(Long userId, QuantityInputDTO input);
    List<QuantityMeasurementEntity> getHistory();
    List<QuantityMeasurementEntity> getHistoryByEmail(String email);
    List<QuantityMeasurementEntity> getHistoryByOperation(String operation);
    long getOperationCount(String operation);
    void deleteRecord(Long id);
    void deleteAllByEmail(String email);
    void deleteAll();
}
