package ru.david.compmath4.gui.input.accuracy;

import ru.david.compmath4.gui.input.LabeledNumberInput;
import ru.david.compmath4.gui.input.listener.NumberInputListener;

import javax.swing.*;
import java.awt.*;

public class AccuracyInput extends JComponent {
    private JLabel label = new JLabel("Точность");
    private LabeledNumberInput input;

    private NumberInputListener listener;

    private static final Font FONT = new Font("Arial", Font.PLAIN, 16);
    private static final Color LABEL_COLOR = Color.gray;

    public AccuracyInput(NumberInputListener listener) {
        setLayout(new GridBagLayout());
        input = new LabeledNumberInput("", listener);

        label.setFont(FONT);
        label.setForeground(LABEL_COLOR);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        input.setFont(FONT);

        input.setText("0.1");

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 0;
        labelConstraints.weightx = 1;
        labelConstraints.fill = GridBagConstraints.HORIZONTAL;
        add(label, labelConstraints);

        GridBagConstraints inputConstraints = new GridBagConstraints();
        inputConstraints.gridx = 0;
        inputConstraints.gridy = 1;
        inputConstraints.weightx = 1;
        inputConstraints.fill = GridBagConstraints.HORIZONTAL;
        add(input, inputConstraints);
    }

    public double getAccuracy() {
        return input.getNumericValue();
    }
}
