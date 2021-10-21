package sm.clagenna.crypt.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.commons.codec.binary.Base64;

import sm.clagenna.crypt.rsa.RsaObj;
import sm.clagenna.crypt.swing.DeCodeString;
import sm.clagenna.crypt.swing.IRsa;
import sm.clagenna.crypt.swing.IRsaListen;

public class Pan6TxtCoded extends JPanel implements IRsaListen {

	/** long serialVersionUID */
	private static final long serialVersionUID = 593767533148741772L;
	@SuppressWarnings("unused")
	private IRsa m_irsa;
	private JTextArea txTxtEncoded;
	private int maxBitsCry;
	private List<BigInteger> liUnoCripted;
	private List<BigInteger> liUnoTxt;
	private List<BigInteger> liDueCrypted;
	private List<BigInteger> liDueTxt;

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

		JButton btDecode = new JButton("Dec");
		GridBagConstraints gbc_btDecode = new GridBagConstraints();
		gbc_btDecode.gridx = 1;
		gbc_btDecode.gridy = 0;
		add(btDecode, gbc_btDecode);
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
		}
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

		liUnoCripted = new ArrayList<>();
		for (BigInteger bi : liUnoTxt) {
			bi = rsa.esponenteE(bi);
			liUnoCripted.add(bi);
		}
		OptionalInt opt = liUnoCripted.stream().mapToInt(BigInteger::bitLength).max();
		maxBitsCry = (opt.getAsInt() / shift + 1) * shift;
		deco.setMaxBits(maxBitsCry);
		System.out.println("Max bits crypt=" + maxBitsCry);

		// 3) list2(BigInt) => deco() => sz2
		String szUnoCrypted = deco.toString(liUnoCripted, true);

		// 4) sz2 => Base64
		String szUnoCryptedB64 = Base64.encodeBase64String(szUnoCrypted.getBytes());

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
		decodifica(sb.toString());
	}

	private void decodifica(String string) {
		RsaObj rsa = MainFrame.getInst().getRsaObj();
		DeCodeString deco = new DeCodeString();

		// 5) Chunk64 chars == Base64
		String szDueCryptedB64 = string.replace("\n", "");

		// 4) Base64 == sz2
		String szDueCrypted = new String(Base64.decodeBase64(szDueCryptedB64));

		// 3) sz2 => codi() => list2(BigInt)
		deco.setShift(8);
		// ??????? solo decode non va !!!!
		if (maxBitsCry != 0)
			deco.setMaxBits(maxBitsCry);
		liDueCrypted = deco.toList(szDueCrypted, true);

		liDueTxt = new ArrayList<>();
		for (BigInteger bi : liDueCrypted) {
			BigInteger bi2 = rsa.esponenteD(bi);
			liDueTxt.add(bi2);
		}

		// 1) list(BigInt) => codi() => sz
		deco.setShift(8);
		String szOut = deco.toString(liDueTxt, false);
		System.out.println("Pan6TxtCoded.decodifica():" + szOut);
		Controllore.getInst().setValue(Controllore.FLD_TXT_DECODED, szOut);
	}

}
