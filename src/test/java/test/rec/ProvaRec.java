package test.rec;

import org.junit.Test;

class RecGcd {
  int a;
  int b;
  int resto;
  int x;
  int y;

  public RecGcd(int a2, int b2) {
    a = a2;
    b = b2;
    x = 0;
    y = 1;
  }

  @Override
  public String toString() {
    int cc = a * x + b * y;
    String sz = String.format("a=%d, b=%d\tresto=%d, x=%d, y=%d\tcalc=%d\n", a, b, resto, x, y, cc);
    return sz;
  }
}

public class ProvaRec {

  private int[] x1;
  private int[] y1;
  private int   k1;

  @Test
  public void provalo() {
    int v1 = 2 * 3 * 7 * 13;
    int v2 = 7 * 17 * 23;
    int v3 = 5 * 11 * 29;
    // int x = 1, y = 1;
    x1 = new int[200];
    y1 = new int[200];
    k1 = 0;

    //    int[] ii = gcd(v1, v2);
    //    System.out.println("r[0]=" + ii[0] + "\tr[1]=" + ii[1] + "\tr[2]=" + ii[2]);

    x1[k1] = 1;
    y1[k1] = 1;
    int r = gcdExtended(v1, v2);
    System.out.printf("r=%d\tx=%d\ty=%d\n", r, x1[k1], y1[k1]);
    int r2 = v1 * x1[k1] + v2 * y1[k1];
    System.out.printf("r=%d\tr2=%d\n", r, r2);
    
    
    x1[k1] = 1;
    y1[k1] = 1;
    r = gcdExtended(29, 7);
    System.out.printf("r=%d\tx=%d\ty=%d\n", r, x1[k1], y1[k1]);
    r2 = v1 * x1[k1] + v2 * y1[k1];
    System.out.printf("r=%d\tr2=%d\n", r, r2);

    
    

    //    RecGcd r12 = gcdExtended(v1, v2);
    //    System.out.println(r12.toString());
    //
    //    RecGcd r13 = gcdExtended(v1, v3);
    //    System.out.println(r13.toString());

  }

  //  public RecMcm gcd(BigInteger p_p, BigInteger p_q) {
  //    BigInteger p = p_p;
  //    BigInteger q = p_q;
  //    if (q.compareTo(p) > 0) {
  //      p = p_q;
  //      q = p_p;
  //    }
  //    RecMcm rec = new RecMcm(p, q, null, null);
  //
  //    if (p_q.equals(BigInteger.ZERO))
  //      return rec;
  //
  //    RecMcm vals = gcd(q, p.mod(q));
  //    BigInteger d = vals[0];
  //    BigInteger a = vals[2];
  //    BigInteger b = vals[1] //
  //        .subtract( //
  //            p.divide(q) //
  //                .multiply(a));
  //    System.out.printf("p=%d, q=%d, d=%d, a=%d, b=%d\n", p, q, d, a, b);
  //    return rec;
  //  }

  //  return array [d, a, b] such that d = gcd(p, q), ap + bq = d
  public int[] gcd(int p, int q) {
    if (q == 0) {
      System.out.printf("a=%d b=%d\tresto=%d\n", p, q, p, 1, 0);
      return new int[] { p, 1, 0 };
    }
    // System.out.printf("gcd(%d %d)\n", q, p % q);
    int[] vals = gcd(q, p % q);
    int d = vals[0];
    int a = vals[2];
    int b = vals[1] - (p / q) * vals[2];
    System.out.printf("gcd(%d %d) = %d x=%d, y=%d\n", q, p % q, d, a, b);
    return new int[] { d, a, b };
  }

  /**
   * <pre>
   * class RecGcd {
   *   int a;
   *   int b;
   *   int resto;
   *   int x;
   *   int y;
   * </pre>
   *
   * @param a
   * @param b
   * @return
   */
  //  public RecGcd gcdExtended(int a, int b) {
  //    RecGcd rgc = new RecGcd(a, b);
  //
  //    if (b == 0) {
  //      rgc.resto = rgc.a;
  //      return rgc;
  //    }
  //
  //    RecGcd ret = gcdExtended(b, b % a);
  //    rgc.resto = ret.resto;
  //    int la = ret.y;
  //    int lb = ret.x - (a / b) * ret.y;
  //    rgc.x = la;
  //    rgc.y = lb;
  //
  //    return rgc;
  //  }

  public int gcdExtended(int a, int b) {
    // Base Case
    if (a == 0) {
      x1[k1] = 0;
      y1[k1] = 1;
      System.out.printf("gcdEx(a=%d, b=%d)[x=%d,y=%d]=%d\n", a, b, x1[k1], y1[k1], b);
      return b;
    }
    k1++;
    x1[k1] = 1;
    y1[k1] = 1; // To store results of recursive call
    int gcd = gcdExtended(b % a, a);
    k1--;
    // Update x and y using results of recursive
    // call
    x1[k1] = y1[k1 + 1] - (b / a) * x1[k1 + 1];
    y1[k1] = x1[k1 + 1];
    System.out.printf("gcdEx(a=%d, b=%d)[x=%d,y=%d]=%d\n", a, b, x1[k1], y1[k1], gcd);
    return gcd;
  }

}
