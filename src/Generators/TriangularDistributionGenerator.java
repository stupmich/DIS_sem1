package Generators;

import java.util.Random;

public class TriangularDistributionGenerator {
    private Random random;
    private double min;
    private double max;
    private double mode;

    public TriangularDistributionGenerator(Random seedGenerator, double min, double max, double mode) {
        this.random = new Random(seedGenerator.nextInt());
        this.min = min;
        this.max = max;
        this.mode = mode;
    }

    public double generate() {
        double u = random.nextDouble();
        double c = (mode - min) / (max - min);
        if (u < c) {
            return min + Math.sqrt(u * (max - min) * (mode - min));
        } else {
            return max - Math.sqrt((1 - u) * (max - min) * (max - mode));
        }
    }
}
