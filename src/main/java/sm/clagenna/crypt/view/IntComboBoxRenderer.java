package sm.clagenna.crypt.view;

import java.awt.Component;
import java.math.BigInteger;
import java.text.NumberFormat;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class IntComboBoxRenderer extends DefaultListCellRenderer {

  /** long serialVersionUID */
  private static final long serialVersionUID = -1717509796762199337L;

  private NumberFormat      intFmt;

  public IntComboBoxRenderer() {
    setOpaque(true);
    setHorizontalAlignment(RIGHT);
    intFmt = NumberFormat.getIntegerInstance();
  }

  @Override
  public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index, boolean isSelected,
      boolean cellHasFocus) {
    if (value == null)
      return this;
    // int selectedIndex = ((Integer) value).intValue();
    BigInteger bi = (BigInteger) value;
    if (isSelected) {
      setBackground(list.getSelectionBackground());
      setForeground(list.getSelectionForeground());
    } else {
      setBackground(list.getBackground());
      setForeground(list.getForeground());
    }
    // Object obj = list.
    setText(intFmt.format(bi));
    return this;
  }

}
