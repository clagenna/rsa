package sm.clagenna.crypt.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.List;
import java.util.OptionalInt;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import lombok.Getter;
import lombok.Setter;
import sm.clagenna.crypt.rsa.RsaObj;
import sm.clagenna.crypt.swing.DeCodeString;
import sm.clagenna.crypt.swing.IRsa;
import sm.clagenna.crypt.swing.IRsaListen;

public class Pan6TxtCoded extends JPanel implements IRsaListen {

  /** long serialVersionUID */
  private static final long                serialVersionUID = 593767533148741772L;
  @Getter @Setter private static boolean   debug;
  private static NumberFormat              fmt              = NumberFormat.getIntegerInstance();

  @SuppressWarnings("unused") private IRsa m_irsa;
  private JTextArea                        txTxtEncoded;
  private int                              maxBitsCry;
  private List<BigInteger>                 liUnoCripted;
  private List<BigInteger>                 liUnoTxt;
  private List<BigInteger>                 liDueCrypted;
  private List<BigInteger>                 liDueTxt;
  private JButton                          btDecode;

  /**
   * Create the panel.
   */
  public Pan6TxtCoded() {
    Controllore.getInst().addListener(this);
    initComponents();
  }

  public Pan6TxtCoded(IRsa p_irsa) {
    m_irsa = p_irsa;
    Controllore.getInst().addListener(this);
    initComponents();
  }

