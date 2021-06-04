package ru.david.compmath3.gui.input;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("FieldCanBeLocal")
public class ExpressionInput extends JComponent {
    private HintTextField input;
    private JLabel inputPrefix;
    private JLabel examplePrefix;
    private JLabel example;
    private JButton resetButton;

    private JPanel inputPanel;
    private JPanel examplePanel;

    private LayoutManager globalLayout;
    private LayoutManager inputPanelLayout;
    private LayoutManager examplePanelLayout;

    private Font font = new Font("Arial", Font.PLAIN, 18);
    private Font examplePrefixFont = new Font("Arial", Font.PLAIN, 14);
    private Font exampleFont = new Font("Arial", Font.ITALIC, 14);

    private String[] examples;

    public ExpressionInput() {
        globalLayout = new BorderLayout();
        inputPanelLayout = new GridBagLayout();
        examplePanelLayout = new FlowLayout(FlowLayout.LEFT);

        setLayout(globalLayout);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        inputPanel = new JPanel(inputPanelLayout);
        add(inputPanel, BorderLayout.NORTH);

        inputPrefix = new JLabel("y = ");
        inputPrefix.setFont(font);
        inputPanel.add(inputPrefix);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        input = new HintTextField("Введите выражение");
        input.setFont(font);
        inputPanel.add(input, constraints);

        resetButton = new JButton("Распределить узлы");
        GridBagConstraints resetBtnConstraints = new GridBagConstraints();
        resetBtnConstraints.fill = GridBagConstraints.VERTICAL;
        inputPanel.add(resetButton, resetBtnConstraints);

        examplePanel = new JPanel(examplePanelLayout);
        examplePanel.setVisible(false);
        add(examplePanel, BorderLayout.SOUTH);

        examplePrefix = new JLabel("Например,");
        examplePanel.setBorder(new EmptyBorder(0, 25, 0, 0));
        examplePrefix.setFont(examplePrefixFont);
        examplePanel.add(examplePrefix);

        Map<TextAttribute, Object> attributes = new HashMap<>(exampleFont.getAttributes());
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_DOTTED);
        example = new JLabel();
        example.setCursor(new Cursor(Cursor.HAND_CURSOR));
        example.setFont(exampleFont.deriveFont(attributes));
        example.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                example.setForeground(Color.gray);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                example.setForeground(Color.black);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                input.setText(example.getText());
                for (   int i = 0;
                        i < Math.min(1000, examples.length*10) && example.getText().equals(input.getText());
                        i++
                ) example.setText(examples[(int) (Math.random()*examples.length)]);
            }
        });
        examplePanel.add(example);
    }

    public void setExamples(String[] examples) {
        this.examples = examples;
        example.setText(examples[(int) (Math.random()*examples.length)]);
        examplePanel.setVisible(true);
    }

    public HintTextField getTextField() {
        return input;
    }

    public JButton getResetButton() {
        return resetButton;
    }
}
