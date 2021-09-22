package test.rsa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.time.StopWatch;

public class TestPrimiFactory2 {
  private static final int           MAXV           = 20_000_000;
  private static final DecimalFormat fmt            = new DecimalFormat("#,###,###,###,###");
  private static final String        CSZ_FILE_PRIMI = "tuttiPrimi_%s.txt";

  private StopWatch                  wtch;
  private BigInteger                 bi;
  private List<Long>                 liStreaEProb;
  private List<Long>                 liForEProb;
  private List<Long>                 liForEPrimo1;
  private List<Long>                 liStreaEPrim0;

  public static void main(String[] args) {
    TestPrimiFactory2 ff = new TestPrimiFactory2();
    /**
     * <pre>
     *
     * 1.270.607 forEProb     in 26.217 ms
     * 1.270.607 forEPrimo1   in  6.521 ms
     * 1.270.606 streaEProb   in 27.212 ms
     * 1.270.606 streamEPrim0 in 17.507 ms
     * Fine !
     * </pre>
     */
    // ff.forEProb();
    ff.forEPrimo1();
    // ff.streaEProb();
    // ff.streamEPrimo0();
    // differenze();
    ff.serializza();
    System.out.println("Fine !");
  }

  private void forEProb() {
    liForEProb = new ArrayList<Long>();
    initList(liForEProb);
    wtch = new StopWatch();
    wtch.start();
    for (long lv = 9; lv < MAXV; lv += 2) {
      bi = BigInteger.valueOf(lv);
      if (bi.isProbablePrime(10)) {
        liForEProb.add(lv);
      }
    }
    stampaResult(liForEProb, "forEProb");
  }

  private void forEPrimo1() {
    liForEPrimo1 = new ArrayList<Long>();
    initList(liForEPrimo1);
    wtch = new StopWatch();
    wtch.start();
    for (long lv = 9; lv < MAXV; lv += 2) {
      if ( (lv % 131321) == 0)
        System.out.printf("%16s (%d)\r", fmt.format(lv), liForEPrimo1.size());
      if (isPrimo2(lv)) {
        liForEPrimo1.add(lv);
      }
    }
    stampaResult(liForEPrimo1, "forEPrimo1");
  }

  private void streaEProb() {
    wtch = new StopWatch();
    wtch.start();
    liStreaEProb = IntStream.rangeClosed(2, MAXV) //
        .filter(x -> (x & 1) != 0) //
        .asLongStream() //
        .filter(x -> BigInteger.valueOf(x).isProbablePrime(10)) //
        .boxed() //
        .collect(Collectors.toList());

    stampaResult(liStreaEProb, "streaEProb");
  }

  private void streamEPrimo0() {
    wtch = new StopWatch();
    wtch.start();
    liStreaEPrim0 = IntStream.rangeClosed(2, MAXV) //
        .filter(x -> (x & 1) != 0) //
        .asLongStream() //
        .filter(x -> isPrimo0(x)) //
        .boxed() //
        .collect(Collectors.toList());
    stampaResult(liStreaEPrim0, "streamEPrim0");
  }

  private void differenze(List<Long> li1, List<Long> li2, String p_m1, String p_m2) {
    List<Long> li = new ArrayList<>();
    String msg = "?";
    if (li1.size() < li2.size()) {
      li.addAll(li1);
      li.removeAll(li2);
      msg = p_m2;
    } else if (li2.size() < li1.size()) {
      li.addAll(li2);
      li.removeAll(li1);
      msg = p_m1;
    }
    if (li.size() > 0) {
      System.out.println("Differenza su:" + msg);
      System.out.println(li);
    }
  }

  private boolean isPrimo0(long vv) {
    if (vv == 2)
      return true;
    if ( (vv & 1) == 0 && vv != 2)
      return false;
    int maxFact = (int) Math.sqrt(vv) + 1;
    for (long lv = 3; lv < maxFact; lv += 2) {
      if (vv % lv == 0)
        return false;
    }
    return true;
  }

  private boolean isPrimo1(long vv) {
    boolean bRet = vv == 2 || (vv & 1) != 0;
    if (vv <= 7 || !bRet)
      return bRet;
    int maxFact = (int) Math.sqrt(vv) + 1;
    for (long lv : liForEPrimo1) {
      if (lv > maxFact)
        return bRet;
      if (vv % lv == 0)
        return false;
    }
    return bRet;
  }

  private boolean isPrimo2(long vv) {
    // i primi 2,3,5,7
    boolean bRet = vv == 2 || (vv & 1) != 0;
    if (vv <= 7 || !bRet)
      return bRet;
    // i prossimi saranno distribuiti su (6n-1,6n+1)
    if ( (vv + 1) % 6 != 0 && (vv - 1) % 6 != 0)
      return false;
    long maxFact = (long) Math.sqrt(vv) + 1;
    int maxK = -1;
    int k = 1;
    if (liForEPrimo1 != null || liForEPrimo1.size() > 0)
      maxK = liForEPrimo1.size();
    for (long lastPr = 3; lastPr <= maxFact; lastPr += 2) {
      if (k < maxK)
        lastPr = liForEPrimo1.get(k++);
      // System.out.printf(" %d su %d (%d)\n", lastPr, vv, maxFact);
      if (vv % lastPr == 0)
        return false;
    }
    return bRet;
  }

  private void initList(List<Long> li) {
    li.add(Long.valueOf(2));
    li.add(Long.valueOf(3));
    li.add(Long.valueOf(5));
    li.add(Long.valueOf(7));
  }

  private void stampaResult(List<Long> p_li, String p_msg) {
    wtch.stop();
    System.out.printf("%s %s\tin %10s ms\n", fmt.format(p_li.size()), p_msg, fmt.format(wtch.getTime(TimeUnit.MILLISECONDS)));
  }

  private void serializza() {
    serializza2(liForEPrimo1, "ForEPrimo1");
    // List<Long> lilo = deserializza(lilo, "ForEPrimo1");
  }

  private void serializza(List<Long> li, String msg) {
    String szFil = String.format(CSZ_FILE_PRIMI, msg);
    File fi = new File(szFil);
    if (fi.exists())
      fi.delete();
    try (FileOutputStream fou = new FileOutputStream(fi)) {
      try (ObjectOutputStream oou = new ObjectOutputStream(fou)) {
        oou.writeObject(li);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void serializza2(List<Long> li, String msg) {
    String szFil = String.format(CSZ_FILE_PRIMI, msg);
    File fi = new File(szFil);
    if (fi.exists())
      fi.delete();
    try (PrintWriter oou = new PrintWriter(fi)) {
      li.stream().forEach(s -> oou.printf("%s\n", fmt.format(s.longValue())));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private List<Long> deserializza(String msg) {
    String szFil = String.format(CSZ_FILE_PRIMI, msg);
    File fi = new File(szFil);
    List<Long> li = null;
    try (FileInputStream fin = new FileInputStream(fi)) {
      try (ObjectInputStream ois = new ObjectInputStream(fin)) {
        Object obj = ois.readObject();
        if (obj instanceof ArrayList)
          li = (List<Long>) obj;
        if (obj != null)
          System.out.println("TestPrimiFactory2.deserializza():" + obj.getClass().getSimpleName());
      }
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return li;
  }
}
