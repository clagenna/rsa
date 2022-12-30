package sm.clagenna.crypt.view;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sm.clagenna.crypt.swing.IRsa;
import sm.clagenna.crypt.swing.IRsaListen;
import sm.clagenna.crypt.swing.NumTextField;
import sm.clagenna.crypt.swing.log.MioLogger;
import sm.clagenna.crypt.util.PrimiWorker;

public class Pan1GenPrimi extends JPanel implements IRsaListen, PropertyChangeListener {

  /** long serialVersionUID */
  private static final long         serialVersionUID = -4193143331761193489L;

  private static final Logger       s_log            = LogManager.getLogger(Pan1GenPrimi.class);
  private static final NumberFormat s_fmt            = NumberFormat.getIntegerInstance();
  private NumTextField              txQtaPrimi;
  private NumTextField              txPrimiGenerati;
  private IRsa                      m_irsa;
  private JButton                   btGeneraPrimi;
  private PrimiWorker               primiGen;
  private JProgressBar              progressBar;
  private MioLogger                 mioLog;

  public Pan1GenPrimi() {
    mioLog = MioLogger.getInst();
    initComponents();
  }

  public Pan1GenPrimi(IRsa p_irsa) {
    m_irsa = p_irsa;
    mioLog = MioLogger.getInst();
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
    gbl_fldSet.rowHeights = new int[] { 0, 0, 0 };
    gbl_fldSet.columnWeights = new double[] { 1.0, 1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
    gbl_fldSet.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
    fldSet.setLayout(gbl_fldSet);

    JLabel lblQtaPrimi = new JLabel("Quantita");
    GridBagConstraints gbc_lblQtaPrimi = new GridBagConstraints();
    gbc_lblQtaPrimi.insets = new Insets(0, 0, 5, 5);
    gbc_lblQtaPrimi.anchor = GridBagConstraints.EAST;
    gbc_lblQtaPrimi.gridx = 0;
    gbc_lblQtaPrimi.gridy = 0;
    fldSet.add(lblQtaPrimi, gbc_lblQtaPrimi);

    txQtaPrimi = new NumTextField(Controllore.FLD_QTA_PRIMI);
    txQtaPrimi.addIRsaListener(m_irsa);
    txQtaPrimi.setHorizontalAlignment(SwingConstants.RIGHT);

    GridBagConstraints gbc_txQtaPrimi = new GridBagConstraints();
    gbc_txQtaPrimi.anchor = GridBagConstraints.NORTH;
    gbc_txQtaPrimi.insets = new Insets(0, 0, 5, 5);
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
    gbc_btGeneraPrimi.insets = new Insets(0, 0, 5, 5);
    gbc_btGeneraPrimi.gridx = 2;
    gbc_btGeneraPrimi.gridy = 0;
    fldSet.add(btGeneraPrimi, gbc_btGeneraPrimi);

    JLabel lblNewLabel_1 = new JLabel("Max Primo");
    GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
    gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
    gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
    gbc_lblNewLabel_1.gridx = 3;
    gbc_lblNewLabel_1.gridy = 0;
    fldSet.add(lblNewLabel_1, gbc_lblNewLabel_1);

    txPrimiGenerati = new NumTextField(Controllore.FLD_MAXPRIMO);
    txPrimiGenerati.addIRsaListener(m_irsa);
    txPrimiGenerati.setHorizontalAlignment(SwingConstants.RIGHT);

    txPrimiGenerati.setEditable(false);
    GridBagConstraints gbc_txPrimiGenerati = new GridBagConstraints();
    gbc_txPrimiGenerati.insets = new Insets(0, 0, 5, 0);
    gbc_txPrimiGenerati.fill = GridBagConstraints.HORIZONTAL;
    gbc_txPrimiGenerati.gridx = 4;
    gbc_txPrimiGenerati.gridy = 0;
    fldSet.add(txPrimiGenerati, gbc_txPrimiGenerati);
    txPrimiGenerati.setColumns(5);

    progressBar = new JProgressBar();
    GridBagConstraints gbc_textField = new GridBagConstraints();
    gbc_textField.insets = new Insets(0, 0, 0, 5);
    gbc_textField.fill = GridBagConstraints.HORIZONTAL;
    gbc_textField.gridx = 0;
    gbc_textField.gridy = 1;
    gbc_textField.gridwidth = 5;
    fldSet.add(progressBar, gbc_textField);
    progressBar.setValue(0);
    progressBar.setStringPainted(true);
  }

  protected void btGenera_Click() {
    try {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      // PrimiFactory primFact = PrimiFactory.getInst();
      //      primFact.creaPrimi(qtaP);
      btGeneraPrimi.setEnabled(false);
      primiGen = new PrimiWorker();
      primiGen.addPropertyChangeListener(this);
      int qtaP = ((BigInteger) txQtaPrimi.getValue()).intValue();
      s_log.debug("Genero {} primi", qtaP);
      primiGen.setQtaPrimi(qtaP);
      primiGen.execute();
      s_log.debug("Generati {} primi", qtaP);
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
        if (bi != null) {
          mioLog.debug(String.format("Cambio qta Primi %s", s_fmt.format(bi)));
          s_log.debug(mioLog.getLastMessaggio());
        }
        btGeneraPrimi.setEnabled(bi != null && bi.signum() > 0);
        break;
      //      case Controllore.FLD_QTAPRIMIGEN:
      //        // System.out.printf("Pan1GenPrimi.valueChanged(%s)\n",id);
      //        Integer ii = (Integer) val;
      //        txPrimiGenerati.setValue(BigInteger.valueOf(ii.longValue()));
      //        mioLog.log(String.format("Primi generati:%s", s_fmt.format(ii)));
      //        s_log.info(mioLog.getLastMessaggio());
      //        break;
      case Controllore.FLD_MAXPRIMO:
        // System.out.printf("Pan1GenPrimi.valueChanged(%s)\n",id);
        BigInteger il = (BigInteger) val;
        txPrimiGenerati.setValue(il);
        mioLog.log(String.format("Max Primo:%s", s_fmt.format(il)));
        s_log.info(mioLog.getLastMessaggio());
        break;
    }

  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    // System.out.printf("propertyChange(%s)\n", evt.getPropertyName());
    if ( !"progress".equals(evt.getPropertyName()))
      return;
    int prg = (Integer) evt.getNewValue();
    if (prg >= 99) {
      btGeneraPrimi.setEnabled(true);
      prg = 100;
    }
    progressBar.setValue(prg);
  }

}
