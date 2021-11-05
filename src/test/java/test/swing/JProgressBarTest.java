package test.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class JProgressBarTest extends JFrame {

  /** long serialVersionUID */
  private static final long serialVersionUID = 2849293525148434984L;

  private static final long LOOP_LENGTH      = 85000000;

  private JPanel            contentPane;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
      e1.printStackTrace();
    }
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          JProgressBarTest frame = new JProgressBarTest();
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
  public JProgressBarTest() {
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setBounds(100, 100, 450, 300);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setLayout(new BorderLayout(0, 0));
    JProgressBar progress = new JProgressBar();
    progress.setStringPainted(true);
    contentPane.add(new JLabel("Loop progress is: "), BorderLayout.NORTH);
    contentPane.add(progress, BorderLayout.SOUTH);
    setContentPane(contentPane);
    ProgressWorker worker = new ProgressWorker(progress);
    worker.execute();
  }

  private static class ProgressWorker extends SwingWorker<Void, Integer> {
    private final JProgressBar progress;

    public ProgressWorker(JProgressBar progress) {
      this.progress = progress;
    }

    @Override
    protected Void doInBackground() throws Exception {
      for (long i = LOOP_LENGTH; i > 0; i--) {
        final int progr = (int) ( (100L * (LOOP_LENGTH - i)) / LOOP_LENGTH);
        publish(progr);
      }
      return null;
    }

    @Override
    protected void process(List<Integer> chunks) {
      progress.setValue(chunks.get(chunks.size() - 1));
      super.process(chunks);
    }

    @Override
    protected void done() {
      progress.setValue(100);
    }
  }
}
