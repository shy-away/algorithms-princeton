package ch2_sorting;

import edu.princeton.cs.algs4.StdRandom;

public class DisplaySorts {
  public static void main(String[] args) {
    int N = Integer.parseInt(args[0]);
    String alg = args[1];

    Double[] a = new Double[N];

    for (int i = 0; i < N; i++) {
      a[i] = StdRandom.uniformDouble();
    }

    new Sorter(true).sortUsing(a, alg);
  }
}
