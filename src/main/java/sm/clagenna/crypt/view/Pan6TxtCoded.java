package sm.clagenna.crypt.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.commons.codec.binary.Base64;

import sm.clagenna.crypt.rsa.RsaObj;
import sm.clagenna.crypt.swing.DeCodeString;
import sm.clagenna.crypt.swing.IRsa;
import sm.clagenna.crypt.swing.IRsaListen;

public class Pan6TxtCoded extends JPanel implements IRsaListen {

  /** long serialVersionUID */
  private static final long                serialVersionUID = 593767533148741772L;
  @SuppressWarnings("unused") private IRsa m_irsa;
  private JTextArea                        txTxtEncoded;

  /**
   * Create the panel.
   */
  public Pan6TxtCoded() {
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
    GridBagConstraints gbc_txTxtEncoded = new GridBagConstraints();
    gbc_txTxtEncoded.insets = new Insets(0, 0, 0, 5);
    gbc_txTxtEncoded.fill = GridBagConstraints.BOTH;
    gbc_txTxtEncoded.gridx = 0;
    gbc_txTxtEncoded.gridy = 0;
    add(txTxtEncoded, gbc_txTxtEncoded);

    JButton btDecode = new JButton("Dec");
    GridBagConstraints gbc_btDecode = new GridBagConstraints();
    gbc_btDecode.gridx = 1;
    gbc_btDecode.gridy = 0;
    add(btDecode, gbc_btDecode);

  }

  @Override
  public void valueChanged(String id, Object val) {
    switch (id) {
      case Controllore.FLD_TXT_ORIG:
        codificaTesto((String) val);
        break;
    }
  }

  private void codificaTesto(String val) {
    if (val == null || val.length() == 0)
      return;
    RsaObj rsa = MainFrame.getInst().getRsaObj();
    DeCodeString deco = new DeCodeString();
    deco.setMaxBits(rsa.getNPQmodulus());
    // 1) sz => codi() => list(BigInt)  
    List<BigInteger> li = deco.codifica(val);
    List<BigInteger> li2 = new ArrayList<>();
    // 2) list(BigInt) => RSA.E => list2(BigInt)
    for (BigInteger bi : li) {
      bi = rsa.esponenteE(bi);
      li2.add(bi);
    }
    // 3) list2(BigInt) => deco() => sz2 
    String sz2 = deco.decodi(li2);
    // 4) sz2 => Base64 
    sz2 = Base64.encodeBase64String(sz2.getBytes());
    // 5) chunk di 64 chars
    StringBuilder sb = new StringBuilder();
    for (int k = 0; k < sz2.length(); k += 64) {
      int fine = k + 64;
      if (fine > sz2.length())
        fine = sz2.length();
      sb.append(sz2.substring(k, fine)).append("\n");
    }
    txTxtEncoded.setText(sb.toString());
    decodifica(sb.toString());
  }

  private void decodifica(String string) {
    RsaObj rsa = MainFrame.getInst().getRsaObj();
    DeCodeString deco = new DeCodeString();
    deco.setMaxBits(rsa.getNPQmodulus());
    // 5) Chunk64 chars == Base64
    String sz = string.replace("\n", "");
    // 4) Base64 == sz2
    String sz2 = new String(Base64.decodeBase64(sz));
    // 3) sz2 => codi() => list2(BigInt)
    List<BigInteger> li2 = deco.codifica(sz2);
    List<BigInteger> li = new ArrayList<>();
    // 2) list2(BigInt) => RSA.D => list(BigInt)
    for (BigInteger bi : li2) {
      bi = rsa.esponenteE(bi);
      li.add(bi);
    }
    // 1) list(BigInt) => codi() => sz  
    String szOut = deco.decodi(li);
    System.out.println("Pan6TxtCoded.decodifica():" + szOut);
  }

}
