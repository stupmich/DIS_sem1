package SimulationClasses;

import Generators.ContinuousEmpiricalDistributionGenerator;
import Generators.ContinuousEmpiricalDistributionParameter;

import java.util.ArrayList;
import java.util.Random;

public class Sem1ScenarioA extends SimulationCore{
    private Random discreteUniformRand_2024_25;
    private Random continuousUniformRand_2026_2027;
    private ContinuousEmpiricalDistributionGenerator continuousEmpiricalRand_2028_2029;
    private static final double deterministic_2030_2031 = 1.3;
    private Random continuousUniformRand_2032_2033;

    private static final double vyskaUveru = 100000;

    public Sem1ScenarioA(Random baseGen) {
        discreteUniformRand_2024_25 = new Random(baseGen.nextInt());
        continuousUniformRand_2026_2027 = new Random(baseGen.nextInt());

        Random probabilityRand= new Random(baseGen.nextInt());
        ArrayList<Random> valueRandList = new ArrayList<Random>();
        Random valueRand1 = new Random(baseGen.nextInt());
        Random valueRand2 = new Random(baseGen.nextInt());
        Random valueRand3 = new Random(baseGen.nextInt());
        Random valueRand4 = new Random(baseGen.nextInt());
        Random valueRand5 = new Random(baseGen.nextInt());
        Random valueRand6 = new Random(baseGen.nextInt());
        valueRandList.add(valueRand1);
        valueRandList.add(valueRand2);
        valueRandList.add(valueRand3);
        valueRandList.add(valueRand4);
        valueRandList.add(valueRand5);
        valueRandList.add(valueRand6);

        ArrayList<ContinuousEmpiricalDistributionParameter> parameterArrayList = new ArrayList<ContinuousEmpiricalDistributionParameter>();
        ContinuousEmpiricalDistributionParameter p1 = new ContinuousEmpiricalDistributionParameter(0.1,0.3,0.1);
        ContinuousEmpiricalDistributionParameter p2 = new ContinuousEmpiricalDistributionParameter(0.3,0.8,0.35);
        ContinuousEmpiricalDistributionParameter p3 = new ContinuousEmpiricalDistributionParameter(0.8,1.2,0.2);
        ContinuousEmpiricalDistributionParameter p4 = new ContinuousEmpiricalDistributionParameter(1.2,2.5,0.15);
        ContinuousEmpiricalDistributionParameter p5 = new ContinuousEmpiricalDistributionParameter(2.5,3.8,0.15);
        ContinuousEmpiricalDistributionParameter p6 = new ContinuousEmpiricalDistributionParameter(3.8,4.8,0.05);
        parameterArrayList.add(p1);
        parameterArrayList.add(p2);
        parameterArrayList.add(p3);
        parameterArrayList.add(p4);
        parameterArrayList.add(p5);
        parameterArrayList.add(p6);
        continuousEmpiricalRand_2028_2029 = new ContinuousEmpiricalDistributionGenerator(probabilityRand, valueRandList, parameterArrayList);

        continuousUniformRand_2032_2033 = new Random(baseGen.nextInt());
    }

    @Override
    public void executeOneReplication() {
        double zostatokIstiny = vyskaUveru;
        int ostavajuceRoky = 10;
        double rocnaUrokovaSadzba;
        int[] fixacneObdobia = {5, 3, 1, 1};

        for (int fo : fixacneObdobia) {
            rocnaUrokovaSadzba = generujRocnuUrokovuSadzbu(2024 + 10 - ostavajuceRoky, fo);
            double mesacnaUrokovaSadzba = rocnaUrokovaSadzba / 12;
            double mesacnaSplatka = calculateMonthlyPayment(remainingLoan, monthlyInterestRate, remainingYears);
            System.out.println("Fixačné obdobie: " + fixationPeriod + " rokov, Mesačná splátka: " + monthlyPayment);
            remainingLoan = calculateRemainingLoan(remainingLoan, monthlyInterestRate, remainingYears, fixationPeriod);
            remainingYears -= fixationPeriod;
        }
    }

    @Override
    public void afterOneReplication() {

    }

    private double generujRocnuUrokovuSadzbu(int rok, int fixacia) {
        double urok = 0.0;

        if (rok >= 2024 && rok <= 2025) {
            urok = discreteUniformRand_2024_25.nextInt(1,5);
        } else if (rok >= 2026 && rok <= 2027) {
            urok = 0.3 + (5 - 0.3) * continuousUniformRand_2026_2027.nextDouble();
        } else if (rok >= 2028 && rok <= 2029) {
            urok = continuousEmpiricalRand_2028_2029.generate();
        } else if (rok >= 2030 && rok <= 2031) {
            urok = deterministic_2030_2031;
        } else if (rok >= 2032 && rok <= 2033) {
            urok = 0.9 + (2.2 - 0.9) * continuousUniformRand_2032_2033.nextDouble();
        }

        return urok / 100; // Prevedenie percent na desatinné číslo
    }

    private static double vypocitajMesacnuSplatku(double istina, double mesacnaUrokovaSadzba, int pocetRokovSplacania) {
        double n = years * 12;
        double numerator = loanAmount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, n);
        double denominator = Math.pow(1 + monthlyInterestRate, n) - 1;
        return numerator / denominator;
    }
}
