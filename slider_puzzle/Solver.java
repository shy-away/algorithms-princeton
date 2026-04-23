import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
  private final SearchNode solution;

  private class SearchNode implements Comparable<SearchNode> {
    final Board board;
    final int moves, manhattan, priority;
    final SearchNode prev;

    public SearchNode(Board board, int moves, SearchNode prev) {
      this.board = board;
      this.moves = moves;
      this.prev = prev;
      manhattan = board.manhattan();
      priority = moves + manhattan;
    }

    public int compareTo(SearchNode that) {
      int thisPriorityM = this.priority;
      int thatPriorityM = that.priority;

      if (thisPriorityM < thatPriorityM)
        return -1;
      else if (thisPriorityM > thatPriorityM)
        return 1;

      int thisManhattan = this.manhattan;
      int thatManhattan = that.manhattan;
      if (thisManhattan < thatManhattan)
        return -1;
      else if (thisManhattan > thatManhattan)
        return 1;
      return 0;
    }
  }

  public Solver(Board initial) {
    if (initial == null)
      throw new IllegalArgumentException("Initial board cannot be null");

    SearchNode min = new SearchNode(initial, 0, null);
    SearchNode minTwin = new SearchNode(initial.twin(), 0, null);
    MinPQ<SearchNode> pq = new MinPQ<>();
    pq.insert(min);
    MinPQ<SearchNode> pqTwin = new MinPQ<>();
    pqTwin.insert(minTwin);

    while (!min.board.isGoal() && !minTwin.board.isGoal()) {
      min = pq.delMin();
      minTwin = pqTwin.delMin();

      for (Board neighbor : min.board.neighbors()) {
        if (min.prev != null && min.prev.board.equals(neighbor))
          continue;
        pq.insert(new SearchNode(neighbor, min.moves + 1, min));
      }

      for (Board neighbor : minTwin.board.neighbors()) {
        if (minTwin.prev != null && minTwin.prev.board.equals(neighbor))
          continue;
        pqTwin.insert(new SearchNode(neighbor, minTwin.moves + 1, minTwin));
      }
    }

    if (!min.board.isGoal()) {
      min = null;
    }

    solution = min;
  }

  public boolean isSolvable() {
    return solution != null;
  }

  public int moves() {
    if (solution == null)
      return -1;
    return solution.moves;
  }

  public Iterable<Board> solution() {
    if (solution == null)
      return null;

    Stack<Board> boards = new Stack<>();

    for (SearchNode x = solution; x != null; x = x.prev) {
      boards.push(x.board);
    }

    return boards;
  }

  // Note: This method was provided by Princeton.
  // https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/specification.php
  public static void main(String[] args) {

    // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] tiles = new int[n][n];
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        tiles[i][j] = in.readInt();
    Board initial = new Board(tiles);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable())
      StdOut.println("No solution possible");
    else {
      StdOut.println("Minimum number of moves = " + solver.moves());
      for (Board board : solver.solution())
        StdOut.println(board);
    }
  }
}
