package ch1_fundamentals.analysis_of_algorithms;

import edu.princeton.cs.algs4.StdDraw;

public class ThrowEggs {
  static int numThrows;

  public static int findFloorBruteForce(Building b) {
    numThrows = 0;
    int currentFloor = 0;
    boolean cracked = false;

    while (!cracked) {
      cracked = b.throwEgg(currentFloor);
      numThrows++;
      if (!cracked) {
        currentFloor++;
      }
    }

    return currentFloor;
  }

  public static int findFloorLgN(Building b) {
    numThrows = 0;

    if (b.floors() == 0)
      return 0;

    int hi = b.floors();
    int lo = 0;
    int mid = 0;

    while (lo < hi) {
      mid = (lo + hi) / 2;

      numThrows++;
      if (b.throwEgg(mid)) {
        // egg cracked, search below (including mid)
        hi = mid;
      } else {
        // egg didn't crack, search above (excluding mid)
        lo = mid + 1;
      }
    }

    return hi;
  }

  public static int findFloorLgF(Building b) {
    numThrows = 0;

    if (b.floors() == 0)
      return 0;

    int currentFloor = 1;
    boolean cracked = false;

    while (!cracked && currentFloor <= b.floors()) {
      cracked = b.throwEgg(currentFloor);
      numThrows++;
      if (!cracked) {
        currentFloor *= 2;
      }
    }

    int hi = Math.min(currentFloor, b.floors());
    int lo = currentFloor / 2;
    int mid = 0;

    while (lo < hi) {
      mid = (lo + hi) / 2;

      numThrows++;
      if (b.throwEgg(mid)) {
        // egg cracked, search below
        hi = mid;
      } else {
        // egg didn't crack, search above
        lo = mid + 1;
      }
    }

    return hi;
  }

  public static int findFloorTwoEggsSqrtN(Building b) {
    numThrows = 0;

    if (b.floors() == 0)
      return 0;

    // chunk building into sqrt(b.floors()) sections
    int currentFloor = 0;
    int chunkSize = Math.max(
        (int) Math.sqrt(b.floors()),
        1);

    // check every floor at beginning of each chunk (starting with lowest chunk)
    boolean crackedFirst = false;
    while (!crackedFirst && currentFloor <= b.floors()) {
      crackedFirst = b.throwEgg(currentFloor);
      numThrows++;
      if (!crackedFirst) {
        currentFloor += chunkSize;
      }
    }

    // once finding first chunk where the egg cracks,
    // check previous chunk one floor at a time
    int topBoundary = Math.min(currentFloor, b.floors() + 1);
    currentFloor = Math.max(0, currentFloor - chunkSize + 1);
    boolean crackedSecond = false;
    while (!crackedSecond && currentFloor < topBoundary) {
      crackedSecond = b.throwEgg(currentFloor);
      numThrows++;
      if (!crackedSecond) {
        currentFloor++;
      }
    }

    return currentFloor;
  }

  public static int findFloorTwoEggsSqrtF(Building b) {
    numThrows = 0;

    if (b.floors() == 0)
      return 0;

    int base = 0;
    int currentFloor = 0;

    // search every floor at increasing squares
    boolean crackedFirst = false;
    while (!crackedFirst && currentFloor <= b.floors()) {
      crackedFirst = b.throwEgg(currentFloor);
      numThrows++;
      if (!crackedFirst) {
        base++;
        currentFloor = base * base;
      }
    }

    // if base didn't increase, the egg cracked on the first floor (floor 0)
    if (base == 0)
      return 0;

    // search previous chunk

    int topBoundary = Math.min(currentFloor, b.floors() + 1);
    base--;
    currentFloor = (base * base) + 1;
    boolean crackedSecond = false;

    while (!crackedSecond && currentFloor < topBoundary) {
      crackedSecond = b.throwEgg(currentFloor);
      numThrows++;
      if (!crackedSecond) {
        currentFloor++;
      }
    }

    return currentFloor;
  }

