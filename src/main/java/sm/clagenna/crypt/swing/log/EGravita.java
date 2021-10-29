package sm.clagenna.crypt.swing.log;

public enum EGravita {
  Debug(1), //
  Log(2), //
  Warn(3), //
  Err(4), //
  Fatal(5);

  private int liv;

  EGravita(int v) {
    liv = v;
  }

  public boolean ge(EGravita othr) {
    return liv >= othr.liv;
  }

  public boolean le(EGravita othr) {
    return liv <= othr.liv;
  }

  public static EGravita valueOf(int v) {
    EGravita ret = null;
    for (EGravita eg : EGravita.values()) {
      if (eg.liv == v) {
        ret = eg;
        break;
      }
    }
    return ret;
  }
}
