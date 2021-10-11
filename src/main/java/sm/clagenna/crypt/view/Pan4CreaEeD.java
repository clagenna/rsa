package sm.clagenna.crypt.view;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import sm.clagenna.crypt.rsa.RsaObj;
import sm.clagenna.crypt.swing.IRsa;
import sm.clagenna.crypt.swing.IRsaListen;
import sm.clagenna.crypt.swing.NumTextField;

public class Pan4CreaEeD extends JPanel implements IRsaListen {
  /** long serialVersionUID */
  private static final long serialVersionUID = 7108091499557509334L;
  private NumTextField      txModulus;
  private NumTextField      txFi;
  private NumTextField      txCarmichael;
  private NumTextField      txE;
  private NumTextField      txD;
  private IRsa              m_irsa;
  private JButton           btEeD;
  private boolean           bSemValueChange;

  /**
   * Create the panel.
   */
  public Pan4CreaEeD() {
    Controllore.getInst().addListener(this);
    initComponents();
  }

  public Pan4CreaEeD(IRsa p_irsa) {
    m_irsa = p_irsa;
    Controllore.getInst().addListener(this);
    initComponents();
  }

  private void initComponents() {

    setBorder(BorderFactory.createTitledBorder("Crea E e D"));

    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
    gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
    gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
    gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
    setLayout(gridBagLayout);

    btEeD = new JButton("E e D");
    btEeD.setEnabled(false);
    GridBagConstraints gbc_btEeD = new GridBagConstraints();
    gbc_btEeD.gridheight = 5;
    gbc_btEeD.insets = new Insets(0, 0, 0, 5);
    gbc_btEeD.gridx = 0;
    gbc_btEeD.gridy = 0;
    add(btEeD, gbc_btEeD);
    btEeD.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        calcolaEeD();
      }
    });

    JLabel lbModulus = new JLabel("Modulus");
    GridBagConstraints gbc_lbModulus = new GridBagConstraints();
    gbc_lbModulus.insets = new Insets(0, 0, 5, 5);
    gbc_lbModulus.anchor = GridBagConstraints.EAST;
    gbc_lbModulus.gridx = 1;
    gbc_lbModulus.gridy = 0;
    add(lbModulus, gbc_lbModulus);

    txModulus = new NumTextField(Controllore.FLD_MODULUS);
    txModulus.addIRsaListener(m_irsa);
    GridBagConstraints gbc_txModulus = new GridBagConstraints();
    gbc_txModulus.insets = new Insets(0, 0, 5, 0);
    gbc_txModulus.fill = GridBagConstraints.HORIZONTAL;
    gbc_txModulus.gridx = 2;
    gbc_txModulus.gridy = 0;
    add(txModulus, gbc_txModulus);
    txModulus.setColumns(10);

    JLabel lbFi = new JLabel("Fi");
    GridBagConstraints gbc_lbFi = new GridBagConstraints();
    gbc_lbFi.anchor = GridBagConstraints.EAST;
    gbc_lbFi.insets = new Insets(0, 0, 5, 5);
    gbc_lbFi.gridx = 1;
    gbc_lbFi.gridy = 1;
    add(lbFi, gbc_lbFi);

    txFi = new NumTextField(Controllore.FLD_FITOTIENT);
    txFi.addIRsaListener(m_irsa);
    GridBagConstraints gbc_txFi = new GridBagConstraints();
    gbc_txFi.anchor = GridBagConstraints.NORTH;
    gbc_txFi.insets = new Insets(0, 0, 5, 0);
    gbc_txFi.fill = GridBagConstraints.HORIZONTAL;
    gbc_txFi.gridx = 2;
    gbc_txFi.gridy = 1;
    add(txFi, gbc_txFi);
    txFi.setColumns(10);

    JLabel lbCarmichael = new JLabel("Carmichael");
    GridBagConstraints gbc_lbCarmichael = new GridBagConstraints();
    gbc_lbCarmichael.anchor = GridBagConstraints.EAST;
    gbc_lbCarmichael.insets = new Insets(0, 0, 5, 5);
    gbc_lbCarmichael.gridx = 1;
    gbc_lbCarmichael.gridy = 2;
    add(lbCarmichael, gbc_lbCarmichael);

    txCarmichael = new NumTextField(Controllore.FLD_CARMICAEL);
    txCarmichael.addIRsaListener(m_irsa);
    GridBagConstraints gbc_txCarmichael = new GridBagConstraints();
    gbc_txCarmichael.insets = new Insets(0, 0, 5, 0);
    gbc_txCarmichael.fill = GridBagConstraints.HORIZONTAL;
    gbc_txCarmichael.gridx = 2;
    gbc_txCarmichael.gridy = 2;
    add(txCarmichael, gbc_txCarmichael);
    txCarmichael.setColumns(10);

    JLabel lbE = new JLabel("E");
    GridBagConstraints gbc_lbE = new GridBagConstraints();
    gbc_lbE.anchor = GridBagConstraints.EAST;
    gbc_lbE.insets = new Insets(0, 0, 5, 5);
    gbc_lbE.gridx = 1;
    gbc_lbE.gridy = 3;
    add(lbE, gbc_lbE);

    txE = new NumTextField(Controllore.FLD_NE);
    txE.addIRsaListener(m_irsa);
    GridBagConstraints gbc_txE = new GridBagConstraints();
    gbc_txE.insets = new Insets(0, 0, 5, 0);
    gbc_txE.fill = GridBagConstraints.HORIZONTAL;
    gbc_txE.gridx = 2;
    gbc_txE.gridy = 3;
    add(txE, gbc_txE);
    txE.setColumns(10);

    JLabel lbD = new JLabel("D");
    GridBagConstraints gbc_lbD = new GridBagConstraints();
    gbc_lbD.anchor = GridBagConstraints.EAST;
    gbc_lbD.insets = new Insets(0, 0, 0, 5);
    gbc_lbD.gridx = 1;
    gbc_lbD.gridy = 4;
    add(lbD, gbc_lbD);

    txD = new NumTextField(Controllore.FLD_ND);
    txD.addIRsaListener(m_irsa);
    GridBagConstraints gbc_txD = new GridBagConstraints();
    gbc_txD.fill = GridBagConstraints.HORIZONTAL;
    gbc_txD.gridx = 2;
    gbc_txD.gridy = 4;
    add(txD, gbc_txD);
    txD.setColumns(10);
    //    GridBagLayout gridBagLayout = new GridBagLayout();
    //    gridBagLayout.columnWidths = new int[] { 0, 2, 40, 0 };
    //    gridBagLayout.rowHeights = new int[] { 0, 0 };
    //    gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
    //    gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
    //    setLayout(gridBagLayout);
    //
    //    JButton btGeneraED = new JButton("E e D");
    //    GridBagConstraints gbc_btGeneraED = new GridBagConstraints();
    //    gbc_btGeneraED.insets = new Insets(0, 0, 0, 5);
    //    gbc_btGeneraED.gridx = 0;
    //    gbc_btGeneraED.gridy = 0;
    //    add(btGeneraED, gbc_btGeneraED);
    //
    //    JLabel lbModulus = new JLabel("New label");
    //    GridBagConstraints gbc_lbModulus = new GridBagConstraints();
    //    gbc_lbModulus.insets = new Insets(0, 0, 0, 5);
    //    gbc_lbModulus.anchor = GridBagConstraints.EAST;
    //    gbc_lbModulus.gridx = 1;
    //    gbc_lbModulus.gridy = 0;
    //    add(lbModulus, gbc_lbModulus);
    //
    //    textField = new JTextField();
    //    GridBagConstraints gbc_textField = new GridBagConstraints();
    //    gbc_textField.fill = GridBagConstraints.HORIZONTAL;
    //    gbc_textField.gridx = 2;
    //    gbc_textField.gridy = 0;
    //    add(textField, gbc_textField);
    //    textField.setColumns(10);

  }

  @Override
  public void valueChanged(String id, Object val) {
    if (bSemValueChange)
      return;
    RsaObj rsa = MainFrame.getInst().getRsaObj();
    BigInteger bi = null;
    try {
      bSemValueChange = true;
      switch (id) {
        case Controllore.FLD_NP:
        case Controllore.FLD_NQ:
          btEeD.setEnabled(rsa != null && rsa.isPresentPQ());
          break;
        case Controllore.FLD_MODULUS:
          bi = (BigInteger) val;
          txModulus.setValue(bi);
          break;
        case Controllore.FLD_FITOTIENT:
          bi = (BigInteger) val;
          txFi.setValue(bi);
          break;
        case Controllore.FLD_CARMICAEL:
          bi = (BigInteger) val;
          txCarmichael.setValue(bi);
          break;
        case Controllore.FLD_NE:
          bi = (BigInteger) val;
          txE.setValue(bi);
          break;
        case Controllore.FLD_ND:
          bi = (BigInteger) val;
          txD.setValue(bi);
          break;
      }
    } finally {
      bSemValueChange = false;
    }
  }

  protected void calcolaEeD() {
    try {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      RsaObj rsa = MainFrame.getInst().getRsaObj();
      rsa.calcolaRSAObj();

      BigInteger probe = rsa.getNQ().add(BigInteger.valueOf(121));

      BigInteger verso = rsa.esponenteE(probe);
      BigInteger ritor = rsa.esponenteD(verso);
      NumberFormat fmt = NumberFormat.getInstance();
      System.out.printf("Con %s ==> %s ==> %s\n", fmt.format(probe), fmt.format(verso), fmt.format(ritor));

    } finally {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
  }
}
