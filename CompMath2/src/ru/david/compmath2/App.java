package ru.david.compmath2;

import ru.david.compmath2.input.AccuracyPrompter;
import ru.david.compmath2.input.IntegrationLimitsPrompter;
import ru.david.compmath2.input.StringPrompter;
import ru.david.compmath2.math.ParsingException;
import ru.david.compmath2.math.expression.Expression;
import ru.david.compmath2.math.expression.ExpressionParser;
import ru.david.compmath2.math.expression.VariableExpression;
import ru.david.compmath2.math.integration.IntegrationLimits;
import ru.david.compmath2.math.integration.IntegrationResult;
import ru.david.compmath2.math.integration.Integrator;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.lang.System.out;

public class App {
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    private void start() {
        fixEncoding();
        printWelcomeText();

        // Trick to avoid infinite loop warning
        while (Math.random() < 1) {
            session();
        }
    }

    private void session() {
        out.println("Введите любое выражение:");
        Optional<String> rawExprOptional = new StringPrompter("y = ").prompt();
        if (!rawExprOptional.isPresent()) {
            out.println("Не получилось прочитать выражение");
            return;
        }

        Map<String, Expression> variables = new HashMap<>();
        VariableExpression x = new VariableExpression();
        variables.put("x", x);

        String rawExpression = rawExprOptional.get();

        Expression expression;
        try {
            expression = ExpressionParser.parseExpression(rawExpression, variables);
        } catch (ParsingException e) {
            out.println(e.getMessage());
            return;
        }

        Optional<IntegrationLimits> limitsOptional = new IntegrationLimitsPrompter().prompt();
        if (!limitsOptional.isPresent()) {
            out.println("Не получилось прочитать пределы интегрирования");
            return;
        }
        IntegrationLimits limits = limitsOptional.get();

        Optional<Double> accuracyOptional = new AccuracyPrompter().prompt();
        if (!accuracyOptional.isPresent())
            return;

        double accuracy = accuracyOptional.get();

        IntegrationResult result = Integrator.solve(expression, x, limits, accuracy);

        if (result.getStatus() == IntegrationResult.Status.OK) {
            out.printf("Результат: %.16f\n", result.getResult());
            out.printf("Погрешность: %.16f\n", result.getError());
            out.printf("Сделано разбиений: %s\n", result.getSplits());
        } else if (result.getStatus() == IntegrationResult.Status.GAP) {
            out.println("Не получилось посчитать интеграл.");
            out.println("Возможно, он не сходится или указана слишком высокая точность.");
        }

        out.println();
    }

    private void fixEncoding() {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException ignored) {}
    }

    private void printWelcomeText() {
        out.println("Программа, которая вычисляет интеграл");
        out.println("(С) Давид <david-d25@yandex.ru>");
        out.println();
        out.println("Операции: +, -, /, *, ^");
        out.println("Функции: sin, cos, abs, sqrt, log");
        out.println("Используйте скобки, чтобы менять порядок операций");
        out.println();
    }
}
