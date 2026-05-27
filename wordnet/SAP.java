import java.util.Arrays;
import java.util.HashSet;
import java.util.function.BiConsumer;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdRandom;

public class SAP {
  private final Digraph G;
  private Queue<Integer> queue;
  private final Queue<Integer> changed;
  private final int[] distTo;
  private final boolean[] marked;

  public SAP(Digraph G) {
    validateNotNull(G);

    // defensive copy
    this.G = new Digraph(G);

    this.queue = new Queue<>();

    // data in these objects is mutable, but the objects
    // themselves will never be recreated
    changed = new Queue<>();
    distTo = new int[G.V()];
    marked = new boolean[G.V()];
  }

  private class ParallelBFSResults {
    public final int length;
    public final int ancestor;

    public ParallelBFSResults(int length, int ancestor) {
      this.length = length;
      this.ancestor = ancestor;
    }
  }

  private ParallelBFSResults parallelBFS(Iterable<Integer> v, Iterable<Integer> w) {
    // if any vertex is in both iterables, the BFS can be skipped
    HashSet<Integer> set = new HashSet<>();

    for (int vVx : v)
      set.add(vVx);

    for (int wVx : w)
      if (set.contains(wVx))
        return new ParallelBFSResults(0, wVx);

    // init result vars
    int minLength = G.E() + 1;
    int ancestor = -1;

    // compute shortest paths from any vertex in v
    BreadthFirstDirectedPaths vBfs = new BreadthFirstDirectedPaths(G, v);

    BiConsumer<Integer, Integer> processVx = (vx, dist) -> {
      marked[vx] = true;
      distTo[vx] = dist;
      changed.enqueue(vx);
      queue.enqueue(vx);
    };

    for (int wVx : w) {
      // if vBfs has a path directly to any vertex in w,
      // it must be included as a candidate SAP
      if (vBfs.hasPathTo(wVx)) {
        int candidatePathLength = vBfs.distTo(wVx);

        if (candidatePathLength < minLength) {
          ancestor = wVx;
          minLength = candidatePathLength;
          // must continue searching -- a shorter path may still be available
        }
      }

      // initialize each vertex in w
      processVx.accept(wVx, 0);
    }

    // parallel BFS on all vertices in w
    while (!queue.isEmpty()) {
      int currWVx = queue.dequeue();

      for (int nextWVx : G.adj(currWVx)) {
        if (!marked[nextWVx]) {
          int nextDist = distTo[currWVx] + 1;
          processVx.accept(nextWVx, nextDist);

          // stop search if it goes beyond known minimum length
          if (nextDist > minLength) {
            break;
          }

          // determine whether next vertex is an ancestor on a SAP
          if (vBfs.hasPathTo(nextWVx)) {
            int candidatePathLength = vBfs.distTo(nextWVx) + nextDist;

            if (candidatePathLength < minLength) {
              ancestor = nextWVx;
              minLength = candidatePathLength;
            }
          }
        }
      }
    }

    // clean up search variables
    queue = new Queue<>();
    while (!changed.isEmpty()) {
      int changedVx = changed.dequeue();
      marked[changedVx] = false;
      distTo[changedVx] = 0;
    }

    return ancestor >= 0 ? new ParallelBFSResults(minLength, ancestor) : null;
  }

  public int length(int v, int w) {
    validateNotNull(v);
    validateNotNull(w);

    ParallelBFSResults results = parallelBFS(Arrays.asList(v), Arrays.asList(w));

    return results != null ? results.length : -1;
  }

  public int ancestor(int v, int w) {
    validateNotNull(v);
    validateNotNull(w);

    ParallelBFSResults results = parallelBFS(Arrays.asList(v), Arrays.asList(w));

    return results != null ? results.ancestor : -1;
  }

