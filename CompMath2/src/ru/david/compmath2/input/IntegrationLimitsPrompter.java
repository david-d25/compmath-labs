package ru.david.compmath2.input;

import ru.david.compmath2.math.integration.IntegrationLimits;

import java.util.List;
import java.util.Optional;

public class IntegrationLimitsPrompter implements Prompter<IntegrationLimits> {
    @Override
    public Optional<IntegrationLimits> prompt() {
        while (true) {
            Optional<List<Double>> numbersOptional = new DoubleArrayPrompter("Пределы интегрирования: ").prompt();
            if (!numbersOptional.isPresent())
                return Optional.empty();
            List<Double> numbers = numbersOptional.get();
            if (numbers.size() == 2)
                return Optional.of(new IntegrationLimits(numbers.get(0), numbers.get(1)));

            System.out.println("Введите 2 числа через пробел - нижний и верхний пределы (пустая строка - отменить)");
        }
    }
}
