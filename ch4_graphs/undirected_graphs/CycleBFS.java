package ch4_graphs.undirected_graphs;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

public class CycleBFS {
  private boolean[] marked;
  private boolean hasCycle;
  private int[] prevVx, distTo;
  private int length;

  /**
   * Determine the shortest cycle from the given vertex, if one exists.
   * 
   * The given vertex may or may not be in the found cycle.
   * 
   * @param G The graph to search
   * @param s The vertex to search from
   */
  public CycleBFS(Graph G, int s) {
    hasCycle = false;
    length = 0;

    marked = new boolean[G.V()];
    prevVx = new int[G.V()];
    distTo = new int[G.V()];

    // BFS from vertex s
    Queue<Integer> queue = new Queue<>();

    marked[s] = true;
    queue.enqueue(s);
    prevVx[s] = -1;
    distTo[s] = 0;

    while (!queue.isEmpty()) {
      int currVx = queue.dequeue();

      for (int nextVx : G.adj(currVx)) {
        if (!marked[nextVx]) {
          prevVx[nextVx] = currVx;
          distTo[nextVx] = distTo[currVx] + 1;

          marked[nextVx] = true;
          queue.enqueue(nextVx);
        } else if (nextVx != prevVx[currVx]) {
          hasCycle = true;
          length = distTo[currVx] + distTo[nextVx] + 1;
          return;
        }
      }
    }
  }

  public boolean hasCycle() {
    return hasCycle;
  }

  public int length() {
    return length;
  }

  public static void main(String[] args) {
    Graph G = new Graph(new In(args[0]));
    int s = Integer.parseInt(args[1]);
    CycleBFS c = new CycleBFS(G, s);

    if (c.hasCycle()) {
      System.out.println("Cycle present");
      System.out.println("Length: " + c.length());
    } else {
      System.out.println("No cycle present");
    }
  }
}
