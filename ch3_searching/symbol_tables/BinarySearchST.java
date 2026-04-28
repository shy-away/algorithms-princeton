package ch3_searching.symbol_tables;

import java.util.Arrays;

import edu.princeton.cs.algs4.StdRandom;

@SuppressWarnings("unchecked")
public class BinarySearchST<Key extends Comparable<Key>, Value> {
  private static int initCapacity = 1;
  private Key[] keys;
  private Value[] vals;
  private int N, cachedRank;
  private Key cachedKey;

  public BinarySearchST() {
    keys = (Key[]) new Comparable[initCapacity];
    vals = (Value[]) new Object[initCapacity];
    N = 0;
  }

  public BinarySearchST(int capacity) {
    keys = (Key[]) new Comparable[capacity];
    vals = (Value[]) new Object[capacity];
    N = 0;
  }

  public void put(Key key, Value val) {
    if (val == null) {
      delete(key);
      return;
    }

    if (N == keys.length) {
      resize(2 * keys.length);
    }

    int i = rank(key);

    // if key already exists, update its value
    if (i < N && keys[i].compareTo(key) == 0) {
      vals[i] = val;
      return;
    }

    // if key doesn't exist, move keys/vals and add it
    for (int j = N; j > i; j--) {
      keys[j] = keys[j - 1];
      vals[j] = vals[j - 1];
    }
    keys[i] = key;
    vals[i] = val;
    N++;
  }

  public Value get(Key key) {
    if (isEmpty())
      return null;

    int i = rank(key);
    if (contains(key))
      return vals[i];
    else
      return null;
  }

  public void delete(Key key) {
    if (key == null || isEmpty() || !contains(key))
      return;

    int i = rank(key);

    for (int j = i + 1; j < N; j++) {
      keys[j - 1] = keys[j];
      vals[j - 1] = vals[j];
    }
    N--;
    keys[N] = null;
    vals[N] = null;

    if (N > 0 && N == (keys.length / 4)) {
      resize(keys.length / 2);
    }
  }

  public boolean contains(Key key) {
    int i = rank(key);

    return isInBounds(i) && keys[i].compareTo(key) == 0;
  }

  public boolean isEmpty() {
    return N == 0;
  }

  public int size() {
    return N;
  }

  public Key min() {
    return keys[0];
  }

  public Key max() {
    if (isEmpty())
      return null;
    return keys[N - 1];
  }

  public Key floor(Key key) {
    int i = rank(key);

    if (contains(key))
      return keys[i];

    i--;
    if (isInBounds(i))
      return keys[i];
    else
      return null;
  }

  public Key ceiling(Key key) {
    int i = rank(key);

    if (contains(key))
      return keys[i];

    if (isInBounds(i))
      return keys[i];
    else
      return null;
  }

  // Note: formally defined as the number of keys less than the given key.
  public int rank(Key key) {
    if (cachedKey == null || key.compareTo(cachedKey) != 0) {
      cachedKey = key;
      cachedRank = rank(key, 0, N - 1);
    }

    return cachedRank;
  }

  private int rank(Key key, int lo, int hi) {
    if (hi < lo)
      return lo;

    int mid = (lo + hi) / 2;
    int cmp = key.compareTo(keys[mid]);

    if (cmp < 0) {
      return rank(key, lo, mid - 1);
    } else if (cmp > 0) {
      return rank(key, mid + 1, hi);
    } else {
      return mid;
    }
  }

  public Key select(int k) {
    if (!isInBounds(k))
      throw new IllegalArgumentException("Key " + k + " out of bounds 0 to " + (N - 1));
    return keys[k];
  }

  public void deleteMin() {
    delete(min());
  }

  public void deleteMax() {
    delete(max());
  }

  public int size(Key lo, Key hi) {
    int loRank = rank(lo);
    int hiRank = rank(floor(hi));

    return hiRank - loRank + 1;
  }

  public Iterable<Key> keys() {
    return Arrays.asList(Arrays.copyOf(keys, N));
  }

  public Iterable<Key> keys(Key lo, Key hi) {
    int copySize = size(lo, hi);
    Key[] keysCopy = (Key[]) new Comparable[copySize];

    int loIndex = rank(lo);
    for (int i = 0; i < copySize; i++) {
      keysCopy[i] = keys[loIndex + i];
    }

    return Arrays.asList(keysCopy);
  }

  private boolean isInBounds(int index) {
    return index >= 0 && index < N;
  }

  private void resize(int newSize) {
    Key[] tempKeys = (Key[]) new Comparable[newSize];
    Value[] tempVals = (Value[]) new Object[newSize];

    for (int i = 0; i < N; i++) {
      tempKeys[i] = keys[i];
      tempVals[i] = vals[i];
    }

    keys = tempKeys;
    vals = tempVals;
  }

