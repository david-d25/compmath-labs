package ru.david.compmath.matrix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StdInMatrixProvider implements Provider<Matrix> {

    @Override
    public Matrix get() throws ProvidingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите матрицу вместе со свободными членами, добавьте в конец пустую строку: ");
        StringBuilder builder = new StringBuilder();
        try {
            String line;
            while (!(line = reader.readLine()).isEmpty())
                builder.append(line).append('\n');

            return Matrix.parseMatrix(builder.toString());
        } catch (IOException e) {
            throw new ProvidingException("Не получилось прочитать с клавиатуры: " + e.getMessage());
        }
    }
}
