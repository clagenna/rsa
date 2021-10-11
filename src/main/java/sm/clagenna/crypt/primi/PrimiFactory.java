package sm.clagenna.crypt.primi;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class PrimiFactory {

  private int          m_nQtaPrimi = 20000;
  private Vector<Long> m_vec;
  private int          m_nMaxPrimo;
  private int          m_initIndex;

  public void buildPrimi(int n) {
    setQtaPrimi(n);
    buildPrimi();
  }

  public void buildPrimi() {
    m_vec = new Vector<Long>();
    m_vec.add(Long.valueOf(2));
    m_vec.add(Long.valueOf(3));
    m_vec.add(Long.valueOf(5));

    for (int k = 7; m_vec.size() < m_nQtaPrimi; k += 2) {
      if (isPrimo(k)) {
        m_vec.add(Long.valueOf(k));
        setMaxPrimo(k);
        //        System.out.println("a) Primo="+k);
      }
    }
  }

  public void buildPrimi(Long min, long qta) {
    m_vec = new Vector<Long>();
    m_vec.add(Long.valueOf(2));
    m_vec.add(Long.valueOf(3));
    m_vec.add(Long.valueOf(5));
    m_initIndex = 0;
    for (int k = 7; true; k += 2) {
      if (isPrimo(k)) {
        m_vec.add(Long.valueOf(k));
        m_nMaxPrimo = k;
        if (m_nMaxPrimo >= min)
          if (m_initIndex == 0)
            m_initIndex = m_vec.size();
      }
      if (m_initIndex > 0 && (m_vec.size() - m_initIndex) >= qta)
        break;
    }

  }

  public List<Long> getList() {
    if (m_initIndex > 0) {
      List<Long> li = new ArrayList<>();
      for (int k = m_initIndex; k < m_vec.size(); k++)
        li.add(m_vec.get(k));
      return li;
    }
    return m_vec;
  }

  public boolean isPrimo(int n) {
    boolean bRet = (n & 1) != 0 || n == 2;
    if (n <= 7)
      return bRet;
    int k = (int) Math.sqrt(n) + 1;
    int p = 0;
    // prima test con i primi miei
    for (Long ii : m_vec) {
      p = ii.intValue();
      bRet = n % p != 0;
      if ( !bRet || p > k)
        break;
    }
    // se non mi bastano i primi miei vado coi dispari
    for (; bRet && p < k; p += 2) {
      bRet = (n % p) != 0;
    }
    return bRet;
  }

  /**
   * Calcola il massimo comun divisore (GCD) fra 2 numeri
   *
   * @param a
   * @param b
   * @return
   */
  public long gcdExtended(long a, long b) {
    long x = 1, y = 1;
    return gcdExtended(a, b, x, y);
  }

  // extended Euclidean Algorithm
  public long gcdExtended(long a, long b, long x, long y) {
    // Base Case
    if (a == 0) {
      x = 0;
      y = 1;
      return b;
    }

    long x1 = 1, y1 = 1; // To store results of recursive call
    long gcd = gcdExtended(b % a, a, x1, y1);

    // Update x and y using results of recursive
    // call
    x = y1 - (b / a) * x1;
    y = x1;

    return gcd;
  }

  public void setQtaPrimi(int vv) {
    m_nQtaPrimi = vv;
  }

  public int getQtaPrimi() {
    return m_nQtaPrimi;
  }

  private void setMaxPrimo(int m_nMaxPrimo) {
    this.m_nMaxPrimo = m_nMaxPrimo;
  }

  public int getMaxPrimo() {
    return m_nMaxPrimo;
  }
}
