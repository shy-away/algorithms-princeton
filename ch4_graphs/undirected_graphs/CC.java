package ch4_graphs.undirected_graphs;

import edu.princeton.cs.algs4.Stack;

// stands for "connected components"
public class CC {
  private boolean[] marked;
  private int[] id;
  private int count;

  public CC(Graph G) {
    marked = new boolean[G.V()];
    id = new int[G.V()]; // represents component IDs for each vertex

    count = 0;
    for (int s = 0; s < G.V(); s++) {
      // process all unmarked vertices connected to s using DFS
      if (!marked[s]) {
        dfsIterative(G, s); // mark and ID all connected components
        count++; // increment count for new component ID
      }
    }
  }

  /**
   * Run DFS to mark and ID all connected vertices.
   * 
   * @deprecated Causes stack overflow for large graphs. Use
   *             {@link #dfsIterative()} instead.
   * @param G The graph to search
   * @param v The vertex to search from
   */
  @Deprecated
  private void dfs(Graph G, int v) {
    // mark vertex
    marked[v] = true;

    // set vertex component ID to current count of components
    id[v] = count;

    // recursively mark and ID all adjacent vertices
    for (int w : G.adj(v)) {
      if (!marked[w]) {
        dfs(G, w);
      }
    }
  }

  /**
   * Run iterative DFS to mark and ID all connected vertices.
   * 
   * @param G The graph to search
   * @param v The vertex to search from
   */
  private void dfsIterative(Graph G, int v) {
    Stack<Integer> stack = new Stack<>();
    
    stack.push(v);

    while (!stack.isEmpty()) {
      int currVx = stack.pop();

      marked[currVx] = true;
      id[currVx] = count;

      for (int nextVx : G.adj(currVx)) {
        if (!marked[nextVx])
          stack.push(nextVx);
      }
    }
  }

  public boolean connected(int v, int w) {
    return id[v] == id[w];
  }

  public int count() {
    return count;
  }

  public int id(int v) {
    return id[v];
  }
}
