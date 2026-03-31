package ch1_fundamentals.analysis_of_algorithms;

import edu.princeton.cs.algs4.StdRandom;

public class GenerateDoubles {
  public static void main(String[] args) {
    int MAX = 1000000;

    int N = Integer.parseInt(args[0]);
    for (int i = 0; i < N; i++) {
      System.out.println(StdRandom.uniformDouble(-MAX, MAX));
    }
  }
}
