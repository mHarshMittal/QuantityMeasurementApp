package com.UnitConversion.UnitConversion;

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result = QuantityLength.add(q1, q2, LengthUnit.FEET);
        System.out.println(result);

        QuantityLength length = new QuantityLength(2, LengthUnit.YARDS);
        QuantityLength converted = length.convertTo(LengthUnit.FEET);

        System.out.println(length + " = " + converted);

        QuantityLength l1 = new QuantityLength(3, LengthUnit.FEET);
        QuantityLength l2 = new QuantityLength(36, LengthUnit.INCHES);

        System.out.println("Equal? " + l1.equals(l2));

        double base = LengthUnit.INCHES.convertToBaseUnit(12);
        System.out.println("12 inches in feet = " + base);
    }
}