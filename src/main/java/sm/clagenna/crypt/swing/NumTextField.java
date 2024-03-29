package sm.clagenna.crypt.swing;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import lombok.Setter;

class MousePositionCorrectorListener extends FocusAdapter {
  @Override
  public void focusGained(FocusEvent e) {
    /*
     * After a formatted text field gains focus, it replaces its text with its
     * current value, formatted appropriately of course. It does this after any
     * focus listeners are notified. We want to make sure that the caret is
     * placed in the correct position rather than the dumb default that is
     * before the 1st character !
     */
    final JTextField field = (JTextField) e.getSource();
    final int dot = field.getCaret().getDot();
    final int mark = field.getCaret().getMark();
    if (field.isEnabled() && field.isEditable()) {
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          // Only set the caret if the textfield hasn't got a selection on it
          if (dot == mark) {
            field.getCaret().setDot(dot);
          }
        }
      });
    }
  }
}

public class NumTextField extends JFormattedTextField {

  /** long serialVersionUID */
  private static final long      serialVersionUID       = 4591049179182021815L;

  private static final Color     ERROR_BACKGROUND_COLOR = new Color(255, 215, 215);
  private static final Color     ERROR_FOREGROUND_COLOR = new Color(192, 0, 0);
  @Setter private static boolean debug                  = false;

  private Color                  fBackground, fForeground;
  private String                 name;
  private BigInteger             valore;
  private IRsa                   m_irsa;

  public NumTextField(String p_name) {
    super(NumberFormat.getIntegerInstance());
    setName(p_name);
    setHorizontalAlignment(SwingConstants.RIGHT);
    setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
    updateBackgroundOnEachUpdate();
    fBackground = Color.WHITE;
    fForeground = Color.black;
    //improve the caret behavior
    //see also http://tips4java.wordpress.com/2010/02/21/formatted-text-field-tips/
    addFocusListener(new MousePositionCorrectorListener());
  }

  private void updateBackgroundOnEachUpdate() {
    getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        System.out.println("NumTextField.updateBackgroundOnEachUpdate() --> insertUpdate()");
        updateBackground();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        System.out.println("NumTextField.updateBackgroundOnEachUpdate() --> removeUpdate()");
        updateBackground();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        System.out.println("NumTextField.updateBackgroundOnEachUpdate() --> changedUpdate()");
        updateBackground();
      }
    });
  }

  private void updateBackground() {
    boolean valid = validContent();
    if (ERROR_BACKGROUND_COLOR != null) {
      setBackground(valid ? fBackground : ERROR_BACKGROUND_COLOR);
    }
    if (ERROR_FOREGROUND_COLOR != null) {
      setForeground(valid ? fForeground : ERROR_FOREGROUND_COLOR);
    }
  }

  private boolean validContent() {
    boolean bRet = true;
    String txt = null;
    AbstractFormatter formatter = getFormatter();
    if (formatter != null) {
      try {
        txt = getText();
        txt = txt.replace(".", "");
        formatter.stringToValue(txt);
        valore = new BigInteger(txt);
        if (m_irsa != null)
          m_irsa.setValue(name, valore);
      } catch (NumberFormatException | ParseException e) {
        bRet = false;
      }
    }
    if (debug)
      System.out.printf("validContent:%s (%s)\n", txt, (bRet ? "" : "bad"));
    return bRet;
  }

  public void addIRsaListener(IRsa p) {
    m_irsa = p;
  }

  @Override
  public void setValue(Object value) {
    boolean validValue = true;
    BigInteger l_val = BigInteger.ZERO;
    // evito che venga assegnato (con setValue() da IRsa) lo stesso valore
    if (value == null || value instanceof BigInteger)
      l_val = (BigInteger) value;
    if (valore != null)
      if (l_val.longValue() != 0)
        if (valore.equals(l_val)) {
          // System.out.println("val=value");
          return;
        } 
    valore = l_val;

    if (debug)
      System.out.printf("setValue(\"%s\")\n", (value != null ? value.toString() : "*NULL*"));
    //before setting the value, parse it by using the format
    try {
      AbstractFormatter formatter = getFormatter();
      if (formatter != null) {
        formatter.valueToString(value);
      }
    } catch (ParseException e) {
      validValue = false;
      updateBackground();
    }
    //only set the value when valid
    if (validValue) {
      int old_caret_position = getCaretPosition();
      super.setValue(value);
      setCaretPosition(Math.min(old_caret_position, getText().length()));
    }
  }

  @Override
  public Object getValue() {
    return valore;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

}
