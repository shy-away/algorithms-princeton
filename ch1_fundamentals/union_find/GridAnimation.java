package ch1_fundamentals.union_find;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class GridAnimation {
  public static void main(String[] args) {
    int N = Integer.parseInt(args[0]);
    int gridSize = N * N;

    Connection[] connections = RandomGrid.generate(N);

    StdDraw.setCanvasSize(800, 800);
    StdDraw.setScale(-(N * .05), N + (N * .05) - 1);
    StdDraw.clear(StdDraw.BLACK);

    // draw grid
    StdDraw.setPenRadius(0.015);
    StdDraw.setPenColor(StdDraw.WHITE);
    for (int i = 0; i < gridSize; i++) {
      StdDraw.point(i % N, (double) (i / N));
    }

    // connect points in UF and display connections
    StdDraw.setPenColor(StdDraw.RED);
    StdDraw.setPenRadius(0.005);

    WeightedQuickUnionUF uf = new WeightedQuickUnionUF(N * N);
    for (Connection c : connections) {
      int p = c.getP();
      int q = c.getQ();

      if (uf.find(p) == uf.find(q))
        continue;

      uf.union(p, q);
      StdDraw.line(p % N, p / N, q % N, q / N);
    }
  }
}
