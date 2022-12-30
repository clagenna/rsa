package sm.clagenna.crypt.view;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sm.clagenna.crypt.swing.IRsa;
import sm.clagenna.crypt.swing.IRsaListen;

public class Controllore implements IRsa {

  private static final Logger s_log               = LogManager.getLogger(Controllore.class);
  /** singleton instance */
  private static Controllore  s_inst;

  public static final String  FLD_QTA_PRIMI       = "QtaPrimi";
  public static final String  FLD_PRIMI_GENERATI  = "PrimiGenerati";
  public static final String  FLD_PRIMO_INIZIALEP = "PrimoInizialeP";
  public static final String  FLD_QTA_PRIMIP      = "QtaPrimiP";
  public static final String  FLD_PRIMO_INIZIALEQ = "PrimoInizialeQ";
  public static final String  FLD_QTA_PRIMIQ      = "QtaPrimiQ";
  public static final String  FLD_DEBUG           = "debug";

  public static final String  FLD_KEYNAME         = "KeyName";
  public static final String  FLD_NP              = "NumeroP";
  public static final String  FLD_NQ              = "NumeroQ";
  public static final String  FLD_MODULUS         = "Modulus";
  public static final String  FLD_FITOTIENT       = "FiTotient";
  public static final String  FLD_CARMICAEL       = "Carmicael";
  public static final String  FLD_NE              = "NumeroE";
  public static final String  FLD_ND              = "NumeroD";

  public static final String  FLD_TXT_ORIG        = "TxtOrig";
  public static final String  FLD_TXT_CODED       = "TxtCoded";
  public static final String  FLD_TXT_DECODED     = "TxtDecoded";

  public static final String  FLD_QTAPRIMIGEN     = "QtaPrimiGen";
  public static final String  FLD_MAXPRIMO        = "MaxPrimo";

  private List<IRsaListen>    m_listeners;

  public Controllore() {
    if (s_inst != null)
      throw new UnsupportedOperationException("Controllore già istanziato");
    s_inst = this;
  }

  public static Controllore getInst() {
    return s_inst;
  }

  @Override
  public void addListener(IRsaListen p_l) {
    if (m_listeners == null)
      m_listeners = new ArrayList<IRsaListen>();
    if ( !m_listeners.contains(p_l))
      m_listeners.add(p_l);
  }

  @Override
  public boolean setValue(String id, Object lv) {
    String sz = lv == null ? "*NULL*" : lv.getClass().getSimpleName();
    if (lv instanceof Boolean)
      sz = lv.toString();
    if (lv instanceof BigInteger)
      sz = NumberFormat.getIntegerInstance().format(lv);
    // System.out.printf("Controllore.setValue(\"%s\", %s)\n", id, sz);
    s_log.debug("Controllore.setValue(\"{}\", {})", id, sz);
    if (m_listeners != null)
      m_listeners.stream().forEach(s -> s.valueChanged(id, lv));
    return false;
  }

}
