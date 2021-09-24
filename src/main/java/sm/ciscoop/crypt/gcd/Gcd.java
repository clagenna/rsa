package sm.ciscoop.crypt.gcd;

import java.math.BigInteger;

public class Gcd {

  /**
   * per l'originale vedi la <A href=
   * "https://introcs.cs.princeton.edu/java/99crypto/ExtendedEuclid.java.html">seguente
   * pagina</a>
   *
   * @param a
   * @param b
   * @return
   */
  public GcdRec gcd(BigInteger a, BigInteger b) {
    if (b == null || b.equals(BigInteger.ZERO))
      return new GcdRec(a, BigInteger.ONE, BigInteger.ZERO);

    GcdRec res = gcd(b, a.mod(b));
    BigInteger d = res.resto();
    BigInteger x = res.y();
    BigInteger y = res.x() //
        .subtract( //
            a.divide(b) //
                .multiply(x) //
        );
    return new GcdRec(d, x, y);
  }
}
