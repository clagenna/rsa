package prova.primi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

import sm.clagenna.crypt.primi.PrimiFactory;

public class ProvaPrimiFactory {

  private StopWatch wtch;

  @Test
  public void provaFact() {
    PrimiFactory fact = new PrimiFactory();
    NumberFormat fmt = NumberFormat.getIntegerInstance();
    wtch = new StopWatch();
    wtch.start();
    List<Long> li = fact.creaPrimi(100);
    System.out.println(li);
    long i = 0;
    try (PrintWriter pwr = new PrintWriter(new File("provaPrimiFactory.txt"))) {
      for (i = 3; i < 100_000; i += 2) {
        if (i == 10_399)
          System.out.println("ProvaPrimiFactory.provaFact()");
        if (fact.isPrimo(i) && i > 3) {
          String sz = fmt.format(i);
          System.out.println(sz);
          if ( !li.contains(i))
            li.add(i);
          pwr.println(sz);
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    wtch.stop();
    System.out.printf("%s \tin %10s ms\n", fmt.format(li.size()), fmt.format(wtch.getTime(TimeUnit.MILLISECONDS)));

    // fattorizza6n(fact);
  }

  @SuppressWarnings("unused")
  private void fattorizza6n(PrimiFactory fact) {
    for (long ii = 4; ii < 15; ii++) {
      long lx = ii * 6 - 1;
      System.out.printf("%s \t%s\n", //
          String.valueOf(lx), //
          fact.isPrimo(lx) ? "P" : "-");
      lx += 2;
      System.out.printf("%s \t%s\n", //
          String.valueOf(lx), //
          fact.isPrimo(lx) ? "P" : "-");
    }

    for (long ii = 20; ii < 30; ii++) {
      long lx = ii * 6 - 1;
      System.out.printf("%s \t%s\n", //
          String.valueOf(lx), //
          fact.isPrimo(lx) ? "P" : "-");
      lx += 2;
      System.out.printf("%s \t%s\n", //
          String.valueOf(lx), //
          fact.isPrimo(lx) ? "P" : "-");
    }
  }

}
