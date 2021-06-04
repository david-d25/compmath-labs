package ru.david.compmath4.gui.input;

import ru.david.compmath4.gui.input.listener.NumberInputListener;
import ru.david.compmath4.math.ParsingException;
import ru.david.compmath4.math.expression.ExpressionParser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class LabeledNumberInput extends JTextField {
    private static final int TEXT_HEIGHT_CORRECTION = -4; // Workaround
    private static final Color LABEL_COLOR = Color.gray;

    private int padding = 5;
    private String label;

    public LabeledNumberInput(String label, NumberInputListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("listener is null!");

        setLabel(label);
        getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { update(); }
            @Override public void removeUpdate(DocumentEvent e) { update(); }
            @Override public void changedUpdate(DocumentEvent e) { update(); }
            private void update() {
                listener.onInput(getNumericValue());
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paint(g);
        if (label != null) {
            g.setFont(getFont());
            g.setColor(LABEL_COLOR);
            g.drawString(label, padding, getHeight() / 2 + getFontMetrics(getFont()).getHeight() / 2 + TEXT_HEIGHT_CORRECTION);
        }
    }

    public double getNumericValue() {
        if (getText().isEmpty())
            return Double.NaN;

        try {
            return ExpressionParser.parseExpression(getText(), null).value();
        } catch (ParsingException e) {
            return Double.NaN;
        }
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
        repaint();
        updateBorder();
    }

    private void updateBorder() {
        if (label != null) {
            FontMetrics fontMetrics = getFontMetrics(getFont());
            setBorder(
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.lightGray),
                            new EmptyBorder(padding, fontMetrics.stringWidth(label) + padding * 2, padding, padding)
                    )
            );
        } else {
            setBorder(new EmptyBorder(padding, padding, padding, padding));
        }
    }
}
