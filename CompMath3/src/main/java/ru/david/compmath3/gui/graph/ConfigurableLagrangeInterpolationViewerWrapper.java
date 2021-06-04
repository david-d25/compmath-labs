package ru.david.compmath3.gui.graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ConfigurableLagrangeInterpolationViewerWrapper extends JComponent {
    private LagrangeInterpolationViewer viewer;
    private JCheckBox drawWithinNodesCheckbox;
    private JCheckBox hiResRenderCheckbox;

    {
        setLayout(new BorderLayout());

        viewer = new LagrangeInterpolationViewer();
        add(viewer, BorderLayout.CENTER);


        JPanel checkboxesPanel = new JPanel(new BorderLayout());
        add(checkboxesPanel, BorderLayout.NORTH);

        drawWithinNodesCheckbox = new JCheckBox("Не показывать интерполянт за пределами узлов");
        checkboxesPanel.add(drawWithinNodesCheckbox, BorderLayout.WEST);
        hiResRenderCheckbox = new JCheckBox("Пытаться рисовать точнее");
        checkboxesPanel.add(hiResRenderCheckbox, BorderLayout.EAST);

        drawWithinNodesCheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                viewer.setInterpolateWithinNodes(drawWithinNodesCheckbox.isSelected());
            }
        });

        hiResRenderCheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                viewer.setHiResRender(hiResRenderCheckbox.isSelected());
            }
        });
    }

    public LagrangeInterpolationViewer getViewer() {
        return viewer;
    }
}
