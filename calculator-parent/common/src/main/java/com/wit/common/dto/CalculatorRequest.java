package com.wit.common.dto;

import java.math.BigDecimal;

public class CalculatorRequest {

    private BigDecimal a;
    private BigDecimal b;
    private String operation;
    private String requestId;


    public CalculatorRequest() {}

    public CalculatorRequest(BigDecimal a, BigDecimal b, String operation, String requestId) {
        this.a = a;
        this.b = b;
        this.operation = operation;
        this.requestId = requestId;
    }

    public BigDecimal getA() {
        return a;
    }

    public void setA(BigDecimal a) {
        this.a = a;
    }

    public BigDecimal getB() {
        return b;
    }

    public void setB(BigDecimal b) {
        this.b = b;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}

