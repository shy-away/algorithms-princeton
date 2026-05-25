package ch4_graphs.undirected_graphs;

public class MoviesAnalyzer {
  public static void main(String[] args) {
    SymbolGraph sg = new SymbolGraph("movies.txt", "/");
    Graph G = sg.G();
    CC cc = new CC(G);

    int[] components = new int[cc.count()];
    for (int x = 0; x < G.V(); x++) {
      components[cc.id(x)]++;
    }

    // for (int size : components) {
    // System.out.print(size + " ");
    // }
    // System.out.println();

    int largestComponentId = 0;
    int largestComponentSize = 0;
    int underTenVertices = 0;

    for (int i = 0; i < components.length; i++) {
      int size = components[i];

      if (size < 10)
        underTenVertices++;

      if (size > largestComponentSize) {
        largestComponentId = i;
        largestComponentSize = size;
      }
    }

    System.out.println("Found largest component");

    // create new subgraph of largest component
    int[] subgraphVertexNameMap = new int[G.V()];
    int subgraphVertexName = 0;
    for (int i = 0; i < subgraphVertexNameMap.length; i++) {
      if (cc.connected(i, largestComponentId)) {
        subgraphVertexNameMap[i] = subgraphVertexName++;
      } else {
        subgraphVertexNameMap[i] = -1;
      }
    }
    
    Graph sG = new Graph(largestComponentSize);

    for (int v = 0; v < G.V(); v++) {
      // check if each vertex is in the largest component
      if (cc.id(v) == largestComponentId) {
        // if so, copy vertex edges from original graph
        for (int w : G.adj(v)) {
          sG.addEdge(subgraphVertexNameMap[v], subgraphVertexNameMap[w]);
        }
      }
    }

    System.out.println("Created subgraph");

    GraphProperties props = new GraphProperties(sG);

    System.out.println("Number of connected components: " + cc.count());
    System.out.println("Size of largest component: " + largestComponentSize);
    System.out.println("Number of components with fewer than ten vertices: " + underTenVertices);

    System.out.println();

    System.out.println("Largest component stats:");
    System.out.println("Diameter: " + props.diameter());
    System.out.println("Radius: " + props.radius());
    System.out.println("Center: " + props.center());
    System.out.println("Girth: " + props.girth());

    System.out.println();

    System.out.println("Contains 'Bacon, Kevin'? " + (cc.id(sg.index("Bacon, Kevin")) == largestComponentId));
  }
}
