package test.rsa;

import java.math.BigInteger;
import java.text.NumberFormat;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import sm.ciscoop.crypt.primi.PrimiFactory2;
import sm.ciscoop.crypt.rsa.RsaObj2;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestRsaObj2 {

  @Before
  public void inizio() {
    if (PrimiFactory2.getInst() == null)
      new PrimiFactory2();
  }

  @Test
  public void pr01() {
    long l1 = 61 * 67 * 31 * 43 * 51;
    long l2 = 17 * 19 * 31 * 41 * 51;

    BigInteger p1;
    BigInteger p2;

    System.out.println("-------------------------------------");
    p1 = BigInteger.valueOf(1071);
    p2 = BigInteger.valueOf(1029);
    BigInteger lbi = lgcd(p1, p2);
    printVal("lgcd", lbi, p1, p2);

    lbi = igcd(p1, p2);
    printVal("igcd", lbi, p1, p2);

    System.out.println("-------------------------------------");

    p1 = BigInteger.valueOf(l1);
    p2 = BigInteger.valueOf(l2);
    lbi = lgcd(p1, p2);
    printVal("lgcd", lbi, p1, p2);

    lbi = igcd(p1, p2);
    printVal("igcd", lbi, p1, p2);

    System.out.println("-------------------------------------");
    p1 = BigInteger.valueOf(l2);
    p2 = BigInteger.valueOf(l1);
    lbi = lgcd(p1, p2);
    printVal("lgcd", lbi, p1, p2);

    lbi = igcd(p1, p2);
    printVal("igcd", lbi, p1, p2);

    System.out.println("-------------------------------------");
    l1 = 1_234_967_812_311L;
    l2 = 9_764_918_768_341L;

    p1 = BigInteger.valueOf(l2).multiply(BigInteger.valueOf(19_999_817L));
    p2 = BigInteger.valueOf(l1).multiply(BigInteger.valueOf(19_999_789L));
    lbi = lgcd(p1, p2);
    printVal("lgcd", lbi, p1, p2);

    lbi = igcd(p1, p2);
    printVal("igcd", lbi, p1, p2);

    System.out.println("-------------------------------------");
  }

  @SuppressWarnings("unused")
  @Test
  public void pr02() {
    RsaObj2 rsa = new RsaObj2();

    long l1 = 1_234_967_812_311L;
    long l2 = 9_764_918_768_341L;

    BigInteger p1;
    BigInteger p2;

    p1 = BigInteger.valueOf(61);
    p2 = BigInteger.valueOf(53);
    //    p1 = cercaPrimo(p1);
    //    p2 = cercaPrimo(p2);
    rsa.setNP(p1);
    rsa.setNQ(p2);
    rsa.stampaRis();
    rsa.calcolaRSAObj();

    BigInteger probe = BigInteger.valueOf(121);
    
    BigInteger verso = rsa.esponenteE(probe);
    BigInteger ritor = rsa.esponenteD(verso);

  }

  @SuppressWarnings("unused")
  private long cercaPrimo(long p1) {
    long ret = -1;
    int probes = 0;
    for (long px = p1; probes++ < 1000; px += 2) {
      BigInteger bb = BigInteger.valueOf(px);
      if (bb.isProbablePrime(2000)) {
        ret = px;
        break;
      }
    }
    return ret;
  }

  public BigInteger lgcd(BigInteger p_a, BigInteger p_b) {
    printVal("lgcd", p_a, p_b);
    if (p_b == null || p_b.equals(BigInteger.ZERO))
      return p_a;
    return lgcd(p_b, p_a.mod(p_b));
  }

  public BigInteger igcd(BigInteger p_a, BigInteger p_b) {
    BigInteger r = p_a;
    while ( !p_b.equals(BigInteger.ZERO)) {
      printVal("igcd", p_a, p_b);
      r = p_a.mod(p_b);
      p_a = p_b;
      p_b = r;
    }
    return p_a;
  }

  private void printVal(String sz, BigInteger pa, BigInteger pb) {
    NumberFormat fmt = NumberFormat.getInstance();
    String sza = fmt.format(pa);
    String szb = fmt.format(pb);
    System.out.printf("%s: a=%s, b=%s\n", sz, sza, szb);
  }

  private void printVal(String sz, BigInteger pr, BigInteger pa, BigInteger pb) {
    NumberFormat fmt = NumberFormat.getInstance();
    String szr = fmt.format(pr);
    String sza = fmt.format(pa);
    String szb = fmt.format(pb);
    System.out.printf("%s=%s: a=%s, b=%s\n", sz, szr, sza, szb);
  }

}
