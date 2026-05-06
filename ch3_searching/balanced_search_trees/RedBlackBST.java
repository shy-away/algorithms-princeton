package ch3_searching.balanced_search_trees;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdRandom;

/**
 * A left-leaning red-black binary search tree implementation.
 * <br>
 * My implementation is based on the book's implementation, with some changes.
 * 
 * @param <Key>   the type of keys, must be comparable
 * @param <Value> the type of values
 */
public class RedBlackBST<Key extends Comparable<Key>, Value> {
  private Node root;

  private static final boolean RED = true;
  private static final boolean BLACK = false;

  /**
   * Node in the red-black BST.
   */
  private class Node {
    private Key key;
    private Value val;
    private Node left, right;
    private int N;
    private boolean color;

    /**
     * Constructs a new node.
     * 
     * @param key   the key
     * @param val   the value
     * @param N     the subtree size
     * @param color the color (<code>true</code> for red, <code>false</code> for
     *              black)
     */
    public Node(Key key, Value val, int N, boolean color) {
      this.key = key;
      this.val = val;
      this.N = N;
      this.color = color;
    }

    /**
     * Returns a string representation of the node.
     * 
     * @return string representation
     */
    public String toString() {
      return (color ? "RED" : "BLK") + " " + key.toString() + ": " + val.toString();
    }
  }

  /**
   * Checks if a node is red.
   * 
   * @param x the node
   * @return true if red, false otherwise
   */
  private boolean isRed(Node x) {
    if (x == null)
      return false;
    return x.color;
  }

  /**
   * Returns the number of key-value pairs in the BST.
   * 
   * @return the size
   */
  public int size() {
    return size(root);
  }

  /**
   * Returns the size of the subtree rooted at x.
   * 
   * @param x the root of the subtree
   * @return the size
   */
  private int size(Node x) {
    if (x == null)
      return 0;
    else
      return x.N;
  }

  /**
   * Returns the value associated with the given key.
   * 
   * @param key the key
   * @return the value, or null if not found
   */
  public Value get(Key key) {
    return get(root, key);
  }

  /**
   * Returns the value associated with the given key in the subtree rooted at x.
   * <br>
   * <strong>Note:</strong> Red-black BST uses exact same <code>get()</code> as a
   * regular BST!
   * 
   * @param x   the root of the subtree
   * @param key the key
   * @return the value, or null if not found
   */
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

  /**
   * Inserts a key-value pair into the BST.
   * 
   * @param key the key
   * @param val the value
   */
  public void put(Key key, Value val) {
    root = put(root, key, val);
    root.color = BLACK; // red root would imply the root is a left child of a conceptual 3-node
  }

  /**
   * Inserts a key-value pair into the subtree rooted at h.
   * 
   * @param h   the root of the subtree
   * @param key the key
   * @param val the value
   * @return the updated subtree root
   */
  private Node put(Node h, Key key, Value val) {
    // base case: empty node
    if (h == null)
      // new nodes are red by default
      // red connections make 2-nodes into 3-nodes, or 3-nodes into temporary 4-nodes
      // that will be restructured on the way up
      return new Node(key, val, 1, RED);

    // determine where to insert new node, moving "down"
    int cmp = key.compareTo(h.key);
    if (cmp < 0)
      h.left = put(h.left, key, val);
    else if (cmp > 0)
      h.right = put(h.right, key, val);
    else
      h.val = val; // current node matches key, so update value

    // rebalance tree, moving "up"
    return balance(h);
  }

  /**
   * Returns the smallest key in the BST.
   * 
   * @return the smallest key, or null if empty
   */
  public Key min() {
    if (root == null)
      return null;
    return min(root).key;
  }

  /**
   * Returns the node with the smallest key in the subtree rooted at x.
   * 
   * @param x the root of the subtree
   * @return the node with the smallest key
   */
  private Node min(Node x) {
    if (x.left == null)
      return x;
    return min(x.left);
  }

  /**
   * Returns the largest key in the BST.
   * 
   * @return the largest key, or null if empty
   */
  public Key max() {
    if (root == null)
      return null;
    return max(root).key;
  }

