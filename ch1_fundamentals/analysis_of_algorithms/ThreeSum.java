package ch1_fundamentals.analysis_of_algorithms;

import java.util.Arrays;

import ch1_fundamentals.basic_programming_model.BinarySearch;
import edu.princeton.cs.algs4.In;

public class ThreeSum {
  public static int count(int[] a) {
    int N = a.length;
    int count = 0;
    for (int i = 0; i < N; i++) {
      for (int j = i+1; j < N; j++) {
        for (int k = j+1; k < N; k++) {
          if (a[i] + a[j] + a[k] == 0) count++;
        }
      }
    }

    return count;
  }

  public static int countFast(int[] a) {
    Arrays.sort(a);
    int N = a.length;
    int count = 0;

    for (int i = 0; i < N; i++) {
      for (int j = i+1; j < N; j++) {
        if (BinarySearch.rank(-a[i]-a[j], a) > j) {
          count++;
        }
      }
    }

    return count;
  }

  public static void main(String[] args) {
    In in = new In();
    int[] a = in.readAllInts();
    System.out.println(count(a));
  }
}
