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

        Quantity<LengthUnit> result = a.add(b, LengthUnit.FEET);

        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    void testAddition_InchesTarget() {

        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(12.0, LengthUnit.INCHES);

        Quantity<LengthUnit> result = a.add(b, LengthUnit.INCHES);

        assertEquals(24.0, result.getValue(), EPSILON);
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

        Quantity<WeightUnit> result = a.add(b, WeightUnit.KILOGRAM);

        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    void testAddition_WithTargetUnit() {

        Quantity<WeightUnit> a = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> b = new Quantity<>(1000.0, WeightUnit.GRAM);

        Quantity<WeightUnit> result = a.add(b, WeightUnit.GRAM);

        assertEquals(2000.0, result.getValue(), EPSILON);
    }

    // ---------- Cross Category Safety ----------

    @Test
    void testLengthVsWeight_NotEqual() {

        Quantity<LengthUnit> length = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<WeightUnit> weight = new Quantity<>(1.0, WeightUnit.KILOGRAM);

        assertFalse(length.equals(weight));
    }

    // ---------- UC11 Volume Tests ----------

    @Test
    void testVolumeEquality_LitreToMillilitre() {

        Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> v2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        assertTrue(v1.equals(v2));
    }

    @Test
    void testVolumeConversion_GallonToLitre() {

        Quantity<VolumeUnit> v = new Quantity<>(1.0, VolumeUnit.GALLON);
        Quantity<VolumeUnit> result = v.convertTo(VolumeUnit.LITRE);

        assertEquals(3.78541, result.getValue(), 1e-6);
    }

    @Test
    void testVolumeAddition_LitreAndMillilitre() {

        Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> v2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        Quantity<VolumeUnit> result = v1.add(v2, VolumeUnit.LITRE);

        assertEquals(2.0, result.getValue(), 1e-6);
    }

    @Test
    void testVolumeVsLength_NotEqual() {

        Quantity<VolumeUnit> volume = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<LengthUnit> length = new Quantity<>(1.0, LengthUnit.FEET);

        assertFalse(volume.equals(length));
    }

    // ---------- UC12 Subtraction ----------

    @Test
    void testSubtraction_SameUnit() {

        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5.0, LengthUnit.FEET);

        Quantity<LengthUnit> result = q1.subtract(q2);

        assertEquals(5.0, result.getValue(), EPSILON);
    }

    @Test
    void testSubtraction_CrossUnit() {

        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(6.0, LengthUnit.INCHES);

        Quantity<LengthUnit> result = q1.subtract(q2);

        assertEquals(9.5, result.getValue(), EPSILON);
    }

    // ---------- UC12 Division ----------

    @Test
    void testDivision_Normal() {

        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(2.0, LengthUnit.FEET);

        double result = q1.divide(q2);

        assertEquals(5.0, result, EPSILON);
    }

    @Test
    void testDivision_ByZero() {

        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(0.0, LengthUnit.FEET);

        assertThrows(ArithmeticException.class, () -> q1.divide(q2));
    }

    // ---------- UC13 Refactoring Tests ----------

    @Test
    void testUC13_AddOperationStillWorks() {

        Quantity<LengthUnit> q1 = new Quantity<>(2.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCHES);

        Quantity<LengthUnit> result = q1.add(q2);

        assertEquals(3.0, result.getValue(), EPSILON);
    }

    @Test
    void testUC13_SubtractOperationStillWorks() {

        Quantity<LengthUnit> q1 = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> result = q1.subtract(q2);

        assertEquals(4.0, result.getValue(), EPSILON);
    }

    @Test
    void testUC13_DivideOperationStillWorks() {

        Quantity<LengthUnit> q1 = new Quantity<>(20.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(4.0, LengthUnit.FEET);

        double result = q1.divide(q2);

        assertEquals(5.0, result, EPSILON);
    }

    @Test
    void testUC13_AddNullOperand() {

        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);

        assertThrows(IllegalArgumentException.class, () -> q1.add(null));
    }

    @Test
    void testUC13_CrossCategoryAdditionFails() {

        Quantity<LengthUnit> length = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<WeightUnit> weight = new Quantity<>(5.0, WeightUnit.KILOGRAM);

        assertThrows(IllegalArgumentException.class,
                () -> length.add((Quantity) weight));
    }

    @Test
    void testUC13_ImmutabilityAfterAdd() {

        Quantity<LengthUnit> q1 = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5.0, LengthUnit.FEET);

        Quantity<LengthUnit> result = q1.add(q2);

        assertEquals(5.0, q1.getValue(), EPSILON);
        assertEquals(10.0, result.getValue(), EPSILON);
    }

    // ---------- UC14 Temperature Tests ----------

    @Test
    void testTemperatureEquality_CelsiusToFahrenheit() {

        Quantity<TempratureUnit> c = new Quantity<>(0.0, TempratureUnit.CELSIUS);
        Quantity<TempratureUnit> f = new Quantity<>(32.0, TempratureUnit.FAHRENHEIT);

        assertTrue(c.equals(f));
    }

    @Test
    void testTemperatureConversion_CelsiusToKelvin() {

        Quantity<TempratureUnit> c = new Quantity<>(0.0, TempratureUnit.CELSIUS);
        Quantity<TempratureUnit> result = c.convertTo(TempratureUnit.KELVIN);

        assertEquals(273.15, result.getValue(), EPSILON);
    }

    @Test
    void testTemperatureAddition_NotAllowed() {

        Quantity<TempratureUnit> t1 = new Quantity<>(10.0, TempratureUnit.CELSIUS);
        Quantity<TempratureUnit> t2 = new Quantity<>(5.0, TempratureUnit.CELSIUS);

        assertThrows(UnsupportedOperationException.class, () -> t1.add(t2));
    }

    @Test
    void testTemperatureDivision_NotAllowed() {

        Quantity<TempratureUnit> t1 = new Quantity<>(20.0, TempratureUnit.CELSIUS);
        Quantity<TempratureUnit> t2 = new Quantity<>(10.0, TempratureUnit.CELSIUS);

        assertThrows(UnsupportedOperationException.class, () -> t1.divide(t2));
    }
    
    @Test
    void given1FeetAnd12Inches_ShouldReturnTrue_WhenCompared() {

        Quantity<LengthUnit> q1 = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12, LengthUnit.INCHES);

        assertEquals(q1, q2);
    }

    @Test
    void given1FeetAnd2Feet_ShouldReturn3Feet_WhenAdded() {

        Quantity<LengthUnit> q1 = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(2, LengthUnit.FEET);

        Quantity<LengthUnit> result = q1.add(q2);

        assertEquals(new Quantity<>(3, LengthUnit.FEET), result);
    }

    @Test
    void given1Feet_ShouldConvertTo12Inches() {

        Quantity<LengthUnit> q = new Quantity<>(1, LengthUnit.FEET);

        Quantity<LengthUnit> result = q.convertTo(LengthUnit.INCHES);

        assertEquals(new Quantity<>(12, LengthUnit.INCHES), result);
    }

    @Test
    void given2InchesAnd2Inches_ShouldReturn4Inches_WhenAdded() {

        Quantity<LengthUnit> q1 = new Quantity<>(2, LengthUnit.INCHES);
        Quantity<LengthUnit> q2 = new Quantity<>(2, LengthUnit.INCHES);

        Quantity<LengthUnit> result = q1.add(q2);

        assertEquals(new Quantity<>(4, LengthUnit.INCHES), result);
    }

    @Test
    void givenDifferentMeasurements_ShouldThrowException() {

        Quantity<LengthUnit> q1 = new Quantity<>(1, LengthUnit.FEET);
        Quantity<VolumeUnit> q2 = new Quantity<>(1, VolumeUnit.LITRE);

        assertThrows(IllegalArgumentException.class, () -> {
            q1.add((Quantity) q2);
        });
    }
}