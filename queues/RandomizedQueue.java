package queues;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

@SuppressWarnings("unchecked")
public class RandomizedQueue<Item> implements Iterable<Item> {
  private Item[] a = (Item[]) new Object[1];
  private int N;

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

  public void enqueue(Item item) {
    if (item == null)
      throw new IllegalArgumentException();

    if (N == a.length) {
      resize(a.length * 2);
    }

    a[N++] = item;
  }

  public Item dequeue() {
    if (N == 0)
      throw new NoSuchElementException();

    int i = StdRandom.uniformInt(N);
    Item item = a[i];
    a[i] = a[--N];
    a[N] = null;

    if (N > 0 && N == (a.length / 4)) {
      resize(a.length / 2);
    }

    return item;
  }

  public Item sample() {
    if (N == 0)
      throw new NoSuchElementException();

    int i = StdRandom.uniformInt(N);
    return a[i];
  }

  public Iterator<Item> iterator() {
    return new RandomArrayIterator();
  }

  private class RandomArrayIterator implements Iterator<Item> {
    private int[] indices;
    private int i;

    public RandomArrayIterator() {
      indices = new int[N];

      for (int j = 0; j < N; j++) {
        indices[j] = j;
      }

      StdRandom.shuffle(indices);

      i = N;
    }

    public boolean hasNext() {
      return i > 0;
    }

    public Item next() {
      if (i <= 0)
        throw new NoSuchElementException();
      return a[indices[--i]];
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  public static void main(String[] args) {
    RandomizedQueue<String> rq1 = new RandomizedQueue<>();
    boolean hasErred;

    assert rq1.isEmpty();
    assert rq1.size() == 0;

    hasErred = false;
    try {
      rq1.enqueue(null);
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    hasErred = false;
    try {
      rq1.dequeue();
    } catch (NoSuchElementException e) {
      hasErred = true;
    }
    assert hasErred;

    hasErred = false;
    try {
      rq1.sample();
    } catch (NoSuchElementException e) {
      hasErred = true;
    }
    assert hasErred;

    rq1.enqueue("first");

    assert rq1.size() == 1;

    rq1.enqueue("second");
    rq1.enqueue("third");

    assert rq1.size() == 3;

    System.out.println(rq1.dequeue());

    assert rq1.size() == 2;

    System.out.println(rq1.sample());
    System.out.println(rq1.sample());

    assert rq1.size() == 2;

    RandomizedQueue<Integer> rq2 = new RandomizedQueue<>();

    for (int i = 0; i < 10; i++) {
      rq2.enqueue(i);
    }

    assert rq2.size() == 10;

    for (int i = 0; i < 5; i++) {
      for (int j : rq2) {
        System.out.print(j + ", ");
      }
      System.out.println();
    }

    hasErred = false;
    try {
      Iterator<Integer> i = rq2.iterator();
      while (i.hasNext()) i.next();
      i.next();
    } catch (NoSuchElementException e) {
      hasErred = true;
    }
    assert hasErred;
  }
}
