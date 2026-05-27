import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
  private WordNet wordnet;

  public Outcast(WordNet wordnet) {
    this.wordnet = wordnet;
  }

  public String outcast(String[] nouns) {
    // Note: this method assumes all nouns are in the
    // wordnet, and that at least two nouns are given

    int maxDistance = 0;
    String outcast = "";

    for (int i = 0; i < nouns.length; i++) {
      String currentNoun = nouns[i];
      int currentNounDistance = 0;

      for (int j = 0; j < nouns.length; j++) {
        if (i == j)
          continue;

        currentNounDistance += wordnet.distance(currentNoun, nouns[j]);
      }

      if (currentNounDistance > maxDistance) {
        maxDistance = currentNounDistance;
        outcast = currentNoun;
      }
    }

    return outcast;
  }

  public static void main(String[] args) {
    WordNet wordnet = new WordNet(args[0], args[1]);
    Outcast outcast = new Outcast(wordnet);
    for (int t = 2; t < args.length; t++) {
      In in = new In(args[t]);
      String[] nouns = in.readAllStrings();
      StdOut.println(args[t] + ": " + outcast.outcast(nouns));
    }
  }
}
