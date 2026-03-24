package ch1_fundamentals.data_abstraction;

import edu.princeton.cs.algs4.Interval1D;
import edu.princeton.cs.algs4.Interval2D;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Interval2DClient {
  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    double min = Double.parseDouble(args[1]);
    double max = Double.parseDouble(args[2]);

    Interval2D[] arr = new Interval2D[n];

    for (int i = 0; i < n; i++) {
      double width = StdRandom.uniformDouble(min, max);
      double x = StdRandom.uniformDouble(0.0, (1.0 - width));

      double height = StdRandom.uniformDouble(min, max);
      double y = StdRandom.uniformDouble(0.0, (1.0 - height));

      Interval1D xInterval1d = new Interval1D(x, x + width);
      Interval1D yInterval1d = new Interval1D(y, y + height);

      Interval2D combined = new Interval2D(xInterval1d, yInterval1d);

      arr[i] = combined;

      combined.draw();
    }

    int numContains = 0;
    int numIntersects = 0;

    for (int i = 0; i < n; i++) {
      for (int j = i + 1; j < n; j++) {
        if (arr[i].contains(arr[j])) {
          numContains++;
        }

        if (arr[i].intersects(arr[j])) {
          numIntersects++;
        }
      }
    }

    StdOut.println("Containments: " + numContains);
    StdOut.println("Intersections: " + numIntersects);
  }
}