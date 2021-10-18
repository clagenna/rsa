package sm.clagenna.crypt.swing;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

public class DeCodeString {
  private int        maxBit  = 128;
  private int        shift   = 7;
  private BigInteger maskBit = BigInteger.valueOf(127);
  private BigInteger maxVal;

  public List<BigInteger> toList(String sz) {
    return codifica(sz);
  }

  public List<BigInteger> codifica(String sz) {
    // sz = Base64.encodeBase64String(sz.getBytes());
    BigInteger bi = BigInteger.ZERO;
    List<BigInteger> li = new ArrayList<BigInteger>();
    int k = 0;
    for (char cc : sz.toCharArray()) {
      k += shift;
      if (k > maxBit) {
        li.add(bi);
        bi = BigInteger.ZERO;
        k = shift;
      }
      char canded = (char) (cc & maskBit.intValue());
      if (cc > maskBit.intValue()) {
        System.out.printf("codifica(%d)=%c\n", (int) cc, cc);
        canded = changeChar(cc);
      }

      bi = bi.shiftLeft(shift).add(BigInteger.valueOf(canded));
      if (maxVal != null && maxVal.signum() > 0)
        if (bi.compareTo(maxVal) > 0)
          throw new UnsupportedOperationException("DeCodeString Superato il valore !");
    }
    if (bi.signum() > 0)
      li.add(bi);
    return li;
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

  public String toString(List<BigInteger> li) {
    return decodi(li);
  }

  public String decodi(List<BigInteger> li) {
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
        // k += lsh;
      }
      sb.append(sb2);
    }
    String sz = sb.toString();
    return sz;
  }

  public void setMaxBits(int p_mb) {
    if (p_mb < 7 || p_mb > 128)
      throw new UnsupportedOperationException("maxBit Fuoi range 7-128");
    maxBit = p_mb;
    maxVal = BigInteger.ZERO.setBit(maxBit).subtract(BigInteger.ONE);
  }

  public void setMaxBits(BigInteger p_modulus) {
    maxBit = p_modulus.bitLength() - 1;
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

}
