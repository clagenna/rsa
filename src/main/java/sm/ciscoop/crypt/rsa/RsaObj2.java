package sm.ciscoop.crypt.rsa;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Random;

import lombok.Getter;
import lombok.Setter;
import sm.ciscoop.crypt.gcd.Gcd;
import sm.ciscoop.crypt.gcd.GcdRec;

public class RsaObj2 {
  private static final BigInteger    UNO = BigInteger.ONE;
  // private static int                 MAX_BIT = 64;
  // private static final BigInteger    DUE = BigInteger.valueOf(2);

  @Getter @Setter private BigInteger nP;
  @Getter @Setter private BigInteger nQ;
  @Getter @Setter private BigInteger nPQmodulus;
  @Getter @Setter private BigInteger nPQTotientFi;
  private BigInteger                 nCarmichael;
  @Getter @Setter private BigInteger nE;
  @Getter @Setter private BigInteger nD;
  private long                       m_nProbes;
  // private BigInteger              m_GuessKprev;

  // private BigInteger[]               arrExp;

  public void calcolaRSAObj() {
    nPQmodulus = null;
    nPQTotientFi = null;
    nE = null;
    nD = null;
    if (nP == null || nQ == null)
      return;

    calcolaE2();
    calcolaD2();
  }

  /**
   * <ol>
   * <li>Scelgo 2 numeri primi P e Q</li>
   * <li>Calcolo il modulus <code>N = P * Q</code></li>
   * <li>Inoltre calcolo il Totient
   * <code>&#x3C6;(n) = (P - 1) ( Q - 1)</code></li>
   * <li>Scelgo un numero <b>E</b> che sia :
   * <code>1 &lt; E &lt; &#x3C6;(n)</code></li>
   * <li>Inoltre E deve essere <b>co-primo</b> con <code>&#x3C6;(n)</code><br/>
   * Co-primo significa che <b>NON</b> condividono nessun fattore comune<br/>
   * in matematica si scrive<br/>
   * <code>1 = gcd( E, &#632; ) </code></li>
   * </ol>
   * <h2>da Rsa.cpp</h2> Calcola <b>E</b> con il seguente algoritmo
   * <ul>
   * <li><b>E</b> e' maggiore di 1,</li>
   * <li><b>E</b> e' minore di <b>PQ</b>,</li>
   * <li><b>E</b> e <code>(P-1)(Q-1)</code> sono reciprocamente primi, che
   * significa che non hanno fattori primi in comune.</li>
   * </ul>
   * <b>E</b> non deve essere obbligatoriamente primo, ma deve essere dispari.
   * <p>
   * <code>(P-1)(Q-1)</code> non puo' essere primo perche' e' un numero pari.
   * 
   * @return
   */
  private BigInteger calcolaE2() {
    // PrimiFactory2 fac = PrimiFactory2.getInst();
    nE = BigInteger.ZERO;
    if ( (nP.multiply(nQ).equals(BigInteger.ZERO)) || (nP.equals(nQ)))
      return nE;
    GcdRec res = new GcdRec(BigInteger.TWO, null, null);
    nPQmodulus = nP.multiply(nQ);
    BigInteger npm1 = nP.subtract(UNO);
    BigInteger nqm1 = nQ.subtract(UNO);

    nPQTotientFi = npm1.multiply(nqm1);
    nCarmichael = Gcd.lcm(npm1, nqm1);
    nE = nCarmichael.divide(BigInteger.TWO);
    if ( !nE.testBit(0))
      nE = nE.subtract(UNO);
    while (true) {
      // if (isPrimo(nE)) {
      nE = nE.nextProbablePrime();
      res = Gcd.gcd(nE, nCarmichael);
      if (res.resto().equals(BigInteger.ONE))
        break;
      // }
      nE = nE.add(BigInteger.TWO);
    }
    return nE;
  }

