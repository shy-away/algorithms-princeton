package ch3_searching.binary_search_trees;

import edu.princeton.cs.algs4.Queue;

public class BST<Key extends Comparable<Key>, Value> {
  private Node root;

  private class Node {
    private Key key;
    private Value val;
    private Node left, right;
    private int N;

    public Node(Key key, Value val, int N) {
      this.key = key;
      this.val = val;
      this.N = N;
    }
  }

  public int size() {
    return size(root);
  }

  private int size(Node x) {
    if (x == null)
      return 0;
    else
      return x.N;
  }

  public Value get(Key key) {
    return get(root, key);
  }

  private Value get(Node x, Key key) {
    // base case: empty node
    if (x == null)
      return null;

    // determine where to search next
    int cmp = key.compareTo(x.key);
    if (cmp < 0)
      return get(x.left, key);
    else if (cmp > 0)
      return get(x.right, key);
    else
      return x.val; // current node matches key, so return value
  }

  public void put(Key key, Value val) {
    root = put(root, key, val);
  }

  private Node put(Node x, Key key, Value val) {
    // base case: empty node
    if (x == null)
      return new Node(key, val, 1);

    // determine where to insert new node, moving "down"
    int cmp = key.compareTo(x.key);
    if (cmp < 0)
      x.left = put(x.left, key, val);
    else if (cmp > 0)
      x.right = put(x.right, key, val);
    else
      x.val = val; // current node matches key, so update value

    // update sizes, moving "up"
    x.N = size(x.left) + size(x.right) + 1;
    return x;
  }

  public Key min() {
    if (root == null)
      return null;
    return min(root).key;
  }

  private Node min(Node x) {
    if (x.left == null)
      return x;
    return min(x.left);
  }

  public Key max() {
    if (root == null)
      return null;
    return max(root).key;
  }

  private Node max(Node x) {
    if (x.right == null)
      return x;
    return max(x.right);
  }

  public Key floor(Key key) {
    Node x = floor(root, key);
    if (x == null)
      return null;
    return x.key;
  }

  private Node floor(Node x, Key key) {
    // base case: empty node
    if (x == null)
      return null;

    // determine where the floor may be
    int cmp = key.compareTo(x.key);

    // match
    if (cmp == 0)
      return x;

    // greater, so search left
    if (cmp < 0)
      return floor(x.left, key);

    // less, so search right and compare
    Node t = floor(x.right, key);
    if (t != null)
      return t;
    else
      return x;
  }

  public Key ceiling(Key key) {
    Node x = ceiling(root, key);
    if (x == null)
      return null;
    return x.key;
  }

  private Node ceiling(Node x, Key key) {
    // base case: empty node
    if (x == null)
      return null;

    // determine where the ceiling may be
    int cmp = key.compareTo(x.key);

    // matches
    if (cmp == 0)
      return x;

    // less, so search right
    if (cmp > 0)
      return ceiling(x.right, key);

    // greater, so search left and compare
    Node t = ceiling(x.left, key);
    if (t != null)
      return t;
    else
      return x;
  }

  public Key select(int k) {
    Node result = select(root, k);
    if (result == null)
      throw new IllegalArgumentException("No key of rank " + k);
    return result.key;
  }

  private Node select(Node x, int k) {
    // base case: empty node
    if (x == null)
      return null;

    // determine where rank k node must be
    int t = size(x.left);
    if (t > k)
      return select(x.left, k);
    else if (t < k)
      return select(x.right, k - t - 1); // t for left tree, 1 for current node
    else
      return x;
  }

  public int rank(Key key) {
    return rank(key, root);
  }

  // Note: rank is the number of elements strictly less than the given key.
  private int rank(Key key, Node x) {
    // base case: empty node
    if (x == null)
      return 0;

    // determine count of all left elements
    int cmp = key.compareTo(x.key);

    // less, so search left
    if (cmp < 0)
      return rank(key, x.left);

    // greater, so search right and calculate
    else if (cmp > 0)
      return 1 + size(x.left) + rank(key, x.right);

    // match
    else
      return size(x.left);
  }

  public void deleteMin() {
    if (root == null)
      return;
    root = deleteMin(root);
  }

  private Node deleteMin(Node x) {
    // base case: minimum node (no left child)
    if (x.left == null)
      return x.right;

    // recursively delete min from left, recalculate size
    x.left = deleteMin(x.left);
    x.N = size(x.left) + size(x.right) + 1;
    return x;
  }

