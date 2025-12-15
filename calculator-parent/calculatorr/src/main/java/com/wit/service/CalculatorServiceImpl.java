package com.wit.service;

import com.wit.model.Constants;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CalculatorServiceImpl implements CalculatorService {

    @Override
    public BigDecimal compute(BigDecimal a, BigDecimal b, String operation) {
    	if (Constants.SUM.equals(operation)) {
    	    return a.add(b);
    	} else if (Constants.SUB.equals(operation)) {
    	    return a.subtract(b);
    	} else if (Constants.MUL.equals(operation)) {
    	    return a.multiply(b);
    	} else if (Constants.DIV.equals(operation)) {
    	    return a.divide(b);
    	} else {
    	    throw new IllegalArgumentException("Invalid operation");
    	}
    }
}

