package sm.clagenna.crypt.primi;

import java.beans.Beans;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import lombok.Getter;
import sm.clagenna.crypt.util.PrimiWorker;
import sm.clagenna.crypt.view.Controllore;

@SuppressWarnings("unused")
public class PrimiFactory {
  private static PrimiFactory        s_inst;
  private static final long          MAXV = 2_000_000_000L;
  private static final DecimalFormat fmt  = new DecimalFormat("#,###,###,###,###");

  @Getter
  private List<Long>                 liPrimi;
  PrimiWorker                        swingW;
  private int                        progrSteps;
  private long                       kProgr;

  public PrimiFactory() {
    if (Beans.isDesignTime())
      return;
    if (s_inst != null)
      throw new UnsupportedOperationException("PrimiFactory3 è già stata istanziata !");
    s_inst = this;
  }

  public static PrimiFactory getInst() {
    return s_inst;
  }

  public List<Long> creaPrimi() {
    return creaPrimi(MAXV);
  }

  public List<Long> creaPrimi(long p_QtaMax) {
    liPrimi = new ArrayList<Long>();
    liPrimi.add(Long.valueOf(2));
    liPrimi.add(Long.valueOf(3));
    liPrimi.add(Long.valueOf(5));
    liPrimi.add(Long.valueOf(7));
    // primi sono distribuiti *SOLO* su (6n-1,6n+1)
    long ll = 2;
    long lx = 0;
    kProgr = 0;
    do {
      lx = ll++ * 6 - 1;
      if (isPrimo(lx)) {
        liPrimi.add(lx);
        if (swingW != null)
          progressBar(p_QtaMax);
      }
      lx += 2;
      if (isPrimo(lx)) {
        liPrimi.add(lx);
        if (swingW != null)
          progressBar(p_QtaMax);
      }
    } while (liPrimi.size() < p_QtaMax);
    Controllore cnt = Controllore.getInst();
    if (cnt != null)
      cnt.setValue(Controllore.FLD_QTAPRIMIGEN, liPrimi.size());
    return liPrimi;
  }

  private void progressBar(long p_QtaMax) {
    if (swingW == null)
      return;
    if (++kProgr >= progrSteps) {
      int p = (int) ((double) liPrimi.size() / (double) p_QtaMax * 100F);
      swingW.pubblica(p);
      kProgr = 0;
    }
  }

  public boolean isPrimo(long vv) {
    // 1) i primi 2,3,5,7 sono dispari oppure = 2
    boolean bRet = vv == 2 || (vv & 1) != 0;
    if (vv <= 7 || !bRet)
      return bRet;

    // 2) i prossimi primi saranno distribuiti *SOLO* su (6n-1,6n+1)
    if ( (vv + 1) % 6 != 0 && (vv - 1) % 6 != 0)
      return false;

    // 3) il tetto dei fattori non supera SQRT(n)
    long maxFact = (long) Math.sqrt(vv) + 1;
    // itero fino a che lastPr < maxFact
    long lastPr = 3;
    // verifico la lista
    int maxLiPrimi = -1;
    if (liPrimi != null || liPrimi.size() > 0)
      maxLiPrimi = liPrimi.size();
    // salto 2 e 3 visto il test al punto 2)
    int iLiPrimo = 2;
    // 4) se esiste cerco se è primo con ogni della lista
    for (; iLiPrimo < maxLiPrimi && lastPr <= maxFact; iLiPrimo++) {
      lastPr = liPrimi.get(iLiPrimo);
      if (vv % lastPr == 0)
        return false;
    }
    // 5) ho finito la lista, proseguo con lastPr
    for (; lastPr <= maxFact; lastPr += 2) {
      // System.out.printf(" %d su %d (%d)\n", lastPr, vv, maxFact);
      if (vv % lastPr == 0)
        return false;
    }
    return bRet;
  }

  public void creaPrimi(PrimiWorker primiWorker, int qtaP) {
    swingW = primiWorker;
    progrSteps = (int) (qtaP / 100F);
    creaPrimi(qtaP);
  }

}
