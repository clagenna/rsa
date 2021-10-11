package sm.clagenna.crypt.view;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import sm.clagenna.crypt.primi.PrimiFactory;
import sm.clagenna.crypt.swing.IRsa;
import sm.clagenna.crypt.swing.IRsaListen;
import sm.clagenna.crypt.swing.NumTextField;

public class Pan1GenPrimi extends JPanel implements IRsaListen {

  /** long serialVersionUID */
  private static final long serialVersionUID = -4193143331761193489L;
  private NumTextField      txQtaPrimi;
  private NumTextField      txPrimiGenerati;
  private IRsa              m_irsa;
  private JButton           btGeneraPrimi;

  public Pan1GenPrimi() {
    initComponents();
  }

  public Pan1GenPrimi(IRsa p_irsa) {
    m_irsa = p_irsa;
    Controllore.getInst().addListener(this);
    initComponents();
  }

  private void initComponents() {

    setLayout(new BorderLayout(0, 0));

    JPanel fldSet = new JPanel();
    add(fldSet, BorderLayout.CENTER);
    fldSet.setBorder(BorderFactory.createTitledBorder("Generazione Primi"));
    GridBagLayout gbl_fldSet = new GridBagLayout();
    gbl_fldSet.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
    gbl_fldSet.rowHeights = new int[] { 0, 0 };
    gbl_fldSet.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
    gbl_fldSet.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
    fldSet.setLayout(gbl_fldSet);

    JLabel lblQtaPrimi = new JLabel("Quantita");
    GridBagConstraints gbc_lblQtaPrimi = new GridBagConstraints();
    gbc_lblQtaPrimi.insets = new Insets(0, 0, 0, 5);
    gbc_lblQtaPrimi.anchor = GridBagConstraints.EAST;
    gbc_lblQtaPrimi.gridx = 0;
    gbc_lblQtaPrimi.gridy = 0;
    fldSet.add(lblQtaPrimi, gbc_lblQtaPrimi);

    txQtaPrimi = new NumTextField(Controllore.FLD_QTA_PRIMI);
    txQtaPrimi.addIRsaListener(m_irsa);
    txQtaPrimi.setHorizontalAlignment(SwingConstants.RIGHT);

    GridBagConstraints gbc_txQtaPrimi = new GridBagConstraints();
    gbc_txQtaPrimi.anchor = GridBagConstraints.NORTH;
    gbc_txQtaPrimi.insets = new Insets(0, 0, 0, 5);
    gbc_txQtaPrimi.fill = GridBagConstraints.HORIZONTAL;
    gbc_txQtaPrimi.gridx = 1;
    gbc_txQtaPrimi.gridy = 0;
    fldSet.add(txQtaPrimi, gbc_txQtaPrimi);
    txQtaPrimi.setColumns(5);

    btGeneraPrimi = new JButton("Genera");
    btGeneraPrimi.setEnabled(false);
    btGeneraPrimi.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        btGenera_Click();
      }
    });
    GridBagConstraints gbc_btGeneraPrimi = new GridBagConstraints();
    gbc_btGeneraPrimi.insets = new Insets(0, 0, 0, 5);
    gbc_btGeneraPrimi.gridx = 2;
    gbc_btGeneraPrimi.gridy = 0;
    fldSet.add(btGeneraPrimi, gbc_btGeneraPrimi);

    JLabel lblNewLabel_1 = new JLabel("Generati");
    GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
    gbc_lblNewLabel_1.insets = new Insets(0, 0, 0, 5);
    gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
    gbc_lblNewLabel_1.gridx = 3;
    gbc_lblNewLabel_1.gridy = 0;
    fldSet.add(lblNewLabel_1, gbc_lblNewLabel_1);

    txPrimiGenerati = new NumTextField(Controllore.FLD_PRIMI_GENERATI);
    txPrimiGenerati.addIRsaListener(m_irsa);
    txPrimiGenerati.setHorizontalAlignment(SwingConstants.RIGHT);

    txPrimiGenerati.setEditable(false);
    GridBagConstraints gbc_txPrimiGenerati = new GridBagConstraints();
    gbc_txPrimiGenerati.fill = GridBagConstraints.HORIZONTAL;
    gbc_txPrimiGenerati.gridx = 4;
    gbc_txPrimiGenerati.gridy = 0;
    fldSet.add(txPrimiGenerati, gbc_txPrimiGenerati);
    txPrimiGenerati.setColumns(5);

  }

  protected void btGenera_Click() {
    try {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      PrimiFactory primFact = PrimiFactory.getInst();
      int qtaP = ((BigInteger) txQtaPrimi.getValue()).intValue();
      primFact.creaPrimi(qtaP);
    } finally {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
  }

  @Override
  public void valueChanged(String id, Object val) {
    switch (id) {
      case Controllore.FLD_QTA_PRIMI:
        // System.out.printf("Pan1GenPrimi.valueChanged(%s)\n",id);
        BigInteger bi = (BigInteger) val;
        btGeneraPrimi.setEnabled(bi != null && bi.signum() > 0);
        break;
      case Controllore.FLD_QTAPRIMIGEN:
        // System.out.printf("Pan1GenPrimi.valueChanged(%s)\n",id);
        Integer ii = (Integer) val;
        txPrimiGenerati.setValue(BigInteger.valueOf(ii.longValue()));
        break;
    }

  }

}
