package ch4_graphs.undirected_graphs;

import edu.princeton.cs.algs4.In;

public class GraphProperties {
  private int diameter, radius, center, girth;
  private int[] eccentricity;

  /**
   * Calculate properties of the graph. Constructor is O(<code>G.V()</code>^2),
   * all other operations are constant time.
   * 
   * @param G The graph to analyze
   */
  public GraphProperties(Graph G) {
    if (G == null || G.V() == 0)
      throw new IllegalArgumentException();

    eccentricity = new int[G.V()];
    diameter = 0;
    radius = Integer.MAX_VALUE;
    // center will be initialized in search loop
    girth = Integer.MAX_VALUE;

    // determine ahead of time if the graph has any cycle
    boolean hasCycle = new CycleBFS(G, 0).hasCycle();

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

      // is the cycle from current vertex the shortest cycle?
      if (hasCycle) {
        CycleBFS vc = new CycleBFS(G, v);
        if (vc.hasCycle()) {
          girth = Math.min(girth, vc.length());
        }
      }
    }
  }

  /**
   * Get the eccentricity of the given vertex, defined as the shortest path to the
   * furthest connected vertex.
   * 
   * @param v A vertex in the graph
   * @return The eccentricity of the vertex
   */
  public int eccentricity(int v) {
    return eccentricity[v];
  }

  /**
   * Get the diameter of the graph, defined as the maximum eccentricity of any
   * vertex.
   * 
   * @return The diameter of the graph
   */
  public int diameter() {
    return diameter;
  }

  /**
   * Get the radius of the graph, defined as the minimum eccentricity of any
   * vertex.
   * 
   * @return The radius of the graph
   */
  public int radius() {
    return radius;
  }

  /**
   * Get the center of the graph, which may be any vertex whose eccentricity is
   * the radius.
   * 
   * @return A vertex whose eccentricity is the radius
   */
  public int center() {
    return center;
  }

  /**
   * The girth of a graph is the length of its shortest cycle.
   * 
   * @return The length of the graph's shortest cycle
   */
  public int girth() {
    return girth;
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
    System.out.println("Girth: " + props.girth());
  }
}