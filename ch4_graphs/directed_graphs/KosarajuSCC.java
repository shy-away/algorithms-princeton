package ch4_graphs.directed_graphs;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

// nearly identical to CC for undirected graphs
// uses Kosaraju algorithm to determine strong connectivity
// SCC = strongly connected component
public class KosarajuSCC {
  private boolean[] marked;
  private int[] id;
  private int count;

  public KosarajuSCC(Digraph G) {
    marked = new boolean[G.V()];
    id = new int[G.V()]; // represents SCC IDs for each vertex

    // get orders of the digraph's reverse
    DepthFirstOrder order = new DepthFirstOrder(G.reverse());

    count = 0;

    // normal DFS on original digraph, but process vertices
    // in reverse postorder of the digraph's reverse (!)
    for (int s : order.reversePost()) {
      // process all unmarked vertices connected to s using DFS
      if (!marked[s]) {
        dfsIterative(G, s); // mark and ID all SCCs
        count++; // increment count for new SCC ID
      }
    }
  }

  /**
   * Run DFS to mark and ID all strongly connected vertices.
   * 
   * @deprecated Causes stack overflow for large graphs. Use
   *             {@link #dfsIterative()} instead.
   * @param G The graph to search
   * @param v The vertex to search from
   */
  @Deprecated
  @SuppressWarnings("unused")
  private void dfs(Digraph G, int v) {
    // mark vertex
    marked[v] = true;

    // set vertex component ID to current count of SCCs
    id[v] = count;

    // recursively mark and ID all reachable vertices
    for (int w : G.adj(v)) {
      if (!marked[w]) {
        dfs(G, w);
      }
    }
  }

  /**
   * Run iterative DFS to mark and ID all strongly connected vertices.
   * 
   * @param G The graph to search
   * @param v The vertex to search from
   */
  private void dfsIterative(Digraph G, int v) {
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

  public boolean stronglyConnected(int v, int w) {
    return id[v] == id[w];
  }

  public int count() {
    return count;
  }

  public int id(int v) {
    return id[v];
  }

  @SuppressWarnings("unchecked")
  public static void main(String[] args) {
    Digraph DG = new Digraph(new In(args[0]));
    KosarajuSCC cc = new KosarajuSCC(DG);

    Queue<Integer>[] components = (Queue<Integer>[]) new Queue[cc.count()];
    for (int i = 0; i < components.length; i++) {
      components[i] = new Queue<>();
    }
    
    for (int v = 0; v < DG.V(); v++) {
      components[cc.id(v)].enqueue(v);
    }

    System.out.println("Number of SCCs: " + cc.count());

    System.out.println("Vertices in each SCC:");
    for (int v = 0; v < components.length; v++) {
      System.out.print(v + ": ");

      for (int w : components[v]) {
        System.out.print(w + " ");
      }

      System.out.println();
    }
  }
}
