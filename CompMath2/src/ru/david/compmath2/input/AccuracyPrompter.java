package ru.david.compmath2.input;

import java.util.Optional;

public class AccuracyPrompter implements Prompter<Double> {
    @Override
    public Optional<Double> prompt() {
        while (true) {
            Optional<Double> accuracyOptional = new DoublePrompter("Точность: ").prompt();
            if (!accuracyOptional.isPresent())
                return Optional.empty();
            double accuracy = accuracyOptional.get();
            if (Math.abs(accuracy) <=  0.000000000000001) {
                System.out.println("Многие функции не получится посчитать с такой высокой точностью.");
                System.out.print("Вы точно хотите использовать эту точность? [y/N]: ");
                Optional<String> answer = new StringPrompter("").prompt();
                if (!answer.isPresent() || !answer.get().toLowerCase().equals("y"))
                    continue;
            }
            return Optional.of(accuracy);
        }
    }
}
