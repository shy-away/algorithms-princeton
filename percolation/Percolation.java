import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private boolean[] siteStatuses;
  private int gridSideLength, openSites, topVirtualNode, bottomVirtualNode;
  private WeightedQuickUnionUF sites;

  public Percolation(int n) {
    if (n <= 0)
      throw new IllegalArgumentException();

    gridSideLength = n;
    siteStatuses = new boolean[n * n];
    openSites = 0;
    sites = new WeightedQuickUnionUF(n * n + 2); // O(n^2) constructor

    // note: WeightedQuickUnionUF is 0-indexed internally,
    // so bottomVirtualNode is actually the last node
    topVirtualNode = n * n;
    bottomVirtualNode = n * n + 1;
  }

  public void open(int row, int col) {
    validate(row);
    validate(col);

    int currentNode = xyTo1D(row, col);

    siteStatuses[currentNode] = true;
    openSites++;

    if (col - 1 > 0) {
      int leftNode = xyTo1D(row, col - 1);
      if (siteStatuses[leftNode]) {
        sites.union(currentNode, leftNode);
      }
    }

    if (col + 1 <= gridSideLength) {
      int rightNode = xyTo1D(row, col + 1);
      if (siteStatuses[rightNode]) {
        sites.union(currentNode, rightNode);
      }
    }

    if (row - 1 > 0) {
      int aboveNode = xyTo1D(row - 1, col);
      if (siteStatuses[aboveNode]) {
        sites.union(currentNode, aboveNode);
      }
    } else {
      // top edge, union with top virtual node
      sites.union(currentNode, topVirtualNode);
    }

    if (row + 1 <= gridSideLength) {
      int belowNode = xyTo1D(row + 1, col);
      if (siteStatuses[belowNode]) {
        sites.union(currentNode, belowNode);
      }
    } else {
      // bottom edge, union with bottom virtual node
      sites.union(currentNode, bottomVirtualNode);
    }
  }

  public boolean isOpen(int row, int col) {
    validate(row);
    validate(col);

    return siteStatuses[xyTo1D(row, col)];
  }

  public boolean isFull(int row, int col) {
    validate(row);
    validate(col);

    int currentNode = xyTo1D(row, col);

    return sites.find(currentNode) == sites.find(topVirtualNode);
  }

  public int numberOfOpenSites() {
    return openSites;
  }

  public boolean percolates() {
    return sites.find(topVirtualNode) == sites.find(bottomVirtualNode);
  }

  /**
   * Takes a 1-indexed 2D grid coordinate pair and outputs a 0-indexed result.
   * 
   * @param x 2d grid x-coordinate
   * @param y 2d grid y-coordinate
   * @return 1-dimensional 0-indexed result
   */
  private int xyTo1D(int x, int y) {
    validate(x);
    validate(y);
    return ((y - 1) * gridSideLength) + (x - 1);
  }

  private void validate(int index) {
    if (index < 1 || index > gridSideLength) {
      throw new IllegalArgumentException("index " + index + " out of bounds 1 to " + gridSideLength);
    }
  }

  public static void main(String[] args) {
    Percolation p;
    boolean hasErred;

    // constructor
    hasErred = false;
    try {
      p = new Percolation(0);
    } catch (Exception e) {
      hasErred = true;
    }
    assert hasErred;

    // xyTo1D mapping
    p = new Percolation(5);

    assert p.xyTo1D(1, 1) == 0;
    assert p.xyTo1D(5, 1) == 4;
    assert p.xyTo1D(1, 2) == 5;
    assert p.xyTo1D(3, 2) == 7;
    assert p.xyTo1D(5, 5) == 24;

    // open()
    p = new Percolation(2);

    hasErred = false;
    try {
      p.open(0, 1);
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    hasErred = false;
    try {
      p.open(1, 3);
    } catch (IllegalArgumentException e) {
      hasErred = true;
    }
    assert hasErred;

    p.open(1, 1);
    assert p.isOpen(1, 1);
    assert p.numberOfOpenSites() == 1;

    p.open(2, 1);
    assert p.isOpen(2, 1);
    assert p.numberOfOpenSites() == 2;
  }
}
