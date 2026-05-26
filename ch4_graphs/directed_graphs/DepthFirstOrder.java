package ch4_graphs.directed_graphs;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

public class DepthFirstOrder {
  private boolean[] marked;
  private int count;

  private Queue<Integer> pre;
  private Queue<Integer> post;
  private Stack<Integer> reversePost;

  public DepthFirstOrder(Digraph G) {
    marked = new boolean[G.V()];

    pre = new Queue<>();
    post = new Queue<>();
    reversePost = new Stack<>();

    for (int v = 0; v < G.V(); v++) {
      if (!marked[v])
        dfs(G, v);
    }
  }

  private void dfs(Digraph G, int v) {
    pre.enqueue(v);

    marked[v] = true;
    count++;

    for (int w : G.adj(v)) {
      if (!marked[w])
        dfs(G, w);
    }

    post.enqueue(v);
    reversePost.push(v);
  }

  public boolean marked(int v) {
    return marked[v];
  }

  public int count() {
    return count;
  }

  public Iterable<Integer> pre() {
    return pre;
  }

  public Iterable<Integer> post() {
    return post;
  }

  public Iterable<Integer> reversePost() {
    return reversePost;
  }

  public static void main(String[] args) {
    Digraph DG = new Digraph(new In(args[0]));
    DepthFirstOrder search = new DepthFirstOrder(DG);

    System.out.println("Preorder:");
    for (int v : search.pre()) {
      System.out.print(v + " ");
    }
    System.out.println();

    System.out.println("Postorder:");
    for (int v : search.post()) {
      System.out.print(v + " ");
    }
    System.out.println();

    System.out.println("Reverse postorder:");
    for (int v : search.reversePost()) {
      System.out.print(v + " ");
    }
    System.out.println();

    System.out.println((DG.V() - search.count()) + " vertices are unreachable");
  }
}
