package sm.clagenna.crypt.swing.log;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.beans.Beans;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.Setter;

public class MioLogger {
  private static final Logger  s_log      = LogManager.getLogger(MioLogger.class);
  @Getter
  private static MioLogger     inst;
  private List<Messaggio>      m_liMsg;
  /** ultimo messaggio inserito nella lista */
  private Messaggio            msg;
  @Getter() @Setter
  private EGravita             minGravita = EGravita.Log;
  List<PropertyChangeListener> observers;
  private int                  m_nWarn;
  private int                  m_nErr;
  private int                  m_nFatal;

  public MioLogger() {
    if (inst != null)
      throw new UnsupportedOperationException("MioLogger gia istanziato !");
    inst = this;
  }

  public void addLogListener(PropertyChangeListener p_ev) {
    if (Beans.isDesignTime())
      return;
    if (observers == null)
      observers = new ArrayList<PropertyChangeListener>();
    observers.add(p_ev);
  }

  public void removeLogListener(PropertyChangeListener p_ev) {
    if (Beans.isDesignTime())
      return;
    if (observers == null)
      return;
    observers.remove(p_ev);
    if (observers.size() == 0)
      observers = null;
  }

  public void debug(String p_szMsg) {
    msg = new Messaggio(EGravita.Debug, p_szMsg);
    addAndNotify(msg);
  }

  public void log(String p_szMsg) {
    msg = new Messaggio(EGravita.Log, p_szMsg);
    addAndNotify(msg);
  }

  public void warn(String p_szMsg) {
    m_nWarn++;
    // System.out.println(p_szMsg);
    msg = new Messaggio(EGravita.Warn, p_szMsg);
    addAndNotify(msg);
  }

  public void error(String p_szMsg) {
    m_nErr++;
    msg = new Messaggio(EGravita.Err, p_szMsg);
    addAndNotify(msg);
  }

  public void fatal(String p_szMsg) {
    m_nFatal++;
    msg = new Messaggio(EGravita.Fatal, p_szMsg);
    addAndNotify(msg);
  }

  private void addAndNotify(Messaggio msg) {
    if (msg == null)
      return;
    if (m_liMsg == null)
      m_liMsg = new ArrayList<Messaggio>();
    m_liMsg.add(msg);
    Level lev = null;
    switch (msg.getGravita()) {
      case Debug:
        lev = Level.DEBUG;
        break;
      case Err:
        lev = Level.ERROR;
        break;
      case Fatal:
        lev = Level.FATAL;
        break;
      case Log:
        lev = Level.INFO;
        break;
      case Warn:
        lev = Level.WARN;
        break;
    }
    s_log.log(lev, msg.getMsg());
    if (minGravita.le(msg.getGravita()))
      notifyObservers(msg);
  }

  private void notifyObservers(Messaggio msg) {
    if (observers == null)
      return;
    PropertyChangeEvent ev = new PropertyChangeEvent(this, msg.getGravita().toString(), null, msg);
    for (PropertyChangeListener evl : observers)
      evl.propertyChange(ev);
  }

  public boolean isWarning() {
    return m_nWarn > 0 || m_nErr > 0 || m_nFatal > 0;
  }

  public boolean isError() {
    return m_nErr > 0 || m_nFatal > 0;
  }

  public boolean isFatal() {
    return m_nFatal > 0;
  }

  public void clear() {
    m_nWarn = 0;
    m_nErr = 0;
    m_nFatal = 0;
    msg = null;
    if (m_liMsg != null)
      m_liMsg.clear();
    m_liMsg = null;
  }

  public Messaggio getLastMessaggio() {
    return msg;
  }

  public boolean isEmpty() {
    return m_liMsg == null || m_liMsg.size() == 0;
  }

  public void copyLogs() {
    StringBuffer sb = new StringBuffer();
    for (Messaggio me : getMessages(minGravita)) {
      sb.append(me.toString()).append("\n");
    }
    StringSelection sel = new StringSelection(sb.toString());
    Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
    clip.setContents(sel, null);
  }

  public List<Messaggio> getMessages() {
    return getMessages(EGravita.Debug);
  }

  public List<Messaggio> getMessages(EGravita p) {
    List<Messaggio> vec = new ArrayList<Messaggio>();
    EGravita ultGravitaLimite = p;
    if (ultGravitaLimite == null)
      ultGravitaLimite = EGravita.Debug;
    for (Messaggio me : m_liMsg) {
      if (me.getGravita().ge(ultGravitaLimite))
        vec.add(me);
    }
    return vec;
  }

  public void saveLogs() {
    System.out.println("MioLogger.saveLogs()");
  }

  public void log(EGravita eg, String sz) {
    switch (eg) {
      case Debug:
        debug(sz);
        break;
      case Err:
        error(sz);
        break;
      case Fatal:
        fatal(sz);
        break;
      case Log:
        log(sz);
        break;
      case Warn:
        warn(sz);
        break;
    }
  }

}
