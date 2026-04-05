package ch1_fundamentals.union_find;

import edu.princeton.cs.algs4.StdRandom;

public class RandomGrid {

  public static Connection[] generate(int N) {
    RandomizedQueue<Connection> rq = new RandomizedQueue<>();

    int gridSize = N * N;

    for (int i = 0; i < gridSize; i++) {
      // if not on right edge of grid, make connection to the right
      if (i % N != N - 1) {
        if (StdRandom.bernoulli(.5)) {
          rq.enqueue(new Connection(i, i + 1));
        } else {
          rq.enqueue(new Connection(i + 1, i));
        }
      }

      // if not on bottom edge, make connection to the bottom
      if (i < gridSize - N) {
        if (StdRandom.bernoulli(.5)) {
          rq.enqueue(new Connection(i, i + N));
        } else {
          rq.enqueue(new Connection(i + N, i));
        }
      }
    }

    Connection[] connections = new Connection[2 * N * (N - 1)];
    int i = 0;

    for (Connection c : rq) {
      connections[i] = c;
      i++;
    }

    return connections;
  }

  public static void main(String[] args) {
    int N = Integer.parseInt(args[0]);
    
    Connection[] connections = generate(N);

    for(int i = 0; i < connections.length; i++) {
      System.out.println(connections[i]);
    }
  }
}
