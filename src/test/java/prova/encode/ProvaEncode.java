package prova.encode;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

public class ProvaEncode {
  String csz = "Prova Æ Œ Ψ @òçèéàù \t\f\r\ncosa scàppa fuori";

  @Test
  public void provalo() {
    for (char cc : csz.toCharArray()) {
      String sz0 = Integer.toHexString(cc);
      String sz = Integer.toHexString(cc | 0x10000).substring(1);
      int ii = cc;
      System.out.printf("%c=%s (%s-%d)\n", cc, sz, sz0, ii);
    }
  }

  @Test
  public void doit() {
    String sz = "Gennari\nclaudio\nProva Æ Œ Ψ ";
    sz += "IGNORE – drop the erroneous input\r\n" //
        + "REPLACE – replace the erroneous input\r\n" //
        + "REPORT – report the error by returning a CoderResult object or throwing a CharacterCodingException\n";
    sz += "Hello ਸੰਸਾਰ!";
    byte[] by = sz.getBytes(StandardCharsets.UTF_8);
    Base64 b64 = new Base64();
    String enc = new String(b64.encode(by));
    String szret = new String(b64.decode(enc.getBytes()));
    System.out.println("Stringa:" + sz);
    System.out.println("Encoded:" + enc);
    System.out.println("Decoded:" + szret);

    // int n = enc.length();
    int maxBit = 100;
    int shift = 7; // 7 bit = 127 max
    BigInteger bi = BigInteger.ZERO;
    List<BigInteger> li = new ArrayList<BigInteger>();
    int k = 0;
    for (char cc : sz.toCharArray()) {
      k += shift;
      if (k > maxBit) {
        li.add(bi);
        bi = BigInteger.ZERO;
        k = 0;
      }
      bi = bi.shiftLeft(shift).add(BigInteger.valueOf(cc & 127));
    }
    System.out.println("li Size:" + li.size());
    li.stream().forEach(System.out::println);

    StringBuilder sb = new StringBuilder();
    BigInteger b127 = BigInteger.valueOf(127);
    for (BigInteger b : li) {
      k = maxBit;
      while (k > 0) {
        int coda;
        coda = b.and(b127).intValue();
        char cc = (char) coda;
        sb.append(cc);
        System.out.println(sb);
        b = b.shiftRight(shift);
        k -= shift;
      }
    }

  }

}
