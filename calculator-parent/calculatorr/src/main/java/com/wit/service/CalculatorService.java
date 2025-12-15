package com.wit.service;

import java.math.BigDecimal;

public interface CalculatorService {
    BigDecimal compute(BigDecimal a, BigDecimal b, String operation);
}
