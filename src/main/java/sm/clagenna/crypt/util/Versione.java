package sm.clagenna.crypt.util;

import java.io.Serializable;

/**
 * Questa classe ha l'unico scopo di mantenere la versione degli standard
 * corrente. Bisogna mettere il branch corrente e la versione corrente.
 *
 */
public class Versione implements Serializable {
  private static final long  serialVersionUID = 7926044879949766871L;

  /** Nome del branch a cui appartiene il progetto */
  public static final String BRANCH           = "HEAD";

  /** Nome Applicativo */
  public static final String NOME_APPL        = "Rsa";

  /** Nome Applicativo */
  public static final String DESC_APPL        = "Studio algoritmo RSA";

  /** logo grande, mi aspetto che sia sotto /newappl/images */
  public static final String LOGO_BIG_APPL    = "logo128.gif";
  /** logo piccolo, mi aspetto che sia sotto /newappl/images */
  public static final String LOGO_SMALL_APPL  = "logo16.gif";

  /** Major Version */
  public static final int    APP_MAX_VERSION  = 0;
  /** Minor Version */
  public static final int    APP_MIN_VERSION  = 9;
  /** Build Version */
  public static final int    APP_BUILD             = 7;

  // e oggi esteso ${dh:CSZ_DATEDEPLOY}
  public static final String CSZ_DATEDEPLOY                 = "29/10/2021 08:33:11";


  /** il nome dell'elemento in cui racchiudere l'XML di questa classe */
  private static String      mainElem;
  static {
    mainElem = "Vers.";
  }

  public static void main(String[] args) {
    System.out.println(DESC_APPL + " " + getVersion());
  }

  /**
   * Costruttore vuoto che inizializza le variabili interne per avere un XML
   * corretto.
   */
  public Versione() {
  }

  /**
   * Ritorna la versione corrente degli standard nella forma
   *
   * <pre>
   * maxver.minver.build
   * </pre>
   *
   * @return versione applicativo
   */
  public static String getVersion() {
    String szVer = String.format("%s %d.%d.%d", mainElem, toi(APP_MAX_VERSION), toi(APP_MIN_VERSION), toi(APP_BUILD));
    return szVer;
  }

  public static String getVersionEx() {
    String szVer = getVersion();
    String szLoc = String.format("Applic. %s Progetto %s %s creata il %s", NOME_APPL, DESC_APPL, szVer, CSZ_DATEDEPLOY);
    return szLoc;
  }

  public String toString() {
    return getVersion();
  }

  private static Integer toi(int i) {
    return Integer.valueOf(i);
  }
}
