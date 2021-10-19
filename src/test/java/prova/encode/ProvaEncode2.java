package prova.encode;

import java.math.BigInteger;
import java.util.List;

import org.junit.Test;

import sm.clagenna.crypt.swing.DeCodeString;

public class ProvaEncode2 {
  private static final String CSZ_COMPLEX = "Gennari\nclaudio\nProva Æ Œ Ψ " //
      + "\r\nIGNORE – drop the erroneous input\r\n" //
      + "REPLACE – replace the erroneous input\r\n" //
      + "REPORT – report the error by returning a CoderResult object or throwing a CharacterCodingException\n" //
      + "Hello ਸੰਸਾਰ!";

  @Test
  public void provalo() {
    String sz = "claudio Gennari";
    DeCodeString deco = new DeCodeString();
    List<BigInteger> li = deco.toList(sz, false);
    String sz2 = deco.toString(li, false);
    System.out.println(" In:" + sz);
    System.out.println("Out:" + sz2);
  }

  @Test
  public void provalo2() {
    DeCodeString deco = new DeCodeString();
    deco.setMaxBits(10);
    deco.setShift(8);
    doDeco(deco, CSZ_COMPLEX);

    deco.setMaxBits(32);
    deco.setShift(8);
    doDeco(deco, CSZ_COMPLEX);
  }

  private void doDeco(DeCodeString deco, String sz) {
    System.out.printf("\n InLu: shift=%d, bits=%d\n%s", deco.getShift(), deco.getMaxBits(), sz);
    List<BigInteger> li = deco.toList(sz, false);
    String sz2 = deco.toString(li, false);
    System.out.printf("\nOutLu:li=%d\n%s\n", li.size(), sz2);
  }
}
