package sm.ciscoop.crypt.primi;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PrimiFactory3 {
  private static PrimiFactory3                                   s_inst;
  @SuppressWarnings("unused")
  private static final long                                      MAXV = 2_000_000_000L;
  @SuppressWarnings("unused") private static final DecimalFormat fmt  = new DecimalFormat("#,###,###,###,###");

  private List<BigInteger>                                       liPrimi;

  public PrimiFactory3() {
    if (s_inst != null)
      throw new UnsupportedOperationException("PrimiFactory3 è già stata istanziata !");
    s_inst = this;
  }

  public static PrimiFactory3 getInst() {
    return s_inst;
  }

  public List<BigInteger> creaPrimi() {
    liPrimi = new ArrayList<BigInteger>();
    
    return liPrimi;
  }

}
