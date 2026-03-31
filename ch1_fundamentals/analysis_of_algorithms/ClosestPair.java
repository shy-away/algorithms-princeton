package ch1_fundamentals.analysis_of_algorithms;

import java.security.InvalidParameterException;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;

public class ClosestPair {
  public static Pair<Double> findClosestPair(double[] a) {
    if (a.length < 2) {
      throw new InvalidParameterException("Array length must be at least 2");
    }

    int N = a.length;
    
    // copy the array to avoid mutating the original
    // double[] copy = new double[N];
    // for (int i = 0; i < N; i++) {
    //   copy[i] = a[i];
    // }

    double[] copy = a;

    // linearithmic quicksort
    Arrays.sort(copy);

    // linear search
    double x = Math.min(copy[0], copy[1]);
    double y = Math.max(copy[0], copy[1]);
    double diff = Math.abs(x - y);

    for (int i = 1; i < N; i++) {
      if (Math.abs(copy[i] - copy[i-1]) < diff) {
        x = Math.min(copy[i], copy[i-1]);
        y = Math.max(copy[i], copy[i-1]);

        if (x == y) {
          break;
        }
      }
    }

    return new Pair<Double>(x, y);
  }

  public static void main(String[] args) {
    In in = new In();
    double[] a = in.readAllDoubles();
    System.out.println(findClosestPair(a));
  }
}
