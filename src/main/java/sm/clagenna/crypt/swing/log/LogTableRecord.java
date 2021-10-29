package sm.clagenna.crypt.swing.log;

import java.util.Date;

import sm.clagenna.crypt.util.Utils;

public class LogTableRecord {

  private static String[]   s_colName  = {   //
      "Momento"                              //
      , "Severita"                           //
      , "Messaggio" };

  private static Class<?>[] s_colClass = {   //
      String.class                           //
      , String.class                         //
      , String.class };

  private Date              m_momento;
  private EGravita          m_gravita;
  private String            m_testo;

  public String[] getColumnNames() {
    return LogTableRecord.s_colName;
  }

  public Class<?>[] getColumnClass() {
    return s_colClass;
  }

  public LogTableRecord() {
    m_momento = new Date();
    m_gravita = EGravita.Debug;
    m_testo = "?!?";
  }

  public LogTableRecord(Date p_dt, EGravita p_sev, String p_tx) {
    m_momento = p_dt;
    m_gravita = p_sev;
    m_testo = p_tx;
  }

  public Object getCol(int p_column) {
    Object obj = "??";
    switch (p_column) {
      case 0:
        obj = Utils.s_fmtY4MDHMS.format(m_momento);
        break;
      case 1:
        obj = m_gravita.toString();
        break;
      case 2:
        obj = m_testo;
        break;
    }
    return obj;
  }

}
