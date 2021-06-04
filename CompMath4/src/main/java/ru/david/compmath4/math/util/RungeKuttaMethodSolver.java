package ru.david.compmath4.math.util;

import ru.david.compmath4.math.expression.MathFunctionAdapter;
import ru.david.compmath4.math.model.XY;

import java.util.LinkedList;
import java.util.List;

public class RungeKuttaMethodSolver {

    public static List<XY> solve(
            double startX,
            double startY,
            double iterations,
            double step,
            MathFunctionAdapter function
    ) {
        List<XY> result = new LinkedList<>();
        result.add(new XY(startX, startY));
        double nextY = startY;
        for (double currentX = startX, iteration = 0; iteration < iterations; currentX += step, iteration++) {
            nextY = generateNextY(currentX, nextY, step, function);
            result.add(new XY(currentX + step, nextY));
        }
        return result;
    }

    private static double generateNextY(double x, double y, double step, MathFunctionAdapter function) {
        double k0, k1, k2, k3, delta;
        k0 = function.value(x, y);
        k1 = function.value(x + step/2, y + k0 * step/2);
        k2 = function.value(x + step/2, y + k1 * step/2);
        k3 = function.value(x + step, y + k2 * step);
        delta = step * (k0 + 2 * k1 + 2 * k2 + k3)/6;
        return y + delta;
    }
}
