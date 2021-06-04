package ru.david.compmath;

import ru.david.compmath.matrix.Matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MatrixUtils {
    public static double getDeterminant(Matrix matrix) {
        double[][] data = matrix.getData();
        if (data.length == 1)
            return data[0][0];
        else if (data.length == 2)
            return data[0][0]*data[1][1] - data[0][1]*data[1][0];
        else if (data.length == 3)
            return  data[0][0]*data[1][1]*data[2][2] +
                    data[2][0]*data[0][1]*data[1][2] +
                    data[1][0]*data[2][1]*data[0][2] -
                    data[2][0]*data[1][1]*data[0][2] -
                    data[1][0]*data[0][1]*data[2][2] -
                    data[0][0]*data[1][2]*data[2][1];
        else if (data.length < 10) {
            double result = 0;
            for (int i = 0; i < data.length; i++)
                result += data[i][0] == 0 ? 0 : (i % 2 == 0 ? 1 : -1) * data[i][0] * getDeterminant(subMatrix(matrix, i, 0));
            return result;
        } else {
            ExecutorService threadPool = Executors.newFixedThreadPool(1);

            List<Future<Double>> futures = new ArrayList<>();
            double result = 0;
            for (int i = 0; i < data.length; i++) {
                int a = i;
                if (Math.round(data[i][0]*10000000)/10000000.0 == 0) continue;
                futures.add(CompletableFuture.supplyAsync(
                        () -> (a % 2 == 0 ? 1 : -1) * data[a][0] * getDeterminant(subMatrix(matrix, a, 0)),
                        threadPool
                ));
            }

            for (Future<Double> future : futures) {
                try {
                    result += future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            return result;
        }
    }

    public static double[] getDiscrepancy(Matrix matrix, double[] solution) {
        double[][] data = matrix.getData();
        double[] freeMembers = matrix.getFreeMembers();
        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            double interResult = 0;
            for (int j = 0; j < data[i].length; j++)
                interResult += data[i][j] * solution[j];
            result[i] = interResult - freeMembers[i];
        }
        return result;
    }

    public static Matrix subMatrix(Matrix matrix, int rowIndexToDelete, int columnIndexToDelete) {
        if (matrix.getData().length <= 1)
            return new Matrix(new double[0][0], new double[0]);

        double[][] data = matrix.getData();
        double[] freeMembers = matrix.getFreeMembers();

        double[][] newData = new double[matrix.getData().length-1][matrix.getFreeMembers().length-1];
        double[] newFreeMembers = new double[matrix.getFreeMembers().length-1];

        for (int row = 0; row < newData.length; row++) {
            newFreeMembers[row] = freeMembers[row < rowIndexToDelete ? row : row+1];

            for (int column = 0; column < newData[row].length; column++)
                newData[row][column] = data
                        [row < rowIndexToDelete ? row : row+1]
                        [column < columnIndexToDelete ? column : column+1];
        }

        return new Matrix(newData, newFreeMembers);
    }

    public static double[] solveGaussian(Matrix m) {
        Matrix matrix = m.clone();
        makeTriangular(matrix);
        if (getDeterminant(matrix) == 0)
            return null;
        triangularToDiagonal(matrix);
        double[] result = new double[m.getData().length];
        double[] freeMembers = matrix.getFreeMembers();
        double[][] data = matrix.getData();
        for (int i = 0; i < result.length; i++)
            result[i] = freeMembers[i]/data[i][i];
        return result;
    }

    public static int makeTriangular(Matrix matrix){
        int switchRowsCount = 0;
        double[][] data = matrix.getData();
        for (int i = 0; i < data.length; i++) {
            int mainElem = getMainElementRowIndex(matrix, i);
            if (mainElem != i) {
                switchRowsCount++;
                switchRows(matrix, i, mainElem);
            }
            if (data[i][i] == 0)
                continue;
            for (int j = i+1; j < data.length; j++)
                addRows(matrix, i, j, -1/data[i][i]*data[j][i]);
        }
        return switchRowsCount;
    }

    public static void triangularToDiagonal(Matrix matrix) {
        double[][] data = matrix.getData();
        for (int i = data.length-1; i >= 0; i--) {
            for (int row = i-1; row >= 0; row--) {
                addRows(matrix, i, row, -1/data[i][i]*data[row][i]);
            }
        }
    }

    public static void addRows(Matrix matrix, int rowIndex1, int rowIndex2, double multiplier) {
        double[] row1 = matrix.getData()[rowIndex1];
        double[] row2 = matrix.getData()[rowIndex2];
        double[] freeMembers = matrix.getFreeMembers();

        for (int i = 0; i < row1.length; i++)
            row2[i] += multiplier * row1[i];
        freeMembers[rowIndex2] += multiplier * freeMembers[rowIndex1];
    }

    public static void switchRows(Matrix matrix, int row1, int row2) {
        double[][] data = matrix.getData();
        double[] buffer = data[row1];
        data[row1] = data[row2];
        data[row2] = buffer;

        double[] freeMembers = matrix.getFreeMembers();
        double freeMemberBuffer = freeMembers[row1];
        freeMembers[row1] = freeMembers[row2];
        freeMembers[row2] = freeMemberBuffer;
    }

    public static int getMainElementRowIndex(Matrix matrix, int index) {
        double[][] data = matrix.getData();
        int result = index;
        for (int i = index+1; i < data.length; i++) {
            if (Math.abs(data[i][index]) > Math.abs(data[result][index]))
                result = i;
        }
        return result;
    }
}
