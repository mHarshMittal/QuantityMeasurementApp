package com.UnitConversion.UnitConversion.entity;

public class QuantityDTO {

    private double value;
    private String unit;
    private String measurement;

    public QuantityDTO(double value, String unit, String measurement) {
        this.value = value;
        this.unit = unit;
        this.measurement = measurement;
    }

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public String getMeasurement() {
        return measurement;
    }
}