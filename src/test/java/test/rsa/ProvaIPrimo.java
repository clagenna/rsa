package test.rsa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.NumberFormat;

import org.junit.Test;

import sm.clagenna.crypt.rsa.RsaObj2;

public class ProvaIPrimo {

  @Test
  public void testalo() {
    RsaObj2 rsa = new RsaObj2();
    NumberFormat fmt = NumberFormat.getInstance();
    try (PrintWriter pwr = new PrintWriter(new File("provaIsPrimo.txt"))) {
      for (long i = 1; i < 100_000; i += 2) {
        BigInteger vv = BigInteger.valueOf(i);
        if (rsa.isPrimo(vv)) {
          String sz = fmt.format(vv);
          System.out.println(sz);
          pwr.println(sz);
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
