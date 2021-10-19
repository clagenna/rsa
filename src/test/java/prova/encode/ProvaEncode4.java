package prova.encode;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import sm.clagenna.crypt.rsa.RsaObj;
import sm.clagenna.crypt.swing.DeCodeString;

public class ProvaEncode4 {

  private String           CSZ      = "Prima del ventesimo secolo, il viaggio spaziale era in gran parte \r\n"   //
      + "un volo di fantasia. La maggior parte degli autori in quel periodo \r\n"                                //
      + "non riusciva a comprendere la natura del movimento di un veicolo \r\n"                                  //
      + "spaziale. Solo all'inizio del ventesimo secolo, un insegnante russo, \r\n"                              //
      + "K. E. Tsiolkovsky, pose le basi per la missilistica fornendo \r\n"                                      //
      + "informazioni sulla natura del movimento propulsivo. \r\n"                                               //
      + "I suoi studi hanno fornito una comprensione dei requisiti propulsivi, \r\n"                             //
      + "ma non hanno fornito la tecnologia. ";

  private static int       s_shift  = 8;
  private static int       s_maxBit = 32;

  /**
   * <pre>
   *   P=897.527
   *   Q=345.923
   *   N=310.475.232.421
   *   Fi=310.473.988.972
   *   Cr=155.236.994.486
   *   E=77.618.497.259
   *   D=295.920.520.739
   * </pre>
   */
  private RsaObj           rsa;
  private DeCodeString     deco;

  private List<BigInteger> liUnoTxt;
  private List<BigInteger> liDueTxt;

  private List<BigInteger> liUnoCripted;
  private List<BigInteger> liDueCrypted;

  private String           szUnoCry;
  private String           szDueCry;

  private String           szUnoB64;
  private String           szDueTxt;

  private int              maxBitsCry;

  @Test
  public void provalo() {
    inizializza();

    uno01Txt2List();
    uno02ListToCrypted();
    uno03CryptToString();
    uno04CryptStr2B64();

    due01StrB64ToStr(szUnoB64);
    due02CryptStr2List();
    due03DecryToList();
    due04DecList2Txt();
    System.out.println(szDueTxt);
  }

  private void uno01Txt2List() {
    liUnoTxt = deco.toList(CSZ, false);
    String sz = deco.toString(liUnoTxt, false);
    if ( !CSZ.equals(sz))
      System.out.println("Stringhe differenti !");
  }

  private void uno02ListToCrypted() {
    liUnoCripted = new ArrayList<>();
    for (BigInteger bi : liUnoTxt) {
      bi = rsa.esponenteE(bi);
      liUnoCripted.add(bi);
    }
    // cerco il valore massimo dopo crypt
    OptionalInt opt = liUnoCripted.stream().mapToInt(BigInteger::bitLength).max();
    // setto deco su quella qta di bit normalizzata per eccesso
    maxBitsCry = (opt.getAsInt() / deco.getShift() + 1) * deco.getShift();
    deco.setMaxBits(maxBitsCry);
    System.out.println("Max bits crypt=" + maxBitsCry);
    liDueTxt = new ArrayList<BigInteger>();
    for (BigInteger bi : liUnoCripted) {
      bi = rsa.esponenteD(bi);
      liDueTxt.add(bi);
    }
    if ( !deco.confronta(liUnoTxt, liDueTxt))
      System.out.println("Cry decri diversi !");
  }

  private void uno03CryptToString() {
    // deco.setDebug(true);
    szUnoCry = deco.toString(liUnoCripted, true);
    liDueCrypted = deco.toList(szUnoCry, true);
    if ( !deco.confronta(liUnoCripted, liDueCrypted))
      System.out.println("String Cry diversi !");
  }

  private void uno04CryptStr2B64() {
    szUnoB64 = Base64.encodeBase64String(szUnoCry.getBytes());

  }

  private void due01StrB64ToStr(String szVal) {
    szDueCry = new String(Base64.decodeBase64(szVal));
  }

  private void due02CryptStr2List() {
    deco.setMaxBits(maxBitsCry);
    deco.setShift(8);
    liDueCrypted = deco.toList(szDueCry, true);
    deco.confronta(liUnoCripted, liDueCrypted);
  }

  private void due03DecryToList() {
    liDueTxt = new ArrayList<>();
    for (BigInteger bi : liDueCrypted) {
      BigInteger bi2 = rsa.esponenteD(bi);
      liDueTxt.add(bi2);
    }
    deco.confronta(liUnoTxt, liDueTxt);
  }

  private void due04DecList2Txt() {
    szDueTxt = deco.toString(liDueTxt, false);
  }

  private void inizializza() {
    deco = new DeCodeString();
    deco.setShift(s_shift);
    deco.setMaxBits(s_maxBit);

    rsa = new RsaObj();
    rsa.setNP(BigInteger.valueOf(897_527L));
    rsa.setNQ(BigInteger.valueOf(345_923L));
    rsa.calcolaRSAObj();
    rsa.stampaRis();
  }

}
