package sm.clagenna.crypt.util;

import javax.swing.SwingWorker;

import sm.clagenna.crypt.primi.PrimiFactory;

public class PrimiWorker extends SwingWorker<Void, Void> {
  private int qtaPrimi;

  @Override
  protected Void doInBackground() throws Exception {
    PrimiFactory primFact = PrimiFactory.getInst();
    primFact.creaPrimi(this, qtaPrimi);
    return null;
  }

  @Override
  protected void done() {
    // System.out.println("PrimiWorker.done()");
  }

  public void pubblica(int p) {
    setProgress(p);
  }

  public void setQtaPrimi(int qtaP) {
    qtaPrimi = qtaP;
  }

}
