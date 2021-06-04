package ru.david.compmath4.math.expression;

public class VariableExpression implements Expression {
    private double value = 0;

    public void setValue(double variable) {
        this.value = variable;
    }

    @Override
    public Double value() {
        return value;
    }
}
