package test.swing;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import sm.clagenna.crypt.swing.NumTextField;

public class ProvaNumTextFld extends JFrame {

  /** serialVersionUID */
  private static final long serialVersionUID = -3262198056820646697L;
  private JPanel            contentPane;
  private JTextField        textField;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          ProvaNumTextFld frame = new ProvaNumTextFld();
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
  public ProvaNumTextFld() {
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

    textField = new NumTextField("provalo");
    GridBagConstraints gbc_textField = new GridBagConstraints();
    gbc_textField.insets = new Insets(0, 0, 0, 5);
    gbc_textField.fill = GridBagConstraints.HORIZONTAL;
    gbc_textField.gridx = 1;
    gbc_textField.gridy = 0;
    contentPane.add(textField, gbc_textField);
    textField.setColumns(10);

    JButton btnNewButton = new JButton("New button");
    GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
    gbc_btnNewButton.gridx = 2;
    gbc_btnNewButton.gridy = 0;
    contentPane.add(btnNewButton, gbc_btnNewButton);
  }

}