  /**
   * Returns the node with the largest key in the subtree rooted at x.
   * 
   * @param x the root of the subtree
   * @return the node with the largest key
   */
  private Node max(Node x) {
    if (x.right == null)
      return x;
    return max(x.right);
  }

  /**
   * Returns the largest key less than or equal to the given key.
   * 
   * @param key the key
   * @return the floor key, or null if none
   */
  public Key floor(Key key) {
    Node x = floor(root, key);
    if (x == null)
      return null;
    return x.key;
  }

  /**
   * Returns the node with the largest key <= the given <code>key</code> in the
   * subtree rooted at x.
   * 
   * @param x   the root of the subtree
   * @param key the key
   * @return the floor node, or null if none
   */
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

  /**
   * Returns the smallest key greater than or equal to the given key.
   * 
   * @param key the key
   * @return the ceiling key, or null if none
   */
  public Key ceiling(Key key) {
    Node x = ceiling(root, key);
    if (x == null)
      return null;
    return x.key;
  }

  /**
   * Returns the node with the smallest key >= the given <code>key</code> in the
   * subtree rooted at x.
   * 
   * @param x   the root of the subtree
   * @param key the key
   * @return the ceiling node, or null if none
   */
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

  /**
   * Returns the key of rank k (0-based).
   * 
   * @param k the rank
   * @return the key of rank k
   * @throws IllegalArgumentException if k is out of bounds
   */
  public Key select(int k) {
    Node result = select(root, k);
    if (result == null)
      throw new IllegalArgumentException("No key of rank " + k);
    return result.key;
  }

  /**
   * Returns the node of rank k in the subtree rooted at x.
   * 
   * @param x the root of the subtree
   * @param k the rank
   * @return the node of rank k, or null if none
   */
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

  /**
   * Returns the number of keys strictly less than the given key.
   * <br>
   * <strong>Note:</strong> The rank of any <code>key</code> is
   * defined as the number of keys less than <code>key</code> in
   * <code>compareTo()</code> order.
   * 
   * @param key the key
   * @return the rank
   */
  public int rank(Key key) {
    return rank(key, root);
  }

  /**
   * Returns the rank of key in the subtree rooted at x.
   * 
   * @param key the key
   * @param x   the root of the subtree
   * @return the rank
   */
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

  /**
   * Eliminates temporary 4-nodes, shifts red links to be left-leaning, and
   * recalculates the size of the given node.
   * 
   * @param h the root of the subtree
   * @return the balanced subtree root
   */
  private Node balance(Node h) {
    if (isRed(h.right))
      h = rotateLeft(h);

    if (isRed(h.right) && !isRed(h.left))
      h = rotateLeft(h);
    if (isRed(h.left) && isRed(h.left.left))
      h = rotateRight(h);
    if (isRed(h.left) && isRed(h.right))
      flipColors(h);

    h.N = size(h.left) + size(h.right) + 1;

    return h;
  }

  /**
   * Rotates the subtree rooted at h to the left.
   * 
   * @param h the root of the subtree
   * @return the rotated subtree root
   */
  private Node rotateLeft(Node h) {
    // rearrange nodes
    Node x = h.right; // x will become the parent node
    h.right = x.left; // move subtree, preserving order
    x.left = h; // reincorporate original parent

    // correct colors
    x.color = h.color; // preserve original color
    h.color = RED; // h will always be left-leaning

    // recalculate sizes
    x.N = h.N; // x now contains all nodes h contained
    h.N = 1 + size(h.left) + size(h.right); // h has new subtrees, so recalculate

    return x;
  }

  /**
   * Rotates the subtree rooted at h to the right.
   * 
   * @param h the root of the subtree
   * @return the rotated subtree root
   */
  private Node rotateRight(Node h) {
    Node x = h.left;
    h.left = x.right;
    x.right = h;

    x.color = h.color;
    h.color = RED;

    x.N = h.N;
    h.N = 1 + size(h.left) + size(h.right);

    return x;
  }

  /**
   * Toggles the colors of the node and its children, based on the color of the
   * given node.
   * 
   * @param h the node
   */
  private void flipColors(Node h) {
    // toggle colors
    if (!isRed(h)) {
      h.color = RED;
      h.left.color = BLACK;
      h.right.color = BLACK;
    } else {
      h.color = BLACK;
      h.left.color = RED;
      h.right.color = RED;
    }
  }

