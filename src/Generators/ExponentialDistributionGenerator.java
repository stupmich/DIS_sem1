package Generators;

import java.util.Random;

public class ExponentialDistributionGenerator {
    private Random random;
    private double lambda;

    public ExponentialDistributionGenerator(Random seedGenerator, double lambda) {
        this.lambda = lambda;
        this.random = new Random(seedGenerator.nextInt());
    }

    public ExponentialDistributionGenerator(int seed, double lambda) {
        this.lambda = lambda;
        this.random = new Random(seed);
    }

    public ExponentialDistributionGenerator(double lambda) {
        this.lambda = lambda;
        this.random = new Random();
    }

    public double generate() {
        double u = random.nextDouble();
        double x = -Math.log(1 - u) / (lambda);
        return x;
    }

}
