package sm.clagenna.crypt.rsa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

// import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
// import com.sun.org.apache.xml.internal.security.utils.Base64;

public class CodeFrase {
  private static String CSZTR        = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789'?-_=.,;:+*/\\";
  private boolean       m_bDebug     = false;
  private int           m_nMaxSymb;
  private int           m_nQtaCodici = CSZTR.length();
  private long          m_nPQ;
  private List<Long>    m_vec;

  public CodeFrase(long pq) {
    setPQ(pq);
  }

  public List<Long> encode(String p_sz) {
    String sz = normalizzaStringa(p_sz);
    m_vec = new ArrayList<Long>();
    int k = 1;
    long ww = 0;
    for (char cc : sz.toCharArray()) {
      int n = charcode(cc);
      if (isDebug())
        System.out.println("cc[" + n + "]=" + cc);
      ww = ww * m_nQtaCodici + n;
      if (k++ >= m_nMaxSymb) {
        m_vec.add(ww);
        if (isDebug())
          System.out.println("-->" + ww);
        k = 1;
        ww = 0;
      }
    }
    if (k > 1) {
      while (k++ < m_nMaxSymb)
        ww *= m_nQtaCodici;
      m_vec.add(ww);
    }
    sz = encode(m_vec);
    return m_vec;
  }

  public String encode(List<Long> pv) {
    int qtaBytes = pv.size() * 8;
    byte[] arr = new byte[qtaBytes];
    int k = 0;
    for (Long ll : pv) {
      byte[] la = BigInteger.valueOf(ll.longValue()).toByteArray();
      int l = la.length;
      arr[k++] = (byte) l;
      for (byte bb : la)
        arr[k++] = bb;
    }
    if (k < qtaBytes) {
      byte[] larr = new byte[k];
      System.arraycopy(arr, 0, larr, 0, k);
      arr = larr;
    }
    String sz = Base64.getEncoder().encodeToString(arr);
    sz = sz.replaceAll("\\\\n", "\n");
    return sz;
  }

  public List<Long> decode(String sz) {
    byte[] arr = Base64.getDecoder().decode(sz.replaceAll("\n", ""));
    List<Long> vec = new ArrayList<Long>();
    int nLen = 0, k = 0;
    byte[] larr = null;
    for (byte bb : arr) {
      if (nLen <= 0) {
        nLen = bb;
        k = 0;
        larr = new byte[nLen];
        continue;
      }
      larr[k++] = bb;
      if (--nLen == 0) {
        BigInteger bi = new BigInteger(larr);
        Long ll = Long.valueOf(bi.longValue());
        vec.add(ll);
      }
    }
    return vec;
  }

  public String decode(List<Long> vec) {
    StringBuilder sz = new StringBuilder();
    BigInteger qts = BigInteger.valueOf(m_nQtaCodici);
    for (Long lo : vec) {
      BigInteger bi = BigInteger.valueOf(lo);
      int[] arrindx = new int[m_nMaxSymb];
      BigInteger[] arrBi = null;
      if (isDebug())
        System.out.println("-->" + lo);
      for (int i = 1; i <= m_nMaxSymb; i++) {
        arrBi = bi.divideAndRemainder(qts);
        if (isDebug())
          System.out.println("Div=" + arrBi[0] + " rest=" + arrBi[1]);
        arrindx[m_nMaxSymb - i] = arrBi[1].intValue();
        bi = arrBi[0];
      }
      if (isDebug()) {
        if ( !bi.equals(BigInteger.ZERO))
          System.out.println("!!! Ehiiiii !!!");
      }
      for (int i : arrindx) {
        char cc = CSZTR.charAt(i);
        if (isDebug())
          System.out.println("cc[" + i + "]=" + cc);
        sz.append(cc);
      }
    }
    String sz2 = sz.toString().replaceAll("\\\\n", "\n");
    return sz2;
  }

  private String normalizzaStringa(String pSz) {
    if (pSz == null)
      return pSz;
    StringBuilder sb = new StringBuilder();
    for (char cc : pSz.toCharArray()) {
      switch (cc) {
        case 'è':
        case 'é':
          sb.append('e');
          break;
        case 'à':
          sb.append('a');
          break;
        case 'ù':
          sb.append('u');
          break;
        case 'ò':
          sb.append('o');
          break;
        case '\n':
          sb.append("\\n");
          break;
        default:
          sb.append(cc);
      }
    }
    return sb.toString();
  }

  /**
   * Cerca l'index del carattere fornito all'interno della stringa costante
   * {@link CSZTR}<br/>
   * Opera una normalizzazione sui caratteri. Quelli che non sono inclusi nel
   * elenco sono paragonati a blank.
   * 
   * @param p_cc
   * @return
   */
  private int charcode(char p_cc) {
    int k = 0;
    char cc = p_cc;
    switch (cc) {
      case 'è':
      case 'é':
        cc = 'e';
        break;
      case 'à':
        cc = 'a';
        break;
      case 'ù':
        cc = 'u';
        break;
      case 'ò':
        cc = 'o';
        break;
      case '\n':
        cc = 'n';
        break;
    }
    k = CSZTR.indexOf(cc);
    k = k > 0 ? k : 0;
    return k;
  }

  public void setPQ(long pq) {
    m_nPQ = pq;
    // max=log(pq,qtaCod)
    double dbl = Math.log(getPQ()) / Math.log(m_nQtaCodici);
    m_nMaxSymb = (int) dbl;
  }

  public long getPQ() {
    return m_nPQ;
  }

  public void setDebug(boolean bv) {
    m_bDebug = bv;
  }

  public boolean isDebug() {
    return m_bDebug;
  }

}
