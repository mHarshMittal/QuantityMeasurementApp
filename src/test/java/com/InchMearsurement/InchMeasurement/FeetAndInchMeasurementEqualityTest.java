package com.InchMearsurement.InchMeasurement;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class FeetAndInchMeasurementEqualityTest {
	
	@Test
    void testFeetEquality_SameValue() {
        assertTrue(FeetAndInchMeasurementEquality.checkFeetEquality(5.0, 5.0));
    }

    @Test
    void testFeetEquality_DifferentValue() {
        assertFalse(FeetAndInchMeasurementEquality.checkFeetEquality(5.0, 6.0));
    }

    @Test
    void testFeetEquality_SameReference() {
        FeetAndInchMeasurementEquality.Feet feet = new FeetAndInchMeasurementEquality.Feet(5.0);
        assertTrue(feet.equals(feet));
    }

    @Test
    void testFeetEquality_NullEquivalent() {
    	FeetAndInchMeasurementEquality.Feet feet = new FeetAndInchMeasurementEquality.Feet(5.0);
        assertFalse(feet.equals(null));
    }

    @Test
    void testFeetEquality_DifferentType() {
    	FeetAndInchMeasurementEquality.Feet feet = new FeetAndInchMeasurementEquality.Feet(5.0);
        assertFalse(feet.equals("5.0"));
    }

    //Inches test

    @Test
    void testInchesEquality_SameValue() {
        assertTrue(FeetAndInchMeasurementEquality.checkInchesEquality(12.0, 12.0));
    }

    @Test
    void testInchesEquality_DifferentValue() {
        assertFalse(FeetAndInchMeasurementEquality.checkInchesEquality(12.0, 10.0));
    }

    @Test
    void testInchesEquality_SameReference() {
        FeetAndInchMeasurementEquality.Inches inches = new FeetAndInchMeasurementEquality.Inches(12.0);
        assertTrue(inches.equals(inches));
    }
    
    @Test
    void testInchesEquality_NullEquivalent() {
        FeetAndInchMeasurementEquality.Inches inches = new FeetAndInchMeasurementEquality.Inches(12.0);
        assertFalse(inches.equals(null));
    }

    @Test
    void testInchesEquality_DifferentType() {
    	FeetAndInchMeasurementEquality.Inches inches = new FeetAndInchMeasurementEquality.Inches(12.0);
        assertFalse(inches.equals("12.0"));
    }

    
}
