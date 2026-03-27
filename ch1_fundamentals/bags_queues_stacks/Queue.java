package ch1_fundamentals.bags_queues_stacks;

import java.util.Iterator;

public class Queue<Item> implements Iterable<Item> {
  private Node first;
  private Node last;
  private int N;

  private class Node {
    Item item;
    Node next;
  }

  public boolean isEmpty() {
    return first == null;
  }

  public int size() {
    return N;
  }

  public void enqueue(Item item) {
    Node oldlast = last;
    last = new Node();
    last.item = item;
    last.next = null;

    if (isEmpty()) {
      first = last;
    } else {
      oldlast.next = last;
    }

    N++;
  }

  public Item dequeue() {
    Item item = first.item;
    first = first.next;

    /*
     * Checking isEmpty() here relies on isEmpty() directly
     * checking first == null. If isEmpty() instead checked
     * N == 0, this wouldn't work.
     */
    if (isEmpty()) {
      last = null;
    }
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
    Queue<Integer> q1 = new Queue<>();

    assert q1.isEmpty() && q1.size() == 0 : "Initial size should be 0, i.e. empty";

    q1.enqueue(3);
    q1.enqueue(12);
    q1.enqueue(-33);
    q1.enqueue(5);

    assert q1.size() == 4 : "Size should increase as items are enqueued";

    assert q1.dequeue() == 3 : "FIFO retrieval";

    assert q1.size() == 3 : "Size sould decrease as items are dequeued";

    Queue<String> q2 = new Queue<>();

    q2.enqueue("Sphinx");

    assert q2.dequeue() == "Sphinx" : "Generic class should allow any type";

    q2.enqueue("of");
    q2.enqueue("black");
    q2.dequeue();
    q2.enqueue("quartz,");
    q2.enqueue("judge");
    q2.enqueue("my");
    q2.dequeue();
    q2.enqueue("vow.");

    Queue<String> q3 = new Queue<>();

    for (String str : q2) {
      System.out.println(str);
      q3.enqueue(str);
    }

    assert q3.size() == 4 : "Iterator functionality in foreach loop";

    assert q3.dequeue() == "quartz," &&
        q3.dequeue() == "judge" &&
        q3.dequeue() == "my" &&
        q3.dequeue() == "vow." : "Preserved FIFO order in iterator";

    System.out.println("All tests pass");
  }
}
