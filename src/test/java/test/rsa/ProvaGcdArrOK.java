package test.rsa;

import java.math.BigInteger;
import java.text.NumberFormat;

import org.junit.Test;

import sm.ciscoop.crypt.gcd.Gcd;
import sm.ciscoop.crypt.gcd.GcdRec;

public class ProvaGcdArrOK {

  @Test
  public void provalo() {
    long v1 = 2 * 3 * 7 * 13;
    long v2 = 7 * 17 * 23;
    long v3 = 5 * 11 * 29;
    prova(v1, v2);
    prova(v1, v3);
    prova(36_163, 21_199);
    prova(36_163, 1_058);
    prova(9_977_263, 5_532_209);
    prova(3_122_883_319L, 1_731_581_417L);

    provaBI(BigInteger.valueOf(9_977_263L), BigInteger.valueOf(5_532_209L));
    provaBI(BigInteger.valueOf(3_122_883_319L), BigInteger.valueOf(1_731_581_417L));

  }

  void prova(long p1, long p2) {
    long[] res = gcd(p1, p2);
    long r = res[0];
    long x = res[1];
    long y = res[2];
    long res2 = p1 * x + p2 * y;
    // System.out.printf("gcd(%d, %d)=%d\tx=%d y=%d\tres2=%d\n", a, b, r, x, y, res2);
    NumberFormat fmt = NumberFormat.getIntegerInstance();
    System.out.printf("gcd(%s, %s)=%s\tx=%s y=%s\tres2=%s\n", //
        fmt.format(p1), //
        fmt.format(p2), //
        fmt.format(r), //
        fmt.format(x), //
        fmt.format(y), //
        fmt.format(res2));

  }

  void provaBI(BigInteger p1, BigInteger p2) {
    BigInteger a = p1;
    BigInteger b = p2;

    GcdRec res = Gcd.gcd(a, b);
    BigInteger res2 = a.multiply(res.x()).add(b.multiply(res.y()));
    NumberFormat fmt = NumberFormat.getIntegerInstance();
    System.out.printf("gcdBI(%s, %s)=%s\tx=%s y=%s\tres2=%s\n", //
        fmt.format(a), //
        fmt.format(b), //
        fmt.format(res.resto()), //
        fmt.format(res.x()), //
        fmt.format(res.y()), //
        fmt.format(res2));
  }



  public long[] gcd(long a, long b) {
    if (b == 0)
      return new long[] { a, 1, 0 };

    long[] res = gcd(b, a % b);
    long d = res[0];
    long x = res[2];
    long y = res[1] - (a / b) * x;
    return new long[] { d, x, y };
  }
}
