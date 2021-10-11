package sm.clagenna.crypt.gcd;

import java.math.BigInteger;
import java.text.NumberFormat;

public class Gcd {

  private static boolean debug;

  public Gcd() {
    //
  }

  /**
   * Sinonimo per {@link gcd} (da inglese <i>Greatest common divisors</i> ) ma
   * in italiano <i><b>m</b>assimo <b>c</b>omun <b>d</b>ivisore</i>
   * 
   * @param a
   * @param b
   * @return
   */
  public static GcdRec mcd(BigInteger a, BigInteger b) {
    return gcd(a, b);
  }

  /**
   * per l'originale vedi la <A href=
   * "https://introcs.cs.princeton.edu/java/99crypto/ExtendedEuclid.java.html">seguente
   * pagina</a><br/>
   *
   * L'algoritmo euclideo esteso &egrave; particolarmente utile quando
   * <code>a</code> e <code>b</code> sono <b>coprimi</b> (o mcd &egrave; =
   * 1)<br/>
   * poiché <code>x</code> &egrave; l'inverso moltiplicativo modulare di
   * "<code>a mod b</code>", ergo <code>AX mod B = 1</code><br/>
   * e <code>y</code> &egrave; l'inverso moltiplicativo modulare di
   * "<code>b mod a</code>", ergo <code>BY mod A = 1</code><br/>
   * Il calcolo dell'inverso moltiplicativo modulare &egrave; un passaggio
   * essenziale nel metodo di crittografia a chiave pubblica <b>RSA</b>.</br>
   * dove i coefficienti X e Y sono il risultato:<br/>
   * <code>x<sub>i</sub> = y<sub>i-1</sub></br></code>
   * <code>y<sub>i</sub> = x<sub>i-1</sub> - a / b * y<sub>i-1</sub></code></br>
   * ergo:<br/>
   * <code>y<sub>i</sub> = x<sub>i-1</sub> - a / b * x<sub>i</sub></code>
   *
   *
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
    /** y<inf>i</inf> = a / b * y<inf>i-1</inf> */
    BigInteger y = res.x() //
        .subtract( //
            a.divide(b) //
                .multiply(x) //
        );
    if (Gcd.isDebug())
      Gcd.printGcd(a, b, d, x, y);
    return new GcdRec(d, x, y);
  }

  /**
   * vedi {@link gcd} per la spiegazione. questa è fatta in forma <b>non</b>
   * ricorsiva
   *
   * @param p_a
   *          primo intero
   * @param p_b
   *          secondo intero
   * @return un record {@link GcdRec} che contiene il resto e i due valori di x
   *         e y
   */
  public static GcdRec gcdnor(BigInteger p_a, BigInteger p_b) {
    BigInteger a = p_a, b = p_b;
    if (b == null || b.signum() == 0)
      return new GcdRec(a, BigInteger.ONE, BigInteger.ZERO);
    BigInteger unPrev = BigInteger.ONE;
    BigInteger vnPrev = BigInteger.ZERO;
    BigInteger unCur = BigInteger.ZERO;
    BigInteger vnCur = BigInteger.ONE;

    while (b.signum() != 0) {
      BigInteger bn = a; // b
      BigInteger newB = a.mod(b);
      a = b;
      b = newB;

      // Update coefficients
      BigInteger unNew = unPrev.subtract(bn.multiply(unCur));
      BigInteger vnNew = vnPrev.subtract(bn.multiply(vnCur));

      //  Shift coefficients
      unPrev = unCur;
      vnPrev = vnCur;
      unCur = unNew;
      vnCur = vnNew;
    }
    GcdRec ret = new GcdRec(a, unPrev, vnPrev);
    if (Gcd.isDebug())
      Gcd.printGcd(p_a, p_b, ret.resto(), ret.x(), ret.y());
    return ret;
  }

  private static void printGcd(BigInteger a, BigInteger b, BigInteger d, BigInteger x, BigInteger y) {
    NumberFormat fmt = NumberFormat.getIntegerInstance();
    System.out.printf("gcd(%s, %s)=%s\tx=%s\ty=%s\n", //
        fmt.format(a), //
        fmt.format(b), //
        fmt.format(d), //
        fmt.format(x), //
        fmt.format(y) //
    );
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
   * Cerca il minimo comune multiplo fra due interi</br>
   * <code>mcm = ( a / (mcd(a,b) ) * b</code>
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

  public static void setDebug(boolean p_b) {
    debug = p_b;
  }

  public static boolean isDebug() {
    return debug;
  }

}
