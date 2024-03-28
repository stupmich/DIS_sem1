package Generators;

import java.util.Random;

public class ExponentialDistributionGenerator {
    private Random random;
    private double lambda;

    public ExponentialDistributionGenerator(Random random, double lambda) {
        this.lambda = lambda;
        this.random = random;
    }

    public double generate() {
        double u = random.nextDouble();
        double x = Math.log(1 - u) / (-lambda);
        return x;
    }

}