  /**
   * Moves a red node into the left subtree.
   * 
   * @param h the root of the subtree
   * @return the updated subtree root
   */
  private Node moveRedLeft(Node h) {
    // assuming h is red and h.left is a 2-node,
    // make h.left or one of its children red
    flipColors(h);

    // since h.right is now red (given h was red before flipColors()),
    // make sure h.right is not a 4-node
    if (isRed(h.right.left)) {
      h.right = rotateRight(h.right);
      h = rotateLeft(h);
    }

    return h;
  }

  /**
   * Moves a red node into the right subtree.
   * 
   * @param h the root of the subtree
   * @return the updated subtree root
   */
  private Node moveRedRight(Node h) {
    // assuming h is red and h.right is a 2-node,
    // make h.right or one of its children red
    flipColors(h);

    // since h.left is now red (given h was red before flipColors()),
    // make sure h.left is not a 4-node
    if (isRed(h.left.left))
      h = rotateRight(h);

    return h;
  }

  /**
   * Removes the smallest key from the BST.
   */
  public void deleteMin() {
    if (root == null)
      return;

    // temporarily set root to red for later moveRedLeft() calls
    if (!isRed(root.left) && !isRed(root.right))
      root.color = RED;

    root = deleteMin(root);

    // reset root to black
    if (size() != 0)
      root.color = BLACK;
  }

  /**
   * Removes the smallest key from the subtree rooted at h.
   * 
   * @param h the root of the subtree
   * @return the updated subtree root
   */
  private Node deleteMin(Node h) {
    if (h.left == null)
      return null;

    // if h.left is a 2-node, make it a 3-node
    if (!isRed(h.left) && !isRed(h.left.left))
      h = moveRedLeft(h);

    h.left = deleteMin(h.left);

    // rebalance tree on the way up
    return balance(h);
  }

  /**
   * Removes the largest key from the BST.
   */
  public void deleteMax() {
    if (root == null)
      return;

    // temporarily set root to red for later moveRedRight() calls
    if (!isRed(root.left) && !isRed(root.right))
      root.color = RED;

    root = deleteMax(root);

    // reset root to black
    if (size() != 0)
      root.color = BLACK;
  }

  /**
   * Removes the largest key from the subtree rooted at h.
   * 
   * @param h the root of the subtree
   * @return the updated subtree root
   */
  private Node deleteMax(Node h) {
    // force all nodes along path to be red
    if (isRed(h.left))
      h = rotateRight(h);

    // base case: max key found, replace max with null
    if (h.right == null)
      return null;

    // if h.right is a 2-node, make it a 3-node
    if (!isRed(h.right) && !isRed(h.right.left))
      h = moveRedRight(h);

    h.right = deleteMax(h.right);

    // rebalance tree on the way up
    return balance(h);
  }

  /**
   * Removes the specified key from the BST.
   * 
   * @param key the key to remove
   */
  public void delete(Key key) {
    // null root OR key is not in tree
    if (root == null || get(key) == null)
      return;

    // temporarily set root to red for later moveRedRight/Left() calls
    if (!isRed(root.left) && !isRed(root.right))
      root.color = RED;

    root = delete(root, key);

    // reset root to black
    if (size() != 0)
      root.color = BLACK;
  }

  /**
   * Removes the specified key from the subtree rooted at h.
   * 
   * @param h   the root of the subtree
   * @param key the key to remove
   * @return the updated subtree root
   */
  private Node delete(Node h, Key key) {
    // base case: empty node
    if (h == null)
      return null;

    // determine where to go next
    if (key.compareTo(h.key) < 0) {
      // target key is to the left

      // if h.left is a 2-node, make it a 3-node
      if (!isRed(h.left) && !isRed(h.left.left))
        h = moveRedLeft(h);

      h.left = delete(h.left, key);
    } else {
      // target key is current key OR to the right
      // ensure next node is a 3- or 4-node for both cases

      // avoid orphaning red left nodes (right nodes will be traversed)
      if (isRed(h.left))
        h = rotateRight(h);

      //
      if (key.compareTo(h.key) == 0 && h.right == null)
        return null;

      // if h.right is a 2-node, make it a 3-node
      if (!isRed(h.right) && !isRed(h.right.left))
        h = moveRedRight(h);

      //
      if (key.compareTo(h.key) == 0) {
        // eager Hibbard deletion, LLRB style

        Key newKey = min(h.right).key; // h.right by this point should never be null
        h.key = newKey;
        h.val = get(h.right, newKey);

        h.right = deleteMin(h.right);
      } else {
        h.right = delete(h.right, key);
      }
    }

    // rebalance tree on the way up
    return balance(h);
  }

