package ru.david.compmath4;

import ru.david.compmath4.gui.GUI;
import ru.david.compmath4.gui.graph.GraphData;
import ru.david.compmath4.gui.graph.GraphDataUtils;

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
                gui.setExamples(builder.toString().split("\n"));
        } catch (IOException | NullPointerException e) {
            System.err.println("Could not load examples file: " + e.getMessage());
        }
    }
}
