package ch1_fundamentals.basic_programming_model;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

public class RandomConns {
  public static void main(String[] args) {
    int N = Integer.parseInt(args[0]);        // number of points
    double p = Double.parseDouble(args[1]);   // connection probability
    int ms = 10;                              // milliseconds to pause for animation

    double circumference = 2 * Math.PI;
    double increment = circumference / N;

    Point2D[] points = new Point2D[N];

    StdDraw.setPenRadius(0.01);
    StdDraw.setPenColor(StdDraw.BLACK);

    double curr = 0.0;
    double x;
    double y;
    for (int i = 0; i < N; i++) {
      x = 0.5 + (0.5 * Math.cos(curr));
      y = 0.5 + (0.5 * Math.sin(curr));

      points[i] = new Point2D(x, y);

      StdDraw.point(x, y);
      StdDraw.pause(ms);

      curr += increment;
    }

    StdDraw.setPenRadius(0.002);
    StdDraw.setPenColor(StdDraw.RED);

    for (int i = 0; i < N; i++) {
      for (int j = i + 1; j < N; j++) {
        if (StdRandom.bernoulli(p)) {
          StdDraw.line(points[i].x(), points[i].y(), points[j].x(), points[j].y());
          StdDraw.pause(ms);
        }
      }
    }
  }
}