  /**
   * Returns all keys in the BST in sorted order.
   * 
   * @return an iterable of all keys
   */
  public Iterable<Key> keys() {
    return keys(min(), max());
  }

  /**
   * Returns all keys in the BST between lo and hi in sorted order.
   * 
   * @param lo the lowest key
   * @param hi the highest key
   * @return an iterable of keys in [lo, hi]
   */
  public Iterable<Key> keys(Key lo, Key hi) {
    Queue<Key> queue = new Queue<>();
    keys(root, queue, lo, hi);
    return queue;
  }

  /**
   * Adds keys in the subtree rooted at x between lo and hi to the queue.
   * 
   * @param x     the root of the subtree
   * @param queue the queue to add keys to
   * @param lo    the lowest key
   * @param hi    the highest key
   */
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

  /**
   * Returns a string representation of the BST.
   * 
   * @return string representation
   */
  public String toString() {
    return toString(root, 0);
  }

  /**
   * Returns a string representation of the subtree rooted at x.
   * 
   * @param x             the root of the subtree
   * @param currentHeight the current height for indentation
   * @return string representation
   */
  private String toString(Node x, int currentHeight) {
    String tabs = "";
    for (int i = 0; i < currentHeight; i++) {
      tabs = tabs.concat("-- ");
    }

    if (x.left == null && x.right == null)
      return tabs + x.toString() + "\n";

    String result = "";

    if (x.right != null)
      result += toString(x.right, currentHeight + 1);

    result += tabs + x.toString() + "\n";

    if (x.left != null)
      result += toString(x.left, currentHeight + 1);

    return result;
  }

  /* Private BST + LLRB testing methods */

  /**
   * Checks if the subtree satisfies the 2-3 tree property.
   * 
   * @param h the root of the subtree
   * @return true if valid 2-3 tree
   */
  private boolean is23(Node h) {
    if (h == null)
      return true;

    if (!isRed(h))
      return !isRed(h.right) && is23(h.left) && is23(h.right);
    else
      return !isRed(h.left) && !isRed(h.right) && is23(h.left) && is23(h.right);
  }

  /**
   * Checks if the subtree is balanced.
   * 
   * @param h the root of the subtree
   * @return true if balanced
   */
  private boolean isBalanced(Node h) {
    if (h == null)
      return true;

    class DepthNode {
      private final Node node;
      private final int depth;

      public DepthNode(Node node, int depth) {
        this.node = node;
        this.depth = depth;
      }
    }

    Stack<DepthNode> stack = new Stack<>();

    Node currNode = h;
    int currDepth = 0;
    int maxDepth = 0;
    boolean isMaxDepthSet = false;

    while (currNode != null || !stack.isEmpty()) {
      if (currNode != null) {
        if (!isRed(currNode))
          currDepth++;

        stack.push(new DepthNode(currNode, currDepth));

        currNode = currNode.left;
      } else {
        if (!isMaxDepthSet) {
          maxDepth = currDepth;
          isMaxDepthSet = true;
        } else {
          if (currDepth != maxDepth)
            return false;
        }

        currNode = stack.peek().node.right;
        currDepth = stack.peek().depth;

        stack.pop();
      }
    }

    return true;
  }

  /**
   * Checks if the subtree is a valid binary tree.
   * 
   * @param h the root of the subtree
   * @return true if valid binary tree
   */
  private boolean isBinaryTree(Node h) {
    if (h == null)
      return true;

    Stack<Node> stack = new Stack<>();
    Node currNode = h;
    int numNodes = 0;

    while (!stack.isEmpty() || currNode != null) {
      if (currNode != null) {
        stack.push(currNode);
        currNode = currNode.left;
        numNodes++;
      } else {
        currNode = stack.pop().right;
      }
    }

    return numNodes == h.N;
  }

