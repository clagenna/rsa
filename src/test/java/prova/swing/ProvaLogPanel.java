package prova.swing;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sm.clagenna.crypt.swing.log.EGravita;
import sm.clagenna.crypt.swing.log.MioLogger;
import sm.clagenna.crypt.swing.log.PanLogGrid;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Random;
import java.awt.event.ActionEvent;

public class ProvaLogPanel extends JFrame {

  private static final Logger s_log            = LogManager.getLogger(ProvaLogPanel.class);
  /** long serialVersionUID */
  private static final long   serialVersionUID = 4421062859118932553L;

  public ProvaLogPanel() {
    s_log.info("Istanzio prima MioLogger");
    new MioLogger();
    initComponents();
  }

  private void initComponents() {
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setBounds(100, 100, 450, 300);

    JPanel panSopra = new JPanel();

    PanLogGrid panSotto = new PanLogGrid();

    JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panSopra, panSotto);

    JButton btAddLog = new JButton("Add");
    btAddLog.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        btAddLog_click();
      }
    });
    panSopra.add(btAddLog);

    getContentPane().add(split);
  }

  protected void btAddLog_click() {
    MioLogger logr = MioLogger.getInst();
    Random rnd = new Random(new Date().getTime());
    int vv = rnd.nextInt(5) + 1;
    EGravita eg = EGravita.valueOf(vv);
    String sz = String.format("Prova messaggio gravita=%s", eg);
    logr.log(eg, sz);
  }

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
          ProvaLogPanel frame = new ProvaLogPanel();
          frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

}
