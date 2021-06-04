package ru.david.compmath4.gui.graph;

import java.awt.*;
import java.awt.event.*;

public class InteractiveGraphViewer extends RawGraphViewer implements MouseMotionListener, MouseListener, MouseWheelListener {
    private static final double WHEEL_SENSITIVITY = 0.5;

    private double draggingBufferX;
    private double draggingBufferY;

    public InteractiveGraphViewer() {
        setCursor(new Cursor(Cursor.MOVE_CURSOR));
        addMouseMotionListener(this);
        addMouseListener(this);
        addMouseWheelListener(this);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        draggingBufferX = e.getX();
        draggingBufferY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        double offsetX = e.getX() - draggingBufferX;
        double offsetY = e.getY() - draggingBufferY;
        draggingBufferX = e.getX();
        draggingBufferY = e.getY();
        double projOffsetX = offsetX/getWidth()*(getUpperX() - getLowerX());
        double projOffsetY = offsetY/getHeight()*(getUpperY() - getLowerY());
        setLowerX(getLowerX() - projOffsetX);
        setUpperX(getUpperX() - projOffsetX);
        setLowerY(getLowerY() + projOffsetY);
        setUpperY(getUpperY() + projOffsetY);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double rotationX = e.getPreciseWheelRotation()*WHEEL_SENSITIVITY*(getUpperX() - getLowerX())/Math.log(getUpperX() - getLowerX());
        double rotationY = e.getPreciseWheelRotation()*WHEEL_SENSITIVITY*(getUpperY() - getLowerY())/Math.log(getUpperX() - getLowerX());
        setGraphBounds(
                getLowerX() - rotationX*(1.0*e.getPoint().x/getWidth()),
                getUpperX() + rotationX*(1 - 1.0*e.getPoint().x/getWidth()),
                getLowerY() - rotationY*(1 - 1.0*e.getPoint().y/getHeight()),
                getUpperY() + rotationY*(1.0*e.getPoint().y/getHeight())
        );
    }
}
