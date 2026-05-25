package ch4_graphs.undirected_graphs;

import edu.princeton.cs.algs4.StdIn;

public class DegreesOfSeparation {
  public static void main(String[] args) {
    SymbolGraph sg = new SymbolGraph(args[0], args[1]);

    Graph G = sg.G();

    String source = args[2];
    if (!sg.contains(source)) {
      System.out.println(source + " not in database.");
       return;
    }

    int s = sg.index(source);
    BreadthFirstPaths bfs = new BreadthFirstPaths(G, s);

    System.out.println("Symbol graph constructed. Enter a symbol to find its adjacent vertices. Ctrl+C to exit.");

    while (!StdIn.isEmpty()) {
      String sink = StdIn.readLine();
      if (sg.contains(sink)) {
        int t = sg.index(sink);
        if (bfs.hasPathTo(t)) {
          for (int v : bfs.pathTo(t)) {
            System.out.println("\t" + sg.name(v));
          }
        }
      } else {
        System.out.println("Not in database.");
      }
    }
  }
}
