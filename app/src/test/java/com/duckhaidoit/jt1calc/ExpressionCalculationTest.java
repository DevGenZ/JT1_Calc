package com.duckhaidoit.jt1calc;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(value = Parameterized.class)
public class ExpressionCalculationTest {
    private ExpressionCalculation e;

    private String actual;

    private double expected;

    private static final double DELTA = 1E-11;

    public ExpressionCalculationTest(double expected, String actual) {
        this.expected = expected;
        this.actual = actual;
    }

    @Before
    public void initialize() {
        e = new ExpressionCalculation(actual);
    }

    // × ÷ √ π
    @Parameterized.Parameters(name = "{index}: ExpressionCalculation.calculate({0}) = {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {0, "0"},
                {Double.parseDouble("4294967294"), Integer.MAX_VALUE + "+" + Integer.MAX_VALUE},
                {Double.parseDouble("4.6116860141324206E+18"), Integer.MAX_VALUE + "×" + Integer.MAX_VALUE},
                {921877, "922000+-123"},
                {-921877, "-922000+123"},
                {-922123, "-922000-+++-+-+123"},
                {113406000, "---+922000×---123"},
                {Double.NaN, "922000××123"},
                {Double.NaN, "1÷0"},
                {Double.NaN, "1.0÷0.0"},
                {0.33333333333, "1÷3"},
                {3.697296376497268E+197, "99^99"},
                {Double.POSITIVE_INFINITY, "999^999"},
                {Double.POSITIVE_INFINITY, "(99999^99999)+(99999^99999)"},
                {Double.POSITIVE_INFINITY / Double.NEGATIVE_INFINITY, "(999^999)÷(-999^999)"},
                {Double.NEGATIVE_INFINITY * Double.POSITIVE_INFINITY, "(999^999)×(-999^999)"},
                {0.06666666666, "1÷(3+2)÷3"},
                {Double.parseDouble("3.697296376497268E+197"), "99^99"},
                {Double.parseDouble("1E+45"), "99999999999999999999999×9999999999999999999999"},
                {0.12727922061, "9%×√(2)"},
                {0.03937402486, "√(π)%÷√(2)π"},
                {5.65685424949, "2^2√(2)"},
                {1.97845602639, "√(2√(2√(2√(2√(2√(2))))))"},
                {-49, "-7^2"},
                {49, "(-7)^2"},
                {Double.NaN, "√(92×2000"},
                {Double.NaN, "√(-2)"},
                {Math.cbrt(2), "2^(1÷3)"},
                {Math.cbrt(-2), "-2^(1÷3)"},
                {0.5035971223, "(69^4-69^3-2×69-4)÷(2×69^4-3×69^3+2×69^2-6×69-4)"},
                {45334242, "2×69^4"}
        });
    }

    @Test
    public void calculate() {
        assertEquals(expected, e.calculate(), DELTA);
    }
}