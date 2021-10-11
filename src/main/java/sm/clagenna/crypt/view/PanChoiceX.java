package sm.clagenna.crypt.view;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import sm.clagenna.crypt.primi.PrimiFactory2;

public class PanChoiceX extends JPanel implements IRsa {

  /** long serialVersionUID */
  private static final long serialVersionUID = -3188028111517571156L;
  private NumTextField      txMinPrimo;
  private Long              nMinPrimo;
  private NumTextField      txQtaPrimi;
  private Long              nQtaPrimi;
  private NumTextField      txMaxPrimo;
  private JComboBox<Long>   cbValP;
  private Long              nValP;
  private JComboBox<Long>   cbValQ;
  private Long              nValQ;
  private NumTextField      txPQ;
  private Long              nValPQ;

  private NumTextField      txValE;
  private NumTextField      txValD;
  private NumTextField      txProbes;
  private JProgressBar      prgressBar;
  private JButton           btnCalcola;
  private JButton           btCalcPrimi;

  private boolean           doEvent;

  public PanChoiceX() {
    initComponents();
    miaInit();
  }

  private void initComponents() {
    GridBagLayout gbl_panBottoni = new GridBagLayout();
    gbl_panBottoni.columnWeights = new double[] { 1.0, 1.0, 1.0, 0.0 };
    setLayout(gbl_panBottoni);
    doEvent = true;

    JLabel lbQtaPrimi = new JLabel("Qta Primi");
    GridBagConstraints gbc_lbQtaPrimi = new GridBagConstraints();
    gbc_lbQtaPrimi.insets = new Insets(0, 0, 5, 5);
    gbc_lbQtaPrimi.gridx = 1;
    gbc_lbQtaPrimi.gridy = 0;
    add(lbQtaPrimi, gbc_lbQtaPrimi);

    txMinPrimo = new NumTextField("MinPrimo");
    GridBagConstraints gbc_txMinPrimo = new GridBagConstraints();
    gbc_txMinPrimo.insets = new Insets(0, 0, 5, 5);
    gbc_txMinPrimo.fill = GridBagConstraints.HORIZONTAL;
    gbc_txMinPrimo.gridx = 0;
    gbc_txMinPrimo.gridy = 1;
    add(txMinPrimo, gbc_txMinPrimo);
    txMinPrimo.setColumns(10);
    txMinPrimo.addIRsaListener(new IRsa() {

      @Override
      public boolean setValue(String id, Long lv) {
        nMinPrimo = lv;
        validaCreaPrimi();
        return true;
      }
    });

    JLabel lbMinPrimo = new JLabel("Min Primo");
    GridBagConstraints gbc_lbMinPrimo = new GridBagConstraints();
    gbc_lbMinPrimo.insets = new Insets(0, 0, 5, 5);
    gbc_lbMinPrimo.gridx = 0;
    gbc_lbMinPrimo.gridy = 0;
    add(lbMinPrimo, gbc_lbMinPrimo);

    txQtaPrimi = new NumTextField("QtaPrimi");
    GridBagConstraints gbc_txQtaPrimi = new GridBagConstraints();
    gbc_txQtaPrimi.insets = new Insets(0, 0, 5, 5);
    gbc_txQtaPrimi.fill = GridBagConstraints.HORIZONTAL;
    gbc_txQtaPrimi.gridx = 1;
    gbc_txQtaPrimi.gridy = 1;
    add(txQtaPrimi, gbc_txQtaPrimi);
    txQtaPrimi.setColumns(10);
    txQtaPrimi.addIRsaListener(new IRsa() {

      @Override
      public boolean setValue(String id, Long lv) {
        nQtaPrimi = lv;
        validaCreaPrimi();
        return true;
      }
    });

    JLabel lbMaxPrimo = new JLabel("Max Primo");
    GridBagConstraints gbc_lbMaxPrimo = new GridBagConstraints();
    gbc_lbMaxPrimo.insets = new Insets(0, 0, 5, 5);
    gbc_lbMaxPrimo.gridx = 2;
    gbc_lbMaxPrimo.gridy = 0;
    add(lbMaxPrimo, gbc_lbMaxPrimo);

    txMaxPrimo = new NumTextField("MaxPrimo");
    GridBagConstraints gbc_txMaxPrimo = new GridBagConstraints();
    gbc_txMaxPrimo.insets = new Insets(0, 0, 5, 5);
    gbc_txMaxPrimo.fill = GridBagConstraints.HORIZONTAL;
    gbc_txMaxPrimo.gridx = 2;
    gbc_txMaxPrimo.gridy = 1;
    add(txMaxPrimo, gbc_txMaxPrimo);
    txMaxPrimo.setColumns(10);

    btCalcPrimi = new JButton("Calc Primi");
    btCalcPrimi.setEnabled(false);
    btCalcPrimi.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        btCalcPrimi_Click();
      }
    });
    GridBagConstraints gbc_btCalcPrimi = new GridBagConstraints();
    gbc_btCalcPrimi.insets = new Insets(0, 0, 5, 0);
    gbc_btCalcPrimi.gridx = 3;
    gbc_btCalcPrimi.gridy = 1;
    add(btCalcPrimi, gbc_btCalcPrimi);

    JLabel lbValP = new JLabel("Val P");
    GridBagConstraints gbc_lbValP = new GridBagConstraints();
    gbc_lbValP.insets = new Insets(0, 0, 5, 5);
    gbc_lbValP.gridx = 0;
    gbc_lbValP.gridy = 2;
    add(lbValP, gbc_lbValP);

    cbValP = new JComboBox<Long>();
    GridBagConstraints gbc_txValP = new GridBagConstraints();
    gbc_txValP.insets = new Insets(0, 0, 0, 5);
    gbc_txValP.fill = GridBagConstraints.HORIZONTAL;
    gbc_txValP.gridx = 0;
    gbc_txValP.gridy = 3;
    add(cbValP, gbc_txValP);
    cbValP.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        cbValP_Click(evt);
      }
    });

    JLabel lbValQ = new JLabel("Val Q");
    GridBagConstraints gbc_lbValQ = new GridBagConstraints();
    gbc_lbValQ.insets = new Insets(0, 0, 5, 5);
    gbc_lbValQ.gridx = 1;
    gbc_lbValQ.gridy = 2;
    add(lbValQ, gbc_lbValQ);

    cbValQ = new JComboBox<Long>();
    GridBagConstraints gbc_txValQ = new GridBagConstraints();
    gbc_txValQ.insets = new Insets(0, 0, 0, 5);
    gbc_txValQ.fill = GridBagConstraints.HORIZONTAL;
    gbc_txValQ.gridx = 1;
    gbc_txValQ.gridy = 3;
    add(cbValQ, gbc_txValQ);
    cbValQ.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        cbValQ_Click(evt);
      }
    });

    JLabel lbPQ = new JLabel("PQ");
    GridBagConstraints gbc_lbPQ = new GridBagConstraints();
    gbc_lbPQ.insets = new Insets(0, 0, 5, 5);
    gbc_lbPQ.gridx = 2;
    gbc_lbPQ.gridy = 2;
    add(lbPQ, gbc_lbPQ);

    txPQ = new NumTextField("PQ");
    GridBagConstraints gbc_txPQ = new GridBagConstraints();
    gbc_txPQ.insets = new Insets(0, 0, 0, 5);
    gbc_txPQ.fill = GridBagConstraints.HORIZONTAL;
    gbc_txPQ.gridx = 2;
    gbc_txPQ.gridy = 3;
    add(txPQ, gbc_txPQ);

    JLabel lbValE = new JLabel("Val E");
    GridBagConstraints gbc_lbValE = new GridBagConstraints();
    gbc_lbValE.insets = new Insets(0, 0, 5, 5);
    gbc_lbValE.gridx = 0;
    gbc_lbValE.gridy = 4;
    add(lbValE, gbc_lbValE);

    txValE = new NumTextField("ValE");
    GridBagConstraints gbc_txValE = new GridBagConstraints();
    gbc_txValE.insets = new Insets(0, 0, 0, 5);
    gbc_txValE.fill = GridBagConstraints.HORIZONTAL;
    gbc_txValE.gridx = 0;
    gbc_txValE.gridy = 5;
    add(txValE, gbc_txValE);

    JLabel lbValD = new JLabel("Val D");
    GridBagConstraints gbc_lbValD = new GridBagConstraints();
    gbc_lbValD.insets = new Insets(0, 0, 5, 5);
    gbc_lbValD.gridx = 1;
    gbc_lbValD.gridy = 4;
    add(lbValD, gbc_lbValD);

    txValD = new NumTextField("ValD");
    GridBagConstraints gbc_txValD = new GridBagConstraints();
    gbc_txValD.insets = new Insets(0, 0, 0, 5);
    gbc_txValD.fill = GridBagConstraints.HORIZONTAL;
    gbc_txValD.gridx = 1;
    gbc_txValD.gridy = 5;
    add(txValD, gbc_txValD);

    JLabel lbProbes = new JLabel("Probes");
    GridBagConstraints gbc_lbProbes = new GridBagConstraints();
    gbc_lbProbes.insets = new Insets(0, 0, 5, 5);
    gbc_lbProbes.gridx = 2;
    gbc_lbProbes.gridy = 4;
    add(lbProbes, gbc_lbProbes);

    txProbes = new NumTextField("Probes");
    GridBagConstraints gbc_txProbes = new GridBagConstraints();
    gbc_txProbes.insets = new Insets(0, 0, 0, 5);
    gbc_txProbes.fill = GridBagConstraints.HORIZONTAL;
    gbc_txProbes.gridx = 2;
    gbc_txProbes.gridy = 5;
    add(txProbes, gbc_txProbes);

    btnCalcola = new JButton("Calcola");
    btnCalcola.setEnabled(false);
    btnCalcola.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        btCalcola_Click();
      }
    });
    GridBagConstraints gbc_btnCalcola = new GridBagConstraints();
    gbc_btnCalcola.insets = new Insets(0, 0, 5, 0);
    gbc_btnCalcola.gridx = 3;
    gbc_btnCalcola.gridy = 5;
    add(btnCalcola, gbc_btnCalcola);

    prgressBar = new JProgressBar();
    GridBagConstraints gbc_prgressBar = new GridBagConstraints();
    gbc_prgressBar.insets = new Insets(0, 0, 0, 5);
    gbc_prgressBar.fill = GridBagConstraints.HORIZONTAL;
    gbc_prgressBar.gridx = 0;
    gbc_prgressBar.gridy = 6;
    gbc_prgressBar.gridwidth = 2;
    add(prgressBar, gbc_prgressBar);

  }

  void miaInit() {
    prgressBar.setValue(0);
    prgressBar.setStringPainted(true);
    PrimiFactory2 fact = PrimiFactory2.getInst();
    if (fact != null)
      fact.setProgressBar(prgressBar);
  }

  @Override
  public boolean setValue(String id, Long lv) {
    // TODO Auto-generated method stub
    return false;
  }

  protected void btCalcPrimi_Click() {
    try {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      PrimiFactory2 fact = PrimiFactory2.getInst();
      fact.creaPrimi(nMinPrimo, nQtaPrimi);
      txMaxPrimo.setValue(fact.getMaxPrimo());
      caricaCombo(fact);
    } finally {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
  }

  protected void btCalcola_Click() {
    // TODO Auto-generated method stub

  }

  protected void validaCreaPrimi() {
    boolean bRet = nMinPrimo != null;
    if (bRet)
      bRet = nMinPrimo.longValue() >= 2;
    if (bRet)
      bRet = nQtaPrimi != null;
    if (bRet)
      bRet = nQtaPrimi.longValue() > 2 && nQtaPrimi.longValue() < 500;
    btCalcPrimi.setEnabled(bRet);
  }

  private void caricaCombo(PrimiFactory2 fact) {
    List<Long> lista = fact.getLiSelez();
    try {
      setDoEvent(false);
      DefaultComboBoxModel<Long> cbm = (DefaultComboBoxModel<Long>) cbValP.getModel();
      cbm.removeAllElements();
      for (Long ll : lista) {
        cbm.addElement(ll);
      }
      cbm = (DefaultComboBoxModel<Long>) cbValQ.getModel();
      cbm.removeAllElements();
      for (Long ll : lista) {
        cbm.addElement(ll);
      }
    } finally {
      setDoEvent(true);
    }
  }

  protected void cbValP_Click(ActionEvent evt) {
    if ( !doEvent)
      return;
    DefaultComboBoxModel<Long> cbm = (DefaultComboBoxModel<Long>) cbValP.getModel();
    nValP = (Long) cbm.getSelectedItem();
    if (nValQ != null && nValP != null) {
      nValPQ = nValP.longValue() * nValQ.longValue();
      txPQ.setValue(nValPQ);
    }
  }

  protected void cbValQ_Click(ActionEvent evt) {
    if ( !doEvent)
      return;
    DefaultComboBoxModel<Long> cbm = (DefaultComboBoxModel<Long>) cbValQ.getModel();
    nValQ = (Long) cbm.getSelectedItem();
    if (nValQ != null && nValP != null) {
      nValPQ = nValP.longValue() * nValQ.longValue();
      txPQ.setValue(nValPQ);
    }
  }

  public boolean isDoEvent() {
    return doEvent;
  }

  public void setDoEvent(boolean doEvent) {
    this.doEvent = doEvent;
  }

}
