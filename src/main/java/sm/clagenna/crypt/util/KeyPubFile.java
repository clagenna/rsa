package sm.clagenna.crypt.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Properties;

import javax.swing.JOptionPane;

import lombok.Getter;
import lombok.Setter;
import sm.clagenna.crypt.rsa.RsaObj;
import sm.clagenna.crypt.view.Controllore;
import sm.clagenna.crypt.view.MainFrame;

public class KeyPubFile {
  private static final String    CSZ_KEY_TYPE    = "Key.Type";

  private static final String    CSZ_KEY_KEYTYPE = "pub";

  protected Properties           m_prop;
  @Getter @Setter protected File fileKey;

  protected RsaObj               rsa;

  public KeyPubFile() {
    m_prop = new Properties();
    rsa = MainFrame.getInst().getRsaObj();
    creaFileName();
  }

  public String getKeyType() {
    return CSZ_KEY_KEYTYPE;
  }

  public File creaFileName() {
    AppProperties props = AppProperties.getInst();
    String szLastDir = props.getLastDir();
    if (szLastDir == null)
      szLastDir = System.getProperty("user.dir");
    String sep = System.getProperty("file.separator");
    String sz = String.format("%s%s%s_%s.txt", //
        szLastDir, //
        sep, //
        rsa.getKeyName(), //
        getKeyType());
    fileKey = new File(sz);
    return fileKey;
  }

  public void saveFile() {
    salvaCampi();
    try (FileOutputStream fio = new FileOutputStream(fileKey)) {
      m_prop.store(fio, "RSA  key tipo " + getKeyType());
      String szMsg = String.format("Salvato la %s key sul file %s", getKeyType(), fileKey.getAbsolutePath());
      System.out.println(szMsg);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  protected void salvaCampi() {
    m_prop.setProperty(CSZ_KEY_TYPE, getKeyType());
    m_prop.setProperty(Controllore.FLD_KEYNAME, rsa.getKeyName());
    m_prop.setProperty(Controllore.FLD_MODULUS, rsa.getNPQmodulus().toString());
    m_prop.setProperty(Controllore.FLD_FITOTIENT, rsa.getNPQTotientFi().toString());
    m_prop.setProperty(Controllore.FLD_NE, rsa.getNE().toString());
  }

  public void leggiFile() {
    m_prop = new Properties();
    rsa.cleanKeyValues();
    try (InputStream is = new FileInputStream(fileKey)) {
      m_prop.load(is);
    } catch (Exception l_ex) {
      System.out.println("Errore lettura:" + fileKey.getAbsolutePath() + " " + l_ex.getMessage());
      return;
    }
    String sz = m_prop.getProperty(CSZ_KEY_TYPE);
    if (sz == null || !sz.equals(getKeyType())) {
      int dialogButton = JOptionPane.OK_OPTION;
      String szMsg = String.format("Hai selezionato un %s Key file\n %s\nche non corrisponde", getKeyType(),
          fileKey.getAbsolutePath());
      JOptionPane.showConfirmDialog(null, szMsg, "Errore File " + getKeyType(), dialogButton);
      return;
    }
    leggiCampi();
    rsa.updateValues();
  }

  protected void leggiCampi() {
    BigInteger bi;
    String sz = m_prop.getProperty(Controllore.FLD_KEYNAME);
    rsa.setKeyName(sz);

    sz = m_prop.getProperty(Controllore.FLD_MODULUS);
    bi = new BigInteger(sz);
    rsa.setNPQmodulus(bi);

    sz = m_prop.getProperty(Controllore.FLD_FITOTIENT);
    bi = new BigInteger(sz);
    rsa.setNPQTotientFi(bi);

    sz = m_prop.getProperty(Controllore.FLD_NE);
    bi = new BigInteger(sz);
    rsa.setNE(bi);
  }

}
