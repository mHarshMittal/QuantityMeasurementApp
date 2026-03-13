package com.UnitConversion.UnitConversion.repository;

import com.UnitConversion.UnitConversion.entity.QuantityMeasurementEntity;
import java.util.List;

public interface IQuantityMeasurementRepository {

    void save(QuantityMeasurementEntity entity);

    List<QuantityMeasurementEntity> findAll();
}