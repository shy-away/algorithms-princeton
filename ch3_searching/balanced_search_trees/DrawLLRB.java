package ch3_searching.balanced_search_trees;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

public class DrawLLRB {
  public static void main(String[] args) {
    int T;
    try {
      T = Integer.parseInt(args[0]);
    } catch (ArrayIndexOutOfBoundsException oobe) {
      System.err.println("Add an argument for the maximum number of nodes, e.g. 100.");
      throw oobe;
    } catch (NumberFormatException nfe) {
      System.err.println("Make sure you're using a number, e.g. \"1000\" instead of \"one thousand\".");
      throw nfe;
    }

    StdDraw.setTitle("Left-Leaning Red-Black BST");
    StdDraw.setCanvasSize(1600, 600);
    StdDraw.setScale(-.1, 1.1);

    Runnable clearToBlack = () -> StdDraw.clear(StdDraw.BLACK);
    clearToBlack.run();

    int[] keys = new int[T];
    for (int i = 0; i < T; i++) {
      keys[i] = i;
    }
    
    RedBlackBST<Integer, Integer> st = new RedBlackBST<>();
    
    StdDraw.enableDoubleBuffering();

    // fancy math for setting decent minimum double-buffering delays
    int delay = Math.max((6000 / (T + 50)) + 15, (50 * T) / (T + 100));
    int intermission = 1000;

    Runnable drawTreeWithStats = () -> {
      st.draw(StdDraw.RED, StdDraw.WHITE);
      StdDraw.setPenColor(StdDraw.WHITE);
      StdDraw.textLeft(-0.05, 1.0, "n = " + st.size());
      StdDraw.textLeft(-0.05, 0.95, "height = " + st.maxHeight());
      StdDraw.textLeft(-0.05, 0.9, "average depth = " + st.avgHeight());
    };
    
    while (true) {
      StdRandom.shuffle(keys);
      for (int key : keys) {
        st.put(key, key);
        clearToBlack.run();
        drawTreeWithStats.run();
        StdDraw.show();
        StdDraw.pause(delay);
      }

      StdDraw.pause(intermission);

      // StdDraw.save("llrb.png");

      StdRandom.shuffle(keys);
      for (int key : keys) {
        st.delete(key);
        clearToBlack.run();
        drawTreeWithStats.run();
        StdDraw.show();
        StdDraw.pause(delay);
      }

      StdDraw.pause(intermission);
    }
  }
}
