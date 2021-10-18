package prova.encode;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

import org.junit.Test;

import sm.clagenna.crypt.swing.DeCodeString;

public class ProvaSeqX {
  private DeCodeString     deco;
  private List<BigInteger> li2;

  /**
   * provo se {@link DeCodeString#decodi(List)} torna una corretta lista per una
   * data sequenza
   */
  @Test
  public void provalo() {
    caricaDati();
    int shift = 8;
    //li2.stream().forEach(s -> System.out.printf("%d, l=%d\n", s.longValue(), s.bitLength()));

    // ----------------------------------------------
    // prova la formula per maxBits = ((maxLen / shift) + 1 ) * shift
    OptionalInt opt = li2.stream().mapToInt(BigInteger::bitLength).max();
    int maxBits = (opt.getAsInt() / shift + 1) * shift;
    deco.setShift(shift);
    deco.setMaxBits(maxBits);
    boolean bRet = codDeco(li2, maxBits);
    if (bRet)
      System.out.printf("Con %d bits !\n", maxBits);
    else
      System.err.println("Non funziona !!");
    // ----------------------------------------------

    // ----------------------------------------------
    // ----- provo maxBits da 12 a 40 ---------------
    for (int mb = 12; mb < 40; mb++)
      System.out.printf("maxBit=%d %s\n", mb, (codDeco(li2, mb) ? "==" : "-"));
    // ----------------------------------------------

  }

  private void caricaDati() {
    deco = new DeCodeString();
    li2 = new ArrayList<BigInteger>();
    li2.add(BigInteger.valueOf(813279L));
    li2.add(BigInteger.valueOf(146003L));
    li2.add(BigInteger.valueOf(713785L));
    li2.add(BigInteger.valueOf(102290L));
    li2.add(BigInteger.valueOf(804771L));
    li2.add(BigInteger.valueOf(544220L));
    li2.add(BigInteger.valueOf(666593L));
    li2.add(BigInteger.valueOf(758336L));
    li2.add(BigInteger.valueOf(78720L));
    li2.add(BigInteger.valueOf(1048575L));
  }

  private boolean codDeco(List<BigInteger> p_li, int p_maxBit) {
    boolean bRet = false;
    deco.setMaxBits(p_maxBit);
    String sz2 = deco.decodi(p_li);
    List<BigInteger> li3 = deco.codifica(sz2);
    bRet = p_li.size() == li3.size();
    if (bRet) {
      for (int k = 0; k < li3.size(); k++) {
        if ( !p_li.get(k).equals(li3.get(k))) {
          System.out.printf("(li2(%d) %d !=  li3 %d )\n", k, p_li.get(k), li3.get(k));
          bRet = false;
        }
      }

    }
    return bRet;
  }

}
