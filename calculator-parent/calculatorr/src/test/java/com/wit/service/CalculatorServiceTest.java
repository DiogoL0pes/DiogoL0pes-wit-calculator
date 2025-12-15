package com.wit.service;

import com.wit.model.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculatorServiceImplTest {

    private CalculatorService service;

    @BeforeEach
    void setUp() {
        service = new CalculatorServiceImpl();
    }

    @Test
    void compute_shouldSumTwoNumbers() {
        BigDecimal result = service.compute(
                new BigDecimal("2"),
                new BigDecimal("3"),
                Constants.SUM
        );

        assertThat(result).isEqualByComparingTo("5");
    }

    @Test
    void compute_shouldSubtractTwoNumbers() {
        BigDecimal result = service.compute(
                new BigDecimal("10"),
                new BigDecimal("4"),
                Constants.SUB
        );

        assertThat(result).isEqualByComparingTo("6");
    }

    @Test
    void compute_shouldMultiplyTwoNumbers() {
        BigDecimal result = service.compute(
                new BigDecimal("4"),
                new BigDecimal("5"),
                Constants.MUL
        );

        assertThat(result).isEqualByComparingTo("20");
    }

    @Test
    void compute_shouldDivideTwoNumbers() {
        BigDecimal result = service.compute(
                new BigDecimal("10"),
                new BigDecimal("4"),
                Constants.DIV
        );

        assertThat(result).isEqualByComparingTo("2.5");
    }

    @Test
    void compute_shouldThrowExceptionForInvalidOperation() {
        assertThrows(IllegalArgumentException.class, () ->
                service.compute(
                        BigDecimal.ONE,
                        BigDecimal.ONE,
                        "INVALID"
                )
        );
    }
}

