package ru.david.compmath3.gui;

import ru.david.compmath3.gui.graph.ConfigurableLagrangeInterpolationViewerWrapper;
import ru.david.compmath3.gui.input.ExpressionInput;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

@SuppressWarnings("FieldCanBeLocal")
public class GUI {
    private JFrame frame;
    private BorderLayout layout;

    private ExpressionInput expressionInput;
    private ConfigurableLagrangeInterpolationViewerWrapper interpolationViewer;

    public GUI() {
        frame = new JFrame("Вычислительная математика 3 :: Давтян Д. А. :: P3201 :: (C) david-d25@yandex.ru");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 800);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        Container pane = frame.getContentPane();

        layout = new BorderLayout();
        frame.setLayout(layout);

        expressionInput = new ExpressionInput();

        pane.add(expressionInput, BorderLayout.NORTH);

        JPanel interpolationViewerPanel = new JPanel(new BorderLayout());
        interpolationViewer = new ConfigurableLagrangeInterpolationViewerWrapper();
        pane.add(interpolationViewerPanel, BorderLayout.CENTER);
        interpolationViewerPanel.add(interpolationViewer, BorderLayout.CENTER);
        interpolationViewerPanel.setBorder(new EmptyBorder(0, 10, 10, 10));

        JLabel altDrawHintLabel = new JLabel("Удерживайте Alt для режима привязки узла к графику. Удерживайте Ctrl для режима создания узлов", SwingConstants.CENTER);
        altDrawHintLabel.setForeground(Color.GRAY);
        altDrawHintLabel.setBorder(new EmptyBorder(0, 10, 10, 10));
        pane.add(altDrawHintLabel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public ExpressionInput getExpressionInput() {
        return expressionInput;
    }

    public ConfigurableLagrangeInterpolationViewerWrapper getInterpolationViewerWrapper() {
        return interpolationViewer;
    }

    public JFrame getFrame() {
        return frame;
    }
}
