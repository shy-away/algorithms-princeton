package ch1_fundamentals.bags_queues_stacks;

import java.util.Iterator;

public class LinkedListGeneric<Item> implements Iterable<Item> {
  private Node first = null;
  private int N = 0;

  private class Node {
    Item key;
    Node next;
  }

  public int size() {
    return N;
  }

  public void add(Item item) {
    Node oldFirst = first;
    first = new Node();
    first.key = item;
    first.next = oldFirst;
    N++;
  }

  public void delete(int k) {
    if (first == null || k > N - 1 || k < 0) {
      return;
    }
    if (k == 0) {
      first = first.next;
      N--;
      return;
    }

    int i = 0;
    for (Node x = first; x != null; x = x.next) {
      if (i == k - 1) {
        x.next = x.next.next;
        N--;
        return;
      }
      i++;
    }
  }

  public boolean find(Item item) {
    for (Node x = first; x != null; x = x.next) {
      if (x.key == item)
        return true;
    }
    return false;
  }

  public void remove(Item item) {
    if (first == null) {
      return;
    }
    if (first.key == item && N == 1) {
      first = null;
      N--;
      return;
    }

    for (Node x = first; x != null && x.next != null; x = x.next) {
      if (x.next.key == item) {
        x.next = x.next.next;
        N--;
      }
    }
  }

  public int max() {
    if (first != null && first.key != (Integer) first.key) {
      return 0;
    }
    return max(first);
  }

  private int max(Node current) {
    if (current == null) return 0;
    return Math.max((int) current.key, max(current.next));
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
      Item key = current.key;
      current = current.next;
      return key;
    }
  }

  public static void main(String[] args) {
    LinkedListGeneric<String> l1 = new LinkedListGeneric<>();
    l1.add("first");
    l1.add("second");
    l1.add("third");
    assert l1.size() == 3;

    l1.delete(0);
    assert l1.size() == 2;

    l1.delete(-3);
    assert l1.size() == 2;

    assert l1.find("second");
    assert !l1.find("third");

    l1 = new LinkedListGeneric<>();

    l1.add("to");
    l1.add("be");
    l1.add("or");
    l1.add("not");
    l1.add("to");
    l1.add("be");

    l1.remove("to");

    assert l1.size() == 4;

    for (String s : l1) {
      System.out.println(s);
    }

    LinkedListGeneric<Integer> l2 = new LinkedListGeneric<>();

    l2.add(1);
    l2.add(-3);
    l2.add(17);
    l2.add(5);

    assert l2.max() == 17;
  }
}
