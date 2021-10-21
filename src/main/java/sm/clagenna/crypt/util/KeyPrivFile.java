package sm.clagenna.crypt.util;

import java.math.BigInteger;

import sm.clagenna.crypt.view.Controllore;

public class KeyPrivFile extends KeyPubFile {
  private static final String CSZ_KEY_KEYTYPE = "priv";

  public KeyPrivFile() {
    super();
  }

  @Override
  public String getKeyType() {
    return CSZ_KEY_KEYTYPE;
  }

  @Override
  protected void salvaCampi() {
    super.salvaCampi();
    m_prop.setProperty(Controllore.FLD_NP, rsa.getNP().toString());
    m_prop.setProperty(Controllore.FLD_NQ, rsa.getNQ().toString());
    m_prop.setProperty(Controllore.FLD_ND, rsa.getND().toString());
    m_prop.setProperty(Controllore.FLD_CARMICAEL, rsa.getNCarmichael().toString());

  }

  @Override
  protected void leggiCampi() {
    super.leggiCampi();
    String sz;
    BigInteger bi;

    sz = m_prop.getProperty(Controllore.FLD_NP);
    bi = new BigInteger(sz);
    rsa.setNP(bi);

    sz = m_prop.getProperty(Controllore.FLD_NQ);
    bi = new BigInteger(sz);
    rsa.setNQ(bi);

    sz = m_prop.getProperty(Controllore.FLD_ND);
    bi = new BigInteger(sz);
    rsa.setND(bi);

    sz = m_prop.getProperty(Controllore.FLD_CARMICAEL);
    bi = new BigInteger(sz);
    rsa.setNCarmichael(bi);

  }

}
