
/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

  private final int x; // x-coordinate of this point
  private final int y; // y-coordinate of this point

  /**
   * Initializes a new point.
   *
   * @param x the <em>x</em>-coordinate of the point
   * @param y the <em>y</em>-coordinate of the point
   */
  public Point(int x, int y) {
    /* DO NOT MODIFY */
    this.x = x;
    this.y = y;
  }

  /**
   * Draws this point to standard draw.
   */
  public void draw() {
    /* DO NOT MODIFY */
    StdDraw.point(x, y);
  }

  /**
   * Draws the line segment between this point and the specified point
   * to standard draw.
   *
   * @param that the other point
   */
  public void drawTo(Point that) {
    /* DO NOT MODIFY */
    StdDraw.line(this.x, this.y, that.x, that.y);
  }

  /**
   * Returns the slope between this point and the specified point.
   * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
   * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
   * +0.0 if the line segment connecting the two points is horizontal;
   * Double.POSITIVE_INFINITY if the line segment is vertical;
   * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
   *
   * @param that the other point
   * @return the slope between this point and the specified point
   */
  public double slopeTo(Point that) {
    if (this.x == that.x) {
      if (this.y == that.y)
        return Double.NEGATIVE_INFINITY;
      return Double.POSITIVE_INFINITY;
    } else if (this.y == that.y)
      return +0.0;

    return (that.y - this.y) / (double) (that.x - this.x);
  }

  /**
   * Compares two points by y-coordinate, breaking ties by x-coordinate.
   * Formally, the invoking point (x0, y0) is less than the argument point
   * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
   *
   * @param that the other point
   * @return the value <tt>0</tt> if this point is equal to the argument
   *         point (x0 = x1 and y0 = y1);
   *         a negative integer if this point is less than the argument
   *         point; and a positive integer if this point is greater than the
   *         argument point
   */
  public int compareTo(Point that) {
    if (this.y < that.y)
      return -1;
    if (this.y > that.y)
      return 1;
    if (this.x < that.x)
      return -1;
    if (this.x > that.x)
      return 1;
    return 0;
  }

  /**
   * Compares two points by the slope they make with this point.
   * The slope is defined as in the slopeTo() method.
   *
   * @return the Comparator that defines this ordering on points
   */
  public Comparator<Point> slopeOrder() {
    return new Comparator<Point>() {
      public int compare(Point a, Point b) {
        return Double.compare(slopeTo(a), slopeTo(b));
      }
    };
  }

  /**
   * Returns a string representation of this point.
   * This method is provide for debugging;
   * your program should not rely on the format of the string representation.
   *
   * @return a string representation of this point
   */
  public String toString() {
    /* DO NOT MODIFY */
    return "(" + x + ", " + y + ")";
  }

  /**
   * Unit tests the Point data type.
   */
  public static void main(String[] args) {
    Point a, b, c;
    int testX = 2, testY = 3;
    a = new Point(testX, testY);

    // compareTo()
    b = new Point(testX, testY + 1);
    assert a.compareTo(b) < 0 : "compareTo greater y";

    b = new Point(testX, testY - 1);
    assert a.compareTo(b) > 0 : "compareTo smaller y";

    b = new Point(testX + 1, testY);
    assert a.compareTo(b) < 0 : "compareTo greater x";

    b = new Point(testX - 1, testY);
    assert a.compareTo(b) > 0 : "compareTo smaller x";

    b = new Point(testX, testY);
    assert a.compareTo(b) == 0 : "compareTo equal";

    // slopeTo()
    b = new Point(testX + 1, testY + 1);
    assert a.slopeTo(b) == 1 : "slopeTo positive, x+ y+";

    b = new Point(testX - 1, testY + 1);
    assert a.slopeTo(b) == -1 : "slopeTo negative, x- y+";

    b = new Point(testX - 1, testY - 1);
    assert a.slopeTo(b) == 1 : "slopeTo positive, x- y-";

    b = new Point(testX + 1, testY - 1);
    assert a.slopeTo(b) == -1 : "slopeTo negative, x+ y-";

    b = new Point(testX + 1, testY);
    assert a.slopeTo(b) == +0.0 : "slopeTo horizontal";

    b = new Point(testX, testY + 1);
    assert a.slopeTo(b) == Double.POSITIVE_INFINITY : "slopeTo vertical";

    b = new Point(testX, testY);
    assert a.slopeTo(b) == Double.NEGATIVE_INFINITY : "slopeTo equal";

    b = new Point(testX + 2, testY + 1);
    System.out.println(a.slopeTo(b));
    assert a.slopeTo(b) == 0.5 : "slopeTo fractions";

    // comparator
    b = new Point(testX + 2, testY);
    c = new Point(testX + 1, testY - 1);
    Comparator<Point> comp = c.slopeOrder();
    assert comp.compare(a, b) < 0 : "comparator negative slope to positive slope";
    assert comp.compare(b, a) > 0 : "comparator negative slope to positive slope";

    b = new Point(testX - 1, testY + 1);
    assert comp.compare(a, b) == 0 : "comparator equal slope";
    assert comp.compare(b, a) == 0 : "comparator equal slope (order agnostic)";

    assert comp.compare(a, c) > 0 : "comparator against comparator's own point";

    System.out.println("All tests pass");
  }
}
