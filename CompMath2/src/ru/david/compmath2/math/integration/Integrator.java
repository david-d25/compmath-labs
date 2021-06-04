package ru.david.compmath2.math.integration;

import ru.david.compmath2.math.expression.Expression;
import ru.david.compmath2.math.expression.VariableExpression;

public class Integrator {
    private static final double VERY_SMALL_NUMBER = 0.0000001;
    private static final int SPLITS_LIMIT = 100_000_000;

    public static IntegrationResult solve(
            Expression expression,
            VariableExpression variable,
            IntegrationLimits limits,
            double accuracy
    ) {
        int splits = 4;

        double previousResult;
        double result = solve(expression, variable, limits, 2);
        double error;

        do {
            previousResult = result;
            result = solve(expression, variable, limits, splits);
            error = Math.abs(previousResult - result)/3;
            splits *= 2;
        } while (Math.abs(error) > Math.abs(accuracy) && Double.isFinite(result) && splits < SPLITS_LIMIT);

        if (Double.isFinite(result) && splits < SPLITS_LIMIT)
            return new IntegrationResult(result, error, splits);
        return new IntegrationResult(IntegrationResult.Status.GAP);
    }

    private static double solve(
            Expression expression,
            VariableExpression variable,
            IntegrationLimits limits,
            int splits) {
        double deltaXSize = (limits.getHighLimit() - limits.getLowLimit())/splits;
        double result = 0;

        for (int i = 0; i < splits; i++) {
            variable.setValue(limits.getLowLimit() + i*deltaXSize);
            double leftY = expression.value();
            if (!Double.isFinite(leftY)) {
                if (i == 0)
                    return Double.POSITIVE_INFINITY;

                variable.setValue(limits.getLowLimit() + i*deltaXSize + VERY_SMALL_NUMBER);
                leftY = expression.value();
            }

            variable.setValue(limits.getLowLimit() + (i+1)*deltaXSize);
            double rightY = expression.value();
            if (!Double.isFinite(rightY)) {
                if (i == splits - 1)
                    return Double.POSITIVE_INFINITY;

                variable.setValue(limits.getLowLimit() + (i+1)*deltaXSize - VERY_SMALL_NUMBER);
                rightY = expression.value();
            }

            double avgY = (leftY+rightY)/2;
            result += avgY*deltaXSize;
        }

        return result;
    }
}
