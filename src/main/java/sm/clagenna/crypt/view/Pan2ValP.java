package sm.clagenna.crypt.view;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sm.clagenna.crypt.primi.PrimiFactory;
import sm.clagenna.crypt.swing.IRsa;
import sm.clagenna.crypt.swing.IRsaListen;
import sm.clagenna.crypt.swing.NumTextField;
import sm.clagenna.crypt.swing.log.MioLogger;

public class Pan2ValP extends JPanel implements IRsaListen {
  /** long serialVersionUID */
  private static final long         serialVersionUID = -8805994965261663006L;

  private static final Logger       s_log            = LogManager.getLogger(Pan2ValP.class);
  private static final NumberFormat s_fmt            = NumberFormat.getIntegerInstance();

  private NumTextField              txPrimoIniziale;
  private JComboBox<BigInteger>     cbValP;
  private IRsa                      m_irsa;
  private JSpinner                  spinner;
  private boolean                   bSemAddCbValP;
  private MioLogger                 mioLog;

  /**
   * Create the panel.
   */
  public Pan2ValP() {
    mioLog = MioLogger.getInst();
    initComponents();
  }

  public Pan2ValP(IRsa p_irsa) {
    m_irsa = p_irsa;
    mioLog = MioLogger.getInst();
    Controllore.getInst().addListener(this);
    initComponents();
  }

  private void initComponents() {
    setBorder(BorderFactory.createTitledBorder("Valore P"));

    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
    gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
    gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
    gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
    setLayout(gridBagLayout);

    JLabel lbPrimoIniz = new JLabel("Primo Iniz");
    GridBagConstraints gbc_lbPrimoIniz = new GridBagConstraints();
    gbc_lbPrimoIniz.insets = new Insets(0, 0, 5, 5);
    gbc_lbPrimoIniz.anchor = GridBagConstraints.EAST;
    gbc_lbPrimoIniz.gridx = 0;
    gbc_lbPrimoIniz.gridy = 0;
    add(lbPrimoIniz, gbc_lbPrimoIniz);

    txPrimoIniziale = new NumTextField(Controllore.FLD_PRIMO_INIZIALEP);
    txPrimoIniziale.addIRsaListener(m_irsa);
    txPrimoIniziale.setHorizontalAlignment(SwingConstants.RIGHT);
    GridBagConstraints gbc_txPrimoIniziale = new GridBagConstraints();
    gbc_txPrimoIniziale.insets = new Insets(0, 0, 5, 0);
    gbc_txPrimoIniziale.fill = GridBagConstraints.HORIZONTAL;
    gbc_txPrimoIniziale.gridx = 1;
    gbc_txPrimoIniziale.gridy = 0;
    add(txPrimoIniziale, gbc_txPrimoIniziale);
    txPrimoIniziale.setColumns(10);

    JLabel lbQta = new JLabel("Quantità");
    GridBagConstraints gbc_lbQta = new GridBagConstraints();
    gbc_lbQta.insets = new Insets(0, 0, 5, 5);
    gbc_lbQta.gridx = 0;
    gbc_lbQta.gridy = 1;
    add(lbQta, gbc_lbQta);

    spinner = new JSpinner();
    spinner.setModel(new SpinnerNumberModel(50, 5, 500, 10));
    GridBagConstraints gbc_spinner = new GridBagConstraints();
    gbc_spinner.anchor = GridBagConstraints.WEST;
    gbc_spinner.insets = new Insets(0, 0, 5, 0);
    gbc_spinner.gridx = 1;
    gbc_spinner.gridy = 1;
    add(spinner, gbc_spinner);
    spinner.addChangeListener(new ChangeListener() {

      @Override
      public void stateChanged(ChangeEvent e) {
        spinner_change();
      }
    });

    JLabel lblNewLabel_2 = new JLabel("P");
    GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
    gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
    gbc_lblNewLabel_2.insets = new Insets(0, 0, 0, 5);
    gbc_lblNewLabel_2.gridx = 0;
    gbc_lblNewLabel_2.gridy = 2;
    add(lblNewLabel_2, gbc_lblNewLabel_2);

    cbValP = new JComboBox<>();
    GridBagConstraints gbc_cbValP = new GridBagConstraints();
    gbc_cbValP.fill = GridBagConstraints.HORIZONTAL;
    gbc_cbValP.gridx = 1;
    gbc_cbValP.gridy = 2;
    add(cbValP, gbc_cbValP);
    cbValP.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
          Object item = event.getItem();
          cbValP_sel(item);
        }
      }
    });

  }

  @Override
  public void valueChanged(String id, Object val) {

    switch (id) {
      case Controllore.FLD_QTA_PRIMIP:
      case Controllore.FLD_PRIMO_INIZIALEP:
      case Controllore.FLD_QTAPRIMIGEN:
        PrimiFactory fact = PrimiFactory.getInst();
        List<Long> li = fact.getLiPrimi();
        int qta = (Integer) spinner.getValue();
        BigInteger biStart = (BigInteger) txPrimoIniziale.getValue();
        if (biStart == null || biStart.signum() <= 0)
          biStart = BigInteger.ONE;

        try {
          this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
          bSemAddCbValP = true;
          cbValP.removeAllItems();
          for (Long vv : li) {
            if (vv.longValue() >= biStart.longValue())
              cbValP.addItem(BigInteger.valueOf(vv));
            if (cbValP.getModel().getSize() > qta)
              break;
          }
        } finally {
          this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
          bSemAddCbValP = false;
        }
        break;
    }

  }

  protected void spinner_change() {
    Integer qtaPrimi = (int) spinner.getValue();
    Controllore.getInst().setValue(Controllore.FLD_QTA_PRIMIP, qtaPrimi);
  }

  protected void cbValP_sel(Object item) {
    if (bSemAddCbValP)
      return;
    BigInteger bi = (BigInteger) item;
    Controllore.getInst().setValue(Controllore.FLD_NP, bi);
    mioLog.log(String.format("Scelto Primo P=%s", s_fmt.format(bi)));
    s_log.debug(mioLog.getLastMessaggio());
  }

}
