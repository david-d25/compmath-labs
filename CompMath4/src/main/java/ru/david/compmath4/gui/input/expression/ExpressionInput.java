package ru.david.compmath4.gui.input.expression;

import ru.david.compmath4.gui.input.HintTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private JPanel inputPanel;
    private JPanel examplePanel;

    private LayoutManager globalLayout;
    private LayoutManager inputPanelLayout;
    private LayoutManager examplePanelLayout;

    private Font font = new Font("Arial", Font.PLAIN, 18);
    private Font examplePrefixFont = new Font("Arial", Font.PLAIN, 14);
    private Font exampleFont = new Font("Arial", Font.ITALIC, 14);

    private String[] examples;

    public ExpressionInput(ExpressionInputListener listener) {
        globalLayout = new BorderLayout();
        inputPanelLayout = new GridBagLayout();
        examplePanelLayout = new FlowLayout(FlowLayout.LEFT);

        setLayout(globalLayout);
        setBorder(new EmptyBorder(0, 0, 10, 0));

        inputPanel = new JPanel(inputPanelLayout);
        add(inputPanel, BorderLayout.NORTH);

        inputPrefix = new JLabel("y' = ");
        inputPrefix.setFont(font);
        inputPanel.add(inputPrefix);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        input = new HintTextField("Введите выражение");
        input.setFont(font);
        inputPanel.add(input, constraints);

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


        input.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { update(); }
            @Override public void removeUpdate(DocumentEvent e) { update(); }
            @Override public void changedUpdate(DocumentEvent e) { update(); }
            private void update() {
                listener.onRawExpressionInput(input.getText());
            }
        });
    }

    public void setExamples(String[] examples) {
        this.examples = examples;
        example.setText(examples[(int) (Math.random()*examples.length)]);
        examplePanel.setVisible(true);
    }

    public String getText() {
        return input.getText();
    }
}
