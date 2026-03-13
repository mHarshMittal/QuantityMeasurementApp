package com.UnitConversion.UnitConversion.service;

import com.UnitConversion.UnitConversion.entity.QuantityDTO;

public interface IQuantityMeasurementService {

    boolean compare(QuantityDTO a, QuantityDTO b);

    QuantityDTO add(QuantityDTO a, QuantityDTO b);

    QuantityDTO convert(QuantityDTO q, String targetUnit);
}