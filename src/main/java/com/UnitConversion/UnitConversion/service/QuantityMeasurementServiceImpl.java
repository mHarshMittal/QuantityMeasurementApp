package com.UnitConversion.UnitConversion.service;

import com.UnitConversion.UnitConversion.LengthUnit;
import com.UnitConversion.UnitConversion.Quantity;
import com.UnitConversion.UnitConversion.entity.QuantityDTO;
import com.UnitConversion.UnitConversion.repository.IQuantityMeasurementRepository;

public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private IQuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean compare(QuantityDTO a, QuantityDTO b) {

        Quantity<LengthUnit> q1 =
                new Quantity<>(a.getValue(), LengthUnit.valueOf(a.getUnit()));

        Quantity<LengthUnit> q2 =
                new Quantity<>(b.getValue(), LengthUnit.valueOf(b.getUnit()));

        return q1.equals(q2);
    }

    @Override
    public QuantityDTO add(QuantityDTO a, QuantityDTO b) {

        Quantity<LengthUnit> q1 =
                new Quantity<>(a.getValue(), LengthUnit.valueOf(a.getUnit()));

        Quantity<LengthUnit> q2 =
                new Quantity<>(b.getValue(), LengthUnit.valueOf(b.getUnit()));

        Quantity<LengthUnit> result = q1.add(q2);

        return new QuantityDTO(
                result.getValue(),
                result.getUnit().getUnitName(),
                a.getMeasurement()
        );
    }

    @Override
    public QuantityDTO convert(QuantityDTO q, String targetUnit) {

        Quantity<LengthUnit> quantity =
                new Quantity<>(q.getValue(), LengthUnit.valueOf(q.getUnit()));

        Quantity<LengthUnit> result =
                quantity.convertTo(LengthUnit.valueOf(targetUnit));

        return new QuantityDTO(
                result.getValue(),
                targetUnit,
                q.getMeasurement()
        );
    }
}