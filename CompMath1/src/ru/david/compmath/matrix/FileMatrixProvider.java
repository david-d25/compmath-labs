package ru.david.compmath.matrix;

import ru.david.compmath.ui.FilePrompter;

import java.io.*;

public class FileMatrixProvider implements Provider<Matrix> {

    @Override
    public Matrix get() throws ProvidingException {
        File file = new FilePrompter().prompt().orElse(null);
        if (file == null)
            return null;

        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null)
                builder.append(line).append('\n');

            return Matrix.parseMatrix(builder.toString());
        } catch (IOException e) {
            throw new ProvidingException("Не получилось прочитать файл " + file.toString());
        }
    }
}
