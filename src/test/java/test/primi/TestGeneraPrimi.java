package test.primi;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import sm.clagenna.crypt.primi.PrimiFactory;
import sm.clagenna.crypt.util.PrimiWorker;

public class TestGeneraPrimi extends JPanel  implements ActionListener, PropertyChangeListener {

  /** long serialVersionUID */
  private static final long serialVersionUID = -9191558190110822394L;
  private JButton           startButton;
  private JProgressBar      progressBar;
  private PrimiWorker       primiGen;

  public TestGeneraPrimi() {
    super(new BorderLayout());
    initComponents();
    new PrimiFactory();
  }

  private void initComponents() {

    startButton = new JButton("Start");
    startButton.setActionCommand("start");
    startButton.addActionListener(this);

    progressBar = new JProgressBar(0, 100);
    progressBar.setValue(0);
    progressBar.setStringPainted(true);

    add(startButton, BorderLayout.NORTH);
    add(progressBar, BorderLayout.SOUTH);

  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    System.out.printf("propertyChange(%s)\n", evt.getPropertyName());
    if ( !"progress".equals(evt.getPropertyName()))
      return;
    int prg = (Integer) evt.getNewValue();
    if (prg >= 99) {
      startButton.setEnabled(true);
      prg = 100;
    }
    progressBar.setValue(prg);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    startButton.setEnabled(false);
    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    primiGen = new PrimiWorker();
    primiGen.addPropertyChangeListener(this);
    primiGen.execute();

  }

  public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        JFrame frame = new JFrame("Test Genera Primi");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new TestGeneraPrimi();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);

      }
    });

  }

}