  public static void main(String[] args) {
    int maxFloors = Integer.parseInt(args[0]);

    StdDraw.setCanvasSize(800, 800);
    StdDraw.clear(StdDraw.BLACK);
    StdDraw.setXscale(0.0, (double) maxFloors);
    StdDraw.setYscale(0.0, 20 * Math.log(maxFloors));
    StdDraw.setPenRadius(0.005);

    // brute force
    // StdDraw.setYscale(0.0, (double) maxFloors / 2.0);
    StdDraw.setPenColor(StdDraw.RED);
    for (int floors = 0; floors <= maxFloors; floors++) {
      int throwSum = 0;
      for (int i = 0; i <= floors; i++) {
        Building b = new Building(floors, i);
        int f = findFloorBruteForce(b);
        assert f == i : "Returned value " + f + " not equal to " + i + ". Floors in current building: " + floors;

        throwSum += numThrows;
      }

      double averageThrows = (double) throwSum / (double) floors;

      try {
        StdDraw.point((double) floors, averageThrows);
      } catch (Exception e) {
        // ignore all exceptions from StdDraw
      }
    }

    // lg(N)
    // StdDraw.setYscale(0.0, 5 * Math.log(maxFloors));
    StdDraw.setPenColor(StdDraw.YELLOW);
    for (int floors = 0; floors <= maxFloors; floors++) {
      int throwSum = 0;
      for (int i = 0; i <= floors; i++) {
        Building b = new Building(floors, i);
        int f = findFloorLgN(b);
        assert f == i : "Returned value " + f + " not equal to " + i + ". Floors in current building: " + floors;

        throwSum += numThrows;
      }

      double averageThrows = (double) throwSum / (double) floors;

      try {
        StdDraw.point((double) floors, averageThrows);
      } catch (Exception e) {
        // ignore all exceptions from StdDraw
      }
    }

    // lg(F)
    // StdDraw.setYscale(0.0, 5 * Math.log(maxFloors));
    StdDraw.setPenColor(StdDraw.LIME);
    int[] trialsPerFloor = new int[maxFloors + 1];
    double[] avgThrowsPerFloor = new double[maxFloors + 1];

    for (int floors = 0; floors <= maxFloors; floors++) {
      int throwSum = 0;
      for (int i = 0; i <= floors; i++) {
        Building b = new Building(floors, i);
        int f = findFloorLgF(b);
        assert f == i : "Returned value " + f + " not equal to " + i + ". Floors in current building: " + floors;

        throwSum += numThrows;

        avgThrowsPerFloor[i] = (avgThrowsPerFloor[i] * (trialsPerFloor[i]) + numThrows) / (trialsPerFloor[i] + 1);
        trialsPerFloor[i]++;
      }

      double averageThrows = (double) throwSum / (double) floors;

      try {
        StdDraw.point((double) floors, averageThrows);
      } catch (Exception e) {
        // ignore all exceptions from StdDraw
      }
    }

    StdDraw.setPenColor(StdDraw.GREEN);
    for (int i = 0; i < avgThrowsPerFloor.length; i++) {
      StdDraw.point((double) i, avgThrowsPerFloor[i]);
    }

    // two eggs, sqrt(N)
    // StdDraw.setYscale(0.0, 3 * Math.sqrt(maxFloors));
    StdDraw.setPenColor(StdDraw.CYAN);
    for (int floors = 0; floors <= maxFloors; floors++) {
      int throwSum = 0;
      for (int i = 0; i <= floors; i++) {
        Building b = new Building(floors, i);
        int f = findFloorTwoEggsSqrtN(b);
        assert f == i : "Returned value " + f + " not equal to " + i + ". Floors in current building: " + floors;

        throwSum += numThrows;
      }

      double averageThrows = (double) throwSum / (double) floors;

      try {
        StdDraw.point((double) floors, averageThrows);
      } catch (Exception e) {
        // ignore all exceptions from StdDraw
      }
    }

    // two eggs, sqrt(F)
    // StdDraw.setYscale(0.0, 10 * Math.log(maxFloors));
    StdDraw.setPenColor(StdDraw.MAGENTA);
    trialsPerFloor = new int[maxFloors + 1];
    avgThrowsPerFloor = new double[maxFloors + 1];

    for (int floors = 0; floors <= maxFloors; floors++) {
      int throwSum = 0;
      for (int i = 0; i <= floors; i++) {
        Building b = new Building(floors, i);
        int f = findFloorTwoEggsSqrtF(b);
        assert f == i : "Returned value " + f + " not equal to " + i + ". Floors in current building: " + floors;

        throwSum += numThrows;

        avgThrowsPerFloor[i] = (avgThrowsPerFloor[i] * (trialsPerFloor[i]) + numThrows) / (trialsPerFloor[i] + 1);
        trialsPerFloor[i]++;
      }

      double averageThrows = (double) throwSum / (double) floors;

      try {
        StdDraw.point((double) floors, averageThrows);
      } catch (Exception e) {
        // ignore all exceptions from StdDraw
      }
    }

    StdDraw.setPenColor(StdDraw.PURPLE);
    for (int i = 0; i < avgThrowsPerFloor.length; i++) {
      StdDraw.point((double) i, avgThrowsPerFloor[i]);
    }
  }
}
