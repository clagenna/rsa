package sm.clagenna.crypt.swing.log;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class MioJTableCellRenderer extends DefaultTableCellRenderer {

  /** long serialVersionUID */
  private static final long serialVersionUID = -820989928940602998L;
  private static Color      s_colorDebug     = Color.LIGHT_GRAY;
  private static Color      s_colorInfo      = Color.BLACK;
  private static Color      s_colorWarn      = Color.ORANGE;
  private static Color      s_colorError     = Color.RED;

  @Override
  public Component getTableCellRendererComponent(JTable p_table, Object p_value, boolean p_isSelected, boolean p_hasFocus,
      int p_row, int p_column) {
    Component cellComp = super.getTableCellRendererComponent(p_table, p_value, p_isSelected, p_hasFocus, p_row, p_column);
    RecordSetTable mod = (RecordSetTable) p_table.getModel();
    String obj = (String) mod.getValueAt(p_row, 1);
    EGravita grav = EGravita.valueOf(obj);
    switch (grav) {
      case Debug:
        cellComp.setForeground(s_colorDebug);
        break;
      case Err:
        cellComp.setForeground(s_colorError);
        break;
      case Fatal:
        cellComp.setForeground(s_colorError);
        break;
      case Log:
        cellComp.setForeground(s_colorInfo);
        break;
      case Warn:
        cellComp.setForeground(s_colorWarn);
        break;
      default:
        break;

    }

    return cellComp;
  }
}
