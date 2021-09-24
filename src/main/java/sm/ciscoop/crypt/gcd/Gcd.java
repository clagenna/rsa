package sm.ciscoop.crypt.gcd;

import java.math.BigInteger;

public class Gcd {

  /**
   * per l'originale vedi la <A href=
   * "https://introcs.cs.princeton.edu/java/99crypto/ExtendedEuclid.java.html">seguente
   * pagina</a><br/>
   * 
   * L'algoritmo euclideo esteso &egrave; particolarmente utile quando
   * <code>a</code> e <code>b</code> sono <b>coprimi</b> (o mcd &egrave; = 1)
   * poiché <code>x</code> &egrave; l'inverso moltiplicativo modulare di
   * "<code>a mod b</code>", e <code>y</code> &egrave; l'inverso moltiplicativo
   * modulare di "<code>b mod a</code>". <br/>
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
