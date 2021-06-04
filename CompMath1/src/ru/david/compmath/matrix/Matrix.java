package ru.david.compmath.matrix;

public class Matrix implements Cloneable {
    private double[][] data;
    private double[] freeMembers;

    public Matrix(double[][] data, double[] freeMembers) {
        this.data = data;
        this.freeMembers = freeMembers;
    }

    public double[][] getData() {
        return data;
    }

    public double[] getFreeMembers() {
        return freeMembers;
    }

    public static Matrix parseMatrix(String src) throws IllegalArgumentException {
        String[] lines = src.replaceAll("[\\t ]+", " ").split("\n");
        int size = lines.length;
        double[][] data = new double[size][size];
        double[] freeMembers = new double[size];

        for (int i = 0; i < size; i++) {
            String[] row = lines[i].trim().split(" ");
            if (row.length != size+1)
                throw new MatrixFormatException("Неправильный формат матрицы. Матрица должна быть квадратной со свободными членаим в конце");
            for (int j = 0; j < size; j++)
                data[i][j] = Double.parseDouble(row[j]);
            freeMembers[i] = Double.parseDouble(row[size]);
        }

        return new Matrix(data, freeMembers);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < data.length; row++) {
            for (int column = 0; column < data[row].length; column++) {
                builder.append(toNiceString(data[row][column]));
            }
            builder.append(" | ").append(toNiceString(freeMembers[row])).append('\n');
        }
        return builder.toString();
    }

    private String toNiceString(double number) {
        return String.format(number >= 0 ? " %8.2f" : "%9.2f", Math.round(number*1000)/1000.0);
    }

    @Override
    public Matrix clone() {
        double[][] data = new double[this.data.length][];
        double[] freeMembers = new double[this.freeMembers.length];
        System.arraycopy(this.freeMembers, 0, freeMembers, 0, freeMembers.length);
        for (int i = 0; i < data.length; i++) {
            data[i] = new double[this.data[i].length];
            System.arraycopy(this.data[i], 0, data[i], 0, this.data[i].length);
        }
        return new Matrix(data, freeMembers);
    }
}
