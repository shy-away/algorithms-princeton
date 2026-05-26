package ch4_graphs.directed_graphs;

import edu.princeton.cs.algs4.In;

public class DirectedDFS {
  private boolean[] marked;
  private int count;

  public DirectedDFS(Digraph G, int s) {
    marked = new boolean[G.V()];
    dfs(G, s);
  }

  // constructor for multiple sources
  public DirectedDFS(Digraph G, Iterable<Integer> sources) {
    marked = new boolean[G.V()];

    for (int s : sources) {
      // if a source is marked, a previous DFS already searched it
      if (!marked[s])
        dfs(G, s);
    }
  }

  private void dfs(Digraph G, int v) {
    marked[v] = true;
    count++;

    for (int w : G.adj(v)) {
      if (!marked[w])
        dfs(G, w);
    }
  }

  public boolean marked(int v) {
    return marked[v];
  }

  public int count() {
    return count;
  }

  public static void main(String[] args) {
    Digraph DG = new Digraph(new In(args[0]));
    int s = Integer.parseInt(args[1]);
    DirectedDFS search = new DirectedDFS(DG, s);

    System.out.println("Vertices connected to " + s + ":");
    for (int v = 0; v < DG.V(); v++) {
      if (search.marked(v))
        System.out.print(v + " ");
    }
    System.out.println();

    System.out.println((DG.V() - search.count()) + " vertices are unreachable");
  }
}
