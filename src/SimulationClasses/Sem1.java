package SimulationClasses;

import Generators.ContinuousEmpiricalDistributionGenerator;
import Generators.ContinuousEmpiricalDistributionParameter;
import Observer.ISimDelegate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Random;

public class Sem1 extends SimulationCore{
    private Random discreteUniformRand_2024_25;
    private Random continuousUniformRand_2026_2027;
    private ContinuousEmpiricalDistributionGenerator continuousEmpiricalRand_2028_2029;
    private static final double deterministic_2030_2031 = 1.3;
    private Random continuousUniformRand_2032_2033;
    private static final double vyskaUveru = 100000;
    private double splatka = 0.0;
    private int[] fixacneObdobia;

    public Sem1(Random baseGen, int[] fixacneObdobia) {
        this.delegates = new ArrayList<ISimDelegate>();

        this.fixacneObdobia = fixacneObdobia;

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

        for (int fo : this.fixacneObdobia) {
            rocnaUrokovaSadzba = generujRocnuUrokovuSadzbu(2024 + 10 - ostavajuceRoky);
            double mesacnaUrokovaSadzba = rocnaUrokovaSadzba / 12.0;
            double mesacnaSplatka = vypocitajMesacnuSplatku(zostatokIstiny, mesacnaUrokovaSadzba, ostavajuceRoky);
            zostatokIstiny = vypocitajZostatokIstiny(zostatokIstiny, mesacnaUrokovaSadzba, ostavajuceRoky, fo);
            ostavajuceRoky -= fo;

            this.splatka += mesacnaSplatka * 12.0 * fo;
        }
    }

    @Override
    public void afterOneReplication() {
        this.executedReplications++;
        this.result = this.splatka / this.executedReplications;
        this.refreshGUI();
    }

    private double generujRocnuUrokovuSadzbu(int rok) {
        double urok = 0.0;

        if (rok >= 2024 && rok <= 2025) {
            urok = discreteUniformRand_2024_25.nextInt(1,5);
        } else if (rok >= 2026 && rok <= 2027) {
            urok = continuousUniformRand_2026_2027.nextDouble(0.3, 5.0);
        } else if (rok >= 2028 && rok <= 2029) {
            urok = continuousEmpiricalRand_2028_2029.generate();
        } else if (rok >= 2030 && rok <= 2031) {
            urok = deterministic_2030_2031;
        } else if (rok >= 2032 && rok <= 2033) {
            urok = continuousUniformRand_2032_2033.nextDouble(0.9, 2.2);
        }

        return urok / 100.0; // Prevedenie percent na desatinné číslo
    }

    private static double vypocitajMesacnuSplatku(double istina, double mesacnaUrokovaSadzba, int pocetRokovSplacania) {
        double delenec = istina * mesacnaUrokovaSadzba * Math.pow(1.0 + mesacnaUrokovaSadzba, 12.0 * pocetRokovSplacania);
        double delitel = Math.pow(1.0 + mesacnaUrokovaSadzba, 12.0 * pocetRokovSplacania) - 1.0;
        return delenec / delitel;
    }

