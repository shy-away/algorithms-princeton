import java.util.Arrays;
import java.util.Iterator;

import edu.princeton.cs.algs4.SET;

public class FastCollinearPoints {
  private LineSegment[] segments;

  public FastCollinearPoints(Point[] points) {
    if (points == null)
      throw new IllegalArgumentException("Points array cannot be null");

    class LineSegmentComparable implements Comparable<LineSegmentComparable> {
      public final LineSegment segment;

      public LineSegmentComparable(LineSegment segment) {
        this.segment = segment;
      }

      public int compareTo(LineSegmentComparable that) {
        return this.segment.toString().compareTo(that.segment.toString());
      }
    }

    SET<LineSegmentComparable> foundSegments = new SET<>();

    Point[] pointsCopy = new Point[points.length];
    for (int i = 0; i < points.length; i++) {
      if (points[i] == null)
        throw new IllegalArgumentException("Null point in input");

      pointsCopy[i] = points[i];
    }

    for (int i = 0; i < points.length; i++) {
      Point currentPoint = points[i];

      Arrays.sort(pointsCopy, currentPoint.slopeOrder());

      // if second value in pointsCopy is negative infinity,
      // there must be a second point equal to currentPoint (duplicate)
      if (currentPoint.slopeTo(pointsCopy[1]) == Double.NEGATIVE_INFINITY)
        throw new IllegalArgumentException("Duplicate points in input");

      // impossible to find segment in array with too few points
      // must perform this check *after* validation
      if (points.length < 4) {
        segments = new LineSegment[0];
        return;
      }

      // slide a window through pointsCopy to find window with at least three points
      // that make the same slope with currentPoint
      // note: first point in pointsCopy is always currentPoint
      double s1 = currentPoint.slopeTo(pointsCopy[1]);
      double s2 = currentPoint.slopeTo(pointsCopy[2]);
      double s3;
      for (int j = 3; j < pointsCopy.length; j++) {
        s3 = currentPoint.slopeTo(pointsCopy[j]);

        if (s1 == s2 && s2 == s3) {
          // TODO: check for more than four collinear points
          Point[] collinears = { pointsCopy[j - 2], pointsCopy[j - 1], pointsCopy[j], currentPoint };

          Arrays.sort(collinears);

          foundSegments
              .add(new LineSegmentComparable(new LineSegment(collinears[0], collinears[collinears.length - 1])));
        }

        s1 = s2;
        s2 = s3;
      }
    }

    segments = new LineSegment[foundSegments.size()];
    Iterator<LineSegmentComparable> iterator = foundSegments.iterator();
    int i = 0;
    while (iterator.hasNext()) {
      segments[i] = iterator.next().segment;
      i++;
    }
  }

  public int numberOfSegments() {
    return segments.length;
  }

  public LineSegment[] segments() {
    return segments;
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
    } catch (Exception e) {
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
    assert fcp.numberOfSegments() == 2;

    System.out.println("All tests pass");
  }
}
