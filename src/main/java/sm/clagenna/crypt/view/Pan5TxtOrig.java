package sm.clagenna.crypt.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import sm.clagenna.crypt.rsa.RsaObj;
import sm.clagenna.crypt.swing.IRsa;

public class Pan5TxtOrig extends JPanel {

  /** long serialVersionUID */
  private static final long                serialVersionUID = 593767533148741772L;
  @SuppressWarnings("unused") private IRsa m_irsa;
  private JTextArea                        txTxtOrig;
  private JButton                          btEncode;

  /**
   * Create the panel.
   */
  public Pan5TxtOrig() {
    initComponents();
  }

  public Pan5TxtOrig(IRsa p_irsa) {
    m_irsa = p_irsa;
    initComponents();
  }

  private void initComponents() {
    setBorder(BorderFactory.createTitledBorder("Testo Originale"));
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
    gridBagLayout.rowHeights = new int[] { 0, 0 };
    gridBagLayout.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
    gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
    setLayout(gridBagLayout);

    txTxtOrig = new JTextArea();
    JScrollPane scrl = new JScrollPane(txTxtOrig);
    GridBagConstraints gbc_txTxtOrig = new GridBagConstraints();
    gbc_txTxtOrig.insets = new Insets(0, 0, 0, 5);
    gbc_txTxtOrig.fill = GridBagConstraints.BOTH;
    gbc_txTxtOrig.gridx = 0;
    gbc_txTxtOrig.gridy = 0;
    add(scrl, gbc_txTxtOrig);
    txTxtOrig.getDocument().addDocumentListener(new DocumentListener() {

      @Override
      public void removeUpdate(DocumentEvent e) {
        txTxtOrig_Update();
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        txTxtOrig_Update();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        txTxtOrig_Update();
      }
    });

    btEncode = new JButton("Enc");
    GridBagConstraints gbc_btEncode = new GridBagConstraints();
    gbc_btEncode.gridx = 1;
    gbc_btEncode.gridy = 0;
    add(btEncode, gbc_btEncode);
    btEncode.setEnabled(false);
    btEncode.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        btEncode_Click();
      }
    });
  }

  protected void txTxtOrig_Update() {
    String sz = txTxtOrig.getText();
    boolean bEna = sz != null && sz.length() > 6;
    if (bEna) {
      RsaObj rsa = MainFrame.getInst().getRsaObj();
      bEna = rsa.isPubKeyPresent();
    }
    btEncode.setEnabled(bEna);
  }

  protected void btEncode_Click() {
    String sz = txTxtOrig.getText();
    if (sz == null || sz.length() == 0)
      return;
    Controllore.getInst().setValue(Controllore.FLD_TXT_ORIG, sz);
  }

}
