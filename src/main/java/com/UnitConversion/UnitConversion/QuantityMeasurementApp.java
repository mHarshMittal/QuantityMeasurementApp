package com.UnitConversion.UnitConversion;

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        Quantity<LengthUnit> l1 = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(12, LengthUnit.INCHES);

        System.out.println("Length equal: " + l1.equals(l2));

        Quantity<LengthUnit> sum = l1.add(l2);
        System.out.println("Length add: " + sum);

        // Temperature Example

        Quantity<TempratureUnit> t1 = new Quantity<>(0.0, TempratureUnit.CELSIUS);
        Quantity<TempratureUnit> t2 = new Quantity<>(32.0, TempratureUnit.FAHRENHEIT);

        System.out.println("Temperature equal: " + t1.equals(t2));

        Quantity<TempratureUnit> converted = t1.convertTo(TempratureUnit.FAHRENHEIT);
        System.out.println("Converted temp: " + converted);

        try {
            t1.add(t2);
        } catch (UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        }
    }
}