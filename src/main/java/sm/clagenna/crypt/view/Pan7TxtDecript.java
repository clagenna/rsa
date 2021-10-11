package sm.clagenna.crypt.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import sm.clagenna.crypt.swing.IRsa;

public class Pan7TxtDecript extends JPanel {

  /** long serialVersionUID */
  private static final long serialVersionUID = 593767533148741772L;
  private IRsa              m_irsa;

  /**
   * Create the panel.
   */
  public Pan7TxtDecript() {
    initComponents();
  }

  public Pan7TxtDecript(IRsa p_irsa) {
    m_irsa = p_irsa;
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

    JTextArea txTxtDecripted = new JTextArea();
    GridBagConstraints gbc_txTxtDecripted = new GridBagConstraints();
    gbc_txTxtDecripted.insets = new Insets(0, 0, 0, 5);
    gbc_txTxtDecripted.fill = GridBagConstraints.BOTH;
    gbc_txTxtDecripted.gridx = 0;
    gbc_txTxtDecripted.gridy = 0;
    add(txTxtDecripted, gbc_txTxtDecripted);

  }

}
