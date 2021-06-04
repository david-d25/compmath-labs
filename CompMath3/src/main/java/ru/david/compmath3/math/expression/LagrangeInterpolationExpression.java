package ru.david.compmath3.math.expression;

import ru.david.compmath3.math.model.XY;

import java.util.List;

public class LagrangeInterpolationExpression implements Expression {
    private List<XY> table;
    private Expression x;

    public LagrangeInterpolationExpression(List<XY> table, Expression x) {
        this.table = table;
        this.x = x;
    }

    @Override
    public Double value() {
        double result = 0;
        double xVal = x.value();

        for (int i = 0; i < table.size(); i++) {
            double monomial = table.get(i).y;
            for (int j = 0; j < table.size(); j++) {
                if (j != i) {
                    monomial *= xVal - table.get(j).x;
                    monomial /= table.get(i).x - table.get(j).x;
                }
            }
            result += monomial;
        }

        return result;
    }
}
