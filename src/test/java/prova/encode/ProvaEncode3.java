package prova.encode;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import sm.clagenna.crypt.rsa.RsaObj;
import sm.clagenna.crypt.swing.DeCodeString;

public class ProvaEncode3 {
  private static NumberFormat s_fmt = NumberFormat.getIntegerInstance();

  private RsaObj              rsa;

  @Test
  public void provaRsaObj() {
    creaRsa();
    codificaStringa();

  }

  private void creaRsa() {
    rsa = new RsaObj();

    long l1 = 1_234L;
    long l2 = 764L;

    BigInteger p1 = BigInteger.valueOf(l1);
    BigInteger p2 = BigInteger.valueOf(l2);

    p1 = BigInteger.valueOf(l1).nextProbablePrime();
    p2 = BigInteger.valueOf(l2).nextProbablePrime();

    rsa.setNP(p1);
    rsa.setNQ(p2);
    rsa.calcolaRSAObj();
    rsa.stampaRis();
  }

  private void codificaStringa() {
    String ptf = "%d) %10s:%s\n";
    String sz = "claudio gennari";
    DeCodeString deco = new DeCodeString();
    deco.setMaxBits(rsa.getNPQTotientFi());
    // sz => base64
    String sz1 = deco.toBase64(sz);
    int k = 1;
    System.out.printf(ptf, k++, "sorg", sz);
    System.out.printf(ptf, k++, "base64", sz1);
    // 1) base64 => codi() => list(BigInt)
    List<BigInteger> li = deco.codifica(sz1);
    final int q = k;
    li.stream().forEach(s -> System.out.printf(ptf, q, "deco", s_fmt.format(s)));
    k++;
    List<BigInteger> li2 = new ArrayList<>();
    // 2) list(BigInt) => RSA.E => list2(BigInt)
    for (BigInteger bi : li) {
      bi = rsa.esponenteE(bi);
      li2.add(bi);
      System.out.printf(ptf, k, "rsaE", s_fmt.format(bi));
    }
    // 3) list2(BigInt) => deco() => sz2
    String sz2 = deco.decodi(li2);
    System.out.printf(ptf, k++, "encoRSA", sz2);
    // 4) sz2 => Base64
    sz2 = Base64.encodeBase64String(sz2.getBytes());
    System.out.printf(ptf, k++, "B64encoRSA", sz2);
    // 5) chunk di 64 chars
    StringBuilder sb = new StringBuilder();
    for (k = 0; k < sz2.length(); k += 64) {
      int fine = k + 64;
      if (fine > sz2.length())
        fine = sz2.length();
      sb.append(sz2.substring(k, fine)).append("\n");
    }
  }
}
