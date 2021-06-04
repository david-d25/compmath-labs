package ru.david.compmath3.gui.graph;

import ru.david.compmath3.math.expression.Expression;
import ru.david.compmath3.math.expression.VariableExpression;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class RawGraphViewer extends JComponent {

    private static final Stroke AXIS_STROKE = new BasicStroke(2);
    private static final Stroke GRID_STROKE = new BasicStroke(1);
    private static final Color AXIS_COLOR = new Color(0, 0, 100);
    private static final Color GRID_COLOR = new Color(214, 214, 214);
    private static final Color GRAPH_NAME_COLOR = new Color(61, 61, 61);
    private static final Font AXIS_FONT = new Font("Courier New", Font.PLAIN, 14);
    private static final Font AXIS_NAME_FONT = new Font("Courier New", Font.PLAIN, 18);
    private static final Font GRAPH_NAME_FONT = new Font("Arial", Font.PLAIN, 16);
    private static final int AXIS_DASH_SIZE = 4;
    private static final Color BACKGROUND = Color.WHITE;
    private static final int AXIS_TEXT_PADDING = 4;
    private static final int AXIS_NAME_PADDING = 20;
    private static final double MIN_GAP_SIZE = 1.5;

    private static final Function<Double, Double> GRID_GAP_COMPUTING_FUNC = gap -> {
        double result = Math.pow(10, (int)Math.log10(gap/10)/Math.log10(0.5))*0.5;
        if (gap > 5)
            result = Math.max(
                    result,
                    Math.pow(10, (int)Math.log10(gap/10))
            );
        if (gap > 20)
            result = Math.max(
                    result,
                    Math.pow(10, (int)(Math.log10(gap/10)/Math.log10(20)))*2
            );
        if (gap > 50)
            result = Math.max(
                    result,
                    Math.pow(10, (int)(Math.log10(gap/10)/Math.log10(50)))*5
            );
        return result;
    };

    List<GraphData> data = new ArrayList<>();

    private double lowerX = -10;
    private double upperX = 10;
    private double lowerY = -1000000000000d;
    private double upperY = 1000000000000d;

    private int oldWidth = getWidth();
    private int oldHeight = getHeight();

    private boolean hiResRender = false;

    {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                double widthRatio = 1.0*getWidth()/oldWidth;
                double heightRatio = 1.0*getHeight()/oldHeight;

                if (Double.isFinite(widthRatio) && Double.isFinite(heightRatio)) {
                    double halfDiffX = (upperX - lowerX) / 2;
                    double centerX = (upperX + lowerX) / 2;
                    lowerX = centerX - halfDiffX * widthRatio;
                    upperX = centerX + halfDiffX * widthRatio;

                    double halfDiffY = (upperY - lowerY) / 2;
                    double centerY = (upperY + lowerY) / 2;
                    lowerY = centerY - halfDiffY * heightRatio;
                    upperY = centerY + halfDiffY * heightRatio;
                }

                oldWidth = getWidth();
                oldHeight = getHeight();

                repaint();
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(BACKGROUND);
        g.fillRect(0, 0, getWidth(), getHeight());
        Graphics2D g2d = (Graphics2D) g;
        drawGrid(g2d);
        drawAxis(g2d);
        for (int i = 0; i < data.size(); i++)
            drawGraph(data.get(i), g2d, i);
        super.paint(g);
    }

    private void drawGrid(Graphics2D g) {
        GridGap gap = computeGridGap();
        g.setStroke(GRID_STROKE);
        g.setColor(GRID_COLOR);
        for (double graphX = lowerX - lowerX%gap.width; graphX <= upperX; graphX += gap.width) {
            if (graphX == 0) continue;
            int x = projectX(graphX);
            g.drawLine(x, 0, x, getHeight());
        }
        for (double graphY = lowerY - lowerY%gap.height; graphY <= upperY; graphY += gap.height) {
            if (graphY == 0) continue;
            int y = projectY(graphY);
            g.drawLine(0, y, getWidth(), y);
        }
    }

    private void drawAxis(Graphics2D g) {
        Point projected = project(new Point());
        if (projected.x < 0)
            projected.x = 0;
        if (projected.y < 0)
            projected.y = 0;
        if (projected.x > getWidth() - 1)
            projected.x = getWidth() - 1;
        if (projected.y > getHeight() - 1)
            projected.y = getHeight() - 1;

        g.setStroke(AXIS_STROKE);
        g.setColor(AXIS_COLOR);
        g.drawLine(0, projected.y, getWidth(), projected.y);
        g.drawLine(projected.x, 0, projected.x, getHeight());

        GridGap gap = computeGridGap();
        int xAxisTextY = projected.y + AXIS_DASH_SIZE/2 + AXIS_FONT.getSize() + AXIS_TEXT_PADDING;
        boolean xAxisTextAlignBottom = true;
        if (xAxisTextY > getHeight()/2) {
            xAxisTextY = projected.y - AXIS_DASH_SIZE - AXIS_TEXT_PADDING;
            xAxisTextAlignBottom = false;
        }

        int yAxisTextX = projected.x - AXIS_DASH_SIZE/2 - AXIS_TEXT_PADDING;
        boolean yAxisTextAlignLeft = true;
        if (yAxisTextX < getWidth()/2) {
            yAxisTextX = projected.x + AXIS_DASH_SIZE/2 + AXIS_TEXT_PADDING;
            yAxisTextAlignLeft = false;
        }

        FontMetrics fontMetrics = getFontMetrics(AXIS_FONT);

        for (double graphX = lowerX - lowerX%gap.width; graphX <= upperX; graphX += gap.width) {
            if (graphX == 0) continue;
            int x = projectX(graphX);
            g.drawLine(x, projected.y - AXIS_DASH_SIZE/2, x, projected.y + AXIS_DASH_SIZE/2);
            g.setFont(AXIS_FONT);
            String text = String.format((long)graphX == graphX ? "%.0f" : "%s", graphX);
            g.drawString(text, x - fontMetrics.stringWidth(text)/2, xAxisTextY);
        }
        for (double graphY = lowerY - lowerY%gap.height; graphY <= upperY; graphY += gap.height) {
            if (graphY == 0) continue;
            int y = projectY(graphY);
            g.drawLine(projected.x - AXIS_DASH_SIZE/2, y, projected.x + AXIS_DASH_SIZE/2, y);
            g.setFont(AXIS_FONT);
            String text = String.format((long)graphY == graphY ? "%.0f" : "%s", graphY);
            if (yAxisTextAlignLeft)
                g.drawString(text, yAxisTextX - fontMetrics.stringWidth(text), y + fontMetrics.getHeight()/3);
            else
                g.drawString(text, yAxisTextX, y + fontMetrics.getHeight()/3);
        }

        g.setFont(AXIS_NAME_FONT);
        if (yAxisTextAlignLeft)
            g.drawString("y", yAxisTextX - fontMetrics.stringWidth("y") - AXIS_NAME_PADDING, AXIS_TEXT_PADDING + fontMetrics.getHeight());
        else
            g.drawString("y", yAxisTextX + AXIS_NAME_PADDING, AXIS_TEXT_PADDING + fontMetrics.getHeight());

        if (xAxisTextAlignBottom)
            g.drawString("x", getWidth() - fontMetrics.stringWidth("x") - AXIS_NAME_PADDING, xAxisTextY + AXIS_NAME_PADDING);
        else
            g.drawString("x", getWidth() - fontMetrics.stringWidth("x") - AXIS_NAME_PADDING, xAxisTextY - AXIS_NAME_PADDING);
    }

    private GridGap computeGridGap() {
        double xGap = (upperX - lowerX)/getWidth()*500;
        double yGap = (upperY - lowerY)/getHeight()*500;

        return new GridGap(GRID_GAP_COMPUTING_FUNC.apply(xGap), GRID_GAP_COMPUTING_FUNC.apply(yGap));
    }

    private void drawGraph(GraphData graph, Graphics2D g, int graphIndex) {
        g.setColor(graph.getColor());
        g.setStroke(new BasicStroke(graph.getStrokeWidth()));
        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        Expression expression = graph.getExpression();
        VariableExpression xExpr = graph.getX();
        int prevProjY = 0;
        double prevX = 0;
        double prevY = 0;
        double yDerivative = 0;
        boolean usePrev = false;
        for (
                float projX = -1;
                projX < getWidth();
                projX += usePrev && hiResRender ? Math.max(0.01, Math.min(1, 1/Math.abs(yDerivative))) : 0.1
        ) {
            double x = unprojectX(projX);
            xExpr.setValue(x);
            double y = expression.value();
            int projY = projectY(y);
            yDerivative = (y - prevY)/(x - prevX);

            if (y == Double.POSITIVE_INFINITY) {
                projY = Integer.MIN_VALUE + 1;
                usePrev = false;
            } else if (y == Double.NEGATIVE_INFINITY) {
                projY = Integer.MAX_VALUE - 1;
                usePrev = false;
            } else if (Double.isNaN(y)) {
                usePrev = false;
                continue;
            }

            if (usePrev) { // Considering y and prevY are not NaN
                double deltaY = y - prevY;
                double midX = (x + prevX) / 2;
                xExpr.setValue(midX);
                double midY = expression.value();
                if (Double.isNaN(midY) || Double.isInfinite(midY)) {
                    usePrev = false;
                } else if (Math.abs(midY - prevY) >= Math.abs(deltaY) || Math.abs(y - midY) >= Math.abs(deltaY)) {
                    usePrev = false;
                }
            }

            if (!graph.isLimitEnabled() || graph.getLowerLimit() < x && x <= graph.getUpperLimit()) {
                if (usePrev)
                    g.drawLine((int)(projX - 1), prevProjY, (int)projX, projY);
                else
                    g.drawLine((int)(projX - 1), projY, (int)projX, projY);
            }

            prevY = y;
            prevX = x;
            prevProjY = projY;
            usePrev = true;
        }

        g.setFont(GRAPH_NAME_FONT);
        FontMetrics fontMetrics = g.getFontMetrics();
        g.drawLine(5, (int)((0.7 + 1.1*graphIndex)*fontMetrics.getHeight()), 20, (int)((0.7 + 1.1*graphIndex)*fontMetrics.getHeight()));
        g.setColor(GRAPH_NAME_COLOR);
        g.drawString(graph.getName(), 25, (int)((1 + 1.1*graphIndex)*fontMetrics.getHeight()));
    }

    protected Point project(Point p) {    // From graph coordinates to component coordinates
        return new Point(
                projectX(p.x),
                projectY(p.y)
        );
    }

    protected int projectX(double x) {
        double xGap = upperX - lowerX;
        double xRatio = getWidth()/xGap;
        return (int)((x - lowerX)*xRatio);
    }

    protected int projectY(double y) {
        double yGap = upperY - lowerY;
        double yRatio = getHeight()/yGap;
        return (int)((upperY - y)*yRatio);
    }

    protected double unprojectX(double x) {  // From component coordinates to graph coordinates
        double xGap = upperX - lowerX;
        double xRatio = getWidth()/xGap;
        return x/xRatio + lowerX;
    }

    protected double unprojectY(double y) {  // From component coordinates to graph coordinates
        double yGap = upperY - lowerY;
        double yRatio = getHeight()/yGap;
        return y/yRatio + lowerY;
    }

    public void addData(GraphData data) {
        this.data.add(data);
        repaint();
    }

    public void removeData(GraphData data) {
        this.data.remove(data);
        repaint();
    }

    public void clearData() {
        data.clear();
        repaint();
    }

    public double getLowerX() {
        return lowerX;
    }

    public void setLowerX(double lowerX) {
        this.lowerX = lowerX;
        repaint();
    }

    public double getUpperX() {
        return upperX;
    }

    public void setUpperX(double upperX) {
        this.upperX = upperX;
        repaint();
    }

    public double getLowerY() {
        return lowerY;
    }

    public void setLowerY(double lowerY) {
        this.lowerY = lowerY;
        repaint();
    }

    public double getUpperY() {
        return upperY;
    }

    public void setUpperY(double upperY) {
        this.upperY = upperY;
        repaint();
    }

    public void setGraphBounds(double lowerX, double upperX, double lowerY, double upperY) {
        if (upperX - lowerX > MIN_GAP_SIZE) {
            this.lowerX = lowerX;
            this.upperX = upperX;
        } else {
            double center = (this.upperX + this.lowerX)/2;
            this.upperX = center + MIN_GAP_SIZE/2;
            this.lowerX = center - MIN_GAP_SIZE/2;
        }
        if (upperY - lowerY > MIN_GAP_SIZE) {
            this.lowerY = lowerY;
            this.upperY = upperY;
        } else {
            double center = (this.upperY + this.lowerY)/2;
            this.upperY = center + MIN_GAP_SIZE/2;
            this.lowerY = center - MIN_GAP_SIZE/2;
        }
        repaint();
    }

    public boolean isHiResRender() {
        return hiResRender;
    }

    public void setHiResRender(boolean hiResRender) {
        this.hiResRender = hiResRender;
        repaint();
    }
}
