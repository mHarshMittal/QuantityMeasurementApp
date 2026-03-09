package com.UnitConversion.UnitConversion;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityLengthTest {

    private static final double EPSILON = 1e-6;

    // ---------- Length Equality ----------

    @Test
    void testEquality_FeetToInches() {
        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(12.0, LengthUnit.INCHES);

        assertTrue(a.equals(b));
    }

    @Test
    void testEquality_YardsToFeet() {
        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.YARDS);
        Quantity<LengthUnit> b = new Quantity<>(3.0, LengthUnit.FEET);

        assertTrue(a.equals(b));
    }

    // ---------- Length Conversion ----------

    @Test
    void testConversion_FeetToInches() {

        Quantity<LengthUnit> length = new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> result = length.convertTo(LengthUnit.INCHES);

        assertEquals(12.0, result.getValue(), EPSILON);
    }

    @Test
    void testConversion_InchesToFeet() {

        Quantity<LengthUnit> length = new Quantity<>(24.0, LengthUnit.INCHES);

        Quantity<LengthUnit> result = length.convertTo(LengthUnit.FEET);

        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    void testConversion_YardsToInches() {

        Quantity<LengthUnit> length = new Quantity<>(1.0, LengthUnit.YARDS);

        Quantity<LengthUnit> result = length.convertTo(LengthUnit.INCHES);

        assertEquals(36.0, result.getValue(), EPSILON);
    }

    @Test
    void testConversion_CentimetersToInches() {

        Quantity<LengthUnit> length = new Quantity<>(2.54, LengthUnit.CENTIMETERS);

        Quantity<LengthUnit> result = length.convertTo(LengthUnit.INCHES);

        assertEquals(1.0, result.getValue(), EPSILON);
    }

    @Test
    void testConversion_RoundTrip() {

        Quantity<LengthUnit> original = new Quantity<>(5.5, LengthUnit.FEET);

        Quantity<LengthUnit> converted = original.convertTo(LengthUnit.INCHES);
        Quantity<LengthUnit> back = converted.convertTo(LengthUnit.FEET);

        assertEquals(original.getValue(), back.getValue(), EPSILON);
    }

    // ---------- Length Addition ----------

    @Test
    void testAddition_FeetPlusInches() {

        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(12.0, LengthUnit.INCHES);

        Quantity<LengthUnit> result = Quantity.add(a, b, LengthUnit.FEET);

        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    void testAddition_InchesTarget() {

        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(12.0, LengthUnit.INCHES);

        Quantity<LengthUnit> result = Quantity.add(a, b, LengthUnit.INCHES);

        assertEquals(24.0, result.getValue(), EPSILON);
    }

    @Test
    void testAddition_YardsTarget() {

        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(12.0, LengthUnit.INCHES);

        Quantity<LengthUnit> result = Quantity.add(a, b, LengthUnit.YARDS);

        assertEquals(0.666666, result.getValue(), EPSILON);
    }

    // ---------- Enum Tests ----------

    @Test
    public void testLengthUnitEnum_FeetConstant() {
        assertEquals(1.0, LengthUnit.FEET.getConversionFactor(), EPSILON);
    }

    @Test
    public void testLengthUnitEnum_InchesConstant() {
        assertEquals(1.0/12.0, LengthUnit.INCHES.getConversionFactor(), EPSILON);
    }

    @Test
    public void testLengthUnitEnum_YardsConstant() {
        assertEquals(3.0, LengthUnit.YARDS.getConversionFactor(), EPSILON);
    }

    @Test
    public void testLengthUnitEnum_CentimetersConstant() {
        assertEquals(1.0/30.48, LengthUnit.CENTIMETERS.getConversionFactor(), EPSILON);
    }

    // ---------- Weight Equality ----------

    @Test
    void testEquality_KgToGram() {

        Quantity<WeightUnit> a = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> b = new Quantity<>(1000.0, WeightUnit.GRAM);

        assertTrue(a.equals(b));
    }

    // ---------- Weight Conversion ----------

    @Test
    void testConversion_KgToGram() {

        Quantity<WeightUnit> q = new Quantity<>(1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> result = q.convertTo(WeightUnit.GRAM);

        assertEquals(1000.0, result.getValue(), EPSILON);
    }

    @Test
    void testConversion_PoundToKg() {

        Quantity<WeightUnit> q = new Quantity<>(2.0, WeightUnit.POUND);

        Quantity<WeightUnit> result = q.convertTo(WeightUnit.KILOGRAM);

        assertEquals(0.907184, result.getValue(), EPSILON);
    }

    // ---------- Weight Addition ----------

    @Test
    void testAddition_KgAndGram() {

        Quantity<WeightUnit> a = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> b = new Quantity<>(1000.0, WeightUnit.GRAM);

        Quantity<WeightUnit> result = Quantity.add(a, b, WeightUnit.KILOGRAM);

        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    void testAddition_WithTargetUnit() {

        Quantity<WeightUnit> a = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> b = new Quantity<>(1000.0, WeightUnit.GRAM);

        Quantity<WeightUnit> result = Quantity.add(a, b, WeightUnit.GRAM);

        assertEquals(2000.0, result.getValue(), EPSILON);
    }

    // ---------- Cross Category Safety ----------

    @Test
    void testLengthVsWeight_NotEqual() {

        Quantity<LengthUnit> length = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<WeightUnit> weight = new Quantity<>(1.0, WeightUnit.KILOGRAM);

        assertFalse(length.equals(weight));
    }
}