package ru.david.compmath3.gui.modal;

import ru.david.compmath3.gui.modal.listener.NodeChangedListener;
import ru.david.compmath3.math.model.XY;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class NodeCoordinatesModal extends JDialog {
    private static final Color BG_COLOR = new Color(0, 0, 0, 175);
    private static final Color LABELS_COLOR = new Color(255, 255, 255);

    private static final Font LABELS_FONT = new Font("Arial", Font.PLAIN, 18);

    private static final int WIDTH = 120;
    private static final int HEIGHT = 70;

    private static final int Y_PADDING = 10;

    private static final FocusListener autoSelectingListener = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            JTextField target = (JTextField)e.getComponent();
            target.select(0, target.getText().length());
        }
    };

    private final KeyListener applyingListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                trySubmit();
            }
        }
    };

    private final KeyListener cancelingListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                dispose();
            }
        }
    };

    private JTextField xInput, yInput;
    private NodeChangedListener changingCallback;
    private SwingWorker<Void, Void> shakingAnimationWorker;

    public NodeCoordinatesModal(JFrame parentFrame, XY node, Point location, NodeChangedListener changingCallback) {
        super(parentFrame);

        this.changingCallback = changingCallback;

        setSize(WIDTH, HEIGHT);
        setLocation(location.x - getWidth()/2, location.y + Y_PADDING);
        setUndecorated(true);
        getRootPane().setBackground(BG_COLOR);
        getRootPane().setOpaque(false);
        setBackground(BG_COLOR);

        setLayout(new BorderLayout());
        JPanel xPanel = new JPanel(new GridBagLayout());
        JPanel yPanel = new JPanel(new GridBagLayout());
        xPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        yPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(xPanel, BorderLayout.NORTH);
        add(yPanel, BorderLayout.SOUTH);

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.fill = GridBagConstraints.VERTICAL;
        JLabel xLabel = new JLabel("x ");
        JLabel yLabel = new JLabel("y ");
        xLabel.setForeground(LABELS_COLOR);
        yLabel.setForeground(LABELS_COLOR);
        xLabel.setFont(LABELS_FONT);
        yLabel.setFont(LABELS_FONT);
        xPanel.add(xLabel, labelConstraints);
        yPanel.add(yLabel, labelConstraints);

        xPanel.setBackground(new Color(0, 0, 0, 0));
        yPanel.setBackground(new Color(0, 0, 0, 0));

        GridBagConstraints inputConstraints = new GridBagConstraints();
        inputConstraints.fill = GridBagConstraints.HORIZONTAL;
        inputConstraints.weightx = 1;
        xInput = new JTextField(String.valueOf((float) node.x));
        yInput = new JTextField(String.valueOf((float) node.y));
        xInput.addFocusListener(autoSelectingListener);
        yInput.addFocusListener(autoSelectingListener);
        xInput.addKeyListener(applyingListener);
        yInput.addKeyListener(applyingListener);
        xInput.addKeyListener(cancelingListener);
        yInput.addKeyListener(cancelingListener);
        xPanel.add(xInput, inputConstraints);
        yPanel.add(yInput, inputConstraints);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                dispose();
            }
        });
        addKeyListener(cancelingListener);
        setVisible(true);
    }

    private void trySubmit() {
        double newX, newY;
        try {
            newX = Double.parseDouble(xInput.getText());
            newY = Double.parseDouble(yInput.getText());
        } catch (NumberFormatException e) {
            shakeWindow();
            return;
        }

        changingCallback.closed(new XY(newX, newY));
        dispose();
    }

    private void shakeWindow() {
        if (shakingAnimationWorker != null && !shakingAnimationWorker.isDone())
            return;

        shakingAnimationWorker = new SwingWorker<Void, Void>() {
            private static final float speed = 0.00000005f;

            private float position = -15.7f;
            private int initialX = getLocation().x;
            private long lastNanos = System.nanoTime();

            @Override
            protected Void doInBackground() throws InterruptedException {
                while (position < 0) {
                    setLocation((int)(initialX + shakingValue(position)), getY());
                    long delta = System.nanoTime() - lastNanos;
                    position += delta*speed;
                    lastNanos = System.nanoTime();
                    Thread.sleep(1000/60);
                }

                setLocation(initialX, getY());
                return null;
            }

            private float shakingValue(float x) {
                return (float)(Math.sin(x)*Math.pow(x, 2)/20);
            }
        };

        shakingAnimationWorker.execute();
    }
}
