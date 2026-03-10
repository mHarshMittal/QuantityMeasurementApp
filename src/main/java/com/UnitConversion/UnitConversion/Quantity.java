package com.UnitConversion.UnitConversion;

public class Quantity<U extends IMeasurable> {

    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    public Quantity<U> convertTo(U newUnit) {

        if (newUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double baseValue = unit.convertToBaseUnit(value);
        double convertedValue = newUnit.convertFromBaseUnit(baseValue);

        return new Quantity<>(convertedValue, newUnit);
    }

    private double performBaseArithmetic(Quantity<U> other, ArithimaticOperation operation) {

        if (other == null) {
            throw new IllegalArgumentException("Operand cannot be null");
        }

        if (!unit.getClass().equals(other.unit.getClass())) {
            throw new IllegalArgumentException("Cannot perform arithmetic on different measurement categories");
        }

        unit.validateOperationSupport(operation.name());

        double baseA = unit.convertToBaseUnit(value);
        double baseB = other.unit.convertToBaseUnit(other.value);

        return operation.apply(baseA, baseB);
    }

    public Quantity<U> add(Quantity<U> other) {

        double result = performBaseArithmetic(other, ArithimaticOperation.ADD);
        double finalValue = unit.convertFromBaseUnit(result);

        return new Quantity<>(finalValue, unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {

        double result = performBaseArithmetic(other, ArithimaticOperation.ADD);
        double finalValue = targetUnit.convertFromBaseUnit(result);

        return new Quantity<>(finalValue, targetUnit);
    }

    public Quantity<U> subtract(Quantity<U> other) {

        double result = performBaseArithmetic(other, ArithimaticOperation.SUBTRACT);
        double finalValue = unit.convertFromBaseUnit(result);

        return new Quantity<>(finalValue, unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {

        double result = performBaseArithmetic(other, ArithimaticOperation.SUBTRACT);
        double finalValue = targetUnit.convertFromBaseUnit(result);

        return new Quantity<>(finalValue, targetUnit);
    }

    public double divide(Quantity<U> other) {

        return performBaseArithmetic(other, ArithimaticOperation.DIVIDE);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (!(obj instanceof Quantity<?> other))
            return false;

        if (!unit.getClass().equals(other.unit.getClass()))
            return false;

        double baseA = unit.convertToBaseUnit(value);
        double baseB = ((IMeasurable) other.unit).convertToBaseUnit(other.value);

        double epsilon = 0.0001;

        return Math.abs(baseA - baseB) < epsilon;
    }

    @Override
    public String toString() {
        return value + " " + unit.getUnitName();
    }
}