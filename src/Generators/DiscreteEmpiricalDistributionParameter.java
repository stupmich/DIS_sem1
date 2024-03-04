package Generators;

public class DiscreteEmpiricalDistributionParameter {
    private int min;
    private int max;
    private double p;

    public DiscreteEmpiricalDistributionParameter(int min, int max, double p) {
        this.min = min;
        this.max = max;
        this.p = p;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public double getP() {
        return p;
    }
}
