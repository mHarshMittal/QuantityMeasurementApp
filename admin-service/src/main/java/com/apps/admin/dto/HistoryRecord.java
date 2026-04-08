package com.apps.admin.dto;

import java.time.LocalDateTime;

public class HistoryRecord {
    private Long id;
    private Long userId;
    private String userEmail;
    private String operation;
    private Double thisValue;
    private String thisUnit;
    private String thisMeasurementType;
    private Double thatValue;
    private String thatUnit;
    private String thatMeasurementType;
    private Double resultValue;
    private String resultUnit;
    private String resultString;
    private boolean error;
    private String errorMessage;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long u) { this.userId = u; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String e) { this.userEmail = e; }
    public String getOperation() { return operation; }
    public void setOperation(String o) { this.operation = o; }
    public Double getThisValue() { return thisValue; }
    public void setThisValue(Double v) { this.thisValue = v; }
    public String getThisUnit() { return thisUnit; }
    public void setThisUnit(String u) { this.thisUnit = u; }
    public String getThisMeasurementType() { return thisMeasurementType; }
    public void setThisMeasurementType(String t) { this.thisMeasurementType = t; }
    public Double getThatValue() { return thatValue; }
    public void setThatValue(Double v) { this.thatValue = v; }
    public String getThatUnit() { return thatUnit; }
    public void setThatUnit(String u) { this.thatUnit = u; }
    public String getThatMeasurementType() { return thatMeasurementType; }
    public void setThatMeasurementType(String t) { this.thatMeasurementType = t; }
    public Double getResultValue() { return resultValue; }
    public void setResultValue(Double v) { this.resultValue = v; }
    public String getResultUnit() { return resultUnit; }
    public void setResultUnit(String u) { this.resultUnit = u; }
    public String getResultString() { return resultString; }
    public void setResultString(String s) { this.resultString = s; }
    public boolean isError() { return error; }
    public void setError(boolean e) { this.error = e; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String m) { this.errorMessage = m; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime t) { this.createdAt = t; }
}
