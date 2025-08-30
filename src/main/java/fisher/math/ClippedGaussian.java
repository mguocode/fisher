package fisher.math;

import java.util.Random;

public class ClippedGaussian {
    private static final Random random = new Random();
    private double mean;
    private double stdev;
    private double minTime;
    private double maxTime;

    public ClippedGaussian(double mean, double stdev, double minTime, double maxTime) {
        this.mean = mean;
        this.stdev = stdev;
        this.minTime = minTime;
        this.maxTime = maxTime;
    }

    public double get() {
        double s = random.nextGaussian() * stdev + mean;
        s = Math.max(Math.min(s, maxTime), minTime);
        return s;

    }
}
