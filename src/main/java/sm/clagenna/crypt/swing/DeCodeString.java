package sm.clagenna.crypt.swing;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

public class DeCodeString {
  private int        maxBit = 128;
  private int        shift  = 7;
  private BigInteger b127   = BigInteger.valueOf(127);
  private BigInteger maxVal;

  public List<BigInteger> codifica(String sz) {
    sz = Base64.encodeBase64String(sz.getBytes());
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
      bi = bi.shiftLeft(shift).add(BigInteger.valueOf(cc & 127));
      if (maxVal != null && maxVal.signum() > 0)
        if (bi.compareTo(maxVal) > 0)
          System.out.println("DeCodeString Superato il valore !");
    }
    if (bi.signum() > 0)
      li.add(bi);
    return li;
  }

  public String decodi(List<BigInteger> li) {
    StringBuilder sb = new StringBuilder();
    for (BigInteger bi : li) {
      int k = 0;
      while (k <= maxBit && bi.signum() > 0) {
        char cc = (char) bi.and(b127).intValue();
        sb.insert(0, cc);
        bi = bi.shiftRight(shift);
        k -= shift;
      }
    }
    String sz = sb.toString();
    return sz;
  }

  public void setMaxBits(BigInteger p_modulus) {
    maxBit = p_modulus.bitLength() - 1;
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

}
