package test.rsa;

public class ExtEuclAlg {

  public static long gcdExtended(long a, long b) {
    long x = 1, y = 1;
    return gcdExtended(a, b, x, y);
  }

  // extended Euclidean Algorithm
  public static long gcdExtended(long a, long b, long x, long y) {
    // Base Case
    if (a == 0) {
      x = 0;
      y = 1;
      return b;
    }

    long x1 = 1, y1 = 1; // To store results of recursive call
    long gcd = ExtEuclAlg.gcdExtended(b % a, a, x1, y1);

    // Update x and y using results of recursive
    // call
    x = y1 - (b / a) * x1;
    y = x1;

    return gcd;
  }

  public static long gcd2(long a, long b) {
    while (a != b) {
      if (a > b)
        a = a - b;
      else
        b = b - a;
    }
    return a;
  }

  //Driver Program
  public static void main(String[] args) {
    long a, b, a1, b1;

    long Prim1 = 5_974_211L;
    long Prim2 = 11_555_651L;
    // long PrimX = 440_893L;
    long PrimY = 8_179_511L;
    a = Prim1 * PrimY;
    b = Prim2 * PrimY;
    a1 = 5_974_211L * 440_893L;
    b1 = 11_555_651L * 440_893L;
    a1 = 5_974_211L * 8_179_511L;
    b1 = 11_555_651L * 8_179_511L;

    if (a != a1)
      System.out.println("Non è lo stesso !?!?!?");
    if (b != b1)
      System.out.println("Non è lo stesso !?!?!?");

    long g /* = gcdExtended(a, b, x, y) */;
    g = gcd2(a1, b1);
    g = gcd2(b1, a1);
    g = gcdExtended(a1, b1);
    g = gcdExtended(b1, a1);

    g = a / PrimY;
    if (g != Prim1)
      System.out.println("Non è lo stesso !?!?!?");
    g = b / PrimY;
    if (g != Prim2)
      System.out.println("Non è lo stesso !?!?!?");

    g = ExtEuclAlg.gcd2(a, b);
    System.out.print("gcd(" + a + " , " + b + ") = " + g);

  }
}
