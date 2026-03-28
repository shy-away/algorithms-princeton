package ch1_fundamentals.bags_queues_stacks;

import java.util.Iterator;

/**
 * A generic, iterable stack implementation that uses
 * a dynamically resizing array.
 */
@SuppressWarnings("unchecked") // annotation to ignore the Item[] cast
public class ResizingArrayStack<Item> implements Iterable<Item> {
  private Item[] a = (Item[]) new Object[1];
  private int N = 0;

  /*
   * No constructor necessary. Java automatically uses a constructor
   * with instance variables initialzed to the above values.
   * 
   * If the construction of an object requires other operations,
   * an explicit constructor would be necessary.
   */

  public boolean isEmpty() {
    return N == 0;
  }

  public int size() {
    return N;
  }

  private void resize(int newSize) {
    Item[] temp = (Item[]) new Object[newSize];

    for (int i = 0; i < N; i++) {
      temp[i] = a[i];
    }

    a = temp;
  }

  public void push(Item item) {
    if (N == a.length) {
      resize(2 * a.length);
    }

    a[N++] = item;
  }

  public Item pop() {
    Item item = a[--N];
    a[N] = null;

    if (N > 0 && N == (a.length / 4)) {
      resize(a.length / 2);
    }

    return item;
  }

  public Iterator<Item> iterator() {
    return new ReverseArrayIterator();
  }

  private class ReverseArrayIterator implements Iterator<Item> {
    private int i = N;

    public boolean hasNext() {
      return i > 0;
      /*
       * Should technically throw a `NoSuchElementException` when i == 0,
       * but this doesn't matter here because ResizingArrayStack will
       * only be used as an iterator in foreach loops.
       */
    }

    public Item next() {
      return a[--i];
    }

    public void remove() {
      throw new UnsupportedOperationException();
      /*
       * Modifying the data structure while iterating is, according to
       * Sedgewick and Wayne, "best avoided." They don't explicitly throw
       * this error, but it conforms to what is technically supposed to
       * happen according to Java specifications.
       */
    }
  }

  public static void main(String[] args) {
    ResizingArrayStack<Integer> s1 = new ResizingArrayStack<Integer>();

    assert s1.isEmpty() && s1.size() == 0 : "Initial size should be 0, i.e. empty";

    s1.push(3);
    s1.push(12);
    s1.push(-33);
    s1.push(5);

    assert s1.size() == 4 : "Size should increase as items are pushed";

    assert s1.pop() == 5 : "LIFO retrieval";

    assert s1.size() == 3 : "Size sould decrease as items are popped";

    ResizingArrayStack<String> s2 = new ResizingArrayStack<String>();

    s2.push("Sphinx");

    assert s2.pop() == "Sphinx" : "Generic class should allow any type";

    s2.push("of");
    s2.push("black");
    s2.pop();
    s2.push("quartz,");
    s2.push("judge");
    s2.push("my");
    s2.pop();
    s2.push("vow.");

    ResizingArrayStack<String> s3 = new ResizingArrayStack<String>();

    for (String str : s2) {
      // System.out.println(str);
      s3.push(str);
    }

    assert s3.size() == 4 : "Iterator functionality in foreach loop";

    assert s3.pop() == "of" &&
        s3.pop() == "quartz," &&
        s3.pop() == "judge" &&
        s3.pop() == "vow."
        : "Reversed order in iterator";

    System.out.println("All tests pass");
  }
}
