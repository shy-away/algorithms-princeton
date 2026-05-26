package ch4_graphs.directed_graphs;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;

public class Digraph {
  private final int V;
  private int E;
  private Bag<Integer>[] adj;

  @SuppressWarnings("unchecked")
  public Digraph(int V) {
    this.V = V;

    adj = (Bag<Integer>[]) new Bag[V];
    for (int v = 0; v < V; v++) {
      adj[v] = new Bag<>();
    }
  }

  public Digraph(In in) {
    this(in.readInt());
    int E = in.readInt();

    for (int i = 0; i < E; i++) {
      int v = in.readInt();
      int w = in.readInt();
      addEdge(v, w);
    }
  }

  public int V() {
    return V;
  }

  public int E() {
    return E;
  }

  public void addEdge(int v, int w) {
    // no self-loops
    if (v == w)
      return;

    adj[v].add(w);
    E++;
  }

  public Iterable<Integer> adj(int v) {
    return adj[v];
  }

  public Digraph reverse() {
    Digraph R = new Digraph(V);

    for (int v = 0; v < V; v++) {
      for (int w : adj(v)) {
        // for each edge of each vertex, add its inverse
        R.addEdge(w, v);
      }
    }

    return R;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(V + " vertices, " + E + " edges\n");

    for (int v = 0; v < V; v++) {
      sb.append(v + ": ");

      for (int w : adj(v)) {
        sb.append(w + " ");
      }

      sb.append("\n");
    }

    return sb.toString();
  }

  public static void main(String[] args) {
    In inputGraph = new In(args[0]);

    Digraph dg = new Digraph(inputGraph);

    System.out.println(dg.toString());
  }
}
