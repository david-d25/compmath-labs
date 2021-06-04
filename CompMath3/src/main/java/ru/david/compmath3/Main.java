package ru.david.compmath3;

import ru.david.compmath3.gui.GUI;
import ru.david.compmath3.gui.graph.GraphData;
import ru.david.compmath3.gui.graph.GraphDataUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private static final String EXAMPLES_FILENAME = "/expression_examples.lst";
    private static GUI gui;

    public static void main(String[] args) {
        gui = new GUI();
        tryLoadExamples();
        addExpressionInputListener();
        addResetButtonListener();
    }

    private static void addExpressionInputListener() {
        gui.getExpressionInput().getTextField().getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { update(); }
            @Override public void removeUpdate(DocumentEvent e) { update(); }
            @Override public void changedUpdate(DocumentEvent e) { update(); }
            private void update() {
                GraphData data = GraphDataUtils.tryCreateGraphData(
                        gui.getExpressionInput().getTextField().getText(),
                        Color.RED,
                        "Исходный",
                        3
                );
                gui.getInterpolationViewerWrapper().getViewer().clearData();
                if (data != null)
                    gui.getInterpolationViewerWrapper().getViewer().setGraphToInterpolate(data, Color.GREEN, 3, true);
            }
        });
    }

    private static void addResetButtonListener() {
        gui.getExpressionInput().getResetButton().addActionListener(
                e -> {
                    if (gui.getInterpolationViewerWrapper().getViewer().getInterpolationNodes().size() == 0)
                        gui.getInterpolationViewerWrapper().getViewer().createInterpolationNodes(8);

                    int nodes = gui.getInterpolationViewerWrapper().getViewer().autoNodesAllocation();
                    if (nodes == 0)
                        JOptionPane.showMessageDialog(
                                gui.getFrame(),
                                "В этом месте на график нельзя добавить узлы интерполяции.\n" +
                                        "Переместите вид в другое место и попробуйте снова",
                                "Узлы негде установить",
                                JOptionPane.ERROR_MESSAGE);
                }
        );
    }

    private static void tryLoadExamples() {
        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(Main.class.getResourceAsStream(EXAMPLES_FILENAME))
                )
        ) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                builder.append(line).append('\n');

            if (builder.length() != 0)
                gui.getExpressionInput().setExamples(builder.toString().split("\n"));
        } catch (IOException | NullPointerException e) {
            System.err.println("Could not load examples file: " + e.getMessage());
        }
    }
}
