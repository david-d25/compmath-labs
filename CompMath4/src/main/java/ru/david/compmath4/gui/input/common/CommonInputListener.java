package ru.david.compmath4.gui.input.common;

public interface CommonInputListener {
    void onInput(String rawExpression, double startX, double startY, double stopX, double accuracy);
}
