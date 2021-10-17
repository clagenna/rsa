package prova.encode;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

public class ProvaCompareList {

  List<BigInteger> m_l1;
  List<BigInteger> m_l2;
  private int      MIN_A = 141_234;
  private int      MAX_A = 554_698;
  NumberFormat     s_fmt = NumberFormat.getIntegerInstance();

  @Test
  public void dothetest() {
    caricaDatI();
    confronta(m_l1, m_l2);
  }

  private void caricaDatI() {
    m_l1 = new ArrayList<BigInteger>();
    m_l2 = new ArrayList<BigInteger>();
    Random rnd = new Random();
    for (int i = 0; i < 100; i++) {
      int nx = rnd.nextInt(MAX_A - MIN_A + 1) + MIN_A;
      BigInteger bi = BigInteger.valueOf(nx);
      m_l1.add(bi);
    }
    m_l2.addAll(m_l1);
    int k = rnd.nextInt(m_l2.size());
    m_l2.set(k, BigInteger.TWO);
  }

  private void confronta(List<BigInteger> l1, List<BigInteger> l2) {
    int k = 0;
    for (BigInteger bi : l1) {
      BigInteger b2 = l2.get(k);
      if ( !bi.equals(b2)) {
        System.out.printf("!%d) %s, %s\n", k, s_fmt.format(bi), s_fmt.format(b2));
      }
      k++;
    }
  }

}
