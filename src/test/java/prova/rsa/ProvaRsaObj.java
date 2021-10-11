package prova.rsa;

import java.math.BigInteger;
import java.text.NumberFormat;

import org.junit.Test;

import sm.clagenna.crypt.rsa.RsaObj;

public class ProvaRsaObj {

  @Test
  public void provaRsaObj() {
    RsaObj rsa = new RsaObj();

    long l1 = 1_234_967_812_313L;
    long l2 = 9_764_918_768_393L;

    BigInteger p1 = BigInteger.valueOf(l1);
    BigInteger p2 = BigInteger.valueOf(l2);

    // p1 = BigInteger.valueOf(l1).nextProbablePrime();
    // p2 = BigInteger.valueOf(l2).nextProbablePrime();

    rsa.setNP(p1);
    rsa.setNQ(p2);
    rsa.calcolaRSAObj();
    rsa.stampaRis();

    BigInteger probe = BigInteger.valueOf(121);

    BigInteger verso = rsa.esponenteE(probe);
    BigInteger ritor = rsa.esponenteD(verso);

    NumberFormat fmt = NumberFormat.getInstance();

    System.out.printf("Con %s ==> %s ==> %s\n", fmt.format(probe), fmt.format(verso), fmt.format(ritor));

  }
}
