package prova.encode;

import java.math.BigInteger;
import java.util.List;

import org.junit.Test;

import sm.clagenna.crypt.swing.DeCodeString;

public class ProvaEncode2 {
  int maxBit = 128;
  int shift  = 7;

  @Test
  public void provalo() {
    String sz = "claudio Gennari";
    DeCodeString deco = new DeCodeString();
    List<BigInteger> li = deco.codifica(sz);
    String sz2 = deco.decodi(li);
    System.out.println(" In:" + sz);
    System.out.println("Out:" + sz2);

    li = deco.codifica(sz);
    sz2 = deco.decodi(li);

    System.out.println(" In64:" + sz);
    System.out.println("Out64:" + sz2);

    sz = "Gennari\nclaudio\nProva Æ Œ Ψ ";
    sz += "IGNORE – drop the erroneous input\r\n" //
        + "REPLACE – replace the erroneous input\r\n" //
        + "REPORT – report the error by returning a CoderResult object or throwing a CharacterCodingException\n";
    sz += "Hello ਸੰਸਾਰ!";
    li = deco.codifica(sz);
    sz2 = deco.decodi(li);

    System.out.println(" InLu:" + sz);
    System.out.println("OutLu:" + sz2);
  }
}
