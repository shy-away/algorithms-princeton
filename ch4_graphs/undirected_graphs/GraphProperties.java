package ch4_graphs.undirected_graphs;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

public class GraphProperties {
  private boolean[] marked;
  private int[] prevVx, distTo;
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

    // graph must be connected
    if (new CC(G).count() > 1)
      throw new IllegalArgumentException("Graph must be connected");

    eccentricity = new int[G.V()];

    diameter = 0;
    radius = Integer.MAX_VALUE;
    // center will be initialized in search loop
    girth = Integer.MAX_VALUE;

    // run BFS from each vertex
    for (int v = 0; v < G.V(); v++) {
      // System.out.println(v);
      marked = new boolean[G.V()];
      prevVx = new int[G.V()];
      distTo = new int[G.V()];

      int cycleLength = Integer.MAX_VALUE;
      boolean foundCycle = false;

      Queue<Integer> queue = new Queue<>();

      marked[v] = true;
      prevVx[v] = -1;
      distTo[v] = 0;
      queue.enqueue(v);
      int currVx = v;

      while (!queue.isEmpty()) {
        currVx = queue.dequeue();

        for (int nextVx : G.adj(currVx)) {
          if (!marked[nextVx]) {
            prevVx[nextVx] = currVx;
            distTo[nextVx] = distTo[currVx] + 1;

            marked[nextVx] = true;
            queue.enqueue(nextVx);
          } else if (nextVx != prevVx[currVx] && !foundCycle) {
            // cycle found
            cycleLength = distTo[currVx] + distTo[nextVx] + 1;
            foundCycle = true;
          }
        }
      }

      // currVx is the last vertex dequeued, which must be
      // furthest from the start because of BFS mechanics
      int maxDist = distTo[currVx];

      // set eccentricity
      eccentricity[v] = maxDist;

      // is this the diameter?
      if (maxDist > diameter)
        diameter = maxDist;

      // is this a radius?
      if (maxDist < radius) {
        radius = maxDist;
        center = v;
      }

      // is the found cycle length lower than the previously recorded girth?
      if (cycleLength < girth)
        girth = cycleLength;
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