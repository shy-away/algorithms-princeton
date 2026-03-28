package ch1_fundamentals.bags_queues_stacks;

import java.util.Iterator;

/**
 * Pushdown stack, implemented using a linked list.
 */
public class Stack<Item> implements Iterable<Item> {
  private Node first;
  private int N;

  private class Node {
    Item item;
    Node next;
  }

  public boolean isEmpty() {
    return first == null;
    // Or N == 0.
  }

  public int size() {
    return N;
  }

  public void push(Item item) {
    Node oldfirst = first;
    first = new Node();
    first.item = item;
    first.next = oldfirst;
    N++;
  }

  public Item pop() {
    Item item = first.item;
    first = first.next;
    N--;
    return item;
  }

  public Iterator<Item> iterator() {
    return new ListIterator();
  }

  private class ListIterator implements Iterator<Item> {
    private Node current = first;

    public boolean hasNext() {
      return current != null;
    }

    public Item next() {
      Item item = current.item;
      current = current.next;
      return item;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  public static void main(String[] args) {
    Stack<Integer> s1 = new Stack<>();

    assert s1.isEmpty() && s1.size() == 0 : "Initial size should be 0, i.e. empty";

    s1.push(3);
    s1.push(12);
    s1.push(-33);
    s1.push(5);

    assert s1.size() == 4 : "Size should increase as items are pushed";

    assert s1.pop() == 5 : "LIFO retrieval";

    assert s1.size() == 3 : "Size sould decrease as items are popped";

    Stack<String> s2 = new Stack<>();

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

    Stack<String> s3 = new Stack<String>();

    for (String str : s2) {
      System.out.println(str);
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
