package sm.ciscoop.crypt.rsa;

import java.math.BigInteger;
import java.text.NumberFormat;

import lombok.Getter;
import lombok.Setter;

public class RsaObj2 {
  private static final BigInteger    UNO = BigInteger.ONE;
  // private static final BigInteger    DUE = BigInteger.valueOf(2);

  @Getter @Setter private BigInteger nP;
  @Getter @Setter private BigInteger nQ;
  @Getter @Setter private BigInteger nPQmodulus;
  @Getter @Setter private BigInteger nPQTotientFi;
  @Getter @Setter private BigInteger nE;
  @Getter @Setter private BigInteger nD;
  private long                       m_nProbes;
  // private BigInteger              m_GuessKprev;

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
   *
   * @return
   */
  private BigInteger calcolaE2() {
    // PrimiFactory2 fac = PrimiFactory2.getInst();
    nE = BigInteger.ZERO;
    if ( (nP.multiply(nQ).equals(BigInteger.ZERO)) || (nP.equals(nQ)))
      return nE;
    nPQmodulus = nP.multiply(nQ);
    BigInteger npm1 = nP.subtract(UNO);
    BigInteger nqm1 = nQ.subtract(UNO);
    // BigInteger nProbE = igcd(npm1, nqm1);

    nPQTotientFi = npm1.multiply(nqm1);
    stampaRis();
    BigInteger mcd = BigInteger.TWO;
    // cerco un elemento co-primo che sia inferiore a (P-1)(Q-1) scendendo di 1
    nE = nPQTotientFi.divide(BigInteger.TWO);
    do {
      nE = nE.subtract(BigInteger.ONE);
      mcd = igcd(nPQmodulus, nE);
      stampaRis();
    } while ( !mcd.equals(BigInteger.ONE));
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
   * <li>E - Si sceglie tale che E &lt; N inoltre gcd(e,&#x3C6;(n)) = 1 e
   * cio&egrave; E deve essere <b>co-primo</b> con &#x3C6;(n)</li>
   * <li>D - Si chiama l'inverso moltiplicativo di E.<br/>
   * Inoltre ED = 1 mod &#x3C6;(n)</li>
   * </ul>
   * <br/>
   * Questo &egrave; facile da fare - semplicemente trovare un intero X che
   * causa D = (X*(P-1)(Q-1) + 1) / E per essere un numero intero, quindi
   * utilizzare tale valore di D
   *
   * @return
   */
  private BigInteger calcolaD2() {
    BigInteger biRet = null;
    BigInteger[] biResto = { BigInteger.ZERO, UNO };
    m_nProbes = 1;
    while ( !biResto[1].equals(BigInteger.ZERO) || biResto[0].equals(nE)) {
      BigInteger k = BigInteger.valueOf(m_nProbes++);
      // k*(P-1)(Q-1)+1) / E
      biResto = k //
          .multiply(nPQTotientFi) //
          .add(BigInteger.ONE) //
          .divideAndRemainder(nE);
      System.out.printf("K=%d, prob=%d ( resto=%d)\n", k.longValue(), m_nProbes, biResto[1].longValue());
    }
    nD = biResto[0];
    stampaRis();
    String sz = "Trovato al " + m_nProbes;
    sz += " D=" + biResto[0].toString();
    sz += " ED=1 mod(fi) = " + nE.multiply(nD).mod(nPQTotientFi).toString();
    System.out.println(nE.modInverse(nPQTotientFi).toString());
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
   * ricorsività.
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
   * Calcolare D tale che (DE - 1) &egrave; divisibile per (P-1)(Q-1) senza
   * resto.<br/>
   * I matematici scrivono questo come DE = 1 + (mod(P-1) (Q-1)).<br/>
   * Si chiama D l'inverso moltiplicativo di E. Questo &egrave; facile da fare -
   * semplicemente trovare un intero X che causa D = (X*(P-1)(Q-1) + 1) / E per
   * essere un numero intero, quindi utilizzare tale valore di D
   *
   * @return
   */
  /*-
  @SuppressWarnings("unused")
  private BigInteger calcolaD() {
    // nPQm1 = (P-1)(Q-1)
    boolean bGuess = true;
    BigInteger biPQm1 = BigInteger.valueOf(m_nP - 1).multiply(BigInteger.valueOf(m_nQ - 1));
    m_nD = BigInteger.ZERO;
    BigInteger[] biResto = { BigInteger.ZERO, UNO };
    m_nProbes = 1;
    // ciclo fintanto che (k*(P-1)(Q-1)+1) / E da resto zero
    while ( !biResto[1].equals(BigInteger.ZERO)) {
      BigInteger k = BigInteger.valueOf(m_nProbes++);
      // k*(P-1)(Q-1)+1) / E
      biResto = k.multiply(biPQm1).add(UNO).divideAndRemainder(m_nE);
      if ( !biResto[1].equals(BigInteger.ZERO) && bGuess)
        m_nProbes = guessk(m_nProbes, biResto[1]);
      System.out.printf("K=%d, prob=%d ( resto=%d)\n", k.longValue(), m_nProbes, biResto[1].longValue());
    }
    String sz = "Trovato al " + m_nProbes;
    sz += " D=" + biResto[0].toString();
    sz += " E/D=" + m_nE.divide(biResto[0]).toString();
    log(sz);
    m_nD = biResto[0];
    return m_nD;
  }
  */

  /*-
  private long guessk(long k, BigInteger resto) {
    if (m_GuessKprev == null) {
      m_GuessKprev = resto;
      return k;
    }
    // crescita = delta(n+1)
    BigInteger crescita = resto.subtract(m_GuessKprev);
    if (crescita.compareTo(UNO) <= 0) {
      m_GuessKprev = resto;
      return k;
    }
    // dist E = E - resto
    BigInteger dist = m_nE.subtract(resto);
    double passiToE = dist.doubleValue() / crescita.doubleValue();
    k += Double.valueOf(passiToE).longValue() - 1;
    m_GuessKprev = null;
    return k;
  }
  */

  private void log(String sz) {
    System.out.println(sz);
  }

  /**
   * <ol>
   * <li>Scegli E tale che E è maggiore di 1,</li>
   * <li>E è inferiore a PQ,</li>
   * <li>ed E e (P-1)(Q-1) sono primi tra loro, il che significa che non hanno
   * fattori primi in comune.</li>
   * <li>E non deve essere primo, ma deve essere dispari.</li>
   * </ol>
   * (P-1) (Q-1) non può essere primo perché è un numero pari
   */
  /*-
  @SuppressWarnings("unused")
  private BigInteger calcolaE() {
    m_nE = BigInteger.ZERO;
    if ( (m_nP * m_nQ == 0) || (m_nP == m_nQ))
      return m_nE;
    m_nPQmodulus = BigInteger.valueOf(m_nP).multiply(BigInteger.valueOf(m_nQ));
    m_nPQTotientFi = BigInteger.valueOf(m_nP - 1).multiply(BigInteger.valueOf(m_nQ - 1));
    // nPQm1 e' pari
    m_nE = m_nPQTotientFi.divide(DUE);
    if ( (m_nE.intValue() & 1) == 0)
      m_nE = m_nE.subtract(UNO);
    while (m_nE.doubleValue() > 1) {
      if (m_nE.isProbablePrime(2000))
        break;
      m_nE = m_nE.subtract(BigInteger.valueOf(2));
    }
    return m_nE;
  }
  */

  /**
   * Calcolare D tale che (DE - 1) &egrave; divisibile per (P-1)(Q-1) senza
   * resto.<br/>
   * I matematici scrivono questo come DE = 1 + (mod(P-1) (Q-1)).<br/>
   * Si chiama D l'inverso moltiplicativo di E. Questo &egrave; facile da fare -
   * semplicemente trovare un intero X che causa D = (X*(P-1)(Q-1) + 1) / E per
   * essere un numero intero, quindi utilizzare tale valore di D
   *
   * @return
   */
  /*-
  @SuppressWarnings("unused")
  private BigInteger calcolaD() {
    // nPQm1 = (P-1)(Q-1)
    boolean bGuess = true;
    BigInteger biPQm1 = BigInteger.valueOf(m_nP - 1).multiply(BigInteger.valueOf(m_nQ - 1));
    m_nD = BigInteger.ZERO;
    BigInteger[] biResto = { BigInteger.ZERO, UNO };
    m_nProbes = 1;
    // ciclo fintanto che (k*(P-1)(Q-1)+1) / E da resto zero
    while ( !biResto[1].equals(BigInteger.ZERO)) {
      BigInteger k = BigInteger.valueOf(m_nProbes++);
      // k*(P-1)(Q-1)+1) / E
      biResto = k.multiply(biPQm1).add(UNO).divideAndRemainder(m_nE);
      if ( !biResto[1].equals(BigInteger.ZERO) && bGuess)
        m_nProbes = guessk(m_nProbes, biResto[1]);
      System.out.printf("K=%d, prob=%d ( resto=%d)\n", k.longValue(), m_nProbes, biResto[1].longValue());
    }
    String sz = "Trovato al " + m_nProbes;
    sz += " D=" + biResto[0].toString();
    sz += " E/D=" + m_nE.divide(biResto[0]).toString();
    log(sz);
    m_nD = biResto[0];
    return m_nD;
  }
  */

  /*-
  private long guessk(long k, BigInteger resto) {
    if (m_GuessKprev == null) {
      m_GuessKprev = resto;
      return k;
    }
    // crescita = delta(n+1)
    BigInteger crescita = resto.subtract(m_GuessKprev);
    if (crescita.compareTo(UNO) <= 0) {
      m_GuessKprev = resto;
      return k;
    }
    // dist E = E - resto
    BigInteger dist = m_nE.subtract(resto);
    double passiToE = dist.doubleValue() / crescita.doubleValue();
    k += Double.valueOf(passiToE).longValue() - 1;
    m_GuessKprev = null;
    return k;
  }
  */

  public void stampaRis() {
    System.out.println("--------------------");
    stampaRis(" P", nP);
    stampaRis(" Q", nQ);
    stampaRis(" N", nPQmodulus);
    stampaRis("Fi", nPQTotientFi);
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
