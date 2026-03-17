package com.UnitConversion.UnitConversion.service;

import com.UnitConversion.UnitConversion.model.OperationType;
import com.UnitConversion.UnitConversion.model.QuantityDTO;
import com.UnitConversion.UnitConversion.model.QuantityMeasurementDTO;

import java.util.List;

public interface IQuantityMeasurementService {

    QuantityMeasurementDTO compare(QuantityDTO a, QuantityDTO b);

    QuantityMeasurementDTO add(QuantityDTO a, QuantityDTO b);

    QuantityMeasurementDTO convert(QuantityDTO q, String targetUnit);

    List<QuantityMeasurementDTO> getHistoryByOperation(OperationType operation);

    List<QuantityMeasurementDTO> getHistoryByMeasurementType(String measurementType);

    long getSuccessfulOperationCount(OperationType operation);

    List<QuantityMeasurementDTO> getErroredHistory();
}