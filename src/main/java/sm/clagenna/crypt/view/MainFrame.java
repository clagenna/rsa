package sm.clagenna.crypt.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.Beans;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import sm.clagenna.crypt.primi.PrimiFactory;
import sm.clagenna.crypt.rsa.RsaObj;
import sm.clagenna.crypt.swing.IRsa;
import sm.clagenna.crypt.swing.IRsaListen;
import sm.clagenna.crypt.swing.log.MioLogger;
import sm.clagenna.crypt.swing.log.PanLogGrid;
import sm.clagenna.crypt.util.AppProperties;
import sm.clagenna.crypt.util.KeyPrivFile;
import sm.clagenna.crypt.util.KeyPubFile;

public class MainFrame extends JFrame implements WindowListener, IRsaListen, PropertyChangeListener {

  /** long serialVersionUID */
  private static final long   serialVersionUID = 333609615894254079L;

  private static final Logger s_log            = LogManager.getLogger(MainFrame.class);
  @Getter
  private static MainFrame    inst;
  private JPanel              panRSA;
  @Getter
  private IRsa                irsa;
  @Getter
  private RsaObj              rsaObj;
  @Getter
  private PrimiFactory        primi;
  private AppProperties       m_appProps;
  private JCheckBox           chkDebug;
  private boolean             bSema;

