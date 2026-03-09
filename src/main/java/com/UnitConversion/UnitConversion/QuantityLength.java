package com.UnitConversion.UnitConversion;

public class QuantityLength {

    private double value;
    private LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {
        if (!Double.isFinite(value) || unit == null) {
            throw new IllegalArgumentException("Invalid input");
        }
        this.value = value;
        this.unit = unit;
    }

    public static double convert(double value, LengthUnit from, LengthUnit to) {

        if (!Double.isFinite(value) || from == null || to == null) {
            throw new IllegalArgumentException("Invalid input");
        }

        double baseValue = from.convertToBaseUnit(value);
        return to.convertFromBaseUnit(baseValue);
    }

    public QuantityLength convertTo(LengthUnit targetUnit) {
        double convertedValue = convert(this.value, this.unit, targetUnit);
        return new QuantityLength(convertedValue, targetUnit);
    }

    // UC7 addition with explicit target unit
    public static QuantityLength add(QuantityLength l1, QuantityLength l2, LengthUnit targetUnit) {

        if (l1 == null || l2 == null || targetUnit == null) {
            throw new IllegalArgumentException("Invalid input");
        }

        double result = addBase(l1, l2, targetUnit);

        return new QuantityLength(result, targetUnit);
    }

    // private utility addition method
    private static double addBase(QuantityLength l1, QuantityLength l2, LengthUnit targetUnit) {

        double v1 = l1.unit.convertToBaseUnit(l1.value);
        double v2 = l2.unit.convertToBaseUnit(l2.value);

        double sumBase = v1 + v2;

        return targetUnit.convertFromBaseUnit(sumBase);
    }

    @Override
    public String toString() {
        return value + " " + unit;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof QuantityLength)) return false;

        QuantityLength other = (QuantityLength) obj;

        double thisBase = this.unit.convertToBaseUnit(this.value);
        double otherBase = other.unit.convertToBaseUnit(other.value);

        return thisBase == otherBase;
    }
}