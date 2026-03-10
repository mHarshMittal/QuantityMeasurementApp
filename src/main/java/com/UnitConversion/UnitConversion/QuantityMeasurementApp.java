package com.UnitConversion.UnitConversion;

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        // ---------------- LENGTH EXAMPLES ----------------

        Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCHES);

        Quantity<LengthUnit> result = Quantity.add(q1, q2, LengthUnit.FEET);
        System.out.println("Length Addition: " + result);

        Quantity<LengthUnit> length = new Quantity<>(2, LengthUnit.YARDS);
        Quantity<LengthUnit> converted = length.convertTo(LengthUnit.FEET);

        System.out.println(length + " = " + converted);

        Quantity<LengthUnit> l1 = new Quantity<>(3, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(36, LengthUnit.INCHES);

        System.out.println("Length Equal? " + l1.equals(l2));

        double base = LengthUnit.INCHES.convertToBaseUnit(12);
        System.out.println("12 inches in feet = " + base);


        // ---------------- WEIGHT EXAMPLES ----------------

        Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(1000.0, WeightUnit.GRAM);

        System.out.println("Weight Equal? " + w1.equals(w2));

        Quantity<WeightUnit> weightSum = Quantity.add(w1, w2, WeightUnit.KILOGRAM);
        System.out.println("Weight Sum: " + weightSum);


        // ---------------- VOLUME EXAMPLES (UC11) ----------------

        Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> v2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> v3 = new Quantity<>(1.0, VolumeUnit.GALLON);

        System.out.println("Volume Equal? " + v1.equals(v2));

        Quantity<VolumeUnit> convertedVolume = v1.convertTo(VolumeUnit.MILLILITRE);
        System.out.println(v1 + " = " + convertedVolume);

        Quantity<VolumeUnit> gallonToLitre = v3.convertTo(VolumeUnit.LITRE);
        System.out.println(v3 + " = " + gallonToLitre);

        Quantity<VolumeUnit> volumeSum1 = Quantity.add(v1, v2, VolumeUnit.LITRE);
        System.out.println("Volume Sum (Litre): " + volumeSum1);

        Quantity<VolumeUnit> volumeSum2 = Quantity.add(v1, v3, VolumeUnit.MILLILITRE);
        System.out.println("Volume Sum (Millilitre): " + volumeSum2);


        // ---------------- UC12 : SUBTRACTION ----------------

        Quantity<LengthUnit> s1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> s2 = new Quantity<>(6.0, LengthUnit.INCHES);

        Quantity<LengthUnit> subtract1 = s1.subtract(s2);
        System.out.println("Subtraction (implicit unit): " + subtract1);

        Quantity<LengthUnit> subtract2 = s1.subtract(s2, LengthUnit.INCHES);
        System.out.println("Subtraction (explicit unit): " + subtract2);


        // ---------------- UC12 : DIVISION ----------------

        Quantity<LengthUnit> d1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> d2 = new Quantity<>(2.0, LengthUnit.FEET);

        double divisionResult = d1.divide(d2);
        System.out.println("Division Result: " + divisionResult);


        // Division with different units
        Quantity<LengthUnit> d3 = new Quantity<>(24.0, LengthUnit.INCHES);
        Quantity<LengthUnit> d4 = new Quantity<>(2.0, LengthUnit.FEET);

        System.out.println("Cross Unit Division: " + d3.divide(d4));
    }
}