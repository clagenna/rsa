package prova.encode;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import sm.clagenna.crypt.rsa.RsaObj;
import sm.clagenna.crypt.swing.DeCodeString;

public class ProvaEncode3 {
  private static NumberFormat s_fmt = NumberFormat.getIntegerInstance();

  private RsaObj              rsa;
  private DeCodeString        deco;
  private String              ptf   = "%d) %10s:%s\n";

  @Test
  public void provaRsaObj() {
    creaRsa();
    deco = new DeCodeString();
    deco.setMaxBits(rsa.getNPQTotientFi());
    deco.setMaxVal(rsa.getNPQTotientFi().subtract(BigInteger.ONE));
    deco.setShift(8);

    String sz = "Gennari Claudio";
    // trovaUltimoValore();
    String szEnc = codificaStringa(sz);
    String szDeco = decodificaStringa(szEnc);
    System.out.println("ProvaEncode3.Decode=" + szDeco);
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

  @SuppressWarnings("unused")
  private void trovaUltimoValore() {
    BigInteger bi = BigInteger.ZERO;
    BigInteger bi2;
    BigInteger lastOk = bi;

    int bitUp = 1;
    boolean bEq = false;
    // a salire coi bit
    do {
      bi = BigInteger.ZERO;
      bi = bi.setBit(bitUp++);
      bi2 = rsa.esponenteE(bi);
      bi2 = rsa.esponenteD(bi2);
      bEq = bi.equals(bi2);
      System.out.printf("Salire:%s %s\n", (bEq ? "=" : " "), s_fmt.format(bi));
      lastOk = bEq ? bi : lastOk;
    } while (bi.equals(bi2));
    // ora a scendere
    --bitUp;
    do {
      bi = lastOk.setBit(--bitUp);
      bi2 = rsa.esponenteE(bi);
      bi2 = rsa.esponenteD(bi2);
      bEq = bi.equals(bi2);
      System.out.printf("Scendo:%s %s\n", (bEq ? "=" : " "), s_fmt.format(bi));
      lastOk = bEq ? bi : lastOk;
    } while (bitUp > 0);
    rsa.stampaRis();
    System.out.println("MaxVal:" + s_fmt.format(lastOk));
  }

  @SuppressWarnings("unused")
  private String codificaStringa(String sz) {
    BigInteger a1 = BigInteger.valueOf(121);
    BigInteger a2 = rsa.esponenteE(a1);
    BigInteger a3 = rsa.esponenteD(a2);
    if ( !a1.equals(a3))
      throw new UnsupportedOperationException("ProvaEncode3.codificaStringa( a1 != a3)");

    // sz => base64
    String sz1 = deco.toBase64(sz);
    String szx = deco.fromBase64(sz1);
    int k = 1;
    System.out.printf(ptf, k++, "sorg", sz);
    System.out.println();
    System.out.printf(ptf, k++, "base64", sz1);
    System.out.println();

    // 1) base64 => codi() => list(BigInt)
    List<BigInteger> li = deco.toList(sz1, false);
    szx = deco.toString(li, false);
    final int q = k;
    li.stream().forEach(s -> System.out.printf(ptf, q, "deco", s_fmt.format(s)));
    System.out.println();
    k++;
    List<BigInteger> li2 = new ArrayList<>();

    // 2) list(BigInt) => RSA.E => list2(BigInt)
    for (BigInteger bi : li) {
      BigInteger bi1 = rsa.esponenteE(bi);
      li2.add(bi1);
      BigInteger bi2 = rsa.esponenteD(bi1);
      if ( !bi.equals(bi2))
        System.out.println("Sono diversi !");
    }
    final int q2 = k;
    li2.stream().forEach(s -> System.out.printf(ptf, q2, "rsaE", s_fmt.format(s)));
    System.out.println();

    //     Integer maxL2 = li2 //
    //            .stream() //
    //            .max(Comparator.comparing(s -> s.bitLength())) //
    //            .orElseThrow(NoSuchElementException::new);

    // 3) list2(BigInt) => deco() => sz2
    OptionalInt ii = li2.stream().mapToInt(BigInteger::bitLength).max();
    System.out.println("li2.MaxBitLength=" + ii.getAsInt());
    String sz2 = deco.toString(li2, true);
    List<BigInteger> li3 = deco.toList(sz2, true);
    /**
     * dopo la crittazione ho numeri alti in li2, (da 21.042 passo a 813.279 )
     * anche se minori di Fi ( 949.248 ) eppure la decodifica non riporta alla
     * lista. Provato con @{link ProvaSeqX}
     */
    // ---------------------------------------------------------------------------------
    // cerco la BitMaxLength in li2

    if (li2.size() != li3.size())
      System.out.printf("codificaStringa(li2 %d !=  li3 %d\n", li2.size(), li3.size());
    // ---------------------------------------------------------------------------------
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
    return sb.toString();
  }

  private String decodificaStringa(String szEnc) {
    System.out.println();
    int k = 5;
    // 5) tolgo i CR LF
    String sz1 = szEnc.replace("\n", "");
    System.out.printf(ptf, k--, "B64encoRSA", sz1);
    // 4) riporto la Base64 a elenco di chars encoded
    String sz2 = new String(Base64.decodeBase64(sz1.getBytes()));
    System.out.printf(ptf, k--, "encoRSA", sz2);
    // 3) creo elenco di BigInt
    final int q = k;
    List<BigInteger> li = deco.toList(sz2, true);
    li.stream().forEach(s -> System.out.printf(ptf, q, "RsaE", s_fmt.format(s)));
    k--;
    // 2) decodifica dei BigInt tramite RSA
    List<BigInteger> li2 = new ArrayList<BigInteger>();
    for (BigInteger bi : li) {
      BigInteger bi1 = rsa.esponenteD(bi);
      li2.add(bi1);
      System.out.printf(ptf, k, "rsaD", s_fmt.format(bi1));
    }
    final int p = k;
    li.stream().forEach(s -> System.out.printf(ptf, p, "decoRSA", s_fmt.format(s)));
    k--;
    // 1) ritorno alla stringa orig
    String sz3 = deco.toString(li2, false);
    System.out.printf(ptf, k, " final", sz3);
    return sz3;
  }
}
