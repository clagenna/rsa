package sm.clagenna.crypt.view;

import java.math.BigInteger;
import java.text.NumberFormat;

import javax.swing.DefaultComboBoxModel;

public class IntComboBoxModel extends DefaultComboBoxModel<BigInteger> {

  /** long serialVersionUID */
  private static final long serialVersionUID = -3625638499201330588L;
  @SuppressWarnings("unused")
  private NumberFormat      intFmt;

  public IntComboBoxModel() {
    intFmt = NumberFormat.getIntegerInstance();
  }

  @Override
  public Object getSelectedItem() {
    // TODO Auto-generated method stub
    return super.getSelectedItem();
  }

  @Override
  public void setSelectedItem(Object anObject) {
    // TODO Auto-generated method stub
    super.setSelectedItem(anObject);
  }

}
