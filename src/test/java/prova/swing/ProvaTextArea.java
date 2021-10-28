package prova.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class ProvaTextArea {
  private static final int    MAX                     = 10;
  private static final long   SLEEP_TIME              = 500;
  private static final String FREEZE_EDT              = "Freeze EDT";
  private static final String THAWED_EDT              = "Thawed EDT";
  private JPanel              mainPanel               = new JPanel();
  private JTextArea           textarea                = new JTextArea(25, 30);
  private List<JComponent>    componentSetEnabledList = new ArrayList<>();

  public ProvaTextArea() {
    textarea.setEditable(false);
    JButton freezeEdtBtn = new JButton(ProvaTextArea.FREEZE_EDT);
    JButton thawedEdtBtn = new JButton(ProvaTextArea.THAWED_EDT);

    BtnListener btnListener = new BtnListener();
    freezeEdtBtn.addActionListener(btnListener);
    thawedEdtBtn.addActionListener(btnListener);

    JPanel btnPanel = new JPanel(new GridLayout(1, 0, 5, 0));
    btnPanel.add(freezeEdtBtn);
    btnPanel.add(thawedEdtBtn);

    mainPanel.setLayout(new BorderLayout(5, 5));
    mainPanel.add(new JScrollPane(textarea), BorderLayout.CENTER);
    mainPanel.add(btnPanel, BorderLayout.SOUTH);

    componentSetEnabledList.add(freezeEdtBtn);
    componentSetEnabledList.add(thawedEdtBtn);
  }

  public JComponent getComponent() {
    return mainPanel;
  }

  private class BtnListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      String command = e.getActionCommand();
      if (command.equals(ProvaTextArea.FREEZE_EDT)) {
        doFreezeEdt();
      } else if (command.equals(ProvaTextArea.THAWED_EDT)) {
        doThawedEdt();
      }
    }

    private void doFreezeEdt() {
      clearTextField();
      for (int i = 0; i < ProvaTextArea.MAX; i++) {
        textarea.append("Freeze:" + String.valueOf(i + 1) + "\n");
        try {
          Thread.sleep(ProvaTextArea.SLEEP_TIME);
        } catch (InterruptedException e1) {
        }
      }
      textarea.append("Done!\n");
    }

    private void doThawedEdt() {
      for (JComponent jc : componentSetEnabledList)
        jc.setEnabled(false);
      clearTextField();
      new SwingWorker<Void, String>() {

        @Override
        protected Void doInBackground() throws Exception {
          for (int i = 0; i < ProvaTextArea.MAX; i++) {
            publish("Thaw:" + String.valueOf(i + 1));
            Thread.sleep(ProvaTextArea.SLEEP_TIME);
          }
          return null;
        }

        @Override
        protected void process(List<String> chunks) {
          for (String chunk : chunks) {
            System.out.println("ProvaTextArea.BtnListener.process():chunk=" + chunk);
            textarea.append(chunk + "\n");
          }
        }

        @Override
        protected void done() {
          for (JComponent jc : componentSetEnabledList) {
            jc.setEnabled(true);
          }
          textarea.append("DONE! \n");
        }

      }.execute();
    }

    private void clearTextField() {
      Document doc = textarea.getDocument();
      try {
        doc.remove(0, doc.getLength());
      } catch (BadLocationException e) {
        e.printStackTrace();
      }
    }
  }

  private static void createAndShowUI() {
    JFrame frame = new JFrame("FreezeEDT");
    frame.getContentPane().add(new ProvaTextArea().getComponent());
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        createAndShowUI();
      }
    });
  }
}
