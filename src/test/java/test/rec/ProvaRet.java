package test.rec;

import org.junit.Test;

public class ProvaRet {

  @Test
  public void doit() {
    // recurse(17, 1);
    Integer a = 17;
    System.out.printf("1)a=%d\n", a);
    chiama(a);
    System.out.printf("2)a=%d\n", a);
  }

  private void chiama(Integer a) {
    a += 5;
    System.out.println("a2=" + a);
  }

  @SuppressWarnings("unused")
  private int recurse(int a, int b) {
    System.out.printf("1)a=%d, b=%d\n", a, b);
    if (b >= 100)
      return a;
    b += 7;
    recurse(a % b, b);
    System.out.printf("\t2)a=%d, b=%d\n", a, b);
    return a;
  }

}
