package ru.david.compmath4.gui.input.start_point;

import ru.david.compmath4.gui.input.LabeledNumberInput;
import ru.david.compmath4.gui.input.listener.NumberInputListener;

import javax.swing.*;
import java.awt.*;

public class StartPointInput extends JComponent {
    private JLabel label = new JLabel("Начальная точка");
    private LabeledNumberInput xInput;
    private LabeledNumberInput yInput;

    private static final Font FONT = new Font("Arial", Font.PLAIN, 16);
    private static final Color LABEL_COLOR = Color.gray;

    public StartPointInput(NumberInputListener xInputListener, NumberInputListener yInputListener) {
        setLayout(new GridBagLayout());

        xInput = new LabeledNumberInput("x = ", xInputListener);
        yInput = new LabeledNumberInput("y = ", yInputListener);

        xInput.setText("0");
        yInput.setText("0");

        label.setFont(FONT);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(LABEL_COLOR);

        xInput.setFont(FONT);
        yInput.setFont(FONT);

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 0;
        labelConstraints.gridwidth = 2;
        labelConstraints.weightx = 1;
        labelConstraints.fill = GridBagConstraints.HORIZONTAL;
        add(label, labelConstraints);

        GridBagConstraints xInputConstraints = new GridBagConstraints();
        xInputConstraints.gridx = 0;
        xInputConstraints.gridy = 1;
        xInputConstraints.weightx = 1;
        xInputConstraints.fill = GridBagConstraints.HORIZONTAL;
        add(xInput, xInputConstraints);

        GridBagConstraints yInputConstraints = new GridBagConstraints();
        yInputConstraints.gridx = 1;
        yInputConstraints.gridy = 1;
        yInputConstraints.weightx = 1;
        yInputConstraints.fill = GridBagConstraints.HORIZONTAL;
        add(yInput, yInputConstraints);
    }

    public double getInputX() {
        return xInput.getNumericValue();
    }

    public double getInputY() {
        return yInput.getNumericValue();
    }
}