  private void initComponents() {

    setBorder(BorderFactory.createTitledBorder("Testo Codificato"));
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
    gridBagLayout.rowHeights = new int[] { 0, 0 };
    gridBagLayout.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
    gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
    setLayout(gridBagLayout);

    txTxtEncoded = new JTextArea();
    JScrollPane scrl = new JScrollPane(txTxtEncoded);
    GridBagConstraints gbc_txTxtEncoded = new GridBagConstraints();
    gbc_txTxtEncoded.insets = new Insets(0, 0, 0, 5);
    gbc_txTxtEncoded.fill = GridBagConstraints.BOTH;
    gbc_txTxtEncoded.gridx = 0;
    gbc_txTxtEncoded.gridy = 0;
    add(scrl, gbc_txTxtEncoded);
    txTxtEncoded.getDocument().addDocumentListener(new DocumentListener() {

      @Override
      public void removeUpdate(DocumentEvent e) {
        txtEncoded_Update();
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        txtEncoded_Update();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        txtEncoded_Update();
      }
    });

    btDecode = new JButton("Dec");
    GridBagConstraints gbc_btDecode = new GridBagConstraints();
    gbc_btDecode.gridx = 1;
    gbc_btDecode.gridy = 0;
    add(btDecode, gbc_btDecode);
    btDecode.setEnabled(false);
    btDecode.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String sz = txTxtEncoded.getText();
        decodifica(sz);
      }
    });

  }

  @Override
  public void valueChanged(String id, Object val) {
    switch (id) {
      case Controllore.FLD_TXT_ORIG:
        codificaTesto((String) val);
        break;
      case Controllore.FLD_DEBUG:
        debug = (Boolean) val;
        break;
    }
  }

  protected void txtEncoded_Update() {
    String sz = txTxtEncoded.getText();
    boolean bEna = sz != null && sz.length() > 6;
    if (bEna) {
      RsaObj rsa = MainFrame.getInst().getRsaObj();
      bEna = rsa.isPrivKeyPresent();
    }
    btDecode.setEnabled(bEna);
  }

  private void codificaTesto(String val) {
    if (val == null || val.length() == 0)
      return;
    RsaObj rsa = MainFrame.getInst().getRsaObj();
    DeCodeString deco = new DeCodeString();
    int shift = 8;
    deco.setShift(shift);
    deco.setMaxBits(rsa.getNPQmodulus());

    liUnoTxt = deco.toList(val, false);
    if (isDebug())
      logInfo(liUnoTxt, "cry:txt-tolist");
    liUnoCripted = new ArrayList<>();
    for (BigInteger bi : liUnoTxt) {
      bi = rsa.esponenteE(bi);
      liUnoCripted.add(bi);
    }
    if (isDebug())
      logInfo(liUnoCripted, "cry:list-crypt");
    OptionalInt opt = liUnoCripted.stream().mapToInt(BigInteger::bitLength).max();
    maxBitsCry = (opt.getAsInt() / shift + 1) * shift;
    deco.setMaxBits(maxBitsCry);
    if (isDebug())
      System.out.println("cry:Max bits crypt=" + maxBitsCry);

    // 3) list2(BigInt) => deco() => sz2
    String szUnoCrypted = deco.toString(liUnoCripted, true);
    if (isDebug())
      logInfo(szUnoCrypted, "cry:list-toCryStr");

    // 4) sz2 => Base64
    Encoder b64 = java.util.Base64.getEncoder();
    String szUnoCryptedB64 = b64.encodeToString(szUnoCrypted.getBytes(StandardCharsets.UTF_8));
    if (isDebug())
      System.out.println("cry:Cry_base64:" + szUnoCryptedB64);
    // 5) chunk di 64 chars
    StringBuilder sb = new StringBuilder();
    for (int k = 0; k < szUnoCryptedB64.length(); k += 64) {
      int fine = k + 64;
      if (fine > szUnoCryptedB64.length())
        fine = szUnoCryptedB64.length();
      sb.append(szUnoCryptedB64.substring(k, fine)).append("\n");
    }
    txTxtEncoded.setText(sb.toString());
    Controllore.getInst().setValue(Controllore.FLD_TXT_CODED, sb.toString());
    // decodifica(sb.toString());
  }

  private void decodifica(String string) {
    RsaObj rsa = MainFrame.getInst().getRsaObj();
    DeCodeString deco = new DeCodeString();

    // 5) Chunk64 chars == Base64
    String szDueCryptedB64 = string.replace("\n", "");

    // 4) Base64 == sz2
    Decoder b64 = java.util.Base64.getDecoder();
    // String szDueCrypted = new String(Base64.decodeBase64(szDueCryptedB64));
    String szDueCrypted = new String(b64.decode(szDueCryptedB64));
    if (isDebug())
      logInfo(szDueCrypted, "dec:B64-toCryStr");

    // 3) sz2 => codi() => list2(BigInt)
    deco.setShift(8);
    // questo ha senso in fase di code, forse qui va tolto
    if (maxBitsCry != 0)
      deco.setMaxBits(maxBitsCry);
    liDueCrypted = deco.toList(szDueCrypted, true);
    if (isDebug())
      logInfo(liDueCrypted, "dec:list-crypt");

    liDueTxt = new ArrayList<>();
    for (BigInteger bi : liDueCrypted) {
      BigInteger bi2 = rsa.esponenteD(bi);
      liDueTxt.add(bi2);
    }
    if (isDebug())
      logInfo(liDueTxt, "dec:txt-toList");

    // 1) list(BigInt) => codi() => sz
    String szOut = deco.toString(liDueTxt, false);
    if (isDebug())
      System.out.println("dec:txt:" + szOut);
    Controllore.getInst().setValue(Controllore.FLD_TXT_DECODED, szOut);
  }

  private void logInfo(List<BigInteger> liUnoTxt2, String str) {
    System.out.println("\n-----------------------------------------------------------\n" + str);
    liUnoTxt2 //
        .stream() //
        .map(d -> fmt.format(d)) //
        .forEach(System.out::println);
  }

  private void logInfo(String szCry, String str) {
    System.out.println("\n-----------------------------------------------------------\n" + str);
    StringBuilder sb = new StringBuilder();
    int k = 0;
    for (char cc : szCry.toCharArray()) {
      if (sb.length() > 1)
        sb.append(",");
      sb.append(String.valueOf((int) cc));
      if (++k > 32) {
        System.out.println(sb.toString());
        sb = new StringBuilder();
        k = 0;
      }
    }
    System.out.println(sb.toString());
  }

}
