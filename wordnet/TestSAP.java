import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;

public class TestSAP {
  public static void main(String[] args) {
    Digraph G = new Digraph(new In(args[0]));
    SAP sap = new SAP(G);

    System.out.println(G);

    System.out.println("Enter two vertices to see SAP statistics. Ctrl+D to exit.");

    while (!StdIn.isEmpty()) {
      int v = StdIn.readInt();
      int w = StdIn.readInt();

      int length = sap.length(v, w);
      int ancestor = sap.ancestor(v, w);

      System.out.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
  }
}