  /**
   * Checks the subtree for three properties:
   * <ul>
   * <li>all keys are between <code>min</code> and <code>max</code></li>
   * <li><code>min</code> and <code>max</code> are in fact the minimum and maximum
   * keys, respectively</li>
   * <li>the BST ordering property actually holds for all keys</li>
   * </ul>
   * 
   * @param h   the root of the subtree
   * @param min the minimum key
   * @param max the maximum key
   * @return true if ordered
   */
  private boolean isOrdered(Node h, Key min, Key max) {
    if (h == null)
      return true;

    // check min and max
    Node minNode = h;
    while (minNode.left != null) {
      minNode = minNode.left;
    }
    if (minNode.key.compareTo(min) != 0)
      return false;

    Node maxNode = h;
    while (maxNode.right != null) {
      maxNode = maxNode.right;
    }
    if (maxNode.key.compareTo(max) != 0)
      return false;

    // iterate through tree and check order
    Stack<Node> stack = new Stack<>();
    Node currNode = h, prevNode = null;

    while (!stack.isEmpty() || currNode != null) {
      if (currNode != null) {
        stack.push(currNode);
        currNode = currNode.left;
      } else {
        currNode = stack.pop();

        if (prevNode == null) {
          prevNode = currNode;
        } else if (currNode.key.compareTo(prevNode.key) <= 0) {
          System.out.printf("previous: %d, current: %d\n", prevNode.key, currNode.key);
          System.out.printf("comparison: %d\n", currNode.key.compareTo(prevNode.key));
          return false;
        }

        currNode = currNode.right;
      }
    }

    return true;
  }

  /**
   * Checks if the subtree has no equal keys.
   * 
   * @param h the root of the subtree
   * @return true if no duplicates
   */
  private boolean noEqualKeys(Node h) {
    if (h == null)
      return true;

    Stack<Node> stack = new Stack<>();
    SET<Key> bag = new SET<>();
    stack.push(h);

    while (!stack.isEmpty()) {
      Node currNode = stack.pop();

      if (bag.contains(currNode.key))
        return false;
      else
        bag.add(currNode.key);

      if (currNode.right != null)
        stack.push(currNode.right);
      if (currNode.left != null)
        stack.push(currNode.left);
    }

    return true;
  }

  /**
   * Checks if the subtree is a valid BST.
   * 
   * @param h   the root of the subtree
   * @param min the minimum key
   * @param max the maximum key
   * @return true if valid BST
   */
  private boolean isBST(Node h, Key min, Key max) {
    return isBinaryTree(h) && isOrdered(h, min, max) && noEqualKeys(h);
  }

  /**
   * Checks if the subtree is a valid left-leaning red-black BST.
   * 
   * @param h   the root of the subtree
   * @param min the minimum key
   * @param max the maximum key
   * @return true if valid LLRB
   */
  private boolean isLLRB(Node h, Key min, Key max) {
    return isBST(h, min, max) && is23(h) && isBalanced(h);
  }

  /**
   * Unit tests the RedBlackBST data type.
   * 
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    RedBlackBST<Integer, String> st = new RedBlackBST<>();
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

    // System.out.println(st.toString());

    // invariant testing for delete*()

    int T = 10_000;

    // deleteMin/Max()
    for (int i = 0; i < T; i++) {
      int next = StdRandom.uniformInt(T);
      st.put(next, "" + next);
    }

    for (int i = 0; i < T; i++) {
      if (StdRandom.bernoulli()) {
        st.deleteMin();
      } else {
        st.deleteMax();
      }

      assert st.isLLRB(st.root, st.min(), st.max());
    }

    // delete()
    int[] nums = new int[T];
    for (int i = 0; i < T; i++) {
      nums[i] = i;
    }

    StdRandom.shuffle(nums);

    for (int i = 0; i < nums.length; i++) {
      int num = nums[i];
      st.put(num, "" + num);
    }

    StdRandom.shuffle(nums);

    // System.out.println(st.toString());

    for (int i = 0; i < nums.length; i++) {
      st.delete(nums[i]);

      assert st.isLLRB(st.root, st.min(), st.max());
    }

    System.out.println("All tests pass");
  }
}
