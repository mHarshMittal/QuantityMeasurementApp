package com.UnitConversion.UnitConversion;

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        QuantityLength l1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength l2 = new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength r1 = QuantityLength.add(l1, l2, LengthUnit.FEET);
        System.out.println(r1);

        QuantityLength r2 = QuantityLength.add(l1, l2, LengthUnit.INCHES);
        System.out.println(r2);

        QuantityLength r3 = QuantityLength.add(l1, l2, LengthUnit.YARDS);
        System.out.println(r3);

        QuantityLength a = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength b = new QuantityLength(3.0, LengthUnit.FEET);

        System.out.println(QuantityLength.add(a, b, LengthUnit.YARDS));

        QuantityLength c = new QuantityLength(36.0, LengthUnit.INCHES);
        QuantityLength d = new QuantityLength(1.0, LengthUnit.YARDS);

        System.out.println(QuantityLength.add(c, d, LengthUnit.FEET));

        QuantityLength e = new QuantityLength(2.54, LengthUnit.CENTIMETERS);
        QuantityLength f = new QuantityLength(1.0, LengthUnit.INCHES);

        System.out.println(QuantityLength.add(e, f, LengthUnit.CENTIMETERS));
    }
}