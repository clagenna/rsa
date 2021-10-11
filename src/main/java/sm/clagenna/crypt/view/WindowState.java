/**
 * 
 */
package sm.clagenna.crypt.view;

import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author claudio
 * 
 */
public class WindowState implements WindowListener {

  public interface IWinState {
    public void readProperties(Properties prop);

    public void saveProperties(Properties prop);

    public void chiudi();
  }

  private static final String PROP_INIPATH = "./";

  private static final String CSZ_WORK     = "/work/";

  Window                      m_win;
  private Properties          m_prop;
  private String              m_szIniFile;
  private boolean             WinPositioned;

  /**
   * Va istanziato all'interno di una form che non sia derivata da
   * {@link DialogPadre} (vedi le form fatte con "Matisse").<br/>
   * Per utilizzarla seguire i seguenti passi:
   * <ol>
   * <li>Va dichiarata una variabile di classe, <br/>
   * es: <code>protected WindowState m_winStat;</code> <br/>
   * (<i>protected</i> cosi si evita il warning)</li>
   * <li>e nella miaInit() (<i>una tantum</i> init) della Form va istanziata<br/>
   * cosi: <code>m_winStat = new WindowState(this);</code></li>
   * <li>pubblicare un metodo <code>void chiudi()</code> che verr&agrave;
   * richiamato all'atto della chiusura della form</li>
   * </ol>
   * That's it !!!
   */
  public WindowState(Window ww) {
    m_win = ww;
    m_win.addWindowListener(this);
  }

  public Properties readState() {
    Properties prop = getProperties();
    readProp();
    return prop;
  }

  public Properties saveState() {
    Properties prop = getProperties();
    writeProp();
    return prop;
  }

