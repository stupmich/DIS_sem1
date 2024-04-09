package Statistics;

import SimulationClasses.EventBasedSimulationCore;

import java.util.LinkedList;

public class WeightedArithmeticMean {
    private double lastUpdateTime;
    private double weightedAverage = 0.0;
    private double weightedSum;
    private double weightedSumSquared;
    private double totalWeight;
    private double correctedStandardDeviation;

    public void updateStatistics(EventBasedSimulationCore core, LinkedList<?> list) {
        double currentTime = core.getCurrentTime();
        double timeDelta = currentTime - lastUpdateTime;
        double size = list.size();

        // Use size as the value and timeDelta as the weight
        weightedSum += size * timeDelta;
        weightedSumSquared += Math.pow(size * timeDelta, 2);
        totalWeight += timeDelta;

        lastUpdateTime = currentTime;
    }

    public double calculateWeightedMean() {
        if (totalWeight == 0.0) {
            weightedAverage = weightedSum;
        } else {
            weightedAverage = weightedSum / totalWeight;
        }

        return weightedAverage;
    }

    public void calculateCorrectedStandardDeviation(){
        if (totalWeight > 1) {
            correctedStandardDeviation = Math.sqrt((weightedSumSquared - (Math.pow(weightedSum, 2) / totalWeight)) / (totalWeight - 1));
        }
    }

    public double calculateConfidenceIntervalLower(double ta) {
        return weightedAverage - ((correctedStandardDeviation * ta) / Math.sqrt(totalWeight));
    }

    public double calculateConfidenceIntervalUpper(double ta) {
        return weightedAverage + ((correctedStandardDeviation * ta) / Math.sqrt(totalWeight));
    }

    public void setLastUpdateTime(double lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
