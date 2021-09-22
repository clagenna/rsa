package sm.ciscoop.crypt.rsa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import sm.ciscoop.crypt.primi.PrimiFactory;

public class RsaObj {

  private static final BigInteger UNO     = BigInteger.ONE;
  private static final BigInteger DUE     = BigInteger.valueOf(2);
  private static int              MAX_BIT = 64;
  long                            m_nProbes;

  BigInteger[]                    arrExp;
  private PrimiFactory            m_pf;
  private long                    m_nP;
  private long                    m_nQ;
  private BigInteger              m_nPQ;
  BigInteger                      m_nE;
  BigInteger                      m_nD;

  public RsaObj() {
    setPrimiFactory(new PrimiFactory());
    getPrimiFactory().buildPrimi(1024);
  }

  public List<Long> encode(List<Long> vec) {
    List<Long> lvec = new ArrayList<Long>();
    for (Long ll : vec) {
      BigInteger bi = BigInteger.valueOf(ll);
      bi = esponente(bi, m_nE, m_nPQ);
      lvec.add(bi.longValue());
    }
    return lvec;
  }

  public List<Long> decode(List<Long> vec) {
    List<Long> lvec = new ArrayList<Long>();
    for (Long ll : vec) {
      BigInteger bi = BigInteger.valueOf(ll);
      bi = esponente(bi, m_nD, m_nPQ);
      lvec.add(bi.longValue());
    }
    return lvec;
  }

  public void setPrimiFactory(PrimiFactory m_pf) {
    this.m_pf = m_pf;
  }

  public PrimiFactory getPrimiFactory() {
    return m_pf;
  }

  public BigInteger esponenteE(BigInteger p_val) {
    return esponente(p_val, m_nE, m_nPQ);
  }

  public BigInteger esponenteD(BigInteger p_val) {
    return esponente(p_val, m_nD, m_nPQ);
  }

  private BigInteger esponente(BigInteger p_val, BigInteger p_exp, BigInteger p_mod) {
    BigInteger ris = p_val;
    if (p_val.compareTo(p_mod) >= 0)
      System.err.println("Val=" + p_val + "\t> PQ=" + p_mod);

    arrExp = new BigInteger[MAX_BIT];
    // genero l'esponente a modulo
    arrExp[0] = p_val;
    for (int i = 1; i < MAX_BIT; i++) {
      // v(i) = v(i-1)^2 mod m
      ris = ris.multiply(ris);
      ris = ris.mod(p_mod);
      arrExp[i] = ris;
    }
    ris = BigInteger.ONE;
    for (int i = 0; i < MAX_BIT; i++) {
      if (p_exp.testBit(i)) {
        // CDbl(nRes) * CDbl(arExp(i)), CDbl(nMod)
        ris = ris.multiply(arrExp[i]).mod(p_mod);
      }
    }
    return ris;
  }

