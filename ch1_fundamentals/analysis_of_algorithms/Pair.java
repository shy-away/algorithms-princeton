package ch1_fundamentals.analysis_of_algorithms;

public class Pair<Item> {
  private final Item a, b;

  public Pair(Item a, Item b) {
    this.a = a;
    this.b = b;
  }

  public Item getA() {
    return a;
  }

  public Item getB() {
    return b;
  }

  public String toString() {
    return "(" + a + ", " + b + ")";
  }
}
