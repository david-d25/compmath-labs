package ru.david.compmath4.math.util;

import ru.david.compmath4.math.expression.MathFunctionAdapter;
import ru.david.compmath4.math.model.XY;

import java.util.List;

public class AdamsMethodSolver {
    public static List<XY> solve(
            double startX,
            double startY,
            double step,
            double stepsNum,
            double accuracy,
            MathFunctionAdapter function
    ) {
        List<XY> result = RungeKuttaMethodSolver.solve(startX, startY, 3, step, function);
        int i = 3;
        do {
            double nextY = result.get(i).y + 1.0/24 * step * (
                    55*function.value(result.get(i).x, result.get(i).y) -
                    59*function.value(result.get(i - 1).x, result.get(i - 1).y) +
                    37*function.value(result.get(i - 2).x, result.get(i - 2).y) -
                    9*function.value(result.get(i - 3).x, result.get(i - 3).y)
            );

            double nextX = result.get(i).x + step;
            double b = nextY;

            double a;
            do {
                a = b;
                b = result.get(i).y + 1.0/24 * step * (
                                9*function.value(nextX, nextY) +
                                19*function.value(result.get(i).x, result.get(i).y) -
                                5*function.value(result.get(i - 1).x, result.get(i - 1).y) +
                                function.value(result.get(i - 2).x, result.get(i - 2).y)
                        );
            } while (Math.abs(a - b) >= accuracy);

            result.add(new XY(nextX, b));

        } while (++i <= stepsNum);

        return result;
    }
}
