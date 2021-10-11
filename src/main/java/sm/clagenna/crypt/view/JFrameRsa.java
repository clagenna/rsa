package sm.clagenna.crypt.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import sm.clagenna.crypt.primi.PrimiFactory2;

public class JFrameRsa extends JFrame {

  /** long serialVersionUID */
  private static final long serialVersionUID = -7956357454491150430L;
  private PanChoiceX        panChoice;

  public JFrameRsa() {
    initComponents();
    miaInit();
  }

  private void initComponents() {
    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    panChoice = new PanChoiceX();
    getContentPane().add(panChoice, BorderLayout.NORTH);

    JTextArea textArea = new JTextArea();
    getContentPane().add(textArea, BorderLayout.CENTER);

  }

  private void miaInit() {
    new PrimiFactory2();
    panChoice.miaInit();
  }

  public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        JFrameRsa frame = new JFrameRsa();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
      }
    });
  }

}
