import java.util.Arrays;
import java.util.Iterator;

import edu.princeton.cs.algs4.Queue;

public class FastCollinearPoints {
  private LineSegment[] segments;

  public FastCollinearPoints(Point[] points) {
    if (points == null)
      throw new IllegalArgumentException("Points array cannot be null");

    Queue<LineSegment> foundSegments = new Queue<>();

    Point[] pointsCopy = new Point[points.length];
    for (int i = 0; i < points.length; i++) {
      if (points[i] == null)
        throw new IllegalArgumentException("Null point in input");

      pointsCopy[i] = points[i];
    }

    // separate logic for point arrays less than 4
    if (points.length < 4) {
      for (int i = 0; i < points.length; i++) {
        for (int j = i + 1; j < points.length; j++) {
          if (points[i].compareTo(points[j]) == 0)
            throw new IllegalArgumentException("Duplicate points in input");
        }
      }

      segments = new LineSegment[0];
      return;
    }

    for (int i = 0; i < points.length; i++) {
      Point currentPoint = points[i];

      Arrays.sort(pointsCopy, currentPoint.slopeOrder());

      // if second value in pointsCopy is negative infinity,
      // there must be a second point equal to currentPoint (duplicate)
      if (currentPoint.slopeTo(pointsCopy[1]) == Double.NEGATIVE_INFINITY)
        throw new IllegalArgumentException("Duplicate points in input");

      // slide a window through pointsCopy to find window with at least three points
      // that make the same slope with currentPoint
      // note: first point in pointsCopy is always currentPoint
      double s1 = currentPoint.slopeTo(pointsCopy[1]);
      double s2 = currentPoint.slopeTo(pointsCopy[2]);
      double s3;
      for (int j = 3; j < pointsCopy.length; j++) {
        s3 = currentPoint.slopeTo(pointsCopy[j]);

        if (s1 == s2 && s2 == s3) {
          // check for more than four collinear points
          int lcp = j; // last collinear point
          while (lcp < pointsCopy.length && currentPoint.slopeTo(pointsCopy[lcp]) == s3) {
            lcp++;
          }
          lcp--;

          // the three found collinear points, plus currentPoint, plus any points found by
          // the search for more than four collinear points
          int numCollinears = 4 + (lcp - j);

          Point[] collinears = new Point[numCollinears];

          boolean willAddSegment = true;

          // numCollinears - 1 because of currentPoint
          for (int m = 0; m < numCollinears - 1; m++) {
            collinears[m] = pointsCopy[(j - 2) + m];

            // only add segment if the least point is currentPoint
            // prevents duplication
            if (collinears[m].compareTo(currentPoint) < 0) {
              willAddSegment = false;
            }
          }

          if (willAddSegment) {
            collinears[collinears.length - 1] = currentPoint;

            Arrays.sort(collinears);

            LineSegment newSegment = new LineSegment(collinears[0], collinears[collinears.length - 1]);
            foundSegments.enqueue(newSegment);
          }

          j = lcp;
        }

        s1 = s2;
        s2 = s3;
      }
    }

    segments = new LineSegment[foundSegments.size()];
    Iterator<LineSegment> iterator = foundSegments.iterator();
    int i = 0;
    while (iterator.hasNext()) {
      segments[i] = iterator.next();
      i++;
    }
  }

  public int numberOfSegments() {
    return segments.length;
  }

  public LineSegment[] segments() {
    return Arrays.copyOf(segments, segments.length);
  }

  public static void main(String[] args) {
    FastCollinearPoints fcp;
    Point[] points;
    boolean hasErred;

    hasErred = false;
    try {
      fcp = new FastCollinearPoints(null);
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred : "IllegalArgumentException for null input";

    points = new Point[0];
    fcp = new FastCollinearPoints(points);
    assert fcp.numberOfSegments() == 0 : "no points: numberOfSegments is zero";
    assert fcp.segments().length == 0 : "no points: length of segments array is zero";

    points = new Point[1];
    points[0] = null;
    hasErred = false;
    try {
      fcp = new FastCollinearPoints(points);
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred : "IllegalArgumentException for any null point";

    points = new Point[2];
    points[0] = new Point(0, 0);
    points[1] = new Point(0, 0);
    hasErred = false;
    try {
      fcp = new FastCollinearPoints(points);
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred : "IllegalArgumentException for duplicate points in input";

    points = new Point[2];
    points[0] = new Point(0, 1);
    points[1] = new Point(1, 2);
    fcp = new FastCollinearPoints(points);
    assert fcp.numberOfSegments() == 0 && fcp.segments().length == 0
        : "less than four points: still functions, finds zero segments";

    points = new Point[4];
    for (int i = 0; i < 4; i++) {
      points[i] = new Point(i, i);
    }
    fcp = new FastCollinearPoints(points);
    assert fcp.numberOfSegments() == 1 && fcp.segments().length == 1 : "four points: find one segment";
    assert fcp.segments()[0].toString().equals("(0, 0) -> (3, 3)") : "four points: specific segment";

    points = new Point[7];
    points[0] = new Point(0, 0);
    for (int i = 1; i < 4; i++) {
      points[i] = new Point(i, i);
    }
    for (int i = 4; i < 7; i++) {
      points[i] = new Point((i - 3), -(i - 3) * 2);
    }
    fcp = new FastCollinearPoints(points);
    assert fcp.numberOfSegments() == 2 : "two segments";

    points = new Point[5];
    for (int i = 0; i < points.length; i++) {
      points[i] = new Point(i * 2, i);
    }
    fcp = new FastCollinearPoints(points);
    assert fcp.numberOfSegments() == 1 : "five points: one segment";

    System.out.println("All tests pass");
  }
}
