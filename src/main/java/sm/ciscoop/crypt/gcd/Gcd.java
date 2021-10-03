package sm.ciscoop.crypt.gcd;

import java.math.BigInteger;

public class Gcd {

  /**
   * per l'originale vedi la <A href=
   * "https://introcs.cs.princeton.edu/java/99crypto/ExtendedEuclid.java.html">seguente
   * pagina</a><br/>
   *
   * L'algoritmo euclideo esteso &egrave; particolarmente utile quando
   * <code>a</code> e <code>b</code> sono <b>coprimi</b> (o mcd &egrave; = 1)<br/>
   * poiché <code>x</code> &egrave; l'inverso moltiplicativo modulare di
   * "<code>a mod b</code>", ergo <code>AX mod B = 1</code><br/>
   *  e <code>y</code> &egrave; l'inverso moltiplicativo
   * modulare di "<code>b mod a</code>", ergo <code>BY mod A = 1</code><br/>
   * Il calcolo dell'inverso moltiplicativo modulare &egrave; un passaggio
   * essenziale nel metodo di crittografia a chiave pubblica <b>RSA</b>.
   *
   * @param a
   *          primo intero
   * @param b
   *          secondo intero
   * @return un record {@link GcdRec} che contiene il resto e i due valori di x
   *         e y
   */
  public static GcdRec gcd(BigInteger a, BigInteger b) {
    if (b == null || b.equals(BigInteger.ZERO))
      return new GcdRec(a, BigInteger.ONE, BigInteger.ZERO);
    GcdRec res = Gcd.gcd(b, a.mod(b));
    BigInteger d = res.resto();
    BigInteger x = res.y();
    BigInteger y = res.x() //
        .subtract( //
            a.divide(b) //
                .multiply(x) //
        );
    return new GcdRec(d, x, y);
  }

  /**
   * Check to see if <code>( a * x ) % m == 1</code>
   *
   * @param a
   * @param m
   * @return
   */
  public static BigInteger multModInvNaif(BigInteger a, BigInteger m) {
    BigInteger ret = BigInteger.ZERO;
    BigInteger am = null;
    // long k = 0;

    for (ret = BigInteger.ONE; ret.compareTo(m) < 0; ret = ret.add(BigInteger.ONE)) {
      am = a.multiply(ret).mod(m);
      // k++;
      if (am.equals(BigInteger.ONE))
        break;
    }
    return ret;
  }

  /**
   * Cerca il minimo comune multiplo fra due interi
   *
   * @param a
   * @param b
   * @return
   */
  public static BigInteger lcm(BigInteger a, BigInteger b) {
    BigInteger minMul = BigInteger.ZERO;

    if (a == null || b == null || (a.compareTo(BigInteger.ZERO) <= 0) || (b.compareTo(BigInteger.ZERO) <= 0))
      return minMul;
    minMul = a.divide(a.gcd(b)).multiply(b).abs();

    //    BigInteger piccolo = a.compareTo(b) > 0 ? b : a;
    //    BigInteger grande = a.compareTo(b) > 0 ? a : b;
    //    minMul = piccolo;
    //    BigInteger modMul = minMul.mod(grande);
    //
    //    while ( !modMul.equals(BigInteger.ZERO)) {
    //      minMul = minMul.add(piccolo);
    //      modMul = minMul.mod(grande);
    //    }
    return minMul;
  }
}
