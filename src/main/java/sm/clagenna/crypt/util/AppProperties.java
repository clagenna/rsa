package sm.clagenna.crypt.util;

import java.awt.Frame;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.swing.JOptionPane;

import lombok.Getter;
import lombok.Setter;
import sm.clagenna.crypt.rsa.RsaObj;
import sm.clagenna.crypt.view.Controllore;
import sm.clagenna.crypt.view.MainFrame;

public class AppProperties {
  private static AppProperties s_inst;
  private static final String  CSZ_PROPFILE     = "RsaParams.properties";
  private static final String  CSZ_PROP_PosX    = "frame.posX";
  private static final String  CSZ_PROP_PosY    = "frame.posY";
  private static final String  CSZ_PROP_LenX    = "frame.lenX";
  private static final String  CSZ_PROP_LenY    = "frame.lenY";
  private static final String  CSZ_PROP_LastDir = "last.dir";
  private static final String  CSZ_PROP_Debug   = "debug";

  private MainFrame            m_frame;
  private Properties           m_prop;

  @Getter
  @Setter
  private String               LastDir;
  @Getter
  @Setter
  private String               LastFile;
  @Getter
  @Setter
  private boolean              debug;

  public AppProperties(MainFrame fr) {
    m_frame = fr;
    s_inst = this;
  }

  public static AppProperties getInst() {
    return s_inst;
  }

  public void leggiProperties() {
    m_prop = new Properties();
    File fiProp = getFileProps();
    if ( !fiProp.exists())
      return;
    try (InputStream is = new FileInputStream(fiProp)) {
      m_prop.load(is);
    } catch (Exception l_ex) {
      System.out.println("Errore lettura:" + CSZ_PROPFILE + " " + l_ex.getMessage());
      return;
    }
    int x = Integer.parseInt(m_prop.getProperty(CSZ_PROP_PosX, "-1"));
    int y = Integer.parseInt(m_prop.getProperty(CSZ_PROP_PosY, "-1"));
    int w = Integer.parseInt(m_prop.getProperty(CSZ_PROP_LenX, "-1"));
    int h = Integer.parseInt(m_prop.getProperty(CSZ_PROP_LenY, "-1"));
    debug = Boolean.parseBoolean(m_prop.getProperty(CSZ_PROP_Debug, "false"));
    Controllore.getInst().setValue(Controllore.FLD_DEBUG, debug);
    if (Math.abs(x) > 0 && Math.abs(y) > 0) {
      Rectangle r = new Rectangle(x, y, w, h);
      m_frame.setBounds(r);
    }

  }

  public void salvaProperties() {
    if (m_prop == null)
      m_prop = new Properties();
    m_frame.setExtendedState(Frame.NORMAL);

    Rectangle r = m_frame.getBounds();
    int x = (int) r.getX();
    int y = (int) r.getY();
    int w = (int) r.getWidth();
    int h = (int) r.getHeight();

    m_prop.setProperty(CSZ_PROP_PosX, String.valueOf(x));
    m_prop.setProperty(CSZ_PROP_PosY, String.valueOf(y));
    m_prop.setProperty(CSZ_PROP_LenX, String.valueOf(w));
    m_prop.setProperty(CSZ_PROP_LenY, String.valueOf(h));

    m_prop.setProperty(CSZ_PROP_Debug, Boolean.toString(debug));
    if (LastDir != null)
      m_prop.setProperty(CSZ_PROP_LastDir, LastDir);
    File fiProp = getFileProps();
    try (FileOutputStream fio = new FileOutputStream(fiProp)) {
      m_prop.store(fio, CSZ_PROPFILE);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private File getFileProps() {
    String szFi = System.getProperty("user.home") + System.getProperty("file.separator") + CSZ_PROPFILE;
    return new File(szFi);
  }

  public File getPubKeyFile() {
    File fiRet = null;
    RsaObj rsa = MainFrame.getInst().getRsaObj();
    String keyName = rsa.getKeyName();
    if (keyName == null) {
      JOptionPane.showConfirmDialog(m_frame, "Manca il nome del Key file", "Fornire il nome", JOptionPane.OK_OPTION);
      return fiRet;
    }
    String szDir = LastDir;
    if (LastDir == null)
      szDir = System.getProperty("user.dir");
    Path pth = Paths.get(szDir, String.format("%s_pub.txt", keyName));
    fiRet = pth.toFile();
    return fiRet;
  }

  public File getPrivKeyFile() {
    File fiRet = null;
    RsaObj rsa = MainFrame.getInst().getRsaObj();
    String keyName = rsa.getKeyName();
    if (keyName == null) {
      JOptionPane.showConfirmDialog(m_frame, "Manca il nome del Key file", "Fornire il nome", JOptionPane.OK_OPTION);
      return fiRet;
    }
    String szDir = LastDir;
    if (LastDir == null)
      szDir = System.getProperty("user.dir");
    Path pth = Paths.get(szDir, String.format("%s_priv.txt", keyName));
    fiRet = pth.toFile();
    return fiRet;
  }

}
