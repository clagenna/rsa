package sm.clagenna.crypt.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import lombok.Getter;
import sm.clagenna.crypt.primi.PrimiFactory;
import sm.clagenna.crypt.rsa.RsaObj;
import sm.clagenna.crypt.swing.IRsa;

public class MainFrame extends JFrame {

  /** long serialVersionUID */
  private static final long        serialVersionUID = 333609615894254079L;
  @Getter private static MainFrame inst;
  private JPanel                   contentPane;
  @Getter private IRsa             irsa;
  @Getter private RsaObj           rsaObj;
  @Getter private PrimiFactory     primi;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          try {
            // Set System L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          } catch (Exception e) {
            // handle exception
          }
          MainFrame frame = new MainFrame();
          frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  public MainFrame() {
    if (inst != null)
      throw new UnsupportedOperationException("MainFrame gia istanziata");
    inst = this;
    irsa = new Controllore();
    rsaObj = new RsaObj();
    primi = new PrimiFactory();
    initComponents();
  }

  private void initComponents() {
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setBounds(100, 100, 666, 602);

    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);

    JMenu mnFile = new JMenu("File");
    menuBar.add(mnFile);

    JMenuItem mnLeggi = new JMenuItem("Leggi");
    mnFile.add(mnLeggi);

    JMenuItem mnSalva = new JMenuItem("Salva");
    mnFile.add(mnSalva);

    JMenuItem mnExit = new JMenuItem("Esci");
    mnFile.add(mnExit);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    contentPane.setLayout(new BorderLayout(0, 0));

    JTabbedPane tabPane = new JTabbedPane();
    JPanel panDatiIniziali = new JPanel();
    tabPane.addTab("Dati Chiave", panDatiIniziali);
    GridBagLayout gbl_panDatiIniziali = new GridBagLayout();
    gbl_panDatiIniziali.columnWidths = new int[] { 0, 0 };
    gbl_panDatiIniziali.rowHeights = new int[] { 0, 0 };
    gbl_panDatiIniziali.columnWeights = new double[] { 1.0, 1.0 };
    gbl_panDatiIniziali.rowWeights = new double[] { 0.0, 0.0 };
    panDatiIniziali.setLayout(gbl_panDatiIniziali);

    JPanel panGenPrimi = new Pan1GenPrimi(irsa);
    GridBagConstraints gbc_panGenPrimi = new GridBagConstraints();
    gbc_panGenPrimi.insets = new Insets(0, 0, 5, 0);
    gbc_panGenPrimi.anchor = GridBagConstraints.NORTHWEST;
    gbc_panGenPrimi.fill = GridBagConstraints.BOTH;
    gbc_panGenPrimi.gridx = 0;
    gbc_panGenPrimi.gridy = 0;
    gbc_panGenPrimi.gridwidth = 2;
    panDatiIniziali.add(panGenPrimi, gbc_panGenPrimi);

    JPanel panValP = new Pan2ValP(irsa);
    GridBagConstraints gbc_ValP = new GridBagConstraints();
    gbc_ValP.insets = new Insets(0, 0, 5, 5);
    gbc_ValP.fill = GridBagConstraints.BOTH;
    gbc_ValP.gridx = 0;
    gbc_ValP.gridy = 1;
    gbc_ValP.gridwidth = 1;
    panDatiIniziali.add(panValP, gbc_ValP);

    JPanel panValQ = new Pan3ValQ(irsa);
    GridBagConstraints gbc_ValQ = new GridBagConstraints();
    gbc_ValQ.insets = new Insets(0, 0, 5, 0);
    gbc_ValQ.fill = GridBagConstraints.BOTH;
    gbc_ValQ.gridx = 1;
    gbc_ValQ.gridy = 1;
    gbc_ValQ.gridwidth = 1;
    panDatiIniziali.add(panValQ, gbc_ValQ);

    JPanel panCreaEeD = new Pan4CreaEeD(irsa);
    GridBagConstraints gbc_CreaEeD = new GridBagConstraints();
    gbc_CreaEeD.fill = GridBagConstraints.BOTH;
    gbc_CreaEeD.gridx = 0;
    gbc_CreaEeD.gridy = 2;
    gbc_CreaEeD.gridwidth = 2;
    panDatiIniziali.add(panCreaEeD, gbc_CreaEeD);

    JPanel panDatiTesto = new JPanel();
    tabPane.addTab("Testo", panDatiTesto);
    GridBagLayout gbl_panDatiTesto = new GridBagLayout();
    gbl_panDatiTesto.columnWidths = new int[] { 214, 0 };
    gbl_panDatiTesto.rowHeights = new int[] { 45, 0, 0, 0 };
    gbl_panDatiTesto.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
    gbl_panDatiTesto.rowWeights = new double[] { 1.0, 1.0, 1.0, Double.MIN_VALUE };
    panDatiTesto.setLayout(gbl_panDatiTesto);

    JPanel panTxtOrig = new Pan5TxtOrig(irsa);
    GridBagConstraints gbc_TxtOrig = new GridBagConstraints();
    gbc_TxtOrig.anchor = GridBagConstraints.NORTHWEST;
    gbc_TxtOrig.insets = new Insets(0, 0, 0, 5);
    gbc_TxtOrig.fill = GridBagConstraints.BOTH;
    gbc_TxtOrig.gridx = 0;
    gbc_TxtOrig.gridy = 0;
    GridBagConstraints gbc_panTxtOrig = new GridBagConstraints();
    gbc_panTxtOrig.fill = GridBagConstraints.BOTH;
    gbc_panTxtOrig.insets = new Insets(0, 0, 5, 0);
    gbc_panTxtOrig.gridy = 0;
    gbc_panTxtOrig.gridx = 0;
    panDatiTesto.add(panTxtOrig, gbc_panTxtOrig);

    JPanel panTxtCoded = new Pan6TxtCoded(irsa);
    GridBagConstraints gbc_TxtCoded = new GridBagConstraints();
    gbc_TxtCoded.anchor = GridBagConstraints.NORTHWEST;
    gbc_TxtCoded.insets = new Insets(0, 0, 0, 5);
    gbc_TxtCoded.fill = GridBagConstraints.BOTH;
    gbc_TxtCoded.gridx = 0;
    gbc_TxtCoded.gridy = 1;
    GridBagConstraints gbc_panTxtCoded = new GridBagConstraints();
    gbc_panTxtCoded.fill = GridBagConstraints.BOTH;
    gbc_panTxtCoded.insets = new Insets(0, 0, 5, 0);
    gbc_panTxtCoded.gridy = 1;
    gbc_panTxtCoded.gridx = 0;
    panDatiTesto.add(panTxtCoded, gbc_panTxtCoded);

    JPanel panTxtDecript = new Pan7TxtDecript(irsa);
    GridBagConstraints gbc_TxtDecript = new GridBagConstraints();
    gbc_TxtDecript.anchor = GridBagConstraints.NORTHWEST;
    gbc_TxtDecript.fill = GridBagConstraints.BOTH;
    gbc_TxtDecript.gridx = 0;
    gbc_TxtDecript.gridy = 2;
    GridBagConstraints gbc_panTxtDecript = new GridBagConstraints();
    gbc_panTxtDecript.fill = GridBagConstraints.BOTH;
    gbc_panTxtDecript.gridy = 2;
    gbc_panTxtDecript.gridx = 0;
    panDatiTesto.add(panTxtDecript, gbc_panTxtDecript);

    contentPane.add(tabPane, BorderLayout.CENTER);

  }

}