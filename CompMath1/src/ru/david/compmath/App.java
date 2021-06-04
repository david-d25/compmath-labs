package ru.david.compmath;

import ru.david.compmath.matrix.Matrix;
import ru.david.compmath.matrix.MatrixFormatException;
import ru.david.compmath.matrix.Provider;
import ru.david.compmath.matrix.ProvidingException;
import ru.david.compmath.ui.MatrixProviderPrompter;

public class App {
    @SuppressWarnings("")
    public static void main(String[] args) {
        App app = new App();
        System.out.println("Лаба Давида по вычмату!!!!!!! (первая)");
        new Thread(() -> {
            while (true) app.run();
        }).start();
    }

    public void run() {
        new MatrixProviderPrompter().prompt().ifPresent(this::solve);
    }

    private void solve(Provider<Matrix> provider) {
        Matrix matrix = null;
        try {
            matrix = provider.get();
        } catch (ProvidingException e) {
            System.out.println("Похоже, у нас проблемы: " + e.getMessage());
        } catch (MatrixFormatException e) {
            System.out.println("Проблемы с матрицей: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Матрица должна состоять только из чисел");
        }

        if (matrix == null)
            return;

        double[] solution = MatrixUtils.solveGaussian(matrix);
        Matrix triangularMatrix = matrix.clone();
        int switchesCount = MatrixUtils.makeTriangular(triangularMatrix);

        double determinant = (switchesCount % 2 == 0 ? 1 : -1) * MatrixUtils.getDeterminant(triangularMatrix);

        System.out.printf("Определитель: %.4f\n", determinant);

        if (solution == null) {
            System.out.println("Не получилось найти решения для этой матрицы T.T");
            System.out.println("Попробуйте ввести матрицу с ненулевым определителем");
            return;
        }

        System.out.println("Результат: ");
        for (int i = 0; i < solution.length; i++)
            System.out.printf("x%s = %s;\n", i+1, Math.round(solution[i]*100000)/100000.0);

        double[] discrepancy = MatrixUtils.getDiscrepancy(matrix, solution);
        System.out.println("Невязки: ");
        for (int i = 0; i < discrepancy.length; i++)
            System.out.printf("x%s = %.32f;\n", i+1, discrepancy[i]);
    }
}