  public void deleteMax() {
    if (root == null)
      return;
    root = deleteMax(root);
  }

  private Node deleteMax(Node x) {
    // base case: maximum node (no right child)
    if (x.right == null)
      return x.left;

    // recursively delete max from right, recalculate size
    x.right = deleteMax(x.right);
    x.N = size(x.left) + size(x.right) + 1;
    return x;
  }

  public void delete(Key key) {
    root = delete(root, key);
  }

  private Node delete(Node x, Key key) {
    // base case: empty node
    if (x == null)
      return null;

    // determine where target node may be
    int cmp = key.compareTo(x.key);

    // greater, so move left
    if (cmp < 0)
      x.left = delete(x.left, key);

    // less, so move right
    else if (cmp > 0)
      x.right = delete(x.right, key);

    // process target node
    else {
      // easy cases: only one child
      if (x.right == null)
        return x.left;
      if (x.left == null)
        return x.right;

      // eager Hibbard deletion
      Node t = x; // save current node
      x = min(t.right); // set current node to next largest element
      x.right = deleteMin(t.right); // set right to tree without next largest element
      x.left = t.left; // preserve left
    }

    // recalculate size and return
    x.N = size(x.left) + size(x.right) + 1;
    return x;
  }

  public Iterable<Key> keys() {
    return keys(min(), max());
  }

  public Iterable<Key> keys(Key lo, Key hi) {
    Queue<Key> queue = new Queue<>();
    keys(root, queue, lo, hi);
    return queue;
  }

  private void keys(Node x, Queue<Key> queue, Key lo, Key hi) {
    // base case: empty node
    if (x == null)
      return;

    // process left to right, staying between keys
    int cmplo = lo.compareTo(x.key);
    int cmphi = hi.compareTo(x.key);

    // lo is below, so process left
    if (cmplo < 0)
      keys(x.left, queue, lo, hi);

    // current key is between lo and hi, so enqueue
    if (cmplo <= 0 && cmphi >= 0)
      queue.enqueue(x.key);

    // hi is above, so process right
    if (cmphi > 0)
      keys(x.right, queue, lo, hi);
  }

  public static void main(String[] args) {
    BST<Integer, String> st = new BST<>();
    boolean hasErred;

    assert st.size() == 0;
    assert st.get(123) == null;
    assert st.min() == null;
    assert st.max() == null;
    assert st.floor(123) == null;
    assert st.ceiling(123) == null;
    assert st.rank(123) == 0;
    st.deleteMin();
    st.deleteMax();
    st.delete(123);
    for (int key : st.keys()) {
      assert false;
    }

    hasErred = false;
    try {
      int x = st.select(123);
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    st.put(1, "1");

    assert st.size() == 1;
    assert st.get(1) == "1";
    assert st.get(2) == null;
    assert st.min() == 1;
    assert st.max() == 1;
    assert st.floor(1) == 1;
    assert st.floor(2) == 1;
    assert st.floor(0) == null;
    assert st.ceiling(1) == 1;
    assert st.ceiling(2) == null;
    assert st.ceiling(0) == 1;
    assert st.select(0) == 1;

    hasErred = false;
    try {
      int x = st.select(1);
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    for (int key : st.keys()) {
      assert key == 1;
    }

    st.delete(123);
    assert st.size() == 1;
    st.delete(1);
    assert st.size() == 0;

    for (int i = 0; i < 20; i += 2) {
      st.put(i, "" + i);
    }

    assert st.size() == 10;
    assert st.min() == 0;
    assert st.max() == 18;
    assert st.floor(15) == 14;
    assert st.floor(14) == 14;
    assert st.floor(16) == 16;
    assert st.ceiling(15) == 16;
    assert st.ceiling(14) == 14;
    assert st.ceiling(16) == 16;
    assert st.select(0) == 0;
    assert st.select(9) == 18;

    hasErred = false;
    try {
      int x = st.select(10);
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    assert st.rank(1) == 1;
    assert st.rank(2) == 1;
    assert st.rank(3) == 2;

    st.deleteMin();
    st.deleteMax();

    assert st.size() == 8;
    assert st.min() == 2;
    assert st.max() == 16;

    st.delete(10);

    assert st.size() == 7;
    assert st.min() == 2;
    assert st.max() == 16;
    assert st.rank(10) == 4;

    System.out.println("All tests pass");
  }
}
