package ru.david.compmath3.gui.graph;

import ru.david.compmath3.math.ParsingException;
import ru.david.compmath3.math.expression.Expression;
import ru.david.compmath3.math.expression.ExpressionParser;
import ru.david.compmath3.math.expression.VariableExpression;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GraphDataUtils {
    public static GraphData tryCreateGraphData(String rawExpression, Color color, String name, int strokeWidth) {
        Map<String, Expression> variables = new HashMap<>();
        VariableExpression x = new VariableExpression();
        variables.put("x", x);
        Expression expression;
        try {
            expression = ExpressionParser.parseExpression(rawExpression, variables);
        } catch (ParsingException e) {
            return null;
        }
        return new GraphData(expression, x, color, strokeWidth, name);
    }
}
