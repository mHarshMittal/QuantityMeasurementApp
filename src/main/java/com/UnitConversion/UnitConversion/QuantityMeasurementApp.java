package com.UnitConversion.UnitConversion;

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        // Length Example
        Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCHES);

        Quantity<LengthUnit> result = q1.add(q2, LengthUnit.FEET);
        System.out.println(result);

        Quantity<LengthUnit> length = new Quantity<>(2, LengthUnit.YARDS);
        Quantity<LengthUnit> converted = length.convertTo(LengthUnit.FEET);

        System.out.println(length + " = " + converted);

        Quantity<LengthUnit> l1 = new Quantity<>(3, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(36, LengthUnit.INCHES);

        System.out.println("Equal? " + l1.equals(l2));

        // Weight Example
        Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(1000.0, WeightUnit.GRAM);

        System.out.println("Weight equal? " + w1.equals(w2));

        Quantity<WeightUnit> weightSum = w1.add(w2, WeightUnit.KILOGRAM);
        System.out.println("Weight Sum: " + weightSum);

        // Volume Example
        Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> v2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        System.out.println("Volume equal? " + v1.equals(v2));

        Quantity<VolumeUnit> volumeSum = v1.add(v2, VolumeUnit.LITRE);
        System.out.println("Volume Sum: " + volumeSum);
    }
}