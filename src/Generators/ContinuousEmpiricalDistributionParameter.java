package Generators;

public class ContinuousEmpiricalDistributionParameter {
    private double min;
    private double max;
    private double p;

    public ContinuousEmpiricalDistributionParameter(double min, double max, double p) {
        this.min = min;
        this.max = max;
        this.p = p;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getP() {
        return p;
    }
}
