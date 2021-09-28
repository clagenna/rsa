package sm.ciscoop.crypt.util;

public class TimeExec {
  private long t1;

  public TimeExec() {
    start();
  }

  public void start() {
    t1 = System.currentTimeMillis();
  }

  public String stop() {
    float t2 = (System.currentTimeMillis() - t1) / 1000F;
    start();
    return String.format("%.3f", t2);
  }

}
