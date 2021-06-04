package ru.david.compmath4.gui.input.common;

import ru.david.compmath4.gui.input.accuracy.AccuracyInput;
import ru.david.compmath4.gui.input.end_point.EndPointInput;
import ru.david.compmath4.gui.input.expression.ExpressionInput;
import ru.david.compmath4.gui.input.start_point.StartPointInput;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CommonInputPanel extends JComponent {
    private ExpressionInput expressionInput;
    private StartPointInput startPointInput;
    private EndPointInput endPointInput;
    private AccuracyInput accuracyInput;

    public CommonInputPanel(CommonInputListener listener) {
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        expressionInput = new ExpressionInput(e -> fireInputEvent(listener));
        startPointInput = new StartPointInput(e -> fireInputEvent(listener), e -> fireInputEvent(listener));
        endPointInput = new EndPointInput(e -> fireInputEvent(listener));
        accuracyInput = new AccuracyInput(e -> fireInputEvent(listener));

        GridBagConstraints exprInputConstraints = new GridBagConstraints();
        exprInputConstraints.gridx = 0;
        exprInputConstraints.gridy = 0;
        exprInputConstraints.weightx = 1;
        exprInputConstraints.gridwidth = 4;
        exprInputConstraints.fill = GridBagConstraints.HORIZONTAL;
        add(expressionInput, exprInputConstraints);

        GridBagConstraints startPointInputConstraints = new GridBagConstraints();
        startPointInputConstraints.gridx = 0;
        startPointInputConstraints.gridy = 1;
        startPointInputConstraints.weightx = 1;
        startPointInputConstraints.gridwidth = 2;
        startPointInputConstraints.fill = GridBagConstraints.HORIZONTAL;
        add(startPointInput, startPointInputConstraints);

        GridBagConstraints endPointInputConstraints = new GridBagConstraints();
        endPointInputConstraints.gridx = 2;
        endPointInputConstraints.gridy = 1;
        endPointInputConstraints.weightx = 1;
        endPointInputConstraints.ipady = 10;
        endPointInputConstraints.fill = GridBagConstraints.HORIZONTAL;
        add(endPointInput, endPointInputConstraints);

        GridBagConstraints accuracyInputConstraints = new GridBagConstraints();
        accuracyInputConstraints.gridx = 3;
        accuracyInputConstraints.gridy = 1;
        accuracyInputConstraints.weightx = 1;
        accuracyInputConstraints.ipady = 10;
        accuracyInputConstraints.fill = GridBagConstraints.HORIZONTAL;
        add(accuracyInput, accuracyInputConstraints);
    }

    public void setExamples(String[] examples) {
        expressionInput.setExamples(examples);
    }

    private void fireInputEvent(CommonInputListener listener) {
        // workaround
        if (expressionInput == null || startPointInput == null || endPointInput == null || accuracyInput == null)
            return;

        listener.onInput(
                expressionInput.getText(),
                startPointInput.getInputX(),
                startPointInput.getInputY(),
                endPointInput.getEndXValue(),
                accuracyInput.getAccuracy()
        );
    }
}
