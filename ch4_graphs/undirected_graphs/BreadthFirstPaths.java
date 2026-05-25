package ch4_graphs.undirected_graphs;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

public class BreadthFirstPaths {
  private boolean[] marked;
  private int[] edgeTo;
  private int[] distTo;
  private final int s;

  public BreadthFirstPaths(Graph G, int s) {
    marked = new boolean[G.V()];
    edgeTo = new int[G.V()];
    distTo = new int[G.V()];
    this.s = s;
    bfs(G, s);
  }

  private void bfs(Graph G, int s) {
    // use queue instead of stack (call stack in DFS)
    Queue<Integer> queue = new Queue<>();

    // process starting location
    marked[s] = true;
    distTo[s] = 0;
    queue.enqueue(s);

    while (!queue.isEmpty()) {
      // get least-recently visited node
      int v = queue.dequeue();

      // process and enqueue all unmarked adjacent nodes
      for (int w : G.adj(v)) {
        if (!marked[w]) {
          edgeTo[w] = v;

          distTo[w] = distTo[v] + 1;
          // Note: distTo[v] is never null because v is from the queue,
          // and all vertices on the queue have been processed (starting with s).

          marked[w] = true;
          queue.enqueue(w);
        }
      }
    }
  }

  public boolean hasPathTo(int v) {
    // same as DFS hasPathTo
    return marked[v];
  }

  public Iterable<Integer> pathTo(int v) {
    // same as DFS

    if (!hasPathTo(v))
      return null;

    Stack<Integer> path = new Stack<>();
    for (int x = v; x != s; x = edgeTo[x]) {
      path.push(x);
    }
    path.push(s);

    return path;
  }

  public int distTo(int v) {
    return distTo[v];
  }

  public static void main(String[] args) {
    // only test distTo()

    Graph G = new Graph(new In(args[0]));
    int start = Integer.parseInt(args[1]);
    BreadthFirstPaths bfp = new BreadthFirstPaths(G, start);

    for (int v = 0; v < G.V(); v++) {
      System.out.print(v + ": ");

      if (bfp.hasPathTo(v)) {
        System.out.println(bfp.distTo(v));
      } else {
        System.out.println("Not connected");
      }
    }
  }
}
