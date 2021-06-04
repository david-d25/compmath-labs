package ru.david.compmath.ui;

import ru.david.compmath.EasterEgg;
import ru.david.compmath.matrix.*;

import java.util.Optional;

public class MatrixProviderPrompter implements Prompter<Provider<Matrix>> {
    @Override
    public Optional<Provider<Matrix>> prompt() {
        while (true) {
            System.out.println(
                    "\nИсточник коэффициентов:\n" +
                            "1) Прочитать из файла\n" +
                            "2) Ввести с клавиатуры\n" +
                            "3) Использовать случайные\n" +
                            "4) Показать кота."
            );

            Provider<Matrix> matrixProvider = null;

            switch ((int)Math.round(new DoublePrompter().prompt().orElse(-1d))) {
                case 1:
                    matrixProvider = new FileMatrixProvider();
                    break;
                case 2:
                    matrixProvider = new StdInMatrixProvider();
                    break;
                case 3:
                    matrixProvider = new RandomMatrixProvider();
                    break;
                case 4:
                    System.out.println(EasterEgg.CAT);
                    continue;
            }

            if (matrixProvider == null)
                System.out.println("Выберите один из вариантов");
            else
                return Optional.of(matrixProvider);
        }
    }
}
