package ru.david.compmath.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

public class FilePrompter implements Prompter<File> {
    public Optional<File> prompt() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String line;
        System.out.print("(имя файла) > ");
        try {
            while (!(line = reader.readLine()).isEmpty()) {
                File file = new File(line);
                if (!file.exists() || !file.canRead())
                    System.out.print("Введите имя существующего файла, который можно читать\n(имя файла) > ");
                else
                    return Optional.of(file);
            }
        } catch (IOException ignored) {}
        return Optional.empty();
    }
}
