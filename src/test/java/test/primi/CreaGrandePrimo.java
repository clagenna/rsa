package test.primi;

import java.math.BigInteger;
import java.text.NumberFormat;

import org.junit.Test;

import sm.ciscoop.crypt.rsa.RsaObj2;
import sm.ciscoop.crypt.util.TimeExec;

public class CreaGrandePrimo {
  NumberFormat fmt = NumberFormat.getIntegerInstance();

  @Test
  public void iteraQtaBit() {
    // provalo(34);
    for (int qb = 16; qb <= 2048; qb += 32)
      provalo(qb);
  }

  public void provalo(int qtaBit) {
    System.out.println("\n----- bits=" + qtaBit + " ---------------");
    TimeExec tim1 = new TimeExec();
    RsaObj2 rsa = new RsaObj2();

    TimeExec tim2 = new TimeExec();
    BigInteger p = RsaObj2.creaGrandeNumeroPrimo(qtaBit);
    System.out.printf("Grande Num.P:\tQtaBit=%d, time=%s\n", qtaBit, tim2.stop());
    BigInteger q = BigInteger.ZERO;
    do {
      q = RsaObj2.creaGrandeNumeroPrimo(qtaBit);
    } while (p.equals(q));
    System.out.printf("Grande Num.Q:\tQtaBit=%d, time=%s\n", qtaBit, tim2.stop());

    rsa.setNP(p);
    rsa.setNQ(q);
    rsa.calcolaRSAObj();
    System.out.printf("Calcola RSA:\tQtaBit=%d, time=%s\n", qtaBit, tim2.stop());

    BigInteger probe = RsaObj2.creaGrandeNumeroPrimo(qtaBit);

    BigInteger andata;
    BigInteger ritorn;

    andata = rsa.esponenteE(probe);
    ritorn = rsa.esponenteD(andata);

    System.out.printf("Cry.Decry:\tQtaBit=%d, time=%s\n", qtaBit, tim2.stop());

    if ( !probe.equals(ritorn))
      System.err.printf("--->ERROR QtaBit=%d\t%s != %s\n", qtaBit, fmt.format(probe), fmt.format(ritorn));
    System.out.printf("Completo:\tQtaBit=%d, time=%s\n", qtaBit, tim1.stop());
  }

  @SuppressWarnings("unused")
  private void printBi(BigInteger bi) {
    boolean bPrimo = bi.isProbablePrime(2048);
    System.out.println("Qta bits=" + bi.bitLength());
    System.out.printf("val(%s)=%s\n", (bPrimo ? "P" : "-"), fmt.format(bi));
    System.out.println("val=" + bi.toString(16));
  }

}
