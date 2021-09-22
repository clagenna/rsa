package test.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import sm.ciscoop.crypt.view.NumTextField;

public class ProvaFormTxFld extends JFrame {

  /** long serialVersionUID */
  private static final long serialVersionUID = 1935345470249724484L;
  private int               qtaChange;
  private JTextField        textField;

  private NumTextField      txNumTxFld;
  private JLabel            lbOutput;

  public ProvaFormTxFld() {
    initComponents();
  }

  public ProvaFormTxFld(String tit) {
    super(tit);
    initComponents();
  }

  private void initComponents() {

    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] { 0, 0 };
    gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
    gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
    gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
    getContentPane().setLayout(gridBagLayout);

    JLabel lblFmtTxFld = new JLabel("Formatted Text Field");
    GridBagConstraints gbc_lblFmtTxFld = new GridBagConstraints();
    gbc_lblFmtTxFld.insets = new Insets(0, 0, 5, 0);
    gbc_lblFmtTxFld.gridx = 0;
    gbc_lblFmtTxFld.gridy = 0;
    getContentPane().add(lblFmtTxFld, gbc_lblFmtTxFld);

    textField = new JTextField();
    GridBagConstraints gbc_textField = new GridBagConstraints();
    gbc_textField.insets = new Insets(0, 0, 5, 0);
    gbc_textField.fill = GridBagConstraints.HORIZONTAL;
    gbc_textField.gridx = 0;
    gbc_textField.gridy = 1;
    getContentPane().add(textField, gbc_textField);
    textField.setColumns(10);

    JLabel lblNewLabel = new JLabel("Mio num text field");
    GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
    gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
    gbc_lblNewLabel.gridx = 0;
    gbc_lblNewLabel.gridy = 2;
    getContentPane().add(lblNewLabel, gbc_lblNewLabel);

    txNumTxFld = new NumTextField("NumTxFld");
    GridBagConstraints gbc_txNumTxFld = new GridBagConstraints();
    gbc_txNumTxFld.insets = new Insets(0, 0, 5, 0);
    gbc_txNumTxFld.fill = GridBagConstraints.HORIZONTAL;
    gbc_txNumTxFld.gridx = 0;
    gbc_txNumTxFld.gridy = 3;
    getContentPane().add(txNumTxFld, gbc_txNumTxFld);
    txNumTxFld.setColumns(10);
    txNumTxFld.addPropertyChangeListener("value", new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        txNumTxFld_Chng(evt.getNewValue());
      }
    });

    lbOutput = new JLabel("output");
    GridBagConstraints gbc_lbOutput = new GridBagConstraints();
    gbc_lbOutput.gridx = 0;
    gbc_lbOutput.gridy = 4;
    getContentPane().add(lbOutput, gbc_lbOutput);

  }

  protected void txNumTxFld_Chng(Object newValue) {
    String msg = String.format("%s %5d:  %s == %s", // 
        (newValue == null ? "*NULL*" : newValue.getClass().getSimpleName()), // 
        qtaChange++, (newValue == null ? "*NULL*" : newValue.toString()), //
        txNumTxFld.getValue());
    lbOutput.setText(msg);

  }

  public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        ProvaFormTxFld frame = new ProvaFormTxFld("Prova Formatted Text Field");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
      }
    });
  }

}