  /**
   * Calcolare D tale che <code>(DE - 1)</code> &egrave; divisibile per
   * <code>(P-1)(Q-1)</code> senza resto.<br/>
   * I matematici scrivono questo come DE = 1 + (mod(P-1) (Q-1)). dove
   * <code>(P-1)(Q-1)</code> &egrave; definito &#x3C6;(n)<br/>
   * Quindi
   * <ul>
   * <li>P - numero primo 1</li>
   * <li>Q - numero primo 2</li>
   * <li>N - <code>PQ</code> detto anche <i>modulus</i></li>
   * <li>&#x3C6;(n) -
   * <code>(P-1)(Q-1) chiamata <i>Euler totient function</i></code></li>
   * <li>E - Si sceglie tale che E &lt; N <br/>
   * inoltre gcd(e,&#x3C6;(n)) = 1 e cio&egrave; E deve essere <b>co-primo</b>
   * con &#x3C6;(n)</li>
   * <li>D - Si chiama l'inverso moltiplicativo di E.<br/>
   * Inoltre ED = 1 mod &#x3C6;(n)</li>
   * </ul>
   * <br/>
   * Questo &egrave; facile da fare - semplicemente trovare un intero X che
   * causa D = (X*(P-1)(Q-1) + 1) / E per essere un numero intero, quindi
   * utilizzare tale valore di D
   * <h2>tratto da Rsa.cpp</h2> Trova <b>D</b> cosi che <code>(DE - 1)</code> e'
   * divisibile da <code>(P-1)(Q-1)</code>.
   * <p>
   * In matematica si puo' scrivere cosi <code>DE = 1 (mod (P-1)(Q-1))</code>,
   * <p>
   * e D si chiama il fattore inverso di E.
   * <p>
   * E' facile da fare:
   * <p>
   * trova un'intero <b>X</b> cosi che <code>D = (X(P-1)(Q-1) + 1)/E</code>
   * <p>
   * sia un'intero, utilizza, di conseguenza, questo valore come <b>D</b>.
   * 
   * @return
   */
  private BigInteger calcolaD2() {
    boolean bTest = false;
    if ( !bTest) {
      nD = nE.modInverse(nPQTotientFi);
      return nD;
    }
    NumberFormat fmt = NumberFormat.getIntegerInstance();
    BigInteger biRet = null;
    BigInteger[] biResto = { BigInteger.ZERO, UNO };

    // 1) verifico che x * e mod fi == 1 iterando x
    BigInteger d_naif = Gcd.multModInvNaif(nE, nPQTotientFi);
    // 2) recupero il valore di d=x da euclide
    // se x<0 allora d=x mod fi
    GcdRec rec = Gcd.gcd(nE, nPQTotientFi);
    BigInteger d_eucl = rec.x();
    if (d_eucl.signum() == -1)
      d_eucl = d_eucl.mod(nPQTotientFi);
    // 3) chiedo a Java il inverse moltiplicativo di e modulo fi
    BigInteger d_jmoinv = nE.modInverse(nPQTotientFi);

    System.out.printf("naif d=%s\tEuclide d=%s\tmod.inv=%s\n", //
        fmt.format(d_naif), //
        fmt.format(d_eucl), //
        fmt.format(d_jmoinv));
    // 4) trovare un intero X che causa D = (X*(P-1)(Q-1) + 1) / E per essere un numero intero
    m_nProbes = 1;
    while ( !biResto[1].equals(BigInteger.ZERO) || biResto[0].equals(nE)) {
      BigInteger k = BigInteger.valueOf(m_nProbes++);
      // k*(P-1)(Q-1)+1) / E
      biResto = k //
          .multiply(nPQTotientFi) //
          .add(BigInteger.ONE) //
          .divideAndRemainder(nE);
      // System.out.printf("K=%d, prob=%d ( resto=%d)\n", k.longValue(), m_nProbes, biResto[1].longValue());
    }
    nD = biResto[0];
    stampaRis();
    String sz = "Trovato al " + m_nProbes;
    sz += " D=" + biResto[0].toString();
    sz += " ED=1 mod(fi) = " + nE.multiply(nD).mod(nPQTotientFi).toString();

    log(sz);
    return biRet;
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

  /**
   * Algoritmo di Euclide per trovare il Minimo Comun Divisore (MCD) fra due
   * numeri interi. L'implementazione utilizza l'iterazione invece della
   * ricorsivit&agrave;.
   * 
   * @param p_a
   *          primo intero
   * @param p_b
   *          secondo intero (non = zero!)
   * @return il MCD (se esiste) altrimenti torna 1 nel caso che i due numeri
   *         siano co-primi
   */
  public BigInteger igcd(BigInteger p_a, BigInteger p_b) {
    BigInteger r = p_a;
    while ( !p_b.equals(BigInteger.ZERO)) {
      r = p_a.mod(p_b);
      p_a = p_b;
      p_b = r;
    }
    return p_a;
  }

  /**
   * Costruisco un {@link BigInteger} che :
   * <ol>
   * <li>sar&agrave; lungo <code>qtaBits</code></li>
   * <li>random setto i bits fino al raggiungimento della lunghezza</li>
   * <li>l'eccezione che il bit <b>pi&ugrave;</b> significativo sar&agrave;
   * comunque sempre a 1</li>
   * <li>a questo punto chiedo il prox primo con
   * {@link BigInteger#nextProbablePrime()}
   * </ol>
   * 
   * @param qtaBits
   *          lunghezza in bit del futuro BigInteger
   * @return un <i>probable prime</i> di qtaBits di lunghezza
   */
  public static BigInteger creaGrandeNumeroPrimo(int qtaBits) {
    Random rnd = new Random(new Date().getTime());
    BigInteger bi = BigInteger.valueOf(0L);
    bi = bi.setBit(qtaBits - 1);
    for (int n = qtaBits - 2; n >= 0; n--) {
      if ( (rnd.nextInt() & 1) != 0 || n == 0)
        bi = bi.setBit(n);
    }
    bi = bi.nextProbablePrime();
    return bi;
  }

  public boolean isPrimo(BigInteger p) {
    // range  < p <= 1
    if (p == null || p.compareTo(BigInteger.ONE) <= 0)
      return false;
    // range  2 < p <= 5
    if (p.compareTo(BigInteger.valueOf(25)) <= 0) {
      switch (p.intValue()) {
        case 2:
        case 3:
        case 5:
        case 7:
        case 11:
        case 13:
        case 17:
        case 19:
        case 23:
          return true;
        default:
          return false;
      }
    }
    if (p.mod(BigInteger.TWO).equals(BigInteger.ZERO) || p.mod(BigInteger.valueOf(3)).equals(BigInteger.ZERO))
      return false;
    BigInteger k = BigInteger.valueOf(5);
    // partendo da 5 (x=1) verifico il mod su 6x-1 e 6x+1
    while (k.multiply(k).compareTo(p) <= 0) {
      if (p.mod(k).equals(BigInteger.ZERO))
        return false;
      if (p.mod(k.add(BigInteger.TWO)).equals(BigInteger.ZERO))
        return false;
      k = k.add(BigInteger.valueOf(6));
    }
    return true;
  }

  /**
   * La crittazione del messaggio <code>m</code> avviene com l'utilizzo della
   * elevazione a potenza modulare i.e.<br/>
   * <code>crit = m<sup>E</sup> mod (PQ)</code>
   * 
   * @param p_msg
   *          messaggio in chiaro da crittare con il valore di E
   * @return messaggio originale (decrittato)
   */
  public BigInteger esponenteE(BigInteger p_msg) {
    return p_msg.modPow(nE, nPQmodulus);
  }

  /**
   * La <b>de-</b>crittazione del messaggio <code>crit</code> avviene com
   * l'utilizzo della elevazione a potenza modulare i.e.<br/>
   * <code>m = crit<sup>D</sup> mod (PQ)</code>
   * 
   * @param p_crit
   *          messaggio crittato con il valore di E
   * @return messaggio originale (decrittato)
   */
  public BigInteger esponenteD(BigInteger p_crit) {
    return p_crit.modPow(nD, nPQmodulus);
  }

  private void log(String sz) {
    System.out.println(sz);
  }

  public void stampaRis() {
    System.out.println("--------------------");
    stampaRis(" P", nP);
    stampaRis(" Q", nQ);
    stampaRis(" N", nPQmodulus);
    stampaRis("Fi", nPQTotientFi);
    stampaRis("Cr", nCarmichael);
    stampaRis(" E", nE);
    stampaRis(" D", nD);

  }

  private void stampaRis(String p_tit, BigInteger p_v) {
    NumberFormat fmt = NumberFormat.getIntegerInstance();
    String szv = "*NULL*";
    if (p_v != null)
      szv = fmt.format(p_v.longValue());
    System.out.printf("%3s=%s\n", p_tit, szv);
  }

}
