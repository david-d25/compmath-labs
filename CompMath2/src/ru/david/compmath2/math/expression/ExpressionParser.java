package ru.david.compmath2.math.expression;

import ru.david.compmath2.math.ParsingException;

import java.util.Map;
import java.util.function.Function;

import static ru.david.compmath2.math.expression.ArithmeticExpression.Operation.*;

public class ExpressionParser {
    public static Expression parseExpression(String rawExpression, Map<String, Expression> variables) {
        String preparedRawExpr = rawExpression.replaceAll("\\s+", "");
        checkParenthesis(preparedRawExpr);
        return parse(preparedRawExpr, variables);
    }

    private static Expression parse(String rawExpression, Map<String, Expression> variables) {
        return parseAddSubtract(rawExpression, variables);
    }

    private static Expression parseAddSubtract(String rawExpression, Map<String, Expression> variables) {
        int lastAddSymbol = lastSymbolIgnoringParenthesis(rawExpression, '+');
        int lastSubtractSymbol = lastSymbolIgnoringParenthesis(rawExpression, '-');

        char targetChar;

        if (lastAddSymbol == -1 && lastSubtractSymbol == -1)
            return parseMultiplyDivide(rawExpression, variables);
        else if (lastAddSymbol != -1 && lastSubtractSymbol != -1) {
            if (lastAddSymbol < lastSubtractSymbol)
                targetChar = '-';
            else
                targetChar = '+';
        } else if (lastAddSymbol != -1)
            targetChar = '+';
        else
            targetChar = '-';

        int dividingIndex;
        if (targetChar == '+')
            dividingIndex = lastAddSymbol;
        else
            dividingIndex = lastSubtractSymbol;

        String leftRaw = rawExpression.substring(0, dividingIndex);
        String rightRaw = rawExpression.substring(dividingIndex+1);

        Expression left = parse(leftRaw, variables);
        Expression right = parse(rightRaw, variables);

        return new ArithmeticExpression(left, right, targetChar == '+' ? ADD : SUBTRACT);
    }

    private static Expression parseMultiplyDivide(String rawExpression, Map<String, Expression> variables) {
        int lastMultiplySymbol = lastSymbolIgnoringParenthesis(rawExpression, '*');
        int lastDivideSymbol = lastSymbolIgnoringParenthesis(rawExpression, '/');

        char targetChar;

        if (lastMultiplySymbol == -1 && lastDivideSymbol == -1)
            return parsePower(rawExpression, variables);
        else if (lastMultiplySymbol != -1 && lastDivideSymbol != -1) {
            if (lastMultiplySymbol < lastDivideSymbol)
                targetChar = '/';
            else
                targetChar = '*';
        } else if (lastMultiplySymbol != -1)
            targetChar = '*';
        else
            targetChar = '/';

        int dividingIndex;
        if (targetChar == '*')
            dividingIndex = lastMultiplySymbol;
        else
            dividingIndex = lastDivideSymbol;

        String leftRaw = rawExpression.substring(0, dividingIndex);
        String rightRaw = rawExpression.substring(dividingIndex+1);

        Expression left = parse(leftRaw, variables);
        Expression right = parse(rightRaw, variables);

        return new ArithmeticExpression(left, right, targetChar == '*' ? MULTIPLY : DIVIDE);
    }

    private static Expression parsePower(String rawExpression, Map<String, Expression> variables) {
        int lastPowerSymbol = lastSymbolIgnoringParenthesis(rawExpression, '^');
        if (lastPowerSymbol == -1)
            return parseFunction(rawExpression, variables);

        String leftRaw = rawExpression.substring(0, lastPowerSymbol);
        String rightRaw = rawExpression.substring(lastPowerSymbol+1);

        Expression left = parse(leftRaw, variables);
        Expression right = parse(rightRaw, variables);

        return new ArithmeticExpression(left, right, POWER);
    }

    private static Expression parseFunction(String rawExpression, Map<String, Expression> variables) {
        Function<Double, Double> function = null;
        int functionNameLength = 3;

        if (rawExpression.startsWith("sin"))
            function = Math::sin;
        else if (rawExpression.startsWith("cos"))
            function = Math::cos;
        else if (rawExpression.startsWith("abs"))
            function = Math::abs;
        else if (rawExpression.startsWith("log"))
            function = Math::log;
        else if (rawExpression.startsWith("sqrt")) {
            function = Math::sqrt;
            functionNameLength = 4;
        }

        if (function != null) {
            String subRawExpression = rawExpression.substring(functionNameLength);
            Expression subExpression = parse(subRawExpression, variables);
            return new FunctionExpression(subExpression, function);
        }

        return parseParenthesis(rawExpression, variables);
    }

    private static Expression parseParenthesis(String rawExpression, Map<String, Expression> variables) {
        if (rawExpression.startsWith("(") && rawExpression.endsWith(")"))
            return parse(rawExpression.substring(1, rawExpression.length() - 1), variables);
        return parseVariable(rawExpression, variables);
    }

    private static Expression parseVariable(String rawExpression, Map<String, Expression> variables) {
        if (variables.containsKey(rawExpression))
            return variables.get(rawExpression);
        return parseNumber(rawExpression);
    }

    private static Expression parseNumber(String rawExpression) {
        if (rawExpression.isEmpty())
            return new NumberExpression(0);

        if (rawExpression.toLowerCase().equals("pi"))
            return new NumberExpression(Math.PI);
        if (rawExpression.toLowerCase().equals("e"))
            return new NumberExpression(Math.E);

        try {
            return new NumberExpression(Double.parseDouble(rawExpression));
        } catch (NumberFormatException e) {
            throw new ParsingException("Неизвестное выражение: " + rawExpression);
        }
    }

    private static void checkParenthesis(String rawExpression) {
        int depth = 0;
        for (int i = 0; i < rawExpression.length(); i++) {
            if (rawExpression.charAt(i) == '(')
                depth++;
            else if (rawExpression.charAt(i) == ')')
                depth--;
            if (depth < 0) {
                StringBuilder builder = new StringBuilder();
                builder
                        .append("Синтаксическая ошибка (символ ").append(i).append(")\n")
                        .append(rawExpression).append("\n");
                for (int a = 0; a < i; a++)
                    builder.append(' ');
                builder.append('^');
                throw new ParsingException(builder.toString());
            }
        }

        if (depth > 0)
            throw new ParsingException("Открывающих скобок больше, чем закрывающих. Проверье, закрыты ли все скобки.");
    }

    private static int lastSymbolIgnoringParenthesis(String string, char target) {
        int depth = 0;
        if (string.length() == 0)
            return -1;

        for (int i = string.length() - 1; i >= 0; i--) {
            char current = string.charAt(i);
            if (depth == 0 && current == target)
                return i;

            if (current == ')')
                depth++;
            else if (current == '(')
                depth--;
        }

        return -1;
    }
}
