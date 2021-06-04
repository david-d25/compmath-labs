package ru.david.compmath2.math.integration;

public class IntegrationLimits {
    private double lowLimit, highLimit;

    public IntegrationLimits(double low, double high) {
        this.lowLimit = low;
        this.highLimit = high;
    }

    public double getHighLimit() {
        return highLimit;
    }

    public void setHighLimit(double highLimit) {
        this.highLimit = highLimit;
    }

    public double getLowLimit() {
        return lowLimit;
    }

    public void setLowLimit(double lowLimit) {
        this.lowLimit = lowLimit;
    }
}
