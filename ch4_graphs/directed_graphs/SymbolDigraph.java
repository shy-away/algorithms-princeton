package ch4_graphs.directed_graphs;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;

// identical to SymbolGraph for undirected graphs
// Graph is just replaced by Digraph
public class SymbolDigraph {
  private ST<String, Integer> st;
  private String[] keys;
  private Digraph G;

  public SymbolDigraph(String filename, String delim) {
    st = new ST<>();
    In in = new In(filename);

    /* Create symbol table */

    while (in.hasNextLine()) {
      // read each line and split by given delimiter
      String[] a = in.readLine().split(delim);

      // process each substring
      for (int i = 0; i < a.length; i++) {
        // if substring isn't in symbol table,
        if (!st.contains(a[i]))
          // add it, using current symbol table size as value/ID
          st.put(a[i], st.size());
      }
    }

    /* Create keys array */

    // initialize keys using final symbol table size
    keys = new String[st.size()];

    // put each name in the index of its ID
    for (String name : st.keys()) {
      keys[st.get(name)] = name;
    }

    /* Create graph of associations */

    // initialize digraph using final symbol table size
    G = new Digraph(st.size());

    // re-read file to make associations
    in = new In(filename);

    while (in.hasNextLine()) {
      // read each line and split by given delimiter
      String[] a = in.readLine().split(delim);

      // get vertex associated with *first* element
      int v = st.get(a[0]);

      // point from current vertex to all vertices of later elements
      for (int i = 1; i < a.length; i++) {
        G.addEdge(v, st.get(a[i]));
      }
    }
  }

  public boolean contains(String key) {
    return st.contains(key);
  }

  public int index(String key) {
    return st.get(key);
  }

  public String name(int v) {
    return keys[v];
  }

  public Digraph G() {
    return G;
  }
}