  private void testCalcolo() {
    boolean bCalc = (m_nP * m_nQ) != 0;
    if (bCalc)
      bCalc &= m_nP > 2 && m_nQ > 2;
    if (bCalc)
      bCalc &= m_nP != m_nQ;
    if (bCalc) {
      calcolaE();
      calcolaD();
    }
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
  private BigInteger calcolaE() {
    m_nE = BigInteger.ZERO;
    if (m_nP * m_nQ == 0)
      return m_nE;
    if (m_nP == m_nQ)
      return m_nE;
    setPQ(m_nP * m_nQ);
    BigInteger nPQm1 = BigInteger.valueOf(m_nP - 1).multiply(BigInteger.valueOf(m_nQ - 1));
    // nPQm1 e' pari
    m_nE = nPQm1.divide(DUE);
    if ( (m_nE.intValue() & 1) == 0)
      m_nE = m_nE.subtract(UNO);
    while (m_nE.doubleValue() > 1) {
      if (m_nE.isProbablePrime(2000))
        break;
      m_nE = m_nE.subtract(BigInteger.valueOf(2));
    }
    return m_nE;
  }

  /**
   * Calcolare D tale che (DE - 1) &egrave; divisibile per (P-1)(Q-1) senza resto.<br/>
   * I matematici scrivono questo come DE = 1 + (mod(P-1) (Q-1)).<br/>
   * Si chiama D l'inverso moltiplicativo di E. Questo &egrave; facile da fare -
   * semplicemente trovare un intero X che causa D = (X*(P-1)(Q-1) + 1) / E per
   * essere un numero intero, quindi utilizzare tale valore di D
   * 
   * @return
   */
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
    }
    String sz = "Trovato al " + m_nProbes;
    sz += " D=" + biResto[0].toString();
    sz += " E/D=" + m_nE.divide(biResto[0]).toString();
    log(sz);
    m_nD = biResto[0];
    return m_nD;
  }

  BigInteger prev;
  BigInteger crescita;

  private long guessk(long k, BigInteger resto) {
    if (prev == null) {
      prev = resto;
      return k;
    }
    // crescita = delta(n+1)
    crescita = resto.subtract(prev);
    if (crescita.compareTo(UNO) <= 0) {
      prev = resto;
      return k;
    }
    // dist E = E - resto
    BigInteger dist = m_nE.subtract(resto);
    double passiToE = dist.doubleValue() / crescita.doubleValue();
    k += Double.valueOf(passiToE).longValue() - 1;
    prev = null;
    return k;
  }

  @SuppressWarnings("unused")
  private BigInteger calcolaD2() {
    // nPQm1 = (P-1)(Q-1)
    BigInteger biPQm1 = BigInteger.valueOf(m_nP - 1).multiply(BigInteger.valueOf(m_nQ - 1));
    m_nD = BigInteger.ZERO;
    BigInteger[] biResto = { BigInteger.ZERO, UNO };
    BigInteger primaVersoE = BigInteger.ZERO;
    m_nProbes = 1;
    // ciclo fintanto che (k*(P-1)(Q-1)+1) / E da resto zero
    while ( !biResto[1].equals(BigInteger.ZERO)) {
      BigInteger bi = BigInteger.valueOf(m_nProbes++);
      // k*(P-1)(Q-1)+1) / E
      biResto = bi.multiply(biPQm1).add(UNO).divideAndRemainder(m_nE);
      if (biResto[1].equals(BigInteger.ZERO)) {
        String sz = "Trovato al " + m_nProbes;
        sz += " D=" + biResto[0].toString();
        sz += " E/D=" + m_nE.divide(biResto[0]).toString();
        log(sz);
        break;
      }
      BigInteger mancaVersoE = m_nE.subtract(biResto[1]);
      if (mancaVersoE.compareTo(BigInteger.ZERO) <= 0) {
        mancaVersoE = BigInteger.ZERO;
      }
      biResto[0] = BigInteger.ZERO;
      if ( !primaVersoE.equals(BigInteger.ZERO)) {
        BigInteger diffSalto = primaVersoE.subtract(mancaVersoE);
        BigInteger forse = m_nE.divide(diffSalto);
        debug("Probes=" + bi.toString() + "\tSalto=" + mancaVersoE.toString() + "\tforse=" + forse.toString());
        if (forse.compareTo(bi) > 0 && mancaVersoE.compareTo(BigInteger.valueOf(10000)) > 0) {
          m_nProbes = forse.longValue() - 10;
          mancaVersoE = BigInteger.ZERO;
        }
      }
      primaVersoE = mancaVersoE;
    }
    m_nD = biResto[0];
    return m_nD;
  }

  private void debug(String sz) {
    System.out.println(sz);
  }

  private void log(String sz) {
    System.out.println(sz);
  }

  public void setQ(long nQ) {
    if (m_nQ == nQ)
      return;
    m_nQ = nQ;
    testCalcolo();
  }

  public long getQ() {
    return m_nQ;
  }

  public void setPQ(long PQ) {
    m_nPQ = BigInteger.valueOf(PQ);
  }

  public long getPQ() {
    return m_nPQ != null ? m_nPQ.longValue() : 0;
  }

  public void setP(long nP) {
    if (m_nP == nP)
      return;
    m_nP = nP;
    testCalcolo();
  }

  public long getP() {
    return m_nP;
  }

  public void setE(long nE) {
    if (m_nE != null && m_nE.longValue() == nE)
      return;
    m_nE = BigInteger.valueOf(nE);
  }

  public Long getE() {
    if (m_nE == null)
      return 0L;
    return m_nE.longValue();
  }

  public void setD(long nD) {
    if (m_nD != null && m_nD.longValue() == nD)
      return;
    m_nD = BigInteger.valueOf(nD);
  }

  public Long getD() {
    if (m_nD == null)
      return 0L;
    return m_nD.longValue();
  }

  public Long getProbes() {
    return Long.valueOf(m_nProbes);
  }
}
