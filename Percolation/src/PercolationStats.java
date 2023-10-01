import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double[] trialData;
    private final int trials;
    private static final double CONFIDENCE = 1.96;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("invalid n or trail provide");

        this.trialData = new double[trials];
        this.trials = trials;

        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            boolean[] takenNumbers = new boolean[n * n];
            for (int j = 1; !percolation.percolates(); j++) {
                int index = StdRandom.uniformInt(0, n * n);

                int row = index / n + 1;
                int col = index % n + 1;

                while (takenNumbers[index]) {
                    index = StdRandom.uniformInt(0, n * n);

                    row = index / n + 1;
                    col = index % n + 1;
                }

                takenNumbers[index] = true;
                percolation.open(row, col);

                if (percolation.percolates()) {
                    trialData[i] = (double) j / (n * n);
                }
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(this.trialData);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(this.trialData);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (PercolationStats.CONFIDENCE * stddev()) / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (PercolationStats.CONFIDENCE * stddev()) / Math.sqrt(trials);
    }



    // test client (see below)
    public static void main(String[] args) {
        PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}
