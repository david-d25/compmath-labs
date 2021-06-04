package ru.david.compmath4.math.expression;

public class MathFunctionAdapter implements Expression {
    private Expression target;
    private VariableExpression[] arguments;

    public MathFunctionAdapter(Expression target, VariableExpression... arguments) {
        this.target = target;
        this.arguments = arguments;
    }

    public Double value(double... argumentValues) {
        for (int i = 0; i < Math.min(arguments.length, argumentValues.length); i++)
            arguments[i].setValue(argumentValues[i]);
        return value();
    }

    @Override
    public Double value() {
        return target.value();
    }
}
