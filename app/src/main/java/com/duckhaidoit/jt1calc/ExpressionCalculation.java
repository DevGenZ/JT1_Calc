package com.duckhaidoit.jt1calc;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.operator.Operator;

public class ExpressionCalculation {
    private String expression;

    public ExpressionCalculation(String expression) {
        this.expression = expression;
    }

    //Đưa toán tử về dạng chuẩn để tính toán
    private void standardizedExpression() {
        this.expression = this.expression.replaceAll("×", "*");
        this.expression = this.expression.replaceAll("÷", "/");
        this.expression = this.expression.replaceAll("√\\(", "sqrt(");
    }

    //Định nghĩa toán tử percent
    private Operator percent = new Operator("%", 1, true, Operator.PRECEDENCE_POWER + 1) {
        @Override
        public double apply(double... args) {
            final double arg = args[0];

            return arg / 100;
        }
    };

    public double calculate() {
        try {
            standardizedExpression();

            Expression e;
            if (expression.contains("π")) {
                e = new ExpressionBuilder(expression).operator(percent).variables("π").build().setVariable("π", Math.PI);
            } else {
                e = new ExpressionBuilder(expression).operator(percent).build();
            }

            return e.evaluate();
        } catch (Exception e) {
            return Double.NaN;
        }
    }
}
