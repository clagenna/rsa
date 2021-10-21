package prova.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class ProvaApriFile extends JFrame {

  /** long serialVersionUID */
  private static final long serialVersionUID = -7425397283326248313L;
  private JPanel            contentPane;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    // Set System L&F
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
      e1.printStackTrace();
    }

    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          ProvaApriFile frame = new ProvaApriFile();
          frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the frame.
   */
  public ProvaApriFile() {
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setBounds(100, 100, 450, 300);

    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);

    JMenu mnNewMenu = new JMenu("File");
    menuBar.add(mnNewMenu);

    JMenuItem mnuOpenPubKey = new JMenuItem("Open Pub Key");
    mnuOpenPubKey.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mnuOpPubKey();
      }
    });
    mnNewMenu.add(mnuOpenPubKey);

    JMenuItem mnuOpenPrivKey = new JMenuItem("Open Priv Key");
    mnNewMenu.add(mnuOpenPrivKey);
    mnuOpenPrivKey.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mnuOpPrivKey();
      }
    });

    JMenuItem mnuSavePubKey = new JMenuItem("Save Pub Key");
    mnNewMenu.add(mnuSavePubKey);
    mnuSavePubKey.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mnuSavPubKey();
      }
    });

    JMenuItem mnuSavePrivKey = new JMenuItem("Save Priv Key");
    mnNewMenu.add(mnuSavePrivKey);
    mnuSavePrivKey.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mnuSavPrivKey();
      }
    });

    mnNewMenu.addSeparator();
    JMenuItem mnuExit = new JMenuItem("Exit");
    mnuExit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mnuExit();
      }
    });
    mnNewMenu.add(mnuExit);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setLayout(new BorderLayout(0, 0));
    setContentPane(contentPane);
  }

  protected void mnuOpPubKey() {

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int result = fileChooser.showOpenDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      System.out.println("Selected file: " + selectedFile.getAbsolutePath());
    }
  }

  protected void mnuOpPrivKey() {
    System.out.println("ProvaApriFile.mnuOpPrivKey()");
  }

  protected void mnuSavPubKey() {
    System.out.println("ProvaApriFile.mnuSavPubKey()");
  }

  protected void mnuSavPrivKey() {
    System.out.println("ProvaApriFile.mnuSavPrivKey()");
  }

  protected void mnuExit() {
    this.dispose();
    System.exit(0);
  }

}
