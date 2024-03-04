package Generators;

import java.util.ArrayList;
import java.util.Random;

public class DiscreteEmpiricalDistributionGenerator {
    private Random probabilityRand;
    private ArrayList<DiscreteEmpiricalDistributionParameter> parameters;
    private ArrayList<Random> valueRandList;
    public DiscreteEmpiricalDistributionGenerator(Random probabilityRand, ArrayList<Random> valueRandList, ArrayList<DiscreteEmpiricalDistributionParameter> parameters) {
        this.probabilityRand = probabilityRand;
        this.valueRandList = valueRandList;
        this.parameters = parameters;
    }

    public int generate() {
        int value = 0;
        boolean valueGenerated = false;
        double pParams = 0;
        double pGen = probabilityRand.nextDouble();
        int indexRand = 0;

        for (DiscreteEmpiricalDistributionParameter par : parameters)
        {
            pParams += par.getP();

            if (pGen < pParams && !valueGenerated) {
                value =  valueRandList.get(indexRand).nextInt(par.getMin(), par.getMax() + 1);
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
