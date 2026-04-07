package ch2_sorting;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

@SuppressWarnings("unchecked")
public class Sorter {
  private Double[] cached;
  private boolean drawIfPossible;

  public Sorter() {
    drawIfPossible = false;
  }

  public Sorter(boolean drawIfPossible) {
    this.drawIfPossible = drawIfPossible;
  }

  public void sortUsing(Comparable[] a, String alg) {
    try {
      Method sortMethod = Sorter.class.getMethod(alg.toLowerCase(), Comparable[].class);
      sortMethod.invoke(this, (Object) a);
    } catch (NoSuchMethodException e) {
      System.out.println("No such sort method: " + alg);

      System.out.println("Options: ");
      Method[] methods = Sorter.class.getMethods();
      for (Method m : methods) {
        if (Modifier.isPublic(m.getModifiers())
            && m.getReturnType() == void.class
            && m.getParameterTypes().length == 1
            && m.getParameterTypes()[0] == Comparable[].class
            && m.getDeclaringClass() == Sorter.class) {
          System.out.println(" - " + m.getName());
        }
      }

      System.exit(0);
    } catch (Exception e) {
      System.out.println("Error invoking sort method: " + e.getMessage());
    }
  }

  public void selection(Comparable[] a) {
    boolean isDrawing = drawIfPossible && a instanceof Double[];

    // Repeatedly select the smallest remaining item
    int N = a.length;
    for (int i = 0; i < N; i++) {
      int min = i;
      for (int j = i + 1; j < N; j++) {
        if (less(a[j], a[min])) {
          min = j;
        }
      }

      exch(a, i, min);
      if (isDrawing)
        draw((Double[]) a);
    }

    cached = null;
  }

  public void insertion(Comparable[] a) {
    boolean isDrawing = drawIfPossible && a instanceof Double[];

    // Insert next item into sorted array section
    int N = a.length;
    for (int i = 1; i < N; i++) {
      // "Bubble down" the current item
      for (int j = i; j > 0 && less(a[j], a[j - 1]); j--) {
        exch(a, j, j - 1);
        if (isDrawing)
          draw((Double[]) a);
      }
    }

    cached = null;
  }

  public void shell(Comparable[] a) {
    boolean isDrawing = drawIfPossible && a instanceof Double[];

    // h-sort for decreasing h
    int N = a.length;
    int h = 1;

    while (h < N / 3) {
      h = 3 * h + 1;
    }

    while (h >= 1) {
      for (int i = h; i < N; i++) {
        // Insertion sort "bubble down" at h intervals
        for (int j = i; j >= h && less(a[j], a[j - h]); j -= h) {
          exch(a, j, j - h);
          if (isDrawing)
            draw((Double[]) a);
        }
      }

      h = h / 3;
    }

    cached = null;
  }

  private static void exch(Comparable[] a, int i, int j) {
    Comparable t = a[i];
    a[i] = a[j];
    a[j] = t;
  }

  private static boolean less(Comparable v, Comparable w) {
    return v.compareTo(w) < 0;
  }

  private static void show(Comparable[] a) {
    for (Comparable c : a) {
      StdOut.print(c + " ");
    }
    StdOut.println();
  }

  /**
   * Draw an array of doubles. Caches the array for faster drawing.
   * The input array is assumed to be an array of doubles from 0.0 to 1.0.
   * 
   * @param a The array to be drawn.
   */
  private void draw(Double[] a) {
    double barWidth = 1.0 / a.length;

    if (cached == null) {
      StdDraw.clear();
      cached = new Double[a.length];

      for (int i = 0; i < a.length; i++) {
        StdDraw.filledRectangle(
            (i * barWidth) + (barWidth / 2),
            a[i] / 2,
            (barWidth / 2),
            a[i] / 2);

        cached[i] = a[i];
      }

      StdDraw.pause(500);

      return;
    }

    for (int i = 0; i < a.length; i++) {
      if (a[i].compareTo(cached[i]) != 0) {
        StdDraw.setPenColor(StdDraw.getBackgroundColor());
        StdDraw.filledRectangle(
            (i * barWidth) + (barWidth / 2),
            0.5,
            (barWidth / 2) + (1.0 / 3200),
            0.5);

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledRectangle(
            (i * barWidth) + (barWidth / 2),
            a[i] / 2,
            (barWidth / 2),
            a[i] / 2);

        cached[i] = a[i];
      }
    }

    StdDraw.pause(2000 / a.length);
  }

  public static boolean isSorted(Comparable[] a) {
    for (int i = 1; i < a.length; i++) {
      if (less(a[i], a[i - 1]))
        return false;
    }
    return true;
  }

  public static void main(String[] args) {
    String[] a = new In().readAllStrings();

    // new Sorter().selection(a);
    // new Sorter().insertion(a);
    new Sorter().shell(a);
    assert isSorted(a);

    show(a);
  }
}
