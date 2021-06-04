package ru.david.compmath3.math.expression;

import java.util.function.BiFunction;

public class ArithmeticExpression implements Expression {

    private Expression left;
    private Expression right;
    private Operation operation;

    public ArithmeticExpression(Expression left, Expression right, Operation operation) {
        this.left = left;
        this.right = right;
        this.operation = operation;
    }

    @Override
    public Double value() {
        return operation.process(left, right);
    }

    public enum Operation {
        ADD((a, b) -> a.value() + b.value()),
        SUBTRACT((a, b) -> a.value() - b.value()),
        MULTIPLY((a, b) -> a.value() * b.value()),
        DIVIDE((a, b) -> a.value() / b.value()),
        POWER((a, b) -> Math.pow(a.value(), b.value())),
        MOD((a, b) -> a.value() % b.value()),
        AND((a, b) -> a.value() != 0 && b.value() != 0 ? 1d : 0d),
        OR((a, b) -> a.value() != 0 || b.value() != 0 ? 1d : 0d),
        LESS_THAN((a, b) -> a.value() < b.value() ? 1d : 0d),
        LESS_OR_EQ((a, b) -> a.value() <= b.value() ? 1d : 0d),
        GREATER_THAN((a, b) -> a.value() > b.value() ? 1d : 0d),
        GREATER_OR_EQ((a, b) -> a.value() >= b.value() ? 1d : 0d);

        private BiFunction<Expression, Expression, Double> operationImpl;

        Operation(BiFunction<Expression, Expression, Double> operationImpl) {
            this.operationImpl = operationImpl;
        }

        private double process(Expression left, Expression right) {
            return operationImpl.apply(left, right);
        }
    }
}
