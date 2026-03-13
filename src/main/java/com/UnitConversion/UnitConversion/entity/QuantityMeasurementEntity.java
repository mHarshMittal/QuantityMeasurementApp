package com.UnitConversion.UnitConversion.entity;

public class QuantityMeasurementEntity {

    private String operation;
    private String operand1;
    private String operand2;
    private String result;

    public QuantityMeasurementEntity(String operation,String operand1,String operand2,String result){
        this.operation = operation;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.result = result;
    }

    public String getOperation() {
        return operation;
    }

    public String getResult() {
        return result;
    }

	public double getValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getUnit() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getMeasurementType() {
		// TODO Auto-generated method stub
		return null;
	}

	public double getValue1() {
		// TODO Auto-generated method stub
		return 0;
	}
}