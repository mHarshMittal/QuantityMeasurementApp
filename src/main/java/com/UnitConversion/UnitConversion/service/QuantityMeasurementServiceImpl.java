package com.UnitConversion.UnitConversion.service;

import com.UnitConversion.UnitConversion.LengthUnit;
import com.UnitConversion.UnitConversion.Quantity;
import com.UnitConversion.UnitConversion.exception.QuantityMeasurementException;
import com.UnitConversion.UnitConversion.model.OperationType;
import com.UnitConversion.UnitConversion.model.QuantityDTO;
import com.UnitConversion.UnitConversion.model.QuantityMeasurementDTO;
import com.UnitConversion.UnitConversion.model.QuantityMeasurementEntity;
import com.UnitConversion.UnitConversion.repository.QuantityMeasurementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private final QuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(QuantityMeasurementRepository repository) {
        this.repository = repository;
    }

    @Override
    public QuantityMeasurementDTO compare(QuantityDTO a, QuantityDTO b) {
        return runOperation(OperationType.COMPARE, a, b, () -> {
            Quantity<LengthUnit> q1 = toLengthQuantity(a);
            Quantity<LengthUnit> q2 = toLengthQuantity(b);
            boolean result = q1.equals(q2);
            QuantityMeasurementDTO out = baseDto(OperationType.COMPARE, a, b);
            out.setResultString(Boolean.toString(result));
            return out;
        });
    }

    @Override
    public QuantityMeasurementDTO add(QuantityDTO a, QuantityDTO b) {
        return runOperation(OperationType.ADD, a, b, () -> {
            Quantity<LengthUnit> q1 = toLengthQuantity(a);
            Quantity<LengthUnit> q2 = toLengthQuantity(b);
            Quantity<LengthUnit> result = q1.add(q2);

            QuantityMeasurementDTO out = baseDto(OperationType.ADD, a, b);
            out.setResultValue(result.getValue());
            out.setResultUnit(result.getUnit().getUnitName());
            out.setResultMeasurementType(a.getMeasurementType());
            return out;
        });
    }

    @Override
    public QuantityMeasurementDTO convert(QuantityDTO q, String targetUnit) {
        QuantityDTO dummyOther = new QuantityDTO(0.0, targetUnit, q.getMeasurementType());
        return runOperation(OperationType.CONVERT, q, dummyOther, () -> {
            Quantity<LengthUnit> quantity = toLengthQuantity(q);
            Quantity<LengthUnit> result = quantity.convertTo(LengthUnit.valueOf(targetUnit));

            QuantityMeasurementDTO out = baseDto(OperationType.CONVERT, q, dummyOther);
            out.setResultValue(result.getValue());
            out.setResultUnit(targetUnit);
            out.setResultMeasurementType(q.getMeasurementType());
            return out;
        });
    }

    @Override
    public List<QuantityMeasurementDTO> getHistoryByOperation(OperationType operation) {
        return QuantityMeasurementDTO.fromEntityList(repository.findByOperation(operation));
    }

    @Override
    public List<QuantityMeasurementDTO> getHistoryByMeasurementType(String measurementType) {
        return QuantityMeasurementDTO.fromEntityList(repository.findByThisMeasurementType(measurementType));
    }

    @Override
    public long getSuccessfulOperationCount(OperationType operation) {
        return repository.countByOperationAndErrorFalse(operation);
    }

    @Override
    public List<QuantityMeasurementDTO> getErroredHistory() {
        return QuantityMeasurementDTO.fromEntityList(repository.findByErrorTrue());
    }

    private Quantity<LengthUnit> toLengthQuantity(QuantityDTO dto) {
        if (dto == null) {
            throw new QuantityMeasurementException("Quantity input cannot be null");
        }
        if (dto.getMeasurementType() == null || dto.getMeasurementType().isBlank()) {
            throw new QuantityMeasurementException("measurementType is required");
        }
        if (!dto.getMeasurementType().equalsIgnoreCase("LengthUnit") && !dto.getMeasurementType().equalsIgnoreCase("LENGTH")) {
            throw new QuantityMeasurementException("Only LengthUnit measurementType is supported in this implementation");
        }
        try {
            return new Quantity<>(dto.getValue(), LengthUnit.valueOf(dto.getUnit()));
        } catch (IllegalArgumentException e) {
            throw new QuantityMeasurementException("Invalid unit name: " + dto.getUnit());
        }
    }

    private QuantityMeasurementDTO baseDto(OperationType operation, QuantityDTO a, QuantityDTO b) {
        QuantityMeasurementDTO out = new QuantityMeasurementDTO();
        out.setOperation(operation);
        out.setThisValue(a.getValue());
        out.setThisUnit(a.getUnit());
        out.setThisMeasurementType(a.getMeasurementType());
        out.setThatValue(b.getValue());
        out.setThatUnit(b.getUnit());
        out.setThatMeasurementType(b.getMeasurementType());
        return out;
    }

    private interface OperationSupplier {
        QuantityMeasurementDTO get();
    }

    private QuantityMeasurementDTO runOperation(OperationType operation, QuantityDTO a, QuantityDTO b, OperationSupplier supplier) {
        QuantityMeasurementDTO dto;
        try {
            dto = supplier.get();
            dto.setError(false);
            dto.setErrorMessage(null);
        } catch (RuntimeException ex) {
            dto = baseDto(operation, a, b);
            dto.setError(true);
            dto.setErrorMessage(ex.getMessage());
        }

        QuantityMeasurementEntity saved = repository.save(dto.toEntity());
        return QuantityMeasurementDTO.fromEntity(saved);
    }
}