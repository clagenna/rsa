package sm.clagenna.crypt.swing;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

public class DeCodeString {
  private int                 maxBit  = 128;
  private int                 shift   = 7;
  private BigInteger          maskBit = BigInteger.valueOf(127);
  private BigInteger          maxVal;
  // private boolean             m_debug;
  private static NumberFormat s_fmt   = NumberFormat.getIntegerInstance();

  enum Lavoro {
    ccLen, ccData
  };

  /**
   *
   * @param sz
   * @return
   */
  public List<BigInteger> toList(String sz, boolean bWithLen) {

    Lavoro seq = Lavoro.ccData;
    if (bWithLen)
      seq = Lavoro.ccLen;
    BigInteger bi = BigInteger.ZERO;
    List<BigInteger> li = new ArrayList<BigInteger>();
    int qtaChars = 1;
    int qtaBitsAdded = 0;

    for (char cc : sz.toCharArray()) {

      switch (seq) {
        case ccLen:
          qtaChars = cc;
          seq = Lavoro.ccData;
          break;

        case ccData:
          char canded = (char) (cc & maskBit.intValue());
          if (cc > maskBit.intValue()) {
            System.out.printf("codifica(%d)=%c\n", (int) cc, cc);
            canded = changeChar(cc);
          }
          bi = bi.shiftLeft(shift).add(BigInteger.valueOf(canded));
          if (maxVal != null && maxVal.signum() > 0)
            if (bi.compareTo(maxVal) > 0)
              throw new UnsupportedOperationException("DeCodeString Superato il valore !");
          qtaBitsAdded += shift;
          boolean endOfVal = qtaBitsAdded >= maxBit;
          if (bWithLen && --qtaChars <= 0) {
            seq = Lavoro.ccLen;
            endOfVal = true;
          }
          if (endOfVal) {
            li.add(bi);
            bi = BigInteger.ZERO;
            qtaBitsAdded = 0;
          }
          break;
      }
    }
    if (bi.signum() > 0)
      li.add(bi);
    return li;
  }

  /**
   * crea una stringa <code>result</code> per accodamento dei singoli valori
   * <code>val</code> della lista in stringa composta. La stringa composta viene
   * costruita scomponendo ogni valore <code>val</code> con :
   * <ol>
   * <li>cc = una operazione di AND di {@link #getMaxBits()} contro
   * <code>val</code></li>
   * <li>dopo di chè si opera uno shiftRight di <code>val</code> di tanti bis
   * quanti sono in {@link #getMaxBits()} == {@link #shift}</li>
   * <li>si inserisce <code>cc</code> nella <code>substring</code> in pos 0 cosi
   * da ottenere <code>"[cn]...[c2][c1][c0]"</code></li>
   * <li>quando <code>val</code> == 0 si conclude inserendo in
   * <code>substring</code> nella pos 0 la qta di chars inseriti cosi da
   * ottenere <code>"[n][cn]..[c2][c1][c0]"</code></li>
   * <li>si accoda la <code>substring</code> a <code>result</code></li>
   * </ol>
   *
   * @param li
   *          lista di valori
   * @return la stringa <code>result</code>
   */
  public String toString(List<BigInteger> li, boolean bWithLen) {
    StringBuilder sb = new StringBuilder();
    for (BigInteger bi : li) {
      // int k = 0;
      StringBuilder sb2 = new StringBuilder();
      int lsh = shift;
      while (/* k <= maxBit && */ bi.signum() > 0) {
        char cc = (char) bi.and(maskBit).intValue();

        sb2.insert(0, cc);
        lsh = bi.bitLength() > shift ? shift : bi.bitLength();
        bi = bi.shiftRight(lsh);
      }
      if (bWithLen) {
        int nc = sb2.length();
        sb2.insert(0, (char) nc);
      }
      sb.append(sb2);
    }
    String sz = sb.toString();
    return sz;
  }

  private char changeChar(char cc) {
    char ret = cc;
    switch (cc) {
      case 8211: //
        ret = '-';
        break;
      default:
        ret = '#';
        break;
    }
    return ret;
  }

  public boolean confronta(List<BigInteger> l1, List<BigInteger> l2) {
    boolean bRet = true;
    int k = 0;
    for (BigInteger bi : l1) {
      BigInteger b2 = l2.get(k);
      if ( !bi.equals(b2)) {
        bRet = false;
        System.out.printf("!%d) %s, %s\n", k, s_fmt.format(bi), s_fmt.format(b2));
      }
      k++;
    }
    return bRet;
  }

  public void setMaxBits(int p_mb) {
    if (p_mb < 7 || p_mb > 128)
      throw new UnsupportedOperationException("maxBit Fuoi range 7-128");
    maxBit = p_mb;
    int resto = maxBit % maskBit.bitLength();
    if (resto != 0) {
      System.err.printf("MaxBit=%d non e multiplo di mask=%d\n", maxBit, maskBit.bitLength());
      maxBit -= resto;
    }

    maxVal = BigInteger.ZERO.setBit(maxBit).subtract(BigInteger.ONE);
  }

  public void setMaxBits(BigInteger p_modulus) {
    setMaxBits(p_modulus.bitLength() - 1);
    maxVal = BigInteger.ZERO.setBit(maxBit).subtract(BigInteger.ONE);
  }

  public int getMaxBits() {
    return maxBit;
  }

  public String toBase64(String p_sz) {
    if (p_sz == null || p_sz.length() == 0)
      return null;
    return Base64.encodeBase64String(p_sz.getBytes());
  }

  public String fromBase64(String p_b64) {
    if (p_b64 == null || p_b64.length() == 0)
      return null;
    return new String(Base64.decodeBase64(p_b64.getBytes()));
  }

  public void setMaxVal(BigInteger p_v) {
    maxVal = p_v;
  }

  public int getShift() {
    return shift;
  }

  public void setShift(int p_v) {
    if (p_v < 6 || p_v > 16)
      throw new UnsupportedOperationException("Shift esagerato(6-16):" + p_v);
    shift = p_v;
    maskBit = BigInteger.ZERO.setBit(p_v).subtract(BigInteger.ONE);
  }

  //  public void setDebug(boolean b) {
  //    m_debug = b;
  //
  //  }

}
