package ch1_fundamentals.bags_queues_stacks;

import java.util.Iterator;

public class Bag<Item> implements Iterable<Item> {
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

  public void add(Item item) {
    Node oldfirst = first;
    first = new Node();
    first.item = item;
    first.next = oldfirst;
    N++;
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
    Bag<Integer> b = new Bag<>();

    assert b.isEmpty() && b.size() == 0 : "Initial size should be 0, i.e. empty";

    b.add(1);
    b.add(2);
    b.add(3);
    b.add(4);
    b.add(5);
    b.add(6);

    assert b.size() == 6 : "Size should increase as items are inserted";

    for (Integer i : b) {
      System.out.println(i);
    }

    System.out.println("If all six integers are printed above (regardless of order), Bag is functional.");
  }
}
