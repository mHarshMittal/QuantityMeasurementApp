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

    public static double convert(double value,LengthUnit from,LengthUnit to) {

        if (!Double.isFinite(value) || from == null || to == null) {
            throw new IllegalArgumentException("Invalid input");
        }

        //Convert to base (FEET)
        double baseValue = value * from.getFactor();

        //Convert base to target
        return baseValue / to.getFactor();
    }

    public QuantityLength convertTo(LengthUnit targetUnit) {
        double convertedValue = convert(this.value, this.unit, targetUnit);
        return new QuantityLength(convertedValue, targetUnit);
    }

    @Override
    public String toString() {
        return value + " " + unit;
    }

    // Simple equality check
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof QuantityLength)) return false;

        QuantityLength other = (QuantityLength) obj;

        double thisInFeet = this.value * this.unit.getFactor();
        double otherInFeet = other.value * other.unit.getFactor();

        return thisInFeet == otherInFeet;
    }

}
