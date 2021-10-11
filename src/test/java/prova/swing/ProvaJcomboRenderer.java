package prova.swing;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigInteger;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import sm.clagenna.crypt.view.IntComboBoxRenderer;

public class ProvaJcomboRenderer extends JFrame {

  /** long serialVersionUID */
  private static final long     serialVersionUID = 2233452263968089052L;
  private JPanel                contentPane;
  private JTextField            textField;
  private NumberFormat          intFmt;
  private JComboBox<BigInteger> comboBox;
  private boolean               m_SemAdd;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          ProvaJcomboRenderer frame = new ProvaJcomboRenderer();
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
  public ProvaJcomboRenderer() {
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setBounds(100, 100, 450, 300);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);

    intFmt = NumberFormat.getIntegerInstance();

    GridBagLayout gbl_contentPane = new GridBagLayout();
    gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0 };
    gbl_contentPane.rowHeights = new int[] { 0, 0, 0 };
    gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
    gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
    contentPane.setLayout(gbl_contentPane);

    JLabel lblNewLabel = new JLabel("New label");
    GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
    gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
    gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
    gbc_lblNewLabel.gridx = 0;
    gbc_lblNewLabel.gridy = 0;
    contentPane.add(lblNewLabel, gbc_lblNewLabel);
    comboBox = new JComboBox<BigInteger>();
    comboBox.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
          Object item = event.getItem();
          comboBox_sel(item);
        }
      }

    });
    comboBox.setRenderer(new IntComboBoxRenderer());

    GridBagConstraints gbc_comboBox = new GridBagConstraints();
    gbc_comboBox.insets = new Insets(0, 0, 5, 5);
    gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
    gbc_comboBox.gridx = 1;
    gbc_comboBox.gridy = 0;
    contentPane.add(comboBox, gbc_comboBox);

    textField = new JFormattedTextField(intFmt);
    GridBagConstraints gbc_textField = new GridBagConstraints();
    gbc_textField.insets = new Insets(0, 0, 0, 5);
    gbc_textField.fill = GridBagConstraints.HORIZONTAL;
    gbc_textField.gridx = 1;
    gbc_textField.gridy = 1;
    contentPane.add(textField, gbc_textField);
    textField.setColumns(10);

    JButton btnNewButton = new JButton("Add");
    btnNewButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        btAdd_Click();
      }
    });
    GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
    gbc_btnNewButton.gridx = 2;
    gbc_btnNewButton.gridy = 1;
    contentPane.add(btnNewButton, gbc_btnNewButton);
  }

  protected void comboBox_sel(Object e) {
    if (m_SemAdd)
      return;
    BigInteger bi = (BigInteger) e;
    textField.setText(intFmt.format(bi));
  }

  protected void btAdd_Click() {
    String sz = textField.getText();
    if (sz == null || sz.length() < 1)
      return;
    try {
      m_SemAdd = true;
      sz = sz.replace(".", "");
      BigInteger bi = new BigInteger(sz);
      comboBox.addItem(bi);
    } finally {
      m_SemAdd = false;
    }
  }

}