  // -------------------------------------------------------------------
  protected void readProp() {
    if (isWinPositioned())
      return;
    String szPath = getIniFile(true);

    FileInputStream fis = null;
    try {
      fis = new FileInputStream(szPath);
    } catch (FileNotFoundException ex) {
    }
    getProperties(); // preparo m_prop
    try {
      if (fis != null) {
        m_prop.load(fis);
        fis.close();
      }
      // else
      // return;
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    m_prop.setProperty(PROP_INIPATH, szPath);
    readProperties(m_prop);
  }

  // -------------------------------------------------------------------
  private void readProperties(Properties prop) {
    Rectangle rect = new Rectangle();
    double px, py, cx, cy;
    try {
      px = getDouble(prop, CisCornice.FRAME_POSX, 10);
      py = getDouble(prop, CisCornice.FRAME_POSY, 10);
      cx = getDouble(prop, CisCornice.FRAME_CX, 500);
      cy = getDouble(prop, CisCornice.FRAME_CY, 400);
      rect.setFrame(px, py, cx, cy);
      m_win.setBounds(rect);
      m_win.setSize((int) cx, (int) cy);
      setWinPositioned(true);
    } catch (NumberFormatException ex) {
    }
    try {
      if (m_win instanceof IWinState)
        ((IWinState) m_win).readProperties(prop);
    } catch (Exception ex) {
      // maschero gli eventuali errori
    }

  }

  private double getDouble(Properties prop, String propName, double defValue) {
    String p = prop.getProperty(propName);
    if (p == null)
      return defValue;
    try {
      double d = Double.parseDouble(p);
      return d;
    } catch (NumberFormatException ex) {
      return defValue;
    }
  }

  // -------------------------------------------------------------------
  private void writeProp() {
    getProperties();
    saveProperties(m_prop);

    String szPath = getIniFile(false);
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(szPath);
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    }

    try {
      if (fos != null) {
        m_prop.store(fos, "");
        fos.close();
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  // -------------------------------------------------------------------
  private void saveProperties(Properties prop) {
    Rectangle rect = m_win.getBounds();
    // --------------------------------
    prop.setProperty(CisCornice.FRAME_POSX, String.valueOf(rect.getX()));
    prop.setProperty(CisCornice.FRAME_POSY, String.valueOf(rect.getY()));
    prop.setProperty(CisCornice.FRAME_CX, String.valueOf(rect.getWidth()));
    prop.setProperty(CisCornice.FRAME_CY, String.valueOf(rect.getHeight()));

    // n = jTabbedPane1.getSelectedIndex();
    // n = n < 0 ? 0 : n;
    // prop.setProperty(CisCornice.PROP_ACTIVETAB, String.valueOf(n));
    try {
      if (m_win instanceof IWinState)
        ((IWinState) m_win).saveProperties(prop);
    } catch (Exception ex) {
      // maschero gli eventuali errori
    }
  }

  public Properties getProperties() {
    if (m_prop == null)
      m_prop = new Properties();
    return m_prop;
  }

  // -------------------------------------------------------------------
  public String getIniFile(boolean p_bRead) {
    if (m_szIniFile != null && m_szIniFile.length() > 1)
      return m_szIniFile;
    String szNew = null;
    String szOld = null;
    String fs = System.getProperty("file.separator");
    String usrd = System.getProperty("user.dir");
    StringBuffer sb = new StringBuffer(getClass().getName());
    int n = sb.indexOf(".");
    while (n >= 0) {
      sb.replace(n, n + 1, fs);
      n = sb.indexOf(".");
    }
    // tiro fuori il solo path del file ini
    n = sb.toString().lastIndexOf(fs);
    szNew = usrd + CSZ_WORK + sb.substring(0, n);
    // se sto scrivendo creo la struct di direttori
    if ( !p_bRead) {
      File fi = new File(szNew);
      fi.mkdirs();
    }
    sb.append(".ini");
    // vecchio path originale
    szOld = usrd + fs + sb.toString();
    szNew = usrd + CSZ_WORK + sb.toString();
    if (p_bRead) {
      File fiNew = new File(szNew);
      // se non esiste il file ini sotto work allora vale quello vecchio
      if ( !fiNew.exists())
        szNew = szOld;
    } else {
      File fiOld = new File(szOld);
      // se non esiste il file ini sotto work allora vale quello vecchio
      if (fiOld.exists())
        try {
          System.out.println("DelIni:" + szOld);
          fiOld.delete();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
    }
    return szNew;
  }

  /*
   * (non-Javadoc)
   * @see
   * java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
   */
  @Override
  public void windowActivated(WindowEvent p_e) {
    readProp();
  }

  /*
   * (non-Javadoc)
   * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
   */
  @Override
  public void windowClosed(WindowEvent p_e) {
    if (m_win != null) {
      writeProp();
      m_win.removeWindowListener(this);
      m_win = null;
    }
  }

  /*
   * (non-Javadoc)
   * @see
   * java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
   */
  @Override
  public void windowClosing(WindowEvent p_e) {
    if (m_win != null) {
      writeProp();
      m_win.removeWindowListener(this);
      m_win = null;
    }

  }

  /*
   * (non-Javadoc)
   * @see
   * java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
   */
  @Override
  public void windowDeactivated(WindowEvent p_e) {

  }

  /*
   * (non-Javadoc)
   * @see
   * java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
   */
  @Override
  public void windowDeiconified(WindowEvent p_e) {

  }

  /*
   * (non-Javadoc)
   * @see
   * java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
   */
  @Override
  public void windowIconified(WindowEvent p_e) {

  }

  /*
   * (non-Javadoc)
   * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
   */
  @Override
  public void windowOpened(WindowEvent p_e) {

  }

  /**
   * @param winPositioned
   *          the winPositioned to set
   */
  public void setWinPositioned(boolean winPositioned) {
    WinPositioned = winPositioned;
  }

  /**
   * @return the winPositioned
   */
  public boolean isWinPositioned() {
    return WinPositioned;
  }

}
