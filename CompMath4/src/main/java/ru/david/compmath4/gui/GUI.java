package ru.david.compmath4.gui;

import ru.david.compmath4.gui.graph.ConfigurableLagrangeInterpolationViewerWrapper;
import ru.david.compmath4.gui.graph.GraphData;
import ru.david.compmath4.gui.input.common.CommonInputPanel;
import ru.david.compmath4.math.ParsingException;
import ru.david.compmath4.math.expression.*;
import ru.david.compmath4.math.model.XY;
import ru.david.compmath4.math.util.AdamsMethodSolver;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("FieldCanBeLocal")
public class GUI {
    private JFrame frame;
    private BorderLayout layout;

    private CommonInputPanel commonInputPanel;
    private ConfigurableLagrangeInterpolationViewerWrapper interpolationViewer;

    public GUI() {
        frame = new JFrame("Вычислительная математика 4 :: Давтян Д. А. :: P3201 :: (C) david-d25@yandex.ru");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 800);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        Container pane = frame.getContentPane();

        layout = new BorderLayout();
        frame.setLayout(layout);

        commonInputPanel = new CommonInputPanel(this::onUserInput);

        pane.add(commonInputPanel, BorderLayout.NORTH);

        JPanel interpolationViewerPanel = new JPanel(new BorderLayout());
        interpolationViewer = new ConfigurableLagrangeInterpolationViewerWrapper();
        pane.add(interpolationViewerPanel, BorderLayout.CENTER);
        interpolationViewerPanel.add(interpolationViewer, BorderLayout.CENTER);
        interpolationViewerPanel.setBorder(new EmptyBorder(0, 10, 10, 10));

        frame.setVisible(true);
    }

    public void setExamples(String[] examples) {
        commonInputPanel.setExamples(examples);
    }

    private void onUserInput(String rawExpression, double startX, double startY, double stopX, double accuracy) {
        // todo validate
        if (Double.isNaN(startX + startY + stopX + accuracy))
            return;

        if (accuracy < 0.01)
            accuracy = 0.01;

        VariableExpression xRef = new VariableExpression();
        VariableExpression yRef = new VariableExpression();
        Map<String, Expression> variables = new HashMap<>();
        variables.put("x", xRef);
        variables.put("y", yRef);

        Expression expression;
        try {
            expression = ExpressionParser.parseExpression(rawExpression, variables);
        } catch (ParsingException e) {
            return;
        }

        double step = Math.pow(accuracy, 1.0/4);
        int stepsNum = (int)(Math.abs(startX - stopX)/step);

        List<XY> equationSolution = AdamsMethodSolver.solve(startX, startY, step, stepsNum, accuracy, new MathFunctionAdapter(expression, xRef, yRef));
        LagrangeInterpolationExpression interpolated = new LagrangeInterpolationExpression(equationSolution, xRef);

        GraphData viewerData = new GraphData(interpolated, xRef, Color.magenta.darker(), 3, "Интерполяция решения");
        interpolationViewer.getViewer().setInterpolatedGraph(viewerData, equationSolution, true);
    }
}
