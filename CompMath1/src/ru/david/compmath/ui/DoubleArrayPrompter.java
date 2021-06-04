package ru.david.compmath.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoubleArrayPrompter implements Prompter<List<Double>> {
    @Override
    public Optional<List<Double>> prompt() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                System.out.print("(числа через пробел) > ");
                String[] raw = reader.readLine().split(" ");
                if (raw.length == 0 || raw[0].isEmpty())
                    break;

                try {
                    List<Double> result = new ArrayList<>(raw.length);
                    for (String current : raw)
                        result.add(Double.parseDouble(current));
                    return Optional.of(result);
                } catch (NumberFormatException e) {
                    System.out.println("Следует вводить только числа или пустую строку");
                }

            }
        } catch (IOException ignored) {}
        return Optional.empty();
    }
}
