package com.UnitConversion.UnitConversion;

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        // Length Example
        Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCHES);

        Quantity<LengthUnit> result = Quantity.add(q1, q2, LengthUnit.FEET);
        System.out.println(result);

        Quantity<LengthUnit> length = new Quantity<>(2, LengthUnit.YARDS);
        Quantity<LengthUnit> converted = length.convertTo(LengthUnit.FEET);

        System.out.println(length + " = " + converted);

        Quantity<LengthUnit> l1 = new Quantity<>(3, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(36, LengthUnit.INCHES);

        System.out.println("Equal? " + l1.equals(l2));

        double base = LengthUnit.INCHES.convertToBaseUnit(12);
        System.out.println("12 inches in feet = " + base);


        // Weight Example
        Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(1000.0, WeightUnit.GRAM);

        System.out.println("Weight equal? " + w1.equals(w2));

        Quantity<WeightUnit> weightSum = Quantity.add(w1, w2, WeightUnit.KILOGRAM);
        System.out.println("Weight Sum: " + weightSum);
    }
}