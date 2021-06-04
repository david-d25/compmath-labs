package ru.david.compmath4.gui.input.end_point;

import ru.david.compmath4.gui.input.LabeledNumberInput;
import ru.david.compmath4.gui.input.listener.NumberInputListener;

import javax.swing.*;
import java.awt.*;

public class EndPointInput extends JComponent {
    private JLabel label = new JLabel("Конечная точка");
    private LabeledNumberInput input;

    private static final Font FONT = new Font("Arial", Font.PLAIN, 16);
    private static final Color LABEL_COLOR = Color.gray;

    public EndPointInput(NumberInputListener listener) {
        setLayout(new GridBagLayout());
        input = new LabeledNumberInput("x = ", listener);

        label.setFont(FONT);
        label.setForeground(LABEL_COLOR);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        input.setFont(FONT);

        input.setText("10");

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

    public double getEndXValue() {
        return input.getNumericValue();
    }
}
