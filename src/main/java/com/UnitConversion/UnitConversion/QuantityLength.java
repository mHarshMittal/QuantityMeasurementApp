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

        double baseValue = value * from.getFactor();
        return baseValue / to.getFactor();
    }

    public QuantityLength convertTo(LengthUnit targetUnit) {
        double convertedValue = convert(this.value, this.unit, targetUnit);
        return new QuantityLength(convertedValue, targetUnit);
    }

    // UC7 Addition with explicit target unit
    public static QuantityLength add(QuantityLength l1, QuantityLength l2, LengthUnit targetUnit) {

        if (l1 == null || l2 == null || targetUnit == null) {
            throw new IllegalArgumentException("Invalid input");
        }

        if (!Double.isFinite(l1.value) || !Double.isFinite(l2.value)) {
            throw new IllegalArgumentException("Invalid input");
        }

        double result = addBase(l1, l2, targetUnit);

        return new QuantityLength(result, targetUnit);
    }

    // private utility method
    private static double addBase(QuantityLength l1, QuantityLength l2, LengthUnit targetUnit) {

        double v1 = l1.value * l1.unit.getFactor();
        double v2 = l2.value * l2.unit.getFactor();

        double sumFeet = v1 + v2;

        return sumFeet / targetUnit.getFactor();
    }

    @Override
    public String toString() {
        return value + " " + unit;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof QuantityLength)) return false;

        QuantityLength other = (QuantityLength) obj;

        double thisFeet = this.value * this.unit.getFactor();
        double otherFeet = other.value * other.unit.getFactor();

        return thisFeet == otherFeet;
    }
}