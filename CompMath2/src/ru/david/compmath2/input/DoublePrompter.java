package ru.david.compmath2.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.Optional;

public class DoublePrompter implements Prompter<Double> {
    private String prompt = "(число) > ";

    public DoublePrompter() {}

    public DoublePrompter(String prompt) {
        this.prompt = prompt;
    }

    @Override
    public Optional<Double> prompt() {
        System.out.print(prompt);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                String line = reader.readLine();
                if (line.isEmpty())
                    return Optional.empty();

                return Optional.of(Double.parseDouble(line));
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.print(prompt);
            } catch (IOException e) {
                return Optional.empty();
            }
        }
    }
}
