package test.rsa;

import java.math.BigInteger;

public class TestGCD {

  /**
   * Accepts arguments p and q and compute the greatest common divisor of p and
   * q using the extended Euclid's algorithm. Also prints out integers a and b
   * such that a(p) + b(q) = gcd(p, q). return array [d, a, b] such that d =
   * gcd(p, q), ap + bq = d
   *
   * @param p
   * @param q
   * @return
   */
  public int[] gcd(int p, int q) {
    if (q == 0)
      return new int[] { p, 1, 0 };

    int[] vals = gcd(q, p % q);
    int d = vals[0];
    int a = vals[2];
    int b = vals[1] - (p / q) * vals[2];
    return new int[] { d, a, b };
  }

  public int gcdr(int a, int b) {
    if (b == 0)
      return a;
    return gcdr(b, a % b);
  }

  public static void main(String[] args) {
    TestGCD app = new TestGCD();
    int p /* = Integer.parseInt(args[0]) */;
    int q /* = Integer.parseInt(args[1]) */;
    p = 1031;
    q = 1033;
    p = 47;
    q = 31;
    if (p <= 0 || q <= 0)
      throw new IllegalArgumentException("p and q must be positive integers");
    int vals[] = app.gcd(p, q);
    int ret = app.gcdr(p, q);
    System.out.println("gcd(" + p + ", " + q + ") = " + vals[0]);
    System.out.println(vals[1] + "(" + p + ") + " + vals[2] + "(" + q + ") = " + vals[0]);
    System.out.println("gcdr(" + p + ", " + q + ") = " + ret);

    BigInteger bip = BigInteger.valueOf(29);
    BigInteger biq = BigInteger.valueOf(31);
    bip = BigInteger.valueOf(29 * 47);
    biq = BigInteger.valueOf(31 * 47);
    BigInteger[] arr = app.gcd(bip, biq);
    System.out.println("gcd(" + bip + ", " + biq + ") = " + arr[0]);
    System.out.println(arr[1] + "(" + bip + ") + " + arr[2] + "(" + biq + ") = " + arr[0]);

  }

  public BigInteger[] gcd(BigInteger p_p, BigInteger p_q) {
    BigInteger p = p_p;
    BigInteger q = p_q;
    if (q.compareTo(p) > 0) {
      p = p_q;
      q = p_p;
    }

    if (p_q.equals(BigInteger.ZERO))
      return new BigInteger[] { p, BigInteger.ONE, BigInteger.ZERO };

    BigInteger[] vals = gcd(q, p.mod(q));
    BigInteger d = vals[0];
    BigInteger a = vals[2];
    BigInteger b = vals[1] //
        .subtract( //
            p.divide(q) //
                .multiply(a));
    System.out.printf("p=%d, q=%d, d=%d, a=%d, b=%d\n", p, q, d, a, b);
    return new BigInteger[] { d, a, b };
  }

}
