package ch1_fundamentals.data_abstraction;

public class Rational {
  private final long numerator;
  private final long denominator;

  /**
   * Create a new Rational object to represent a rational number.
   * 
   * @param numerator   numerator of the rational number
   * @param denominator denominator of the rational number
   */
  public Rational(int numerator, int denominator) {
    long gcd = gcd(numerator, denominator);

    if (gcd != 1) {
      numerator /= gcd;
      denominator /= gcd;
    }

    this.numerator = numerator;
    this.denominator = denominator;
  }

  /**
   * Determine greatest common divisor (Euclid's algorithm, recursion).
   */
  private long gcd(long p, long q) {
    if (q == 0)
      return p;
    long r = p % q;
    return gcd(q, r);
  }

  /**
   * Determine the lowest common denominator.
   * 
   * @see https://en.wikipedia.org/wiki/Least_common_multiple#Using_the_greatest_common_divisor
   */
  private long lcd(long a, long b) {
    return Math.abs(a) * (Math.abs(b) / gcd(a, b));
  }

  private long getNumerator() {
    return numerator;
  }

  private long getDenominator() {
    return denominator;
  }

  /**
   * Add two Rationals.
   * 
   * @param b the Rational to add
   * @return new Rational sum
   */
  public Rational plus(Rational b) {
    long lcd = lcd(this.getDenominator(), b.getDenominator());

    // determine conversion multiplier for each Rational
    long conversionThis = lcd / this.getDenominator();
    long conversionThat = lcd / b.getDenominator();

    // multiply and add numerators
    // TODO: check for overflow
    long numeratorThis = this.getNumerator() * conversionThis;
    long numeratorThat = b.getNumerator() * conversionThat;
    long newNumerator = numeratorThis + numeratorThat;

    // return new Rational (constructor attempts to reduce)
    return new Rational((int) newNumerator, (int) lcd);
  }

  /**
   * Subtract another Rational from this Rational.
   * 
   * @param b the other Rational to subtract
   * @return new Rational result
   */
  public Rational minus(Rational b) {
    long lcd = lcd(this.getDenominator(), b.getDenominator());

    // determine conversion multiplier for each Rational
    long conversionThis = lcd / this.getDenominator();
    long conversionThat = lcd / b.getDenominator();

    // multiply and subtract numerators
    // TODO: check for overflow
    long numeratorThis = this.getNumerator() * conversionThis;
    long numeratorThat = b.getNumerator() * conversionThat;
    long newNumerator = numeratorThis - numeratorThat;

    return new Rational((int) newNumerator, (int) lcd);
  }

  /**
   * Multiply this Rational with another Rational.
   * 
   * @param b the Rational to multiply with
   * @return a new Rational product
   */
  public Rational times(Rational b) {
    long numeratorThis = this.getNumerator();
    long denominatorThis = this.getDenominator();
    long numeratorThat = b.getNumerator();
    long denominatorThat = b.getDenominator();

    // cross-check fractions
    long gcd1 = gcd(numeratorThis, denominatorThat);
    long gcd2 = gcd(denominatorThis, numeratorThat);

    if (gcd1 > 1) {
      numeratorThis /= gcd1;
      denominatorThat /= gcd1;
    }

    if (gcd2 > 1) {
      denominatorThis /= gcd2;
      numeratorThat /= gcd2;
    }

    // multiply simplified numerators and denominators
    // TODO: check for overflow
    long newNumerator = numeratorThis * numeratorThat;
    long newDenominator = denominatorThis * denominatorThat;

    return new Rational((int) newNumerator, (int) newDenominator);
  }

  /**
   * Divide this Rational by another rational.
   * 
   * @param b the other rational to divide by
   * @return new Rational quotient
   */
  public Rational divides(Rational b) {
    // get reciprocal of b
    Rational reciprocalB = new Rational((int) b.getDenominator(), (int) b.getNumerator());

    return this.times(reciprocalB);
  }

  /**
   * Determine whether this and another Rational are equal.
   * 
   * @param that another Rational to compare
   * @return boolean indicating whether this and the other Rational are equal
   */
  public boolean equals(Rational that) {
    if (numerator == that.getNumerator() &&
        denominator == that.getDenominator()) {
      return true;
    }
    return false;
  }

  /**
   * Get String version of this Rational.
   */
  public String toString() {
    return getNumerator() + " / " + getDenominator();
  }

  public static void main(String[] args) {
    // constructor and equals
    Rational a = new Rational(4, 5);
    Rational b = new Rational(4, 5);

    assert a.equals(b) : "Equals";

    b = new Rational(8, 10);

    assert a.equals(b) : "Reducing on construction";

    // addition
    Rational c = a.plus(b);

    assert c.equals(new Rational(8, 5)) : "Addition, same denominators";

    a = new Rational(3, 7);
    b = new Rational(2, 6);

    c = a.plus(b);

    assert c.equals(new Rational(32, 42)) : "Addition, different denominators";

    // subtraction
    c = a.minus(b);

    assert c.equals(new Rational(4, 42)) : "Subtraction";

    // multiplication
    c = a.times(b);

    assert c.equals(new Rational(1, 7)) : "Multiplication";

    // division
    c = a.divides(b);

    assert c.equals(new Rational(9, 7)) : "Division";
  }
}
