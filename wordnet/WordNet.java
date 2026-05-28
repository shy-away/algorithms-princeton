import java.util.HashSet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycleX;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.ST;

public class WordNet {
  private final HashSet<String> nounSet;
  private final ST<String, SET<Integer>> nounSynsetIdsST;
  private final ST<Integer, String> synsetIdST;
  private final SAP sap;

  public WordNet(String synsets, String hypernyms) {
    validateNotNull(synsets);
    validateNotNull(hypernyms);

    nounSet = new HashSet<>();
    nounSynsetIdsST = new ST<>();
    synsetIdST = new ST<>();

    /* Build synset assocations */

    In synsetsIn = new In(synsets);

    while (synsetsIn.hasNextLine()) {
      String[] fields = synsetsIn.readLine().split(","); // comma-separated

      int synsetId = Integer.parseInt(fields[0]);
      String synset = fields[1];
      String[] synsetNouns = synset.split(" ");

      synsetIdST.put(synsetId, synset); // associate synset ids with synsets

      for (String noun : synsetNouns) {
        nounSet.add(noun); // maintain set of all nouns

        if (!nounSynsetIdsST.contains(noun)) {
          SET<Integer> nounSynsetIdSET = new SET<>();
          nounSynsetIdSET.add(synsetId);

          nounSynsetIdsST.put(noun, nounSynsetIdSET);
        } else {
          nounSynsetIdsST.get(noun).add(synsetId);
        }
      }
    }

    /* Build hypernym graph */

    Digraph G = new Digraph(synsetIdST.size()); // use number of synset IDs

    In hypernymsIn = new In(hypernyms);

    while (hypernymsIn.hasNextLine()) {
      String[] idStrs = hypernymsIn.readLine().split(",");

      int fromVx = Integer.parseInt(idStrs[0]);

      for (int i = 1; i < idStrs.length; i++) {
        int toVx = Integer.parseInt(idStrs[i]);
        G.addEdge(fromVx, toVx);
      }
    }

    /* Hypernym graph validation */

    // validate the graph is acyclic
    if (new DirectedCycleX(G).hasCycle())
      throw new IllegalArgumentException("Graph contains a cycle");

    // validate the graph is rooted

    /*
     * In a digraph, any path that terminates must terminate on a vertex with
     * an outdegree of 0 (there's no vertex to travel to next).
     * 
     * An acyclic digraph (DAG) contains at least one such outdegree-0 vertex,
     * otherwise no paths would exist that do not cycle. Every vertex would have
     * another vertex that can be traveled to next.
     * 
     * A rooted DAG is a DAG where all paths terminate at the same outdegree-0
     * vertex. If a DAG has multiple vertices with an outdegree of 0, then there are
     * multiple places a path may terminate.
     * 
     * So, having validated that the hypernym graph G is acyclic, validating that
     * there is only one outdegree-0 vertex is guaranteed to determine rootedness.
     * 
     * Notably, this implicitly checks reachability. If an outdegree-0 vertex is not
     * reachable by some other vertex v, then there must be a path from v that
     * terminates elsewhere. But that other termination point would be another
     * vertex with an outdegree of 0 anyway.
     */
    int countRoots = 0;
    for (int v = 0; v < G.V(); v++) {
      if (G.outdegree(v) == 0)
        countRoots++;
    }

    if (countRoots != 1)
      throw new IllegalArgumentException("Graph is not a rooted DAG");

    sap = new SAP(G);
  }

  public Iterable<String> nouns() {
    return nounSet;
  }

  public boolean isNoun(String word) {
    validateNotNull(word);
    return nounSet.contains(word);
  }

  public int distance(String nounA, String nounB) {
    validateNotNull(nounA);
    validateNotNull(nounB);

    validateNoun(nounA);
    validateNoun(nounB);

    return sap.length(nounSynsetIdsST.get(nounA), nounSynsetIdsST.get(nounB));
  }

  public String sap(String nounA, String nounB) {
    validateNotNull(nounA);
    validateNotNull(nounB);

    validateNoun(nounA);
    validateNoun(nounB);

    int ancestorSynsetId = sap.ancestor(nounSynsetIdsST.get(nounA), nounSynsetIdsST.get(nounB));
    return synsetIdST.get(ancestorSynsetId);
  }

  private void validateNotNull(Object x) {
    if (x == null)
      throw new IllegalArgumentException("Null argument");
  }

  private void validateNoun(String n) {
    if (!isNoun(n))
      throw new IllegalArgumentException("Noun not in WordNet");
  }

  public static void main(String[] args) {
    WordNet W;
    boolean hasErred;

    /* Null arguments */

    hasErred = false;
    try {
      W = new WordNet(null, "");
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    hasErred = false;
    try {
      W = new WordNet("", null);
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    W = new WordNet("synsets6.txt", "hypernyms6TwoAncestors.txt");

    hasErred = false;
    try {
      W.isNoun(null);
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    hasErred = false;
    try {
      W.distance(null, "");
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    hasErred = false;
    try {
      W.distance("", null);
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    hasErred = false;
    try {
      W.sap(null, "");
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    hasErred = false;
    try {
      W.sap("", null);
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    /* Inputs that are not rooted DAGs */

    hasErred = false;
    try {
      W = new WordNet("synsets3.txt", "hypernyms3InvalidCycle.txt");
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    hasErred = false;
    try {
      W = new WordNet("synsets3.txt", "hypernyms3InvalidTwoRoots.txt");
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    /* Word inputs that are not nouns */

    W = new WordNet("synsets6.txt", "hypernyms6TwoAncestors.txt");

    hasErred = false;
    try {
      W.distance("a", "x");
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    hasErred = false;
    try {
      W.distance("x", "b");
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    hasErred = false;
    try {
      W.sap("c", "x");
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    hasErred = false;
    try {
      W.sap("x", "d");
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    W.sap("e", "f"); // shouldn't cause an error

    /* Small WordNet */

    W = new WordNet("synsets6.txt", "hypernyms6TwoAncestors.txt");

    assert W.isNoun("a");
    assert W.isNoun("b");
    assert W.isNoun("c");
    assert W.isNoun("d");
    assert W.isNoun("e");
    assert W.isNoun("f");
    assert !W.isNoun("g");

    assert W.distance("a", "a") == 0;
    assert W.sap("a", "a").equals("a");

    assert W.distance("a", "b") == 1;
    assert W.sap("a", "b").equals("a");

    assert W.distance("f", "b") == 2;
    assert W.sap("f", "b").equals("a");

    assert W.distance("b", "e") == 3;
    String temp = W.sap("b", "e");
    assert temp.equals("a") || temp.equals("e");

    /* Large WordNet */

    // no errors in constructors
    W = new WordNet("synsets15.txt", "hypernyms15Tree.txt");
    W = new WordNet("synsets100-subgraph.txt", "hypernyms100-subgraph.txt");

    /* Full WordNet */

    W = new WordNet("synsets.txt", "hypernyms.txt");

    assert W.distance("worm", "bird") == 5;
    assert W.sap("worm", "bird").equals("animal animate_being beast brute creature fauna");

    assert W.distance("white_marlin", "mileage") == 23;
    assert W.distance("black_marlin", "Black_Plague") == 33;
    assert W.distance("American_water_spaniel", "histology") == 27;
    assert W.distance("Brown_Swiss", "barrel_roll") == 29;

    System.out.println("All tests pass");
  }
}
