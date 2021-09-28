package sm.ciscoop.crypt.primi;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JProgressBar;

import sm.ciscoop.crypt.gcd.Gcd;

public class PrimiFactory2 {
  private static PrimiFactory2                                   s_inst;
  private static final long                                      MAXV = 2_000_000_000L;
  @SuppressWarnings("unused") private static final DecimalFormat fmt  = new DecimalFormat("#,###,###,###,###");

  private List<Long>                                             liPrimi;
  private List<Long>                                             liSelez;
  private Long                                                   maxPrimo;
  private JProgressBar                                           progressBar;

  public PrimiFactory2() {
    if (s_inst != null)
      throw new UnsupportedOperationException("PrimiFactory gia istanziata");
    s_inst = this;
  }

  public PrimiFactory2(JProgressBar p_pr) {
    if (s_inst != null)
      throw new UnsupportedOperationException("PrimiFactory gia istanziata");
    s_inst = this;
    setProgressBar(p_pr);
  }

  public static PrimiFactory2 getInst() {
    return s_inst;
  }

  public List<Long> creaPrimi(Long p_max) {
    return creaPrimi(2L, p_max);
  }

  public List<Long> creaPrimi(Long p_min, Long p_qtaP) {
    liPrimi = new ArrayList<Long>();
    liSelez = new ArrayList<Long>();
    if (p_min == null)
      p_min = 2L;
    if (p_qtaP == null || p_qtaP.longValue() < 200)
      p_qtaP = 200L;
    initList(liPrimi);
    double coef = (double) MAXV / 100F;
    int coPrec = -1;
    for (long lv = 9; lv < MAXV; lv += 2) {
      //      if ( (lv % 131321) == 0)
      //        System.out.printf("%16s (%d)\r", fmt.format(lv), liPrimi.size());
      if (getProgressBar() != null) {
        int np = (int) (lv / coef);
        if (np != coPrec) {
          getProgressBar().setValue(np);
          coPrec = np;
        }
      }
      if (isPrimo2(lv)) {
        liPrimi.add(lv);
        maxPrimo = lv;
        if (lv >= p_min)
          getLiSelez().add(lv);
        if (getLiSelez().size() >= p_qtaP)
          break;
      }
    }
    System.out.println("PrimiFactory2.creaPrimi():" + getLiSelez().size());
    return getLiSelez();
  }

  private void initList(List<Long> li) {
    li.add(Long.valueOf(2));
    li.add(Long.valueOf(3));
    li.add(Long.valueOf(5));
    li.add(Long.valueOf(7));
  }

  private boolean isPrimo2(long vv) {
    // i primi 2,3,5,7
    boolean bRet = vv == 2 || (vv & 1) != 0;
    if (vv <= 7 || !bRet)
      return bRet;
    // i prossimi saranno distribuiti su (6n-1,6n+1)
    if ( (vv + 1) % 6 != 0 && (vv - 1) % 6 != 0)
      return false;
    long maxFact = (long) Math.sqrt(vv) + 1;
    int maxK = -1;
    int k = 1;
    if (liPrimi != null || liPrimi.size() > 0)
      maxK = liPrimi.size();
    for (long lastPr = 3; lastPr <= maxFact; lastPr += 2) {
      if (k < maxK)
        lastPr = liPrimi.get(k++);
      // System.out.printf(" %d su %d (%d)\n", lastPr, vv, maxFact);
      if (vv % lastPr == 0)
        return false;
    }
    return bRet;
  }

  public JProgressBar getProgressBar() {
    return progressBar;
  }

  public void setProgressBar(JProgressBar progressBar) {
    this.progressBar = progressBar;
  }

  public List<Long> getLiSelez() {
    return liSelez;
  }

  public Long getMaxPrimo() {
    return maxPrimo;
  }

  /**
   * Calcola il massimo comun divisore (GCD) fra 2 numeri
   *
   * @deprecated vedi {@link Gcd#gcd(BigInteger, BigInteger)}
   * @param a
   * @param b
   * @return
   */
  @Deprecated
  public BigInteger gcdExtended(BigInteger a, BigInteger b) {
    BigInteger x = BigInteger.ONE, y = BigInteger.ONE;
    return gcdExtended(a, b, x, y);
  }

  /**
   * extended Euclidean Algorithm
   *
   * @deprecated vedi {@link Gcd#gcd(BigInteger, BigInteger)}
   * @param a
   * @param b
   * @param x
   * @param y
   * @return
   */
  @Deprecated
  public BigInteger gcdExtended(BigInteger a, BigInteger b, BigInteger x, BigInteger y) {
    // Base Case
    if (a.equals(BigInteger.ZERO)) {
      x = BigInteger.ZERO;
      y = BigInteger.ONE;
      return b;
    }

    BigInteger x1 = BigInteger.ONE, y1 = BigInteger.ONE; // To store results of recursive call
    BigInteger gcd = gcdExtended(b.remainder(a), a, x1, y1);

    // Update x and y using results of recursive
    // call
    // x = y1 - (b / a) * x1;
    x = y1.subtract( (b.divide(a).multiply(x1)));
    y = x1;

    return gcd;
  }

}
