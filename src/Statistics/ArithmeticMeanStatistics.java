package Statistics;

public class ArithmeticMeanStatistics {
    private double totalSum;
    private double totalSumSquared;
    private int count;
    private double mean;
    private double standardDeviationCorrected;

    public double calculateMean(double sample) {
        this.totalSum += sample;
        this.totalSumSquared += Math.pow(sample, 2);
        this.count++;
        this.mean = this.totalSum / ((double) this.count);
        return this.mean;
    }

    public void calculateCorrectedStandardDeviation(){
        this.standardDeviationCorrected = Math.sqrt((totalSumSquared - ((totalSum * totalSum) / count)) / (count - 1));
    }

    public double calculateConfidenceIntervalLower(double ta) {
        double lowerBound = mean - ((standardDeviationCorrected * ta) / Math.sqrt(count));
        return lowerBound;
    }

    public double calculateConfidenceIntervalUpper(double ta) {
        double upperBound = mean + ((standardDeviationCorrected * ta) / Math.sqrt(count));
        return upperBound;
    }

    public double getMean() {
        return mean;
    }

    public void clearStat() {
        this.totalSum = 0.0;
        this.totalSumSquared = 0.0;
        this.count = 0;
        this.mean = 0.0;
        this.standardDeviationCorrected = 0.0;
    }
}
