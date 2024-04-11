import GUI.ElektrokomponentyGUI;
import GUI.GUI_Sem1;
import Generators.ContinuousEmpiricalDistributionGenerator;
import Generators.ContinuousEmpiricalDistributionParameter;
import Generators.ExponentialDistributionGenerator;
import Generators.TriangularDistributionGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ElektrokomponentyGUI gui = new ElektrokomponentyGUI();
    }

    public static void testTriangularDistribution(String filename, double min, double max, double mode) {
        TriangularDistributionGenerator generator = new TriangularDistributionGenerator(min, max, mode);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < 1000000; i++) {
                double value = generator.generate();
                writer.write(String.valueOf(value));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void testExponentialDistribution(String filename, double lambda) {
        ExponentialDistributionGenerator generator = new ExponentialDistributionGenerator(lambda);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < 100000; i++) {
                double value = generator.generate();
                writer.write(String.valueOf(value));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void testContinuousEmpiricalDistribution(String filename) {
        ArrayList<ContinuousEmpiricalDistributionParameter> parameterArrayListDifficultOrder = new ArrayList<ContinuousEmpiricalDistributionParameter>();
        ContinuousEmpiricalDistributionParameter pdo1 = new ContinuousEmpiricalDistributionParameter(11.0,12.0,0.1);
        ContinuousEmpiricalDistributionParameter pdo2 = new ContinuousEmpiricalDistributionParameter(12.0,20.0,0.6);
        ContinuousEmpiricalDistributionParameter pdo3 = new ContinuousEmpiricalDistributionParameter(20.0,25.0,0.3);
        parameterArrayListDifficultOrder.add(pdo1);
        parameterArrayListDifficultOrder.add(pdo2);
        parameterArrayListDifficultOrder.add(pdo3);
        ContinuousEmpiricalDistributionGenerator generator = new ContinuousEmpiricalDistributionGenerator(parameterArrayListDifficultOrder);;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < 100000; i++) {
                double value = generator.generate();
                writer.write(String.valueOf(value));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }


}