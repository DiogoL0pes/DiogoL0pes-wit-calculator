package com.wit.common.dto;
import java.math.BigDecimal;


public class CalculatorResponse {

    private BigDecimal result;
    private String requestId;

    public CalculatorResponse() {}

    public CalculatorResponse(BigDecimal result, String requestId) {
        this.result = result;
        this.requestId = requestId;
    }
    
    public CalculatorResponse(BigDecimal result) {
        this.result = result;
    }

    public BigDecimal getResult() {
        return result;
    }

    public String getRequestId() {
        return requestId;
    }
}
