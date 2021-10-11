package prova.swing;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import sm.clagenna.crypt.swing.NumTextField;

public class ProvaNumTextField extends JFrame {

  /** long serialVersionUID */
  private static final long serialVersionUID = -5585882485387235481L;
  private JPanel            contentPane;
  private NumTextField      txNumText;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          ProvaNumTextField frame = new ProvaNumTextField();
          frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the frame.
   */
  public ProvaNumTextField() {
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setBounds(100, 100, 450, 300);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    GridBagLayout gbl_contentPane = new GridBagLayout();
    gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0 };
    gbl_contentPane.rowHeights = new int[] { 0, 0 };
    gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
    gbl_contentPane.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
    contentPane.setLayout(gbl_contentPane);

    JLabel lblNewLabel = new JLabel("New label");
    GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
    gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
    gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
    gbc_lblNewLabel.gridx = 0;
    gbc_lblNewLabel.gridy = 0;
    contentPane.add(lblNewLabel, gbc_lblNewLabel);

    txNumText = new NumTextField("valore");
    GridBagConstraints gbc_txNumText = new GridBagConstraints();
    gbc_txNumText.insets = new Insets(0, 0, 0, 5);
    gbc_txNumText.fill = GridBagConstraints.HORIZONTAL;
    gbc_txNumText.gridx = 1;
    gbc_txNumText.gridy = 0;
    contentPane.add(txNumText, gbc_txNumText);
    txNumText.setColumns(10);

    JButton btnNewButton = new JButton("Prova");
    btnNewButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        emettiMsg();
      }
    });
    GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
    gbc_btnNewButton.gridx = 2;
    gbc_btnNewButton.gridy = 0;
    contentPane.add(btnNewButton, gbc_btnNewButton);
  }

  protected void emettiMsg() {
    BigInteger bi = (BigInteger) txNumText.getValue();
    String msg = "valore= *NULL*";
    if (bi != null)
      msg = "Valore=" + NumberFormat.getIntegerInstance().format(bi);
    JOptionPane.showConfirmDialog(this, //
        msg, //
        "Valore del textBox", //
        JOptionPane.OK_OPTION//
    );

  }

}
