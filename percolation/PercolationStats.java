import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
  private double mean, stddev, confidenceLo, confidenceHi;

  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0)
      throw new IllegalArgumentException();

    // initialize indices
    int numGridSites = n * n;
    int[] siteIndices = new int[numGridSites];
    for (int i = 0; i < numGridSites; i++) {
      siteIndices[i] = i;
    }

    // run trials
    Percolation p;
    double[] results = new double[trials];
    for (int i = 0; i < trials; i++) {
      p = new Percolation(n);

      StdRandom.shuffle(siteIndices);

      int numOpenSites = 0;
      while (!p.percolates()) {
        p.open((siteIndices[numOpenSites] / n) + 1, (siteIndices[numOpenSites] % n) + 1);
        numOpenSites++;
      }

      results[i] = (double) numOpenSites / numGridSites;
    }

    // pre-calculate results
    mean = StdStats.mean(results);
    stddev = StdStats.stddev(results);
    confidenceLo = mean - ((1.96 * stddev) / Math.sqrt(trials));
    confidenceHi = mean + ((1.96 * stddev) / Math.sqrt(trials));
  }

  public double mean() {
    return mean;
  }

  public double stddev() {
    return stddev;
  }

  public double confidenceLo() {
    return confidenceLo;
  }

  public double confidenceHi() {
    return confidenceHi;
  }

  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    int T = Integer.parseInt(args[1]);

    PercolationStats ps = new PercolationStats(n, T);

    StdOut.println("mean                    = " + ps.mean());
    StdOut.println("stddev                  = " + ps.stddev());
    StdOut.println("95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
  }
}
