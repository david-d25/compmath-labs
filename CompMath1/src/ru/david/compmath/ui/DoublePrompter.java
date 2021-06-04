package ru.david.compmath.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.Optional;

public class DoublePrompter implements Prompter<Double> {
    @Override
    public Optional<Double> prompt() {
        String PROMPT = "(число) > ";
        System.out.print(PROMPT);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                String line = reader.readLine();
                return Optional.of(Double.parseDouble(line));
            } catch (InputMismatchException | IOException | NumberFormatException e) {
                System.out.print(PROMPT);
            }
        }
    }
}