    private static double vypocitajZostatokIstiny(double istina, double mesacnaUrokovaSadzba, int pocetRokovSplacania, int pocetRokovSplatenych) {
        double delenec = Math.pow(1.0 + mesacnaUrokovaSadzba, 12.0 * pocetRokovSplacania) - Math.pow(1.0 + mesacnaUrokovaSadzba, 12.0 * pocetRokovSplatenych);
        double delitel = Math.pow(1.0 + mesacnaUrokovaSadzba, 12.0 * pocetRokovSplacania) - 1.0;
        return istina * delenec / delitel;
    }

//    public void executeOneReplication() {
//        BigDecimal vyskaUveruBD = new BigDecimal(String.valueOf(vyskaUveru)); // Convert loan amount to BigDecimal
//        int ostavajuceRoky = 10;
//        BigDecimal rocnaUrokovaSadzbaBD;
//
//        BigDecimal splatkaBD = BigDecimal.ZERO; // Initialize splatka as BigDecimal
//
//        for (int fo : this.fixacneObdobia) {
//            rocnaUrokovaSadzbaBD = generujRocnuUrokovuSadzbu(2024 + 10 - ostavajuceRoky); // Assume this method is adjusted to return BigDecimal
//            BigDecimal mesacnaUrokovaSadzbaBD = rocnaUrokovaSadzbaBD.divide(BigDecimal.valueOf(12), 8, RoundingMode.HALF_UP);
//
//            BigDecimal mesacnaSplatkaBD = vypocitajMesacnuSplatku(vyskaUveruBD, mesacnaUrokovaSadzbaBD, ostavajuceRoky);
//            vyskaUveruBD = vypocitajZostatokIstiny(vyskaUveruBD, mesacnaUrokovaSadzbaBD, ostavajuceRoky, fo);
//            ostavajuceRoky -= fo;
//
//            BigDecimal monthlyPaymentsTotal = mesacnaSplatkaBD.multiply(BigDecimal.valueOf(12)).multiply(BigDecimal.valueOf(fo));
//            splatkaBD = splatkaBD.add(monthlyPaymentsTotal);
//        }
//
//        this.splatka += splatkaBD.doubleValue(); // Convert final splatka back to double if necessary
//    }

//    private BigDecimal generujRocnuUrokovuSadzbu(int rok) {
//        double urok = 0.0;
//        BigDecimal urokBD;
//
//        if (rok >= 2024 && rok <= 2025) {
//            urok = discreteUniformRand_2024_25.nextInt(1,5);
//        } else if (rok >= 2026 && rok <= 2027) {
//            urok = continuousUniformRand_2026_2027.nextDouble(0.3, 5.0);
//        } else if (rok >= 2028 && rok <= 2029) {
//            urok = continuousEmpiricalRand_2028_2029.generate();
//        } else if (rok >= 2030 && rok <= 2031) {
//            urok = deterministic_2030_2031;
//        } else if (rok >= 2032 && rok <= 2033) {
//            urok = continuousUniformRand_2032_2033.nextDouble(0.9, 2.2);
//        }
//        urokBD = BigDecimal.valueOf(urok);
//        urokBD = urokBD.divide(BigDecimal.valueOf(100), 8, RoundingMode.HALF_UP);
//
//        return urokBD;
//        //return urok / 100.0; // Prevedenie percent na desatinné číslo
//    }

//private static BigDecimal vypocitajMesacnuSplatku(BigDecimal istina, BigDecimal mesacnaUrokovaSadzba, int pocetRokovSplacania) {
//    BigDecimal twelve = new BigDecimal("12");
//    BigDecimal onePlusR = mesacnaUrokovaSadzba.add(BigDecimal.ONE);
//    BigDecimal n = new BigDecimal(pocetRokovSplacania).multiply(twelve);
//
//    BigDecimal numerator = istina.multiply(mesacnaUrokovaSadzba).multiply(onePlusR.pow(n.intValueExact()));
//    BigDecimal denominator = onePlusR.pow(n.intValueExact()).subtract(BigDecimal.ONE);
//
//    return numerator.divide(denominator, 8, RoundingMode.HALF_UP);
//}
//
//    private static BigDecimal vypocitajZostatokIstiny(BigDecimal istina, BigDecimal mesacnaUrokovaSadzba, int pocetRokovSplacania, int pocetRokovSplatenych) {
//        BigDecimal twelve = new BigDecimal("12");
//        BigDecimal onePlusR = mesacnaUrokovaSadzba.add(BigDecimal.ONE);
//        BigDecimal n = new BigDecimal(pocetRokovSplacania).multiply(twelve);
//        BigDecimal m = new BigDecimal(pocetRokovSplatenych).multiply(twelve);
//
//        BigDecimal numerator = onePlusR.pow(n.intValueExact()).subtract(onePlusR.pow(m.intValueExact()));
//        BigDecimal denominator = onePlusR.pow(n.intValueExact()).subtract(BigDecimal.ONE);
//
//        return istina.multiply(numerator).divide(denominator, 8, RoundingMode.HALF_UP);
//    }
}
