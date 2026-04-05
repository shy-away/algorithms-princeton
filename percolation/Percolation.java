import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private boolean[] sitesOpen, treesConnectedToBottom;
  private boolean percolates;
  private int gridSideLength, numOpenSites, topVirtualNode;
  private WeightedQuickUnionUF sites;

  public Percolation(int n) {
    if (n <= 0)
      throw new IllegalArgumentException();

    gridSideLength = n;
    sitesOpen = new boolean[n * n];
    treesConnectedToBottom = new boolean[n * n];
    numOpenSites = 0;
    sites = new WeightedQuickUnionUF(n * n + 2); // O(n^2) constructor

    // note: WeightedQuickUnionUF is 0-indexed internally,
    // so topVirtualNode is actually the last node
    topVirtualNode = n * n;
  }

  public void open(int row, int col) {
    validate(row);
    validate(col);

    int currentNode = rowColTo1D(row, col);

    sitesOpen[currentNode] = true;
    numOpenSites++;

    int currentNodeRoot = sites.find(currentNode);
    boolean willTouchBottom;
    int[] offsetRow = { 0, 0, -1, 1 };
    int[] offsetCol = { -1, 1, 0, 0 };

    for (int i = 0; i < 4; i++) {
      int neighborRow = row + offsetRow[i];
      int neighborCol = col + offsetCol[i];

      if (neighborCol < 1 || neighborCol > gridSideLength)
        continue;

      if (neighborRow < 1) {
        // top edge, union with top virtual node
        sites.union(currentNode, topVirtualNode);
        continue;
      }

      if (neighborRow > gridSideLength) {
        // bottom edge
        treesConnectedToBottom[currentNodeRoot] = true;
        continue;
      }

      int neighborNode = rowColTo1D(neighborRow, neighborCol);
      int neighborNodeRoot = sites.find(neighborNode);

      if (sitesOpen[neighborNode] && currentNodeRoot != neighborNodeRoot) {
        willTouchBottom = treesConnectedToBottom[currentNodeRoot] || treesConnectedToBottom[neighborNodeRoot];
        sites.union(currentNode, neighborNodeRoot);
        currentNodeRoot = sites.find(currentNode);
        treesConnectedToBottom[currentNodeRoot] = willTouchBottom;
      }
    }
    
    if (treesConnectedToBottom[currentNodeRoot] && currentNodeRoot == sites.find(topVirtualNode)) {
      percolates = true;
    }
  }

  public boolean isOpen(int row, int col) {
    validate(row);
    validate(col);

    return sitesOpen[rowColTo1D(row, col)];
  }

  public boolean isFull(int row, int col) {
    validate(row);
    validate(col);

    int currentNode = rowColTo1D(row, col);

    return sites.find(currentNode) == sites.find(topVirtualNode);
  }

  public int numberOfOpenSites() {
    return numOpenSites;
  }

  public boolean percolates() {
    return percolates;
  }

  /**
   * Takes a 1-indexed 2D grid coordinate pair and outputs a 0-indexed result.
   * 
   * @param row 2d grid row coordinate
   * @param col 2d grid column coordinate
   * @return 1-dimensional 0-indexed result
   */
  private int rowColTo1D(int row, int col) {
    validate(row);
    validate(col);
    return ((row - 1) * gridSideLength) + (col - 1);
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

    assert p.rowColTo1D(1, 1) == 0;
    assert p.rowColTo1D(1, 5) == 4;
    assert p.rowColTo1D(2, 1) == 5;
    assert p.rowColTo1D(2, 3) == 7;
    assert p.rowColTo1D(5, 5) == 24;

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

    // all methods
    assert !p.isOpen(2, 1);
    p.open(2, 1);
    assert p.isOpen(2, 1);
    assert !p.isFull(2, 1);
    assert p.numberOfOpenSites() == 1;
    assert !p.percolates();

    p.open(1, 1);
    assert p.isOpen(1, 1);
    assert p.isFull(1, 1);
    assert p.isFull(2, 1);
    assert p.numberOfOpenSites() == 2;
    assert p.percolates();

    p = new Percolation(5);
    p.open(1, 3);
    p.open(2, 3);
    p.open(3, 3);
    p.open(4, 4);
    p.open(5, 4);
    assert !p.percolates();
    p.open(4, 3);
    assert p.percolates();
  }
}
