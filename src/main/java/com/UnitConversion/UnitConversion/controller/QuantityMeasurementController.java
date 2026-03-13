package com.UnitConversion.UnitConversion.controller;

import com.UnitConversion.UnitConversion.entity.QuantityDTO;
import com.UnitConversion.UnitConversion.service.IQuantityMeasurementService;

public class QuantityMeasurementController {

    private IQuantityMeasurementService service;

    public QuantityMeasurementController(IQuantityMeasurementService service){
        this.service = service;
    }

    public void compare(QuantityDTO a, QuantityDTO b){
        boolean result = service.compare(a,b);
        System.out.println("Comparison Result: " + result);
    }

    public void add(QuantityDTO a, QuantityDTO b){
        QuantityDTO result = service.add(a,b);
        System.out.println("Addition Result: " + result.getValue() + " " + result.getUnit());
    }
}