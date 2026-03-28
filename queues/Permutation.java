import edu.princeton.cs.algs4.StdIn;
import java.util.Iterator;

public class Permutation {
  public static void main(String[] args) {
    int k = Integer.parseInt(args[0]);
    RandomizedQueue<String> rq = new RandomizedQueue<>();

    while (!StdIn.isEmpty()) {
      rq.enqueue(StdIn.readString());
    }

    Iterator<String> it = rq.iterator();
    for (int i = 0; i < k; i++) {
      // Note: k is assumed to be less than rq.size()
      System.out.println(it.next());
    }
  }
}