  public static void main(String[] args) {
    int capacity = 5;
    BinarySearchST<Integer, String> st = new BinarySearchST<>(capacity);
    String firstVal = "first";
    String[] keyStrings = { "first", "second", "third", "fourth", "fifth" };

    assert st.isEmpty();
    assert st.size() == 0;
    assert st.get(123) == null;
    assert st.min() == null;
    assert st.max() == null;

    // these methods shouldn't throw exceptions
    st.delete(123);
    st.deleteMin();
    st.deleteMax();

    int firstKey = 3;
    st.put(firstKey, firstVal);

    assert st.get(firstKey) == firstVal;
    assert !st.isEmpty();
    assert st.size() == 1;
    assert st.min() == firstKey;
    assert st.max() == firstKey;
    assert st.rank(firstKey) == 0;
    assert st.select(st.rank(firstKey)) == firstKey;

    assert st.floor(firstKey) == firstKey;
    assert st.floor(firstKey + 1) == firstKey;
    assert st.floor(firstKey - 1) == null;

    assert st.ceiling(firstKey) == firstKey;
    assert st.ceiling(firstKey - 1) == firstKey;
    assert st.ceiling(firstKey + 1) == null;

    st.delete(firstKey + 1000); // shouldn't do anything

    assert !st.isEmpty();

    st.delete(firstKey);

    assert st.isEmpty();
    assert st.size() == 0;
    assert st.get(firstKey) == null;
    assert st.min() == null;
    assert st.max() == null;

    st.put(firstKey, firstVal);
    st.deleteMin();

    assert st.isEmpty();

    st.put(firstKey, firstVal);
    st.deleteMax();

    assert st.isEmpty();

    for (int i = 0; i < capacity; i++) {
      st.put(i + 1, keyStrings[i]);
    }

    assert !st.isEmpty();
    assert st.size() == 5;
    assert st.min() == 1;
    assert st.max() == capacity;
    for (int j = 1; j <= capacity; j++) {
      assert st.rank(j) == j - 1;
      assert st.select(st.rank(j)) == j;
      assert st.contains(j);
      assert st.get(j) == keyStrings[j - 1];
    }

    st.deleteMin();

    assert st.size() == 4;
    assert st.get(1) == null;
    assert !st.contains(1);
    assert st.min() == 2;

    st.deleteMax();

    assert st.size() == 3;
    assert st.get(5) == null;
    assert !st.contains(5);
    assert st.max() == 4;

    assert st.size(1, 5) == 3;
    assert st.size(3, 5) == 2;
    assert st.size(1, 3) == 2;

    st = new BinarySearchST<>(capacity);
    for (int i = 0; i < capacity; i++) {
      st.put(i * 2, keyStrings[i]);
    }

    // all keys
    int prev = st.min();
    int keyCount = 0;
    for (Integer key : st.keys()) {
      assert key.compareTo(prev) >= 0;
      prev = key;
      keyCount++;
    }
    assert keyCount == st.size();

    // subset of keys: second from min, max
    prev = st.min();
    keyCount = 0;
    for (Integer key : st.keys(st.ceiling(st.min() + 1), st.max())) {
      assert key.compareTo(prev) >= 0;
      prev = key;
      keyCount++;
    }
    assert keyCount == st.size() - 1;

    // subset of keys: second from min, max + 1
    prev = st.min();
    keyCount = 0;
    for (Integer key : st.keys(st.ceiling(st.min() + 1), st.max() + 1)) {
      assert key.compareTo(prev) >= 0;
      prev = key;
      keyCount++;
    }
    assert keyCount == st.size() - 1;

    // subset of keys: min, second from max
    prev = st.min();
    keyCount = 0;
    for (Integer key : st.keys(st.min(), st.floor(st.max() - 1))) {
      assert key.compareTo(prev) >= 0;
      prev = key;
      keyCount++;
    }
    assert keyCount == st.size() - 1;

    // subset of keys: min - 1, second from max
    prev = st.min();
    keyCount = 0;
    for (Integer key : st.keys(st.min() - 1, st.floor(st.max() - 1))) {
      assert key.compareTo(prev) >= 0;
      prev = key;
      keyCount++;
    }
    assert keyCount == st.size() - 1;

    st = new BinarySearchST<>();

    for (int i = 0; i < 100; i++) {
      st.put(i, "" + i);
    }

    for (int j = 99; j >= 0; j--) {
      if (StdRandom.bernoulli(0.5)) {
        st.deleteMax();
      } else {
        st.deleteMin();
      }

      assert st.size() == j;
    }

    // shouldn't throw exceptions
    st.deleteMin();
    st.deleteMax();
    st.delete(123);

    // rank caching
    for (int i = 0; i < 3; i++) {
      st.put(i, "" + i);
    }
    assert st.rank(1) == 1;
    st.delete(1);
    assert st.rank(1) == 1;

    System.out.println("All tests pass");
  }
}
