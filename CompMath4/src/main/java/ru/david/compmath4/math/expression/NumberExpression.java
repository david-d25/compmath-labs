package ru.david.compmath4.math.expression;

public class NumberExpression implements Expression {
    private double value;

    public NumberExpression(double value) {
        this.value = value;
    }

    @Override
    public Double value() {
        return value;
    }
}
