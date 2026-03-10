package com.UnitConversion.UnitConversion;

import java.util.Objects;

public class Quantity<U extends IMeasurable> {

    private static final double EPSILON = 1e-6;

    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {

        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null");

        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Invalid value");

        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    public Quantity<U> convertTo(U targetUnit) {

        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double baseValue = unit.convertToBaseUnit(value);
        double converted = targetUnit.convertFromBaseUnit(baseValue);

        return new Quantity<>(converted, targetUnit);
    }

    // ---------------- ADDITION (UC11) ----------------

    public static <U extends IMeasurable> Quantity<U> add(Quantity<U> a, Quantity<U> b) {
        return add(a, b, a.unit);
    }

    public static <U extends IMeasurable> Quantity<U> add(Quantity<U> a, Quantity<U> b, U targetUnit) {

        if (a == null || b == null || targetUnit == null)
            throw new IllegalArgumentException("Invalid input");

        if (a.unit.getClass() != b.unit.getClass())
            throw new IllegalArgumentException("Different measurement categories");

        double baseA = a.unit.convertToBaseUnit(a.value);
        double baseB = b.unit.convertToBaseUnit(b.value);

        double sumBase = baseA + baseB;

        double result = targetUnit.convertFromBaseUnit(sumBase);

        return new Quantity<>(round(result), targetUnit);
    }

    // ---------------- SUBTRACTION (UC12) ----------------

    public Quantity<U> subtract(Quantity<U> other) {
        return subtract(other, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {

        if (other == null || targetUnit == null)
            throw new IllegalArgumentException("Invalid input");

        if (this.unit.getClass() != other.unit.getClass())
            throw new IllegalArgumentException("Different measurement categories");

        double baseThis = unit.convertToBaseUnit(value);
        double baseOther = other.unit.convertToBaseUnit(other.value);

        double resultBase = baseThis - baseOther;

        double result = targetUnit.convertFromBaseUnit(resultBase);

        return new Quantity<>(round(result), targetUnit);
    }

    // ---------------- DIVISION (UC12) ----------------

    public double divide(Quantity<U> other) {

        if (other == null)
            throw new IllegalArgumentException("Quantity cannot be null");

        if (this.unit.getClass() != other.unit.getClass())
            throw new IllegalArgumentException("Different measurement categories");

        double baseThis = unit.convertToBaseUnit(value);
        double baseOther = other.unit.convertToBaseUnit(other.value);

        if (Math.abs(baseOther) < EPSILON)
            throw new ArithmeticException("Division by zero");

        return baseThis / baseOther;
    }

    // ---------------- ROUNDING ----------------

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    // ---------------- EQUALITY ----------------

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        Quantity<?> other = (Quantity<?>) obj;

        if (unit.getClass() != other.unit.getClass())
            return false;

        double baseThis = unit.convertToBaseUnit(value);
        double baseOther = other.unit.convertToBaseUnit(other.value);

        return Math.abs(baseThis - baseOther) < EPSILON;
    }

    @Override
    public int hashCode() {
        return Objects.hash(unit.convertToBaseUnit(value));
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit.getUnitName() + ")";
    }
}