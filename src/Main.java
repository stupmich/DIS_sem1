import GUI.GUI_Sem1;
import Generators.ContinuousEmpiricalDistributionGenerator;
import Generators.ContinuousEmpiricalDistributionParameter;
import SimulationClasses.Sem1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        GUI_Sem1 gui = new GUI_Sem1();

//        Random baseGen = new Random(1);
//        Sem1 simA = new Sem1(baseGen, new int[]{5, 3, 1, 1});
//        simA.setRunning(true);
//        simA.executeReplications(10000000, null,null);

//        Sem1 simB = new Sem1(baseGen, new int[]{3, 3, 3, 1});
//        simB.setRunning(true);
//        simB.executeReplications(10000000, null,null);
//
//        Sem1 simC = new Sem1(baseGen, new int[]{3, 1, 5, 1});
//        simC.setRunning(true);
//        simC.executeReplications(10000000, null,null);

//        testEmpiricalDist();

    }

    private static void testEmpiricalDist() {
        ContinuousEmpiricalDistributionGenerator generator;
        Random baseGen = new Random();

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
        generator = new ContinuousEmpiricalDistributionGenerator(baseGen, parameterArrayList);

        int testSize = 1000000;
        int[] counts = new int[parameterArrayList.size()];

        for (int i = 0; i < testSize; i++) {
            double value = generator.generate();

            for (int paramIndex = 0; paramIndex < parameterArrayList.size(); paramIndex++) {
                ContinuousEmpiricalDistributionParameter param = parameterArrayList.get(paramIndex);
                if (value >= param.getMin() && value < param.getMax()) { // kontrola ci spada do rangeu napr <0.1,0.3)
                    counts[paramIndex]++;
                    break;
                }
            }
        }

        boolean testPassed = true;
        for (int i = 0; i < parameterArrayList.size(); i++) {
            double expectedCount = parameterArrayList.get(i).getP() * testSize;
            System.out.println("Parameter " + i + ": Expected Count = " + expectedCount + ", Generated Count = " + counts[i]);
            if (Math.abs(counts[i] - expectedCount) > expectedCount * 0.01) { // 1% odchylka
                testPassed = false;
            }
        }

        if (testPassed) {
            System.out.println("Test passed: Counts of generated values are within expected ranges.");
        } else {
            System.out.println("Test failed: Some  Counts of generated values were outside expected ranges.");
        }
    }
}