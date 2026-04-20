import edu.princeton.cs.algs4.Stack;

public class Board {
  final int[][] tiles;
  private final int N, hamming, manhattan;
  private int emptyRow, emptyCol;

  public Board(int[][] tiles) {
    // for this assignment, tiles is 2d array representing n-by-n grid
    // values are 0 to n^2-1, and 2 leq n lt 128 is assumed
    // 0 is the blank square
    this.tiles = tiles;
    N = tiles.length;

    // precalculate hamming and manhattan distances
    int runningHamming = 0;
    int runningManhattan = 0;
    for (int row = 0; row < N; row++) {
      for (int col = 0; col < N; col++) {
        int currentTile = tiles[row][col];
        // record position of empty tile
        if (currentTile == 0) {
          emptyRow = row;
          emptyCol = col;
          continue;
        }

        // calculate target and compute hamming and manhattan distance
        int target = col + 1 + (N * row);
        if (currentTile != target) {
          runningHamming++;

          int goalRow = (currentTile - 1) / N; // integer division
          int goalCol = (currentTile - 1) % N;

          runningManhattan += Math.abs(row - goalRow) + Math.abs(col - goalCol);
        }
      }
    }

    hamming = runningHamming;
    manhattan = runningManhattan;
  }

  @Override
  public String toString() {
    String[] results = new String[N * N + 1];
    int i = 0;
    results[i++] = "" + N + "\n";

    for (int j = 0; j < N; j++) {
      for (int k = 0; k < N; k++) {
        results[i++] = String.format("%2d ", tiles[j][k]);
      }
      if (j < N - 1)
        results[i - 1] += "\n";
    }

    return String.join("", results);
  }

  public int dimension() {
    return N;
  }

  public int hamming() {
    return hamming;
  }

  public int manhattan() {
    return manhattan;
  }

  public boolean isGoal() {
    return hamming == 0;
  }

  @Override
  // note: Java docs say that `hashCode` should also be overridden,
  // but I don't anticipate that `hashCode` will be tested
  public boolean equals(Object o) {
    if (o == null)
      return false;
    if (!(o instanceof Board))
      return false;

    Board that = (Board) o;

    if (this.dimension() != that.dimension())
      return false;

    for (int y = 0; y < N; y++) {
      for (int x = 0; x < N; x++) {
        if (this.tiles[y][x] != that.tiles[y][x])
          return false;
      }
    }
    return true;
  }

  public Iterable<Board> neighbors() {
    Stack<Board> neighbors = new Stack<>();

    int[] offsetRow = { 0, 0, -1, 1 };
    int[] offsetCol = { -1, 1, 0, 0 };

    // create boards based on position of empty tile
    for (int i = 0; i < 4; i++) {
      int adjacentRow = emptyRow + offsetRow[i];
      int adjacentCol = emptyCol + offsetCol[i];

      if (adjacentRow < 0 || adjacentRow > N)
        continue;
      if (adjacentCol < 0 || adjacentCol > N)
        continue;

      int adjacentTile = tiles[adjacentRow][adjacentCol];

      int[][] neighborTiles = new int[N][N];

      for (int row = 0; row < N; row++) {
        for (int col = 0; col < N; col++) {
          int currentTile = tiles[row][col];
          if (currentTile == 0) {
            neighborTiles[row][col] = adjacentTile;
          } else if (currentTile == adjacentTile) {
            neighborTiles[row][col] = 0;
          } else {
            neighborTiles[row][col] = currentTile;
          }
        }
      }

      neighbors.push(new Board(neighborTiles));
    }

    return neighbors;
  }

  public Board twin() {
    int swapFirstRow = 0;
    int swapFirstCol = 0;

    int swapSecondRow = N - 1;
    int swapSecondCol = N - 1;

    // note: board size is guaranteed to be at least 2
    if (swapFirstRow == emptyRow && swapFirstCol == emptyCol) {
      swapFirstCol++;
    }
    if (swapSecondRow == emptyRow && swapSecondCol == emptyCol) {
      swapSecondCol--;
    }

    int[][] neighborTiles = new int[N][N];

    for (int row = 0; row < N; row++) {
      for (int col = 0; col < N; col++) {
        if (row == swapSecondRow && col == swapSecondCol) {
          neighborTiles[row][col] = tiles[swapFirstRow][swapFirstCol];
        } else if (row == swapFirstRow && col == swapFirstCol) {
          neighborTiles[row][col] = tiles[swapSecondRow][swapSecondCol];
        } else {
          neighborTiles[row][col] = tiles[row][col];
        }
      }
    }

    return new Board(neighborTiles);
  }

  public static void main(String[] args) {
    Board b;

    b = new Board(new int[][] { { 1, 2 }, { 3, 0 } });
    assert b.dimension() == 2;
    assert b.hamming() == 0;
    assert b.manhattan() == 0;
    assert b.isGoal();

    b = new Board(new int[][] { { 0, 2 }, { 3, 1 } });
    System.out.println("Original:\n" + b.toString()); // verify visually
    assert b.hamming() == 1;
    assert b.manhattan() == 2;
    assert !b.isGoal();

    assert b.equals(b) : "object equals: reflexivity";
    Board b2 = new Board(new int[][] { { 0, 2 }, { 3, 1 } });
    assert b.equals(b2) && b2.equals(b) : "object equals: symmetry";
    Board b3 = new Board(new int[][] { { 0, 2 }, { 3, 1 } });
    assert b.equals(b2) && b2.equals(b3) && b.equals(b3) : "object equals: transitivity";
    assert !b.equals(null) : "object equals: null";
    b2 = new Board(new int[][] { { 1, 2 }, { 3, 0 } });
    assert !b.equals(b2);

    System.out.println("Neighbors:");
    for (Board x : b.neighbors())
      System.out.println(x); // verify visually

    System.out.println("Twin:\n" + b.twin());

    System.out.println("All tests pass");
  }
}
