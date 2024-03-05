package Generators;

import java.util.ArrayList;
import java.util.Random;
import java.math.BigDecimal;

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
        int pGen = probabilityRand.nextInt(100);
        int indexRand = 0;

        int pParamsInt = 0;

        for (ContinuousEmpiricalDistributionParameter par : parameters)
        {
            pParams += par.getP();
            pParamsInt += (int) (par.getP() * 100);

            if (pGen < pParamsInt && !valueGenerated) {
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
