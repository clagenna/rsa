package test.rsa;

import java.math.BigInteger;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import sm.ciscoop.crypt.rsa.RsaObj2;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestGCDArr {
  private static BigInteger UNO = BigInteger.ONE;

  @Test
  public void aprima() {
    BigInteger ne = BigInteger.valueOf(3);
    BigInteger nfi = BigInteger.valueOf(9167368);
    BigInteger nd = multInv(ne, nfi);
    System.out.printf("(D sara=6111579 ?, d=%d\n", nd.longValue());
  }

  @Test
  public void giocatela() {
    BigInteger[] arr;
    RsaObj2 rsa = new RsaObj2();
    BigInteger np = BigInteger.valueOf(311);
    BigInteger nq = BigInteger.valueOf(313);
    BigInteger nn = np.multiply(nq);
    BigInteger npm1 = np.subtract(UNO);
    BigInteger nqm1 = nq.subtract(UNO);

    BigInteger nfi = npm1.multiply(nqm1);
    // E scelto arbitrariamente 1<E<fi
    BigInteger ne = BigInteger.valueOf(29);
    do {
      ne = ne.add(BigInteger.TWO);
      arr = rsa.gcd(ne, nfi);
    } while ( !arr[0].equals(UNO));
    // [] = { resto, a, b } percui gcd = ne*a + fi*b
    // resto=1 percui ne è co-prime con fi
    BigInteger nd = multInv(ne, nfi);
    System.out.printf("1) d=%d\n", nd.longValue());

    BigInteger ned = nd.multiply(ne);
    ned = ned.mod(nfi);
    System.out.printf("2) ed mod(fi)=%d\n", ned.longValue());

  }

  public int multInv(int e, int fi) {
    double result = 2F;
    int k = 1;
    while ( (Math.round(result) % 1) == 0) {
      result = (1 + (k++ * fi)) / (double) e;
      if ( (Math.round(result) % 1) == 0) //integer
        break;
    }
    return (int) result;
  }

  public BigInteger multInv(BigInteger e, BigInteger fi) {
    BigInteger[] result = { UNO, BigInteger.TWO };
    BigInteger k = BigInteger.ZERO;
    while ( !result[1].equals(BigInteger.ZERO)) {
      k = k.add(UNO);
      result = (UNO //
          .add( //
              k.multiply(fi)) //
      ).divideAndRemainder(e);
      System.out.printf("k=%d, resto=%d\n", k.longValue(), result[1].longValue());
    }
    return result[0];
  }
}
