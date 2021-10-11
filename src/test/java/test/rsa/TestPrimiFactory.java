package test.rsa;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

import sm.clagenna.crypt.primi.PrimiFactory;

public class TestPrimiFactory {

  @Test
  public void testPrimi() {
    int MAX_PRIMI = 2000_000;
    PrimiFactory pf = new PrimiFactory();
    StopWatch wtch = new StopWatch();
    wtch.start();
    pf.buildPrimi(MAX_PRIMI);
    wtch.stop();
    DecimalFormat fmt = new DecimalFormat("#,###,###,###,###");
    System.out.printf("Generati %s, Max=%s, in %d ms\n", fmt.format(pf.getQtaPrimi()), fmt.format(pf.getMaxPrimo()),
        wtch.getTime(TimeUnit.MILLISECONDS));
    System.out.println("Max Primo=" + pf.getMaxPrimo());
    int maxPrime = 0;
    for (int k = 5051; k < 256000; k += 2) {
      boolean bRet = pf.isPrimo(k);
      if (bRet)
        maxPrime = k;
      BigInteger ii = BigInteger.valueOf(k);
      boolean bRet2 = ii.isProbablePrime(20000);
      if (bRet ^ bRet2) {
        System.out.println("Cosa � " + k);
        testa(k);
      }
      // Assert.assertEquals(bRet, bRet2);
    }
    System.out.println("Max Prime=" + maxPrime);
    int prec = -1;
    for (Long ii : pf.getList()) {
      if (prec > 0)
        System.out.println("diff=" + String.valueOf(ii.intValue() - prec));
      prec = ii.intValue();
    }

  }

  private void testa(int k) {
    int mm = (int) Math.sqrt(k + 1) + 1;
    for (int i = 3; i < mm; i += 2) {
      if ( (k % i) == 0)
        System.out.println(" allora " + k + " divisibile x " + i);
    }
  }
}
