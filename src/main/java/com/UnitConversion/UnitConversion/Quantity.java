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

    // ---------- ADD ----------

    public Quantity<U> add(Quantity<U> other) {
        return add(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {

        if (other == null)
            throw new IllegalArgumentException("Operand cannot be null");

        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        if (unit.getClass() != other.unit.getClass())
            throw new IllegalArgumentException("Incompatible unit types");

        double baseA = unit.convertToBaseUnit(value);
        double baseB = other.unit.convertToBaseUnit(other.value);

        double sumBase = baseA + baseB;

        double result = targetUnit.convertFromBaseUnit(sumBase);

        return new Quantity<>(result, targetUnit);
    }

    // ---------- SUBTRACT ----------

    public Quantity<U> subtract(Quantity<U> other) {
        return subtract(other, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {

        if (other == null)
            throw new IllegalArgumentException("Operand cannot be null");

        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        if (unit.getClass() != other.unit.getClass())
            throw new IllegalArgumentException("Incompatible unit types");

        double baseA = unit.convertToBaseUnit(value);
        double baseB = other.unit.convertToBaseUnit(other.value);

        double diffBase = baseA - baseB;

        double result = targetUnit.convertFromBaseUnit(diffBase);

        return new Quantity<>(result, targetUnit);
    }

    // ---------- DIVIDE ----------

    public double divide(Quantity<U> other) {

        if (other == null)
            throw new IllegalArgumentException("Operand cannot be null");

        if (unit.getClass() != other.unit.getClass())
            throw new IllegalArgumentException("Incompatible unit types");

        double baseA = unit.convertToBaseUnit(value);
        double baseB = other.unit.convertToBaseUnit(other.value);

        if (Math.abs(baseB) < EPSILON)
            throw new ArithmeticException("Division by zero");

        return baseA / baseB;
    }

    // ---------- EQUALITY ----------

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