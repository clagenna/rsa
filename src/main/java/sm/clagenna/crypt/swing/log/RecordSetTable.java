package sm.clagenna.crypt.swing.log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.table.DefaultTableModel;

public class RecordSetTable extends DefaultTableModel {
  /** long serialVersionUID */
  private static final long    serialVersionUID = -3299745193463543912L;

  private LogTableRecord       m_rec;

  private List<LogTableRecord> m_arr;

  public RecordSetTable() {
    m_rec = new LogTableRecord();
    m_arr = new ArrayList<>();
  }

  @Override
  public boolean isCellEditable(int p_row, int p_column) {
    return false;
  }

  @Override
  public Class<?> getColumnClass(int p_columnIndex) {
    return m_rec.getColumnClass()[p_columnIndex];
  }

  @Override
  public String getColumnName(int p_column) {
    return m_rec.getColumnNames()[p_column];
  }

  @Override
  public int getColumnCount() {
    return m_rec.getColumnNames().length;
  }

  @Override
  public Object getValueAt(int p_row, int p_column) {
    LogTableRecord rec = m_arr.get(p_row);
    return rec.getCol(p_column);
  }

  @Override
  public int getRowCount() {
    if (m_arr == null)
      return 0;
    return m_arr.size();
  }

  public void clear() {
    m_arr.clear();
    fireTableDataChanged();
  }

  public void addLog(LogTableRecord p_log) {
    m_arr.add(p_log);
    fireTableRowsInserted(m_arr.size() - 1, m_arr.size() - 1);
  }

  public void addLog(String p_tx) {
    addLog(EGravita.Log, p_tx);
  }

  public void addLog(EGravita p_sev, String p_tx) {
    addLog(new Date(), p_sev, p_tx);
  }

  public void addLog(Date p_dt, EGravita p_sev, String p_tx) {
    LogTableRecord log = new LogTableRecord(p_dt, p_sev, p_tx);
    m_arr.add(log);
    fireTableRowsInserted(m_arr.size() - 1, m_arr.size() - 1);
  }

  public void addLog(Messaggio p_me) {
    LogTableRecord log = new LogTableRecord(p_me.getMomento(), p_me.getGravita(), p_me.getMsg());
    m_arr.add(log);
    fireTableRowsInserted(m_arr.size() - 1, m_arr.size() - 1);
  }

}
