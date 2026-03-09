package com.UnitConversion.UnitConversion;

public class QuantityMeasurementApp {

	public static void main(String[] args) {
		
        double inches = QuantityLength.convert(3, LengthUnit.FEET, LengthUnit.INCHES);
        System.out.println("3 FEET = " + inches + " INCHES");
        
        
        QuantityLength length = new QuantityLength(2, LengthUnit.YARDS);
        QuantityLength result = length.convertTo(LengthUnit.FEET);

        System.out.println(length + " = " + result);

        // Equality check
        QuantityLength l1 = new QuantityLength(3, LengthUnit.FEET);
        QuantityLength l2 = new QuantityLength(36, LengthUnit.INCHES);

        System.out.println("Equal? " + l1.equals(l2));

	}

}
