package ru.david.compmath3.gui.input;

import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class HintTextField extends JTextField {
    private Font hintFont = getFont();
    private String hint;

    public HintTextField(final String hint) {
        this.hint = hint;
    }

    @Override
    public void setFont(Font f) {
        super.setFont(f);
        hintFont = getFont();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (getText().isEmpty()) {
            g.setFont(hintFont);
            g.setColor(Color.LIGHT_GRAY);
            ((Graphics2D)g).setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );
            g.drawString(hint, 4, (getHeight() + hintFont.getSize())/2 - 2);
        }
    }
}
