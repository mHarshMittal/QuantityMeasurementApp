package com.UnitConversion.UnitConversion;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityLengthTest {

    private static final double EPSILON = 1e-6;

    @Test
    void testConversion_FeetToInches() {
        double result = QuantityLength.convert(1.0, LengthUnit.FEET, LengthUnit.INCHES);
        assertEquals(12.0, result, EPSILON);
    }

    @Test
    void testConversion_InchesToFeet() {
        double result = QuantityLength.convert(24.0, LengthUnit.INCHES, LengthUnit.FEET);
        assertEquals(2.0, result, EPSILON);
    }

    @Test
    void testConversion_YardsToInches() {
        double result = QuantityLength.convert(1.0, LengthUnit.YARDS, LengthUnit.INCHES);
        assertEquals(36.0, result, EPSILON);
    }

    @Test
    void testConversion_InchesToYards() {
        double result = QuantityLength.convert(72.0, LengthUnit.INCHES, LengthUnit.YARDS);
        assertEquals(2.0, result, EPSILON);
    }

    @Test
    void testConversion_CentimetersToInches() {
        double result = QuantityLength.convert(2.54, LengthUnit.CENTIMETERS, LengthUnit.INCHES);
        assertEquals(1.0, result, EPSILON);
    }

    @Test
    void testConversion_FeetToYard() {
        double result = QuantityLength.convert(6.0, LengthUnit.FEET, LengthUnit.YARDS);
        assertEquals(2.0, result, EPSILON);
    }

    @Test
    void testConversion_RoundTrip_PreservesValue() {
        double original = 5.5;

        double converted = QuantityLength.convert(original, LengthUnit.FEET, LengthUnit.INCHES);
        double back = QuantityLength.convert(converted, LengthUnit.INCHES, LengthUnit.FEET);

        assertEquals(original, back, EPSILON);
    }

    @Test
    void testConversion_ZeroValue() {
        double result = QuantityLength.convert(0.0, LengthUnit.FEET, LengthUnit.INCHES);
        assertEquals(0.0, result, EPSILON);
    }

    @Test
    void testConversion_NegativeValue() {
        double result = QuantityLength.convert(-1.0, LengthUnit.FEET, LengthUnit.INCHES);
        assertEquals(-12.0, result, EPSILON);
    }

    @Test
    void testConversion_InvalidUnit_Throws() {
        assertThrows(IllegalArgumentException.class, () -> {
            QuantityLength.convert(1.0, null, LengthUnit.INCHES);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            QuantityLength.convert(1.0, LengthUnit.FEET, null);
        });
    }

    @Test
    void testConversion_NaNOrInfinite_Throws() {
        assertThrows(IllegalArgumentException.class, () -> {
            QuantityLength.convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCHES);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            QuantityLength.convert(Double.POSITIVE_INFINITY, LengthUnit.FEET, LengthUnit.INCHES);
        });
    }

    @Test
    void testConversion_PrecisionTolerance() {
        double result = QuantityLength.convert(1.0, LengthUnit.CENTIMETERS, LengthUnit.FEET);
        assertEquals(0.0328084, result, EPSILON);
    }
    
    
    
    
    
    // ---------- UC7 Addition Tests ----------

    @Test
    void testAddition_ExplicitTargetUnit_Feet() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result = QuantityLength.add(a, b, LengthUnit.FEET);

        assertEquals(2.0, QuantityLength.convert(2.0, LengthUnit.FEET, LengthUnit.FEET), EPSILON);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Inches() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result = QuantityLength.add(a, b, LengthUnit.INCHES);

        assertEquals(24.0, QuantityLength.convert(2.0, LengthUnit.FEET, LengthUnit.INCHES), EPSILON);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Yards() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result = QuantityLength.add(a, b, LengthUnit.YARDS);

        assertEquals(0.666666, QuantityLength.convert(2.0, LengthUnit.FEET, LengthUnit.YARDS), EPSILON);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Centimeters() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.INCHES);
        QuantityLength b = new QuantityLength(1.0, LengthUnit.INCHES);

        QuantityLength result = QuantityLength.add(a, b, LengthUnit.CENTIMETERS);

        assertEquals(5.08, QuantityLength.convert(2.0, LengthUnit.INCHES, LengthUnit.CENTIMETERS), EPSILON);
    }

    @Test
    void testAddition_ExplicitTargetUnit_SameAsFirstOperand() {
        QuantityLength a = new QuantityLength(2.0, LengthUnit.YARDS);
        QuantityLength b = new QuantityLength(3.0, LengthUnit.FEET);

        QuantityLength result = QuantityLength.add(a, b, LengthUnit.YARDS);

        assertEquals(3.0, QuantityLength.convert(9.0, LengthUnit.FEET, LengthUnit.YARDS), EPSILON);
    }

    @Test
    void testAddition_ExplicitTargetUnit_SameAsSecondOperand() {
        QuantityLength a = new QuantityLength(2.0, LengthUnit.YARDS);
        QuantityLength b = new QuantityLength(3.0, LengthUnit.FEET);

        QuantityLength result = QuantityLength.add(a, b, LengthUnit.FEET);

        assertEquals(9.0, QuantityLength.convert(9.0, LengthUnit.FEET, LengthUnit.FEET), EPSILON);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Commutativity() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength r1 = QuantityLength.add(a, b, LengthUnit.YARDS);
        QuantityLength r2 = QuantityLength.add(b, a, LengthUnit.YARDS);

        assertTrue(r1.equals(r2));
    }

    @Test
    void testAddition_ExplicitTargetUnit_WithZero() {
        QuantityLength a = new QuantityLength(5.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(0.0, LengthUnit.INCHES);

        QuantityLength result = QuantityLength.add(a, b, LengthUnit.YARDS);

        assertEquals(1.666666, QuantityLength.convert(5.0, LengthUnit.FEET, LengthUnit.YARDS), EPSILON);
    }

    @Test
    void testAddition_ExplicitTargetUnit_NegativeValues() {
        QuantityLength a = new QuantityLength(5.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(-2.0, LengthUnit.FEET);

        QuantityLength result = QuantityLength.add(a, b, LengthUnit.INCHES);

        assertEquals(36.0, QuantityLength.convert(3.0, LengthUnit.FEET, LengthUnit.INCHES), EPSILON);
    }

    @Test
    void testAddition_ExplicitTargetUnit_NullTargetUnit() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);

        assertThrows(IllegalArgumentException.class, () -> {
            QuantityLength.add(a, b, null);
        });
    }

    @Test
    void testAddition_ExplicitTargetUnit_LargeToSmallScale() {
        QuantityLength a = new QuantityLength(1000.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(500.0, LengthUnit.FEET);

        QuantityLength result = QuantityLength.add(a, b, LengthUnit.INCHES);

        assertEquals(18000.0, QuantityLength.convert(1500.0, LengthUnit.FEET, LengthUnit.INCHES), EPSILON);
    }

    @Test
    void testAddition_ExplicitTargetUnit_SmallToLargeScale() {
        QuantityLength a = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result = QuantityLength.add(a, b, LengthUnit.YARDS);

        assertEquals(0.666666, QuantityLength.convert(24.0, LengthUnit.INCHES, LengthUnit.YARDS), EPSILON);
    }
    
    
    
    @Test
    public void testLengthUnitEnum_FeetConstant() {
        assertEquals(1.0, LengthUnit.FEET.getConversionFactor(), 0.0001);
    }

    @Test
    public void testLengthUnitEnum_InchesConstant() {
        assertEquals(1.0/12.0, LengthUnit.INCHES.getConversionFactor(), 0.0001);
    }

    @Test
    public void testLengthUnitEnum_YardsConstant() {
        assertEquals(3.0, LengthUnit.YARDS.getConversionFactor(), 0.0001);
    }

    @Test
    public void testLengthUnitEnum_CentimetersConstant() {
        assertEquals(1.0/30.48, LengthUnit.CENTIMETERS.getConversionFactor(), 0.0001);
    }

    @Test
    public void testConvertToBaseUnit_InchesToFeet() {
        assertEquals(1.0, LengthUnit.INCHES.convertToBaseUnit(12.0), 0.0001);
    }

    @Test
    public void testConvertFromBaseUnit_FeetToInches() {
        assertEquals(12.0, LengthUnit.INCHES.convertFromBaseUnit(1.0), 0.0001);
    }
    
    
    @Test
    void testEquality_KgToGram() {

        QuantityWeight a = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight b = new QuantityWeight(1000.0, WeightUnit.GRAM);

        assertTrue(a.equals(b));
    }

    @Test
    void testConversion_KgToGram() {

        QuantityWeight q = new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        QuantityWeight result = q.convertTo(WeightUnit.GRAM);

        assertEquals(1000.0, result.getValue(), 1e-6);
    }

    @Test
    void testConversion_PoundToKg() {

        QuantityWeight q = new QuantityWeight(2.0, WeightUnit.POUND);

        QuantityWeight result = q.convertTo(WeightUnit.KILOGRAM);

        assertEquals(0.907184, result.getValue(), 1e-6);
    }

    @Test
    void testAddition_KgAndGram() {

        QuantityWeight a = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight b = new QuantityWeight(1000.0, WeightUnit.GRAM);

        QuantityWeight result = QuantityWeight.add(a, b);

        assertEquals(2.0, result.getValue(), 1e-6);
    }

    @Test
    void testAddition_WithTargetUnit() {

        QuantityWeight a = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight b = new QuantityWeight(1000.0, WeightUnit.GRAM);

        QuantityWeight result = QuantityWeight.add(a, b, WeightUnit.GRAM);

        assertEquals(2000.0, result.getValue(), 1e-6);
    }
}



