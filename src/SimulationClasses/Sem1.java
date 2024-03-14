package SimulationClasses;

import Generators.ContinuousEmpiricalDistributionGenerator;
import Generators.ContinuousEmpiricalDistributionParameter;
import Observer.ISimDelegate;
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

    public Sem1(Random seedGen, int[] fixacneObdobia) {
        this.delegates = new ArrayList<ISimDelegate>();

        this.fixacneObdobia = fixacneObdobia;

        discreteUniformRand_2024_25 = new Random(seedGen.nextInt());
        continuousUniformRand_2026_2027 = new Random(seedGen.nextInt());

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
        continuousEmpiricalRand_2028_2029 = new ContinuousEmpiricalDistributionGenerator(seedGen, parameterArrayList);

        continuousUniformRand_2032_2033 = new Random(seedGen.nextInt());
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

    private double vypocitajMesacnuSplatku(double istina, double mesacnaUrokovaSadzba, int pocetRokovSplacania) {
        double delenec = istina * mesacnaUrokovaSadzba * Math.pow(1.0 + mesacnaUrokovaSadzba, 12.0 * pocetRokovSplacania);
        double delitel = Math.pow(1.0 + mesacnaUrokovaSadzba, 12.0 * pocetRokovSplacania) - 1.0;
        return delenec / delitel;
    }

    private double vypocitajZostatokIstiny(double istina, double mesacnaUrokovaSadzba, int pocetRokovSplacania, int pocetRokovSplatenych) {
        double delenec = Math.pow(1.0 + mesacnaUrokovaSadzba, 12.0 * pocetRokovSplacania) - Math.pow(1.0 + mesacnaUrokovaSadzba, 12.0 * pocetRokovSplatenych);
        double delitel = Math.pow(1.0 + mesacnaUrokovaSadzba, 12.0 * pocetRokovSplacania) - 1.0;
        return istina * delenec / delitel;
    }
}
