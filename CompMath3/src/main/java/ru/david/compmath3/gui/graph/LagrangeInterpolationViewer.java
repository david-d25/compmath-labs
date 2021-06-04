package ru.david.compmath3.gui.graph;

import ru.david.compmath3.gui.modal.NodeCoordinatesModal;
import ru.david.compmath3.math.expression.LagrangeInterpolationExpression;
import ru.david.compmath3.math.model.XY;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class LagrangeInterpolationViewer extends InteractiveGraphViewer {
    private static final Color NODES_COLOR = new Color(106, 58, 132);
    private static final Color CREATING_NODE_COLOR = new Color(106, 58, 132, 127);
    private static final int NODES_SIZE = 12;

    private static final BufferedImage deleteImage;
    static {
        BufferedImage image;
        try {
            image = ImageIO.read(LagrangeInterpolationViewer.class.getResourceAsStream("/delete.png"));
        } catch (Exception e) {
            image = null;
            JOptionPane.showMessageDialog(
                    null, "Не удалось загрузить файл delete.png\nПричина: " + e.getMessage(),
                    "Ошиб очка",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
            System.exit(1);
        }
        deleteImage = image;
    }

    private GraphData actualGraph;
    private GraphData interpolationGraph;
    private List<XY> interpolationNodes = new ArrayList<>();

    private XY draggingNode = null;
    private int draggingBufferX = 0;
    private int draggingBufferY = 0;

    private JComponent nodeEditingComponent = null;

    private boolean nodeCreatingMode = false;

    private boolean showDeletingIcon = false;
    private int deletingIconBottomPadding = 80;
    private double deletingIconSize = 0;
    private Color deletingIconBg = new Color(255, 255, 255, 0);

    private static final double DELETING_ICON_DEFAULT_SIZE = 50;
    private static final double DELETING_ICON_ACTIVE_SIZE = 80;
    private static final Color DELETING_ICON_DEFAULT_BG = new Color(255, 255, 255, 255);
    private static final Color DELETING_ICON_ACTIVE_BG = new Color(255, 142, 139, 255);

    private SwingWorker<Void, Object> deletingAnimationWorker = null;

    private boolean isInterpolationLimited = false;

    public LagrangeInterpolationViewer() {
        createInterpolationNodes(8);
    }

    public void createInterpolationNodes(int count) {
        for (int i = 0; i < count; i++)
            interpolationNodes.add(new XY(0, 0));
    }

    @Override
    public void paint(Graphics g) {
        updateInterpolationLimits();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2d.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR
        );

        super.paint(g2d);
        if (interpolationGraph != null) {
            drawDeletingIcon(g2d);
            g.setColor(NODES_COLOR);
            Cursor cursor = new Cursor(Cursor.MOVE_CURSOR);
            for (XY node : interpolationNodes) {
                Point nodePoint = new Point(projectX(node.x), projectY(node.y));
                drawNode(node, g2d);
                Point mousePosition = getMousePosition();
                if (mousePosition != null && mousePosition.distance(nodePoint) < NODES_SIZE / 2.0) {
                    cursor = new Cursor(Cursor.W_RESIZE_CURSOR);
                }
            }
            setCursor(cursor);

            if (draggingNode != null) {
                g.setFont(g.getFont().deriveFont(Font.BOLD).deriveFont(16F));
                FontMetrics fontMetrics = g.getFontMetrics();
                g.setColor(new Color(0, 0, 0, 200));
                String text = String.format("x = %.3f", draggingNode.x);
                int projX = projectX(draggingNode.x);
                int projY = projectY(draggingNode.y);
                g.fillPolygon(
                        new int[] {
                                projX,
                                projX - 10,
                                projX - fontMetrics.stringWidth(text)/2,
                                projX - fontMetrics.stringWidth(text)/2,
                                projX + fontMetrics.stringWidth(text)/2 + 10,
                                projX + fontMetrics.stringWidth(text)/2 + 10,
                                projX + 10,
                        },
                        new int[] {
                                projY - 10,
                                projY - 20,
                                projY - 20,
                                projY - 20 - fontMetrics.getHeight() - 4,
                                projY - 20 - fontMetrics.getHeight() - 4,
                                projY - 20,
                                projY - 20,
                        },
                        7
                );
                g.setColor(Color.WHITE);
                g.drawString(text, projX - fontMetrics.stringWidth(text)/2 + 5, projY - 26);
            }
        }

        drawNodeCreatingMode(g2d);
        paintChildren(g2d);
    }

    private void updateInterpolationLimits() {
        if (interpolationGraph != null) {
            double min = Double.POSITIVE_INFINITY;
            double max = Double.NEGATIVE_INFINITY;
            for (XY node : interpolationNodes) {
                if (node.x < min)
                    min = node.x;
                if (node.x > max)
                    max = node.x;
            }
            interpolationGraph.setLowerLimit(min);
            interpolationGraph.setUpperLimit(max);
        }
    }

    private void drawNode(XY node, Graphics2D g) {
        g.fillOval(
                projectX(node.x) - NODES_SIZE / 2,
                projectY(node.y) - NODES_SIZE / 2,
                NODES_SIZE, NODES_SIZE
        );
    }

    private void drawNodeCreatingMode(Graphics2D g) {
        if (nodeCreatingMode && draggingNode == null && actualGraph != null) {
            Point mousePosition = getMousePosition();
            if (mousePosition != null) {
                double x = unprojectX(mousePosition.x);
                actualGraph.getX().setValue(x);
                double y = actualGraph.getExpression().value();
                g.setColor(CREATING_NODE_COLOR);
                drawNode(new XY(x, y), g);
            }
        }
    }

    private void drawDeletingIcon(Graphics2D g) {
        if (showDeletingIcon && deletingIconSize > 0) {
            float shadowRatio = 5;
            RadialGradientPaint shadow = new RadialGradientPaint(
                    getWidth()/2f, getHeight() - deletingIconBottomPadding,
                    (float)deletingIconSize*shadowRatio/2,
                    new float[]{0, 1},
                    new Color[]{ new Color(0, 0, 0, 100), new Color(255, 255, 255, 0)}
            );
            g.setPaint(shadow);
            g.fillOval(
                    (int)(getWidth()/2 - deletingIconSize/2*shadowRatio),
                    (int)(getHeight() - deletingIconBottomPadding - deletingIconSize/2*shadowRatio),
                    (int)(deletingIconSize*shadowRatio), (int)(deletingIconSize*shadowRatio)
            );

            g.setColor(deletingIconBg);
            double ovalRatio = 1.5;
            g.fillOval(
                    (int)(getWidth()/2 - deletingIconSize/2*ovalRatio),
                    (int)(getHeight() - deletingIconBottomPadding - deletingIconSize/2*ovalRatio),
                    (int)(deletingIconSize*ovalRatio), (int)(deletingIconSize*ovalRatio)
            );

            g.drawImage(
                    deleteImage,
                    (int)(getWidth()/2 - deletingIconSize/2),
                    (int)(getHeight() - deletingIconBottomPadding - deletingIconSize/2),
                    (int)deletingIconSize, (int)deletingIconSize,
                    null
            );
        }
    }

    private void initDeleteAnimationWorker() {
        if (deletingAnimationWorker != null && !deletingAnimationWorker.isDone())
            return;

        deletingAnimationWorker = new SwingWorker<Void, Object>() {
            private int smoothness = 5;
            @Override
            protected Void doInBackground() throws InterruptedException {
                showDeletingIcon = true;
                while (true) {
                    if (draggingNode != null) {
                        if (isCursorOverDeleteButton()) {
                            deletingIconSize += (DELETING_ICON_ACTIVE_SIZE - deletingIconSize)/smoothness;
                            bgColorIteration(DELETING_ICON_ACTIVE_BG);
                        } else {
                            deletingIconSize += (DELETING_ICON_DEFAULT_SIZE - deletingIconSize)/smoothness;
                            bgColorIteration(DELETING_ICON_DEFAULT_BG);
                        }
                    } else {
                        deletingIconSize = (int)(deletingIconSize/1.5 - 1);
                        if (deletingIconSize < 0) {
                            showDeletingIcon = false;
                            return null;
                        }
                    }
                    repaint();
                    Thread.sleep(16);
                }
            }

            private void bgColorIteration(Color target) {
                deletingIconBg = new Color(
                        deletingIconBg.getRed() + (target.getRed() - deletingIconBg.getRed())/smoothness,
                        deletingIconBg.getGreen() + (target.getGreen() - deletingIconBg.getGreen())/smoothness,
                        deletingIconBg.getBlue() + (target.getBlue() - deletingIconBg.getBlue())/smoothness,
                        deletingIconBg.getAlpha() + (target.getAlpha() - deletingIconBg.getAlpha())/smoothness
                );
            }
        };
        deletingAnimationWorker.execute();
    }

    public void setGraphToInterpolate(GraphData data, Color color, int strokeWidth, boolean useOwnInterpolationLimit) {
        clearData();
        addData(data);
        actualGraph = data;

        autoNodesAllocation();
        GraphData interpolationData = new GraphData(
                new LagrangeInterpolationExpression(interpolationNodes, data.getX()), data.getX(), color, strokeWidth, "Интерполирующий"
        );

        interpolationGraph = interpolationData;
        if (useOwnInterpolationLimit) {
            interpolationGraph.setLimitEnabled(isInterpolationLimited);
        } else {
            isInterpolationLimited = interpolationGraph.isLimitEnabled();
        }
        addData(interpolationData);
    }

    /**
     * @return the number of nodes
     */
    public int autoNodesAllocation() {
        if (actualGraph == null)
            return interpolationNodes.size();

        for (int i = 0, offsetCounter = 0; i < interpolationNodes.size(); i++, offsetCounter++) {
            XY node = interpolationNodes.get(i);
            double gap = getUpperX() - getLowerX();
            node.x = (getLowerX() + getUpperX())/2 + 1.0*offsetCounter/interpolationNodes.size()*gap*0.6 - gap*(0.5 - 0.2);
            actualGraph.getX().setValue(node.x);
            node.y = actualGraph.getExpression().value();

            if (Double.isNaN(node.y))
                    interpolationNodes.remove(i--);

            if (Double.isNaN(node.y) || Double.isInfinite(node.y))
                node.y = 0;
        }
        updateInterpolationLimits();
        repaint();
        return interpolationNodes.size();
    }

    public void clearData() {
        super.clearData();
        actualGraph = null;
        interpolationGraph = null;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        draggingNode = null;
        if (nodeEditingComponent != null)
            remove(nodeEditingComponent);

        for (XY node : interpolationNodes) {
            Point nodePoint = new Point(projectX(node.x), projectY(node.y));
            if (e.getPoint().distance(nodePoint) < NODES_SIZE / 2.0) {
                draggingNode = node;
                draggingBufferY = e.getY();
                draggingBufferX = e.getX();
                return;
            }
        }

        if (nodeCreatingMode && actualGraph != null) {
            Point mousePosition = getMousePosition();
            if (mousePosition != null) {
                double x = unprojectX(mousePosition.x);
                actualGraph.getX().setValue(x);
                double y = actualGraph.getExpression().value();
                XY node = new XY(x, y);
                interpolationNodes.add(node);
                draggingBufferY = e.getY();
                draggingBufferX = e.getX();
                draggingNode = node;
                repaint();
            }
        }

        super.mousePressed(e);
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (draggingNode == null || interpolationGraph == null)
            super.mouseDragged(e);
        else {
            int offsetX = e.getX() - draggingBufferX;
            int offsetY = e.getY() - draggingBufferY;
            draggingNode.x += 1.0 * offsetX / getWidth() * (getUpperX() - getLowerX());
            actualGraph.getX().setValue(draggingNode.x);

            if (!e.isAltDown() || isCursorOverDeleteButton()) {
                draggingNode.y -= 1.0 * offsetY / getHeight() * (getUpperY() - getLowerY());
                draggingBufferY = e.getY();
            } else {
                draggingNode.y = actualGraph.getExpression().value();
                draggingBufferY = projectY(draggingNode.y);
            }

            draggingBufferX = e.getX();
            initDeleteAnimationWorker();
            repaint();
        }
    }

    private boolean isCursorOverDeleteButton() {
        Point mousePosition = getMousePosition();
        return mousePosition != null &&
                mousePosition.distance(
                        getWidth()/2.0,
                        getHeight() - deletingIconBottomPadding
                ) < deletingIconSize;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        nodeCreatingMode = e.isControlDown();
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        if (isCursorOverDeleteButton())
            interpolationNodes.remove(draggingNode);
        draggingNode = null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (XY node : interpolationNodes) {
            Point nodePoint = new Point(projectX(node.x), projectY(node.y));
            if (e.getPoint().distance(nodePoint) < NODES_SIZE / 2.0) {
                Point modalLocation = new Point(projectX(node.x), projectY(node.y));
                SwingUtilities.convertPointToScreen(modalLocation, this);

                new NodeCoordinatesModal(
                        (JFrame) SwingUtilities.getWindowAncestor(this),
                        node,
                        modalLocation,
                        r -> {
                            node.x = r.x;
                            node.y = r.y;
                            repaint();
                        }
                );
                break;
            }
        }
    }

    public void setInterpolateWithinNodes(boolean b) {
        if (interpolationGraph != null) {
            interpolationGraph.setLimitEnabled(b);
            isInterpolationLimited = b;
            repaint();
        }
    }

    public List<XY> getInterpolationNodes() {
        return interpolationNodes;
    }
}
