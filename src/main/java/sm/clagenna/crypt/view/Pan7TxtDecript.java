package sm.clagenna.crypt.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import sm.clagenna.crypt.swing.IRsa;
import sm.clagenna.crypt.swing.IRsaListen;

public class Pan7TxtDecript extends JPanel implements IRsaListen {

  /** long serialVersionUID */
  private static final long                serialVersionUID = 593767533148741772L;
  @SuppressWarnings("unused") private IRsa m_irsa;
  private JTextArea                        txTxtDecripted;

  /**
   * Create the panel.
   */
  public Pan7TxtDecript() {
    Controllore.getInst().addListener(this);
    initComponents();
  }

  public Pan7TxtDecript(IRsa p_irsa) {
    m_irsa = p_irsa;
    Controllore.getInst().addListener(this);
    initComponents();
  }

  private void initComponents() {

    setBorder(BorderFactory.createTitledBorder("Testo Decriptato"));
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
    gridBagLayout.rowHeights = new int[] { 0, 0 };
    gridBagLayout.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
    gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
    setLayout(gridBagLayout);

    txTxtDecripted = new JTextArea();
    JScrollPane scrl = new JScrollPane(txTxtDecripted);
    GridBagConstraints gbc_txTxtDecripted = new GridBagConstraints();
    gbc_txTxtDecripted.insets = new Insets(0, 0, 0, 5);
    gbc_txTxtDecripted.fill = GridBagConstraints.BOTH;
    gbc_txTxtDecripted.gridx = 0;
    gbc_txTxtDecripted.gridy = 0;
    add(scrl, gbc_txTxtDecripted);

  }

  @Override
  public void valueChanged(String id, Object val) {
    switch (id) {
      case Controllore.FLD_TXT_DECODED:
        txTxtDecripted.setText((String) val);
        break;
    }

  }

}