  private MioLogger           logger;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    s_log.debug("file.encoding={}", System.getProperty("file.encoding"));
    System.setProperty("file.encoding", "UTF-8");
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
    if (Beans.isDesignTime())
      return;
    if (inst != null)
      throw new UnsupportedOperationException("MainFrame gia istanziata");
    inst = this;
    irsa = new Controllore();
    irsa.addListener(this);
    rsaObj = new RsaObj();
    primi = new PrimiFactory();
    addWindowListener(this);
    logger = new MioLogger();
    logger.addLogListener(this);
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
    mnLeggi.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mnuLeggi_Click();
      }
    });

    // JMenuItem mnSalva = new JMenuItem("Salva");
    // mnFile.add(mnSalva);
    // mnSalva.addActionListener(new ActionListener() {
    // @Override
    // public void actionPerformed(ActionEvent e) {
    // mnuSalva_Click();
    // }
    // });
    mnFile.addSeparator();

    JMenuItem mnExit = new JMenuItem("Esci");
    mnFile.add(mnExit);
    mnExit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mnuExit_Click();
      }
    });

    panRSA = new JPanel();
    panRSA.setBorder(new EmptyBorder(5, 5, 5, 5));
    panRSA.setLayout(new BorderLayout(0, 0));

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

    chkDebug = new JCheckBox("Debug");
    chkDebug.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        chkDebug_click(e);
      }
    });
    panGenPrimi.add(chkDebug, BorderLayout.WEST);

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
    gbc_CreaEeD.gridwidth = 1;
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

    panRSA.add(tabPane, BorderLayout.CENTER);

    //JTextArea txLog = new JTextArea();
    //    JScrollPane scrlLog = new JScrollPane(txLog, //
    //        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    PanLogGrid scrlLog = new PanLogGrid();

    JPanel contentPaneOk = new JPanel(new BorderLayout(0, 0));
    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panRSA, scrlLog);
    splitPane.setOneTouchExpandable(true);
    splitPane.setDividerLocation(600);

    contentPaneOk.add(splitPane, BorderLayout.CENTER);
    setContentPane(contentPaneOk);

    setContentPane(contentPaneOk);
  }

  protected void chkDebug_click(ActionEvent e) {
    JCheckBox chk = (JCheckBox) e.getSource();
    Boolean b = chk.isSelected();
    try {
      bSema = true;
      Controllore.getInst().setValue(Controllore.FLD_DEBUG, b);
      AppProperties.getInst().setDebug(b);
    } finally {
      bSema = false;
    }
  }

  private void mnuLeggi_Click() {
    AppProperties props = AppProperties.getInst();
    JFileChooser fch = creaFileChooser();
    fch.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int nRes = fch.showOpenDialog(this);
    if (nRes != JFileChooser.APPROVE_OPTION) {
      s_log.warn("Nessun file scelto !");
      return;
    }
    File selFi = fch.getSelectedFile();
    props.setLastFile(selFi.getAbsolutePath());
    props.setLastDir(selFi.getParent());
    KeyPubFile kfi = new KeyPubFile();
    if (selFi.getName().toLowerCase().endsWith("priv.txt"))
      kfi = new KeyPrivFile();
    kfi.setFileKey(selFi);
    kfi.leggiFile();
  }

  public JFileChooser creaFileChooser() {
    JFileChooser fch = new JFileChooser();
    AppProperties props = AppProperties.getInst();
    String lastDir = System.getProperty("user.dir");

    String szd = props.getLastDir();
    if (szd != null)
      lastDir = szd;
    fch.setCurrentDirectory(new File(lastDir));
    return fch;
  }

  // protected void mnuSalva_Click() {
  // RsaObj rsa = MainFrame.getInst().getRsaObj();
  // String keyNam = rsa.getKeyName();
  // if (keyNam == null) {
  // JOptionPane.showConfirmDialog(this, "Manca il nome del Key file", "Fornire il
  // nome", JOptionPane.OK_OPTION);
  // return;
  // }
  //
  // JFileChooser fch = new JFileChooser();
  // AppProperties props = AppProperties.getInst();
  // String lastDir = System.getProperty("user.dir");
  //
  // String szd = props.getLastDir();
  // if (szd != null)
  // lastDir = szd;
  // fch.setCurrentDirectory(new File(lastDir));
  // fch.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
  //
  // int nRes = fch.showOpenDialog(this);
  // if (nRes != JFileChooser.APPROVE_OPTION) {
  // System.out.println("Nessun direttorio scelto !");
  // return;
  // }
  // File selFi = fch.getSelectedFile();
  // props.setLastDir(selFi.getAbsolutePath());
  // }

  protected void mnuExit_Click() {
    if (m_appProps == null)
      m_appProps = new AppProperties(this);
    m_appProps.salvaProperties();
    this.dispose();
  }

  @Override
  public void windowOpened(WindowEvent e) {
    // System.out.println("MainFrame.windowOpened() : user.home=" +
    s_log.debug("Window Open, user.home={}", System.getProperty("user.home"));
    if (m_appProps == null)
      m_appProps = new AppProperties(this);
    m_appProps.leggiProperties();
  }

  @Override
  public void windowClosing(WindowEvent e) {
    s_log.info("Window closing");
    if (m_appProps == null)
      m_appProps = new AppProperties(this);
    m_appProps.salvaProperties();
  }

  @Override
  public void windowClosed(WindowEvent e) {
    s_log.info("MainFrame.windowClosed()");
  }

  @Override
  public void windowIconified(WindowEvent e) {

  }

  @Override
  public void windowDeiconified(WindowEvent e) {

  }

  @Override
  public void windowActivated(WindowEvent e) {

  }

  @Override
  public void windowDeactivated(WindowEvent e) {

  }

  @Override
  public void valueChanged(String id, Object val) {
    if (bSema)
      return;
    switch (id) {
      case Controllore.FLD_KEYNAME:
        String pup = "*no key*";
        if (rsaObj.isPrivKeyPresent())
          pup = "Pub/Priv";
        else if (rsaObj.isPubKeyPresent())
          pup = "Pub";
        String tit = String.format("Key %s (%s)", val, pup);
        this.setTitle(tit);
        break;
      case Controllore.FLD_DEBUG:
        chkDebug.setSelected((Boolean) val);
        break;
    }
  }

  /**
   * Propety change listener per reagire sugli eventi di {@link MioLogger}
   *
   * @param evt
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    System.out.println("MainFrame.propertyChange():" + evt.toString());
  }

}
