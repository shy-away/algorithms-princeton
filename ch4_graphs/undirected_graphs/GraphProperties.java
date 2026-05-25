package ch4_graphs.undirected_graphs;

import edu.princeton.cs.algs4.In;

public class GraphProperties {
  private int diameter, radius, center;
  private int[] eccentricity;

  public GraphProperties(Graph G) {
    eccentricity = new int[G.V()];
    diameter = 0;
    radius = Integer.MAX_VALUE;
    // center will be initialized in search loop

    // run BFS from each vertex
    for (int v = 0; v < G.V(); v++) {
      BreadthFirstPaths bfp = new BreadthFirstPaths(G, v);

      // of all connected vertices, get distance to the furthest
      int maxDist = 0;
      for (int w = 0; w < G.V(); w++) {
        if (bfp.hasPathTo(w)) {
          maxDist = Math.max(bfp.distTo(w), maxDist);
        }
      }

      // set eccentricity
      eccentricity[v] = maxDist;

      // is this a diameter?
      if (maxDist > diameter)
        diameter = maxDist;

      // is this a radius?
      if (maxDist < radius) {
        radius = maxDist;
        center = v;
      }
    }
  }

  public int eccentricity(int v) {
    return eccentricity[v];
  }

  public int diameter() {
    return diameter;
  }

  public int radius() {
    return radius;
  }

  public int center() {
    return center;
  }

  public static void main(String[] args) {
    Graph G = new Graph(new In(args[0]));
    GraphProperties props = new GraphProperties(G);

    System.out.println("Graph:");
    System.out.println(G.toString());
    System.out.println();

    System.out.println("Eccentricity of all points:");
    for (int v = 0; v < G.V(); v++) {
      System.out.println(v + ": " + props.eccentricity(v));
    }
    System.out.println();

    System.out.println("Diameter: " + props.diameter());
    System.out.println("Radius: " + props.radius());
    System.out.println("Center: " + props.center());
  }
}