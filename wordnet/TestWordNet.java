import edu.princeton.cs.algs4.StdIn;

public class TestWordNet {
  public static void main(String[] args) {
    WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");

    System.out.println("WordNet created. Enter two words to see WordNet statistics. Ctrl+D to exit.");

    while (!StdIn.isEmpty()) {
      String wordA = StdIn.readString();
      String wordB = StdIn.readString();

      if (!wordnet.isNoun(wordA)) {
        System.out.printf("'%s' is not in the WordNet.\n", wordA);
        continue;
      }

      if (!wordnet.isNoun(wordB)) {
        System.out.printf("'%s' is not in the WordNet.\n", wordB);
        continue;
      }

      System.out.printf("length: %d, ancestor: '%s'\n", wordnet.distance(wordA, wordB), wordnet.sap(wordA, wordB));
    }
  }
}
