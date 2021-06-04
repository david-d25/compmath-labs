package ru.david.compmath4.gui.graph;

import ru.david.compmath4.math.expression.Expression;
import ru.david.compmath4.math.expression.VariableExpression;

import java.awt.Color;

public class GraphData {
    private Expression expression;
    private VariableExpression x;
    private Color color;
    private String name;
    private int strokeWidth;

    private double lowerLimit = Double.NEGATIVE_INFINITY;
    private double upperLimit = Double.POSITIVE_INFINITY;
    private boolean limitEnabled = false;

    public GraphData(Expression expression, VariableExpression x, Color color, int strokeWidth, String name) {
        this.expression = expression;
        this.x = x;
        this.color = color;
        this.name = name;
        this.strokeWidth = strokeWidth;
    }

    public Expression getExpression() {
        return expression;
    }

    public VariableExpression getX() {
        return x;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public double getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(double upperLimit) {
        this.upperLimit = upperLimit;
    }

    public double getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(double lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public boolean isLimitEnabled() {
        return limitEnabled;
    }

    public void setLimitEnabled(boolean limitEnabled) {
        this.limitEnabled = limitEnabled;
    }
}
