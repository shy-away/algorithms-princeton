package ch2_sorting;

@SuppressWarnings("unchecked")
public class MaxPQ<Key extends Comparable<Key>> {
  private Key[] pq;
  private int N = 0;

  public MaxPQ() {
    pq = (Key[]) new Comparable[2];
  }

  public boolean isEmpty() {
    return N == 0;
  }

  public int size() {
    return N;
  }

  public void insert(Key v) {
    if (N == pq.length - 1) {
      resize(2 * pq.length + 1);
    }

    pq[++N] = v;
    swim(N);
  }

  public Key delMax() {
    Key max = pq[1];
    exch(1, N--);

    if (N > 0 && N == (pq.length / 4)) {
      resize(pq.length / 2 + 1);
    }

    pq[N + 1] = null;
    sink(1);
    return max;
  }

  private boolean less(int i, int j) {
    return pq[i].compareTo(pq[j]) < 0;
  }

  private void exch(int i, int j) {
    Key t = pq[i];
    pq[i] = pq[j];
    pq[j] = t;
  }

  private void swim(int k) {
    while (k > 1 && less(k / 2, k)) {
      exch(k / 2, k);
      k /= 2;
    }
  }

  private void sink(int k) {
    while (2 * k <= N) {
      int j = 2 * k;
      if (j < N && less(j, j + 1))
        j++;
      if (!less(k, j))
        break;
      exch(k, j);
      k = j;
    }
  }

  private void resize(int newSize) {
    Key[] temp = (Key[]) new Comparable[newSize];

    for (int i = 1; i < pq.length && i < newSize; i++) {
      temp[i] = pq[i];
    }

    pq = temp;
  }

  public static void main(String[] args) {
    MaxPQ<Integer> mpq = new MaxPQ<>();

    assert mpq.isEmpty();
    assert mpq.size() == 0;

    mpq.insert(-1);

    assert !mpq.isEmpty();
    assert mpq.size() == 1;

    mpq.insert(2);

    assert mpq.size() == 2;

    for (int i = 0; i < 10; i++) {
      mpq.insert(10 - i);
    }

    assert mpq.size() == 12;

    for (int i = 0; i < 11; i++) {
      mpq.delMax();
    }

    assert mpq.size() == 1;
    assert mpq.delMax() == -1;
    assert mpq.size() == 0;

    System.out.println("All tests pass");
  }
}
