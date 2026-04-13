import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Note: The main method of this class was provided by Princeton.
 * https://coursera.cs.princeton.edu/algs4/assignments/collinear/specification.php
 */

public class CollinearPointsClient {
  public static void main(String[] args) {

    // read the n points from a file
    In in = new In(args[0]);
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
      int x = in.readInt();
      int y = in.readInt();
      points[i] = new Point(x, y);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
      p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    Stopwatch s = new Stopwatch();
    FastCollinearPoints collinear = new FastCollinearPoints(points);
    // BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    System.out.println(collinear.numberOfSegments() + " segment(s) found in " + s.elapsedTime() + " seconds");
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }
}
