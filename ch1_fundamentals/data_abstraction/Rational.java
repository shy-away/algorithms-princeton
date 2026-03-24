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
    long newDenominator = lcd(this.getDenominator(), b.getDenominator());

    // determine conversion multiplier for each Rational
    long conversionThis = newDenominator / this.getDenominator();
    long conversionThat = newDenominator / b.getDenominator();

    // multiply and add numerators
    long numeratorThis = this.getNumerator() * conversionThis;
    long numeratorThat = b.getNumerator() * conversionThat;
    long newNumerator = numeratorThis + numeratorThat;

    // check numerator overflow
    if (newNumerator > Integer.MAX_VALUE) {
      throw new ArithmeticException("Numerator overflow");
    }

    if (newNumerator < Integer.MIN_VALUE) {
      throw new ArithmeticException("Numerator underflow");
    }

    // check denominator overflow
    if (newDenominator > Integer.MAX_VALUE) {
      // attempt to reduce
      long gcd = gcd(newNumerator, newDenominator);
      if (gcd > 1) {
        newNumerator /= gcd;
        newDenominator /= gcd;
      }
      if (newDenominator > Integer.MAX_VALUE) {
        throw new ArithmeticException("Denominator overflow");
      }
    }

    // return new Rational
    return new Rational((int) newNumerator, (int) newDenominator);
  }

  /**
   * Subtract another Rational from this Rational.
   * 
   * @param b the other Rational to subtract
   * @return new Rational result
   */
  public Rational minus(Rational b) {
    Rational negB = b.times(new Rational(-1, 1));
    return this.plus(negB);
  }

  /**
   * Multiply this Rational with another Rational.
   * 
   * @param b the Rational to multiply with
   * @return a new Rational product
   */
  public Rational times(Rational b) {
    int numeratorThis = (int) this.getNumerator();
    int denominatorThis = (int) this.getDenominator();
    int numeratorThat = (int) b.getNumerator();
    int denominatorThat = (int) b.getDenominator();

    // cross-check fractions
    int gcd1 = (int) gcd(numeratorThis, denominatorThat);
    int gcd2 = (int) gcd(denominatorThis, numeratorThat);

    if (gcd1 > 1) {
      numeratorThis /= gcd1;
      denominatorThat /= gcd1;
    }

    if (gcd2 > 1) {
      denominatorThis /= gcd2;
      numeratorThat /= gcd2;
    }

    // multiply simplified numerators and denominators
    int newNumerator = Math.multiplyExact(numeratorThis, numeratorThat);
    int newDenominator = Math.multiplyExact(denominatorThis, denominatorThat);

    return new Rational(newNumerator, newDenominator);
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
   * Get String version of this Rational, in the form "{@code n / d}".
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

    // --- OVERFLOW/UNDERFLOW ---

    // addition, numerator overflow
    a = new Rational(Integer.MAX_VALUE, 3);
    b = new Rational(Integer.MAX_VALUE, 3);
    boolean hasErred = false;
    try {
      c = a.plus(b);
    } catch (Exception e) {
      hasErred = true;
    }
    assert hasErred : "Addition numerator overflow should cause error";

    // addition, denominator overflow
    a = new Rational(1, 46337);
    b = new Rational(1, 46349);
    hasErred = false;
    try {
      c = a.plus(b);
    } catch (Exception e) {
      hasErred = true;
    }
    assert hasErred : "Addition denominator overflow should cause error";

    // multiplication, numerator overflow
    a = new Rational(Integer.MAX_VALUE, 1);
    b = new Rational(2, 1);
    hasErred = false;
    try {
      c = a.times(b);
    } catch (Exception e) {
      hasErred = true;
    }
    assert hasErred : "Multiplication numerator overflow should cause error";

    // multiplication, denominator underflow
    a = new Rational(1, -46337);
    b = new Rational(1, -46349);
    hasErred = false;
    try {
      c = a.times(b);
    } catch (Exception e) {
      hasErred = true;
    }
    assert hasErred : "Multiplication denominator underflow should cause error";

    // subtraction, numerator underflow
    a = new Rational(Integer.MIN_VALUE, 1);
    b = new Rational(1, 1);
    hasErred = false;
    try {
      c = a.minus(b);
    } catch (Exception e) {
      hasErred = true;
    }
    assert hasErred : "Subtraction numerator underflow should cause error";
  }
}
