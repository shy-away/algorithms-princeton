package ch1_fundamentals.union_find;

public class Connection {
  private final int p, q;

  public Connection(int p, int q) {
    this.p = p;
    this.q = q;
  }

  public int getP() {
    return p;
  }

  public int getQ() {
    return q;
  }

  public String toString() {
    return "(" + p + ", " + q + ")";
  }
}
