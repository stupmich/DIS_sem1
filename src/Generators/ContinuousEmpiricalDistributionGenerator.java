package Generators;

import java.util.ArrayList;
import java.util.Random;
import java.math.BigDecimal;

public class ContinuousEmpiricalDistributionGenerator {
    private Random probabilityRand;
    private ArrayList<ContinuousEmpiricalDistributionParameter> parameters;
    private ArrayList<Random> valueRandGenList;

    public ContinuousEmpiricalDistributionGenerator(Random seedGenerator, ArrayList<ContinuousEmpiricalDistributionParameter> parameters) {
        this.probabilityRand = new Random(seedGenerator.nextInt());
        this.parameters = parameters;
        this.valueRandGenList = new ArrayList<>(parameters.size());
        for (int i = 0; i < parameters.size(); i++) {
            valueRandGenList.add(new Random(seedGenerator.nextInt()));
        }
    }

    public ContinuousEmpiricalDistributionGenerator(int seed, ArrayList<ContinuousEmpiricalDistributionParameter> parameters) {
        this.probabilityRand = new Random(seed);
        this.parameters = parameters;
        this.valueRandGenList = new ArrayList<>(parameters.size());
        for (int i = 0; i < parameters.size(); i++) {
            valueRandGenList.add(new Random(seed));
        }
    }

    public ContinuousEmpiricalDistributionGenerator(ArrayList<ContinuousEmpiricalDistributionParameter> parameters) {
        this.probabilityRand = new Random();
        this.parameters = parameters;
        this.valueRandGenList = new ArrayList<>(parameters.size());
        for (int i = 0; i < parameters.size(); i++) {
            valueRandGenList.add(new Random());
        }
    }

    public double generate() {
        double value = 0;
        boolean valueGenerated = false;
        double pParams = 0;
        double pGen = probabilityRand.nextDouble();
        int indexRand = 0;

        for (ContinuousEmpiricalDistributionParameter par : parameters)
        {
            pParams += par.getP();

            if (pGen < pParams && !valueGenerated) {
                value = valueRandGenList.get(indexRand).nextDouble(par.getMin(), par.getMax());
                valueGenerated = true;
            }
            indexRand++;
        }

        if (pParams != 1) {
            throw new IllegalArgumentException("Sum of probabilities is not equal to 1.");
        }

        return value;
    } 
}
