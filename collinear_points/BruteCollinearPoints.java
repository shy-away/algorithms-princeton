import edu.princeton.cs.algs4.Queue;

public class BruteCollinearPoints {
  private LineSegment[] segments;

  public BruteCollinearPoints(Point[] points) {
    if (points == null)
      throw new IllegalArgumentException("Points array cannot be null");

    Queue<LineSegment> foundSegments = new Queue<>();

    // search all permutations of points
    for (int p = 0; p < points.length; p++) {
      if (points[p] == null)
        throw new IllegalArgumentException("Null point in input");

      for (int q = 0; q < points.length; q++) {
        if (q == p)
          continue;

        if (points[p].compareTo(points[q]) == 0)
          throw new IllegalArgumentException("Duplicate points in input");

        for (int r = 0; r < points.length; r++) {
          if (r == p || r == q)
            continue;

          for (int s = 0; s < points.length; s++) {
            if (s == p || s == q || s == r)
              continue;

            double slopeToB = points[p].slopeTo(points[q]);
            double slopeToC = points[p].slopeTo(points[r]);
            double slopeToD = points[p].slopeTo(points[s]);

            // if all slopes are equal, and points are in ascending order, add new segment
            if ((slopeToB == slopeToC && slopeToC == slopeToB) &&
                points[p].compareTo(points[q]) < 0 && points[q].compareTo(points[r]) < 0
                && points[r].compareTo(points[s]) < 0) {
              foundSegments.enqueue(new LineSegment(points[p], points[s]));
            }
          }
        }
      }
    }

    segments = new LineSegment[foundSegments.size()];
    for (int i = 0; i < segments.length; i++) {
      segments[i] = foundSegments.dequeue();
    }
  }

  public int numberOfSegments() {
    return segments.length;
  }

  public LineSegment[] segments() {
    return segments;
  }

  public static void main(String[] args) {
    BruteCollinearPoints bcp;
    Point[] points;
    boolean hasErred;

    hasErred = false;
    try {
      bcp = new BruteCollinearPoints(null);
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred : "IllegalArgumentException for null input";

    points = new Point[0];
    bcp = new BruteCollinearPoints(points);
    assert bcp.numberOfSegments() == 0 : "no points: numberOfSegments is zero";
    assert bcp.segments().length == 0 : "no points: length of segments array is zero";

    points = new Point[1];
    points[0] = null;
    hasErred = false;
    try {
      bcp = new BruteCollinearPoints(points);
    } catch (Exception e) {
      hasErred = true;
    }
    assert hasErred : "IllegalArgumentException for any null point";

    points = new Point[2];
    points[0] = new Point(0, 0);
    points[1] = new Point(0, 0);
    hasErred = false;
    try {
      bcp = new BruteCollinearPoints(points);
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred : "IllegalArgumentException for duplicate points in input";

    points = new Point[4];
    for (int i = 0; i < 4; i++) {
      points[i] = new Point(i, i);
    }
    bcp = new BruteCollinearPoints(points);
    assert bcp.numberOfSegments() == 1 && bcp.segments().length == 1 : "four points: find one segment";
    assert bcp.segments()[0].toString().equals("(0, 0) -> (3, 3)") : "four points: specific segment";

    System.out.println("All tests pass");
  }
}
