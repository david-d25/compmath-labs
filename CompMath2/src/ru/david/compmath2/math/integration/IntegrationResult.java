package ru.david.compmath2.math.integration;

public class IntegrationResult {
    private double result = 0;
    private double error = 0;
    private int splits = 0;

    private Status status;

    public IntegrationResult(Status status) {
        this.status = status;
    }

    public IntegrationResult(double result, double error, int splits) {
        this.status = Status.OK;
        this.result = result;
        this.splits = splits;
        this.error = error;
    }

    public Status getStatus() {
        return status;
    }

    public double getResult() {
        return result;
    }

    public double getError() {
        return error;
    }

    public int getSplits() {
        return splits;
    }

    public enum Status {
        OK, GAP
    }
}
