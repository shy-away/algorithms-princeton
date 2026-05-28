package ch4_graphs.directed_graphs;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

public class DirectedCycle {
  private boolean[] marked;
  private int[] edgeTo;
  private Stack<Integer> cycle;
  private boolean[] onStack;

  public DirectedCycle(Digraph G) {
    onStack = new boolean[G.V()];
    edgeTo = new int[G.V()];
    marked = new boolean[G.V()];

    for (int v = 0; v < G.V(); v++) {
      if (!marked[v])
        dfs(G, v);
    }
  }

  private void dfs(Digraph G, int v) {
    // dynamically track which vertices are on the call stack
    onStack[v] = true;

    marked[v] = true;

    for (int w : G.adj(v)) {
      // early return if a cycle has been found
      if (hasCycle())
        return;

      if (!marked[w]) {
        // explore all tails of current head vertex
        edgeTo[w] = v;
        dfs(G, w);
      } else if (onStack[w]) {
        // if a previous vertex from the current path
        // is encountered, a cycle has been found
        cycle = new Stack<>();

        // push current vertex and all previous vertices in the path,
        // but only until the vertex where the cycle is known to complete!
        for (int x = v; x != w; x = edgeTo[x]) {
          cycle.push(x);
        }

        // push vertex at which current path cycles
        cycle.push(w);

        // push current vertex to complete the cycle
        cycle.push(v);
      }
    }

    // track vertex coming off the call stack
    onStack[v] = false;
  }

  public boolean hasCycle() {
    return cycle != null;
  }

  public Iterable<Integer> cycle() {
    return cycle;
  }

  public static void main(String[] args) {
    Digraph DG = new Digraph(new In(args[0]));
    DirectedCycle dc = new DirectedCycle(DG);

    System.out.println("Has cycle? " + dc.hasCycle());
    if (dc.hasCycle()) {
      System.out.println("Cycle vertices:");

      for (int v : dc.cycle()) {
        System.out.print(v + " ");
      }
      System.out.println();
    }
  }
}