  public int length(Iterable<Integer> v, Iterable<Integer> w) {
    validateNotNull(v);
    validateNotNull(w);

    for (int x : v)
      validateNotNull(x);

    for (int x : w)
      validateNotNull(x);

    ParallelBFSResults results = parallelBFS(v, w);

    return results != null ? results.length : -1;
  }

  public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
    validateNotNull(v);
    validateNotNull(w);

    for (int x : v)
      validateNotNull(x);

    for (int x : w)
      validateNotNull(x);

    ParallelBFSResults results = parallelBFS(v, w);

    return results != null ? results.ancestor : -1;
  }

  private void validateNotNull(Object x) {
    if (x == null)
      throw new IllegalArgumentException();
  }

  public static void main(String[] args) {
    SAP sap;
    Digraph G;
    boolean hasErred;

    /* Null arguments */

    hasErred = false;
    try {
      sap = new SAP(null);
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    G = new Digraph(0);
    sap = new SAP(G);

    hasErred = false;
    try {
      sap.length(Arrays.asList(0), null);
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    hasErred = false;
    try {
      sap.length(null, Arrays.asList(0));
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    hasErred = false;
    try {
      sap.ancestor(Arrays.asList(0), null);
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    hasErred = false;
    try {
      sap.ancestor(null, Arrays.asList(0));
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    /* Single vertex */

    G = new Digraph(1);
    sap = new SAP(G);

    assert sap.length(0, 0) == 0;
    assert sap.ancestor(0, 0) == 0;
    assert sap.length(Arrays.asList(0), Arrays.asList(0)) == 0;
    assert sap.ancestor(Arrays.asList(0), Arrays.asList(0)) == 0;

    /* Two unconnected vertices */

    G = new Digraph(2);
    sap = new SAP(G);

    assert sap.length(0, 1) == -1;
    assert sap.ancestor(0, 1) == -1;
    assert sap.length(Arrays.asList(0), Arrays.asList(1)) == -1;
    assert sap.ancestor(Arrays.asList(0), Arrays.asList(1)) == -1;

    assert sap.length(Arrays.asList(0, 1), Arrays.asList(1)) == 0;
    assert sap.ancestor(Arrays.asList(0, 1), Arrays.asList(1)) == 1;

    /* Two weakly connected vertices */

    G = new Digraph(2);
    G.addEdge(0, 1);
    sap = new SAP(G);

    assert sap.length(0, 1) == 1;
    assert sap.ancestor(0, 1) == 1;
    assert sap.length(Arrays.asList(0), Arrays.asList(1)) == 1;
    assert sap.ancestor(Arrays.asList(0), Arrays.asList(1)) == 1;

    /* Two strongly connected vertices */

    G = new Digraph(2);
    G.addEdge(0, 1);
    G.addEdge(1, 0);
    sap = new SAP(G);

    assert sap.length(0, 1) == 1;
    assert sap.ancestor(0, 1) != -1;
    assert sap.length(Arrays.asList(0), Arrays.asList(1)) == 1;
    assert sap.ancestor(Arrays.asList(0), Arrays.asList(1)) != -1;

    /* Three-vertex two-edge rooted DAG */

    G = new Digraph(3);
    G.addEdge(0, 1);
    G.addEdge(2, 1);
    sap = new SAP(G);

    assert sap.length(1, 0) == 1;
    assert sap.length(2, 1) == 1;
    assert sap.ancestor(1, 0) == 1;
    assert sap.ancestor(2, 1) == 1;

    assert sap.length(0, 2) == 2;
    assert sap.ancestor(0, 2) == 1;

    /* Three-vertex three-edge rooted DAG */

    G = new Digraph(3);
    G.addEdge(0, 1);
    G.addEdge(0, 2);
    G.addEdge(2, 1);
    sap = new SAP(G);

    assert sap.length(1, 0) == 1;
    assert sap.length(2, 1) == 1;
    assert sap.ancestor(1, 0) == 1;
    assert sap.ancestor(2, 1) == 1;

    assert sap.length(0, 2) == 1;
    assert sap.ancestor(0, 2) == 2;

    /* Three-vertex cyclic */

    G = new Digraph(3);
    G.addEdge(0, 1);
    G.addEdge(1, 2);
    G.addEdge(2, 0);
    sap = new SAP(G);

    assert sap.length(0, 1) == 1;
    assert sap.ancestor(0, 1) == 1;
    assert sap.length(1, 2) == 1;
    assert sap.ancestor(1, 2) == 2;
    assert sap.length(2, 0) == 1;
    assert sap.ancestor(2, 0) == 0;

    /* Random trials */

    int T = 100;
    int Tv = 50; // number of vertex set trials per trial
    int numVertices = 50;
    int numEdges = 50;

    int[] vertices = new int[numVertices];
    for (int i = 0; i < vertices.length; i++) {
      vertices[i] = i;
    }

    for (int i = 0; i < T; i++) {
      G = new Digraph(numVertices);

      for (int j = 0; j < numEdges; j++) {
        int vxFrom = StdRandom.uniformInt(numVertices);
        int vxTo = StdRandom.uniformInt(numVertices);

        if (vxFrom != vxTo)
          G.addEdge(vxFrom, vxTo);
      }

      sap = new SAP(G);

      // pairs of vertices
      for (int vx1 = 0; vx1 < G.V(); vx1++) {
        for (int vx2 = 0; vx2 < G.V(); vx2++) {
          if (vx2 == vx1)
            continue;

          // simple brute-force method to determine
          // SAP between vx1 and vx2 (if any exists)
          BreadthFirstDirectedPaths vx1Bfs = new BreadthFirstDirectedPaths(G, vx1);
          BreadthFirstDirectedPaths vx2Bfs = new BreadthFirstDirectedPaths(G, vx2);

          boolean hasSAP = false;
          int sapLength = G.E();
          for (int v = 0; v < G.V(); v++) {
            if (vx1Bfs.hasPathTo(v) && vx2Bfs.hasPathTo(v)) {
              hasSAP = true;
              int candidateLength = vx1Bfs.distTo(v) + vx2Bfs.distTo(v);
              if (candidateLength < sapLength) {
                sapLength = candidateLength;
              }
            }
          }

          if (hasSAP) {
            assert sap.length(vx1, vx2) == sapLength;
            assert sap.ancestor(vx1, vx2) != -1;
          } else {
            assert sap.length(vx1, vx2) == -1;
            assert sap.ancestor(vx1, vx2) == -1;
          }
        }
      }

      // vertex sets
      for (int j = 0; j < Tv; j++) {
        int a1 = vertices[StdRandom.uniformInt(vertices.length)];
        int a2 = vertices[StdRandom.uniformInt(vertices.length)];
        int b1 = vertices[StdRandom.uniformInt(vertices.length)];
        int b2 = vertices[StdRandom.uniformInt(vertices.length)];

        Iterable<Integer> a = Arrays.asList(a1, a2);
        Iterable<Integer> b = Arrays.asList(b1, b2);

        BreadthFirstDirectedPaths aBfs = new BreadthFirstDirectedPaths(G, a);
        BreadthFirstDirectedPaths bBfs = new BreadthFirstDirectedPaths(G, b);

        boolean hasSAP = false;
        int sapLength = G.E();
        for (int v = 0; v < G.V(); v++) {
          if (aBfs.hasPathTo(v) && bBfs.hasPathTo(v)) {
            hasSAP = true;
            int candidateLength = aBfs.distTo(v) + bBfs.distTo(v);
            if (candidateLength < sapLength) {
              sapLength = candidateLength;
            }
          }
        }

        if (hasSAP) {
          assert sap.length(a, b) == sapLength;
          assert sap.ancestor(a, b) != -1;
        } else {
          assert sap.length(a, b) == -1;
          assert sap.ancestor(a, b) == -1;
        }
      }
    }

    System.out.println("All tests pass");
  }
}
