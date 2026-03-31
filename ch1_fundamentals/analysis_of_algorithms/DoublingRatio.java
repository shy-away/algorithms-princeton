package ch1_fundamentals.analysis_of_algorithms;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

public class DoublingRatio {
  public static double timeTrial(int N) {
    int MAX = 1000000;

    // int[] a = new int[N];
    // for (int i = 0; i < N; i++) {
    // a[i] = StdRandom.uniformInt(-MAX, MAX);
    // }

    double[] b = new double[N];
    for (int i = 0; i < N; i++) {
      b[i] = StdRandom.uniformDouble(-MAX, MAX);
    }

    Stopwatch timer = new Stopwatch();
    // ThreeSum.count(a);
    // ThreeSum.countFast(a);
    ClosestPair.findClosestPair(b);
    return timer.elapsedTime();
  }

  public static double timeTrialBatch(int N, int trials) {
    double timeSum = 0;

    for (int i = 0; i < trials; i++) {
      timeSum += timeTrial(N);
    }

    return timeSum / trials;
  }

  public static void main(String[] args) {
    int initialN = 250;
    int trials = 1;

    double prev = timeTrialBatch(initialN / 2, trials);
    for (int N = initialN; true; N += N) {
      double time = timeTrialBatch(N, trials);
      StdOut.printf("%7d %5.1f ", N, time);
      StdOut.printf("%5.1f\n", time / prev);
      prev = time;
    }
  }
}
