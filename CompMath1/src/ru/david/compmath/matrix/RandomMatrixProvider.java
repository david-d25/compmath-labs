package ru.david.compmath.matrix;

import ru.david.compmath.ui.DoubleArrayPrompter;

import java.util.List;

public class RandomMatrixProvider implements Provider<Matrix> {
    private static final double RANDOM_MIN = -100;
    private static final double RANDOM_MAX = 100;

    @Override
    public Matrix get() {
        System.out.println("Введите размер случайной матрицы");
        System.out.println("Лайфхак: введите 2 числа через пробел, чтобы указать диапазон для случайного размера");

        List<Double> numbers = new DoubleArrayPrompter().prompt().orElse(null);
        if (numbers == null || numbers.size() == 0)
            return null;

        int size;
        if (numbers.size() == 1)
            size = (int)(double)numbers.get(0);
        else {
            double minSize = numbers.get(0);
            double maxSize = numbers.get(1);
            double result = minSize + Math.random()*(maxSize - minSize);
            size = (int)result;
        }

        if (size < 0)
            return null;

        double[][] data = new double[size][size];
        double[] freeMembers = new double[size];
        for (int a = 0; a < size; a++) {
            freeMembers[a] = (int)(RANDOM_MIN + Math.random()*(RANDOM_MAX - RANDOM_MIN));
            for (int b = 0; b < size; b++)
                data[a][b] = (int)(RANDOM_MIN + Math.random()*(RANDOM_MAX - RANDOM_MIN));
        }

        Matrix matrix = new Matrix(data, freeMembers);
        System.out.println("Вот какая матрица получилась: ");
        System.out.println(matrix);

        return matrix;
    }
}
