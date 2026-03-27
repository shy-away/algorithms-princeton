package queues;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
  private Node first, last;
  private int N;

  private class Node {
    Item item;
    Node next;
    Node prev;
  }

  public boolean isEmpty() {
    return first == null;
  }

  public int size() {
    return N;
  }

  public void addFirst(Item item) {
    if (item == null)
      throw new IllegalArgumentException();

    Node oldFirst = first;
    first = new Node();
    first.item = item;
    first.next = oldFirst;

    if (oldFirst == null) {
      last = first;
    } else {
      oldFirst.prev = first;
    }

    N++;
  }

  public void addLast(Item item) {
    if (item == null)
      throw new IllegalArgumentException();

    Node oldLast = last;
    last = new Node();
    last.item = item;
    last.prev = oldLast;

    if (oldLast == null) {
      first = last;
    } else {
      oldLast.next = last;
    }

    N++;
  }

  public Item removeFirst() {
    if (N == 0)
      throw new NoSuchElementException();

    Item item = first.item;
    first = first.next;
    if (first != null) first.prev = null;

    N--;
    return item;
  }

  public Item removeLast() {
    if (N == 0)
      throw new NoSuchElementException();

    Item item = last.item;
    last = last.prev;
    if (last != null) last.next = null;

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
      if (current == null)
        throw new NoSuchElementException();
      Item item = current.item;
      current = current.next;
      return item;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  public static void main(String[] args) {
    Deque<Integer> d = new Deque<>();
    boolean hasErred;

    assert d.isEmpty() && d.size() == 0;

    hasErred = false;
    try {
      d.removeFirst();
    } catch (NoSuchElementException e) {
      hasErred = true;
    }
    assert hasErred;

    hasErred = false;
    try {
      d.removeLast();
    } catch (NoSuchElementException e) {
      hasErred = true;
    }
    assert hasErred;

    hasErred = false;
    try {
      d.addFirst(null);
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    hasErred = false;
    try {
      d.addLast(null);
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    d.addFirst(1);
    d.addFirst(4);
    d.addFirst(22);

    // for (int i : d) {
    // System.out.print(i + ", ");
    // }
    // System.out.println();

    assert d.size() == 3;

    d.addLast(-3);
    d.addLast(-19);
    d.addLast(8);
    d.addLast(52);

    assert d.size() == 7;

    assert d.removeFirst() == 22;
    assert d.removeFirst() == 4;

    assert d.removeLast() == 52;
    assert d.removeLast() == 8;

    assert d.size() == 3;

    hasErred = false;
    try {
      Iterator<Integer> i = d.iterator();
      while (i.hasNext()) {
        i.next();
        i.remove();
      }
    } catch (UnsupportedOperationException e) {
      hasErred = true;
    }
    assert hasErred;

    hasErred = false;
    try {
      Iterator<Integer> i = d.iterator();
      while (i.hasNext()) {
        i.next();
      }
      i.next();
    } catch (NoSuchElementException e) {
      hasErred = true;
    }
    assert hasErred;
  }
}
