package Generators;

import java.util.ArrayList;
import java.util.Random;

public class ContinuousEmpiricalDistributionGenerator {
    private Random probabilityRand;
    private ArrayList<ContinuousEmpiricalDistributionParameter> parameters;
    private ArrayList<Random> valueRandList;
    public ContinuousEmpiricalDistributionGenerator(Random probabilityRand, ArrayList<Random> valueRandList, ArrayList<ContinuousEmpiricalDistributionParameter> parameters) {
        this.probabilityRand = probabilityRand;
        this.valueRandList = valueRandList;
        this.parameters = parameters;
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
                value = valueRandList.get(indexRand).nextDouble(par.getMin(), par.getMax());
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
