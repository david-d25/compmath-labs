package ru.david.compmath2.math.expression;

import java.util.function.Function;

public class FunctionExpression implements Expression {

    private Expression expression;
    private Function<Double, Double> function;

    public FunctionExpression(Expression expression, Function<Double, Double> function) {
        this.expression = expression;
        this.function = function;
    }

    @Override
    public Double value() {
        return function.apply(expression.value());
    }
}
