package com.wit.common.dto;
import java.math.BigDecimal;

public class CalculatorResponse {

    private BigDecimal result;

    public CalculatorResponse() {}

    public CalculatorResponse(BigDecimal result) {
        this.result = result;
    }

    public BigDecimal getResult() {
        return result;
    }
}
