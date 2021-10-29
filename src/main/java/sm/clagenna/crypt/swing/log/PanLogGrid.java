package sm.clagenna.crypt.swing.log;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.beans.Beans;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import sm.clagenna.crypt.util.Versione;

public class PanLogGrid extends JPanel implements PropertyChangeListener {
  /** long serialVersionUID */
  private static final long   serialVersionUID = -6080168612645203284L;

  private JTable              m_table;
  private TableColumnAdjuster m_tbadj;
  private JPanel              m_panButton;

  private JComboBox<EGravita> m_cbLogLevel;

  public PanLogGrid() {
    initComps();
    if (Beans.isDesignTime())
      return;
    MioLogger.getInst().addLogListener(this);
  }

  private void initComps() {
    setLayout(new BorderLayout(10, 10));

    DefaultTableModel model = new RecordSetTable();

    m_table = new JTable(model);
    m_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    m_table.getColumnModel().getColumn(0).setCellRenderer(new MioJTableCellRenderer());
    m_table.getColumnModel().getColumn(1).setCellRenderer(new MioJTableCellRenderer());
    m_table.getColumnModel().getColumn(2).setCellRenderer(new MioJTableCellRenderer());

    m_tbadj = new TableColumnAdjuster(m_table);
    addPropertyChangeListener(m_tbadj);
    model.addTableModelListener(m_tbadj);
    //        break;
    //    }

    JScrollPane m_scrollTable = new JScrollPane(m_table);
    m_scrollTable.setViewportBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
    add(m_scrollTable, BorderLayout.CENTER);

    m_panButton = new JPanel(new FlowLayout());
    add(m_panButton, BorderLayout.SOUTH);
    aggiungiBottoni();

    addData();
  }

  private void aggiungiBottoni() {

    JLabel lbVers = new JLabel(Versione.getVersion());
    lbVers.setToolTipText(Versione.getVersionEx());
    Font lbFont = lbVers.getFont();
    int fSiz = (int) (lbFont.getSize() * 0.7);
    lbVers.setFont(new Font(lbFont.getName(), Font.PLAIN, fSiz));
    m_panButton.add(lbVers);

    JButton btnSave = new JButton("Salva");
    btnSave.addActionListener(e -> btSave_Click());
    btnSave.setToolTipText("Salva il contenuto del log su file");
    m_panButton.add(btnSave);

    JButton btnClear = new JButton("Clear");
    btnClear.addActionListener(actevt -> btnClear_click());
    btnClear.setToolTipText("Cancella il contenuto dei logs");
    m_panButton.add(btnClear);

    JButton btnCopy = new JButton("Copy");
    btnCopy.addActionListener(actevt -> btnCopy_click());
    btnCopy.setToolTipText("Copia il contenuto dei logs negli appunti");
    m_panButton.add(btnCopy);

    m_cbLogLevel = new JComboBox<>();
    m_cbLogLevel.addActionListener(propevt -> {
      try {
        cbLogLevel_Click((EGravita) m_cbLogLevel.getSelectedItem());
      } finally {
        setCursor(Cursor.getDefaultCursor());
      }
    });

    m_cbLogLevel.setModel(new DefaultComboBoxModel<EGravita>(EGravita.values()));
    m_cbLogLevel.setToolTipText("Imposta il livello di log da vedere");
    m_cbLogLevel.setSelectedItem(MioLogger.getInst().getMinGravita());
    m_panButton.add(m_cbLogLevel);

  }

  protected void cbLogLevel_Click(EGravita p_eGravita) {
    MioLogger log = MioLogger.getInst();
    //System.out.println("PanLogGrid.cbLogLevel_Click(), log=" + p_eGravita.toString());
    if (p_eGravita == log.getMinGravita())
      return;
    reset(p_eGravita);
    log.setMinGravita(p_eGravita);
  }

  protected void btnCopy_click() {
    try {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      MioLogger.getInst().copyLogs();
      JOptionPane.showMessageDialog(this, "Messaggi copiati negli appunti");
    } finally {
      this.setCursor(Cursor.getDefaultCursor());
    }
  }

  protected void btnClear_click() {
    try {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      MioLogger.getInst().clear();
      RecordSetTable lm = (RecordSetTable) m_table.getModel();
      lm.clear();
    } finally {
      this.setCursor(Cursor.getDefaultCursor());
    }
  }

  protected void btSave_Click() {
    try {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      MioLogger.getInst().saveLogs();
    } finally {
      this.setCursor(Cursor.getDefaultCursor());
    }
  }

  public void reset(EGravita p) {
    MioLogger ll = MioLogger.getInst();
    //System.out.println("PanLogGrid.reset() log=" + p.toString());
    List<Messaggio> vec = ll.getMessages(p);
    RecordSetTable lm = (RecordSetTable) m_table.getModel();
    m_cbLogLevel.setSelectedItem(p);
    lm.clear();
    for (Messaggio me : vec) {
      if (me.getGravita().ge(p))
        lm.addLog(me);
    }
  }

  private void addData() {
    addLog(new Date(), EGravita.Log, "Prima Riga di log");
  }

  public void addLog(String p_tx) {
    addLog(new Date(), EGravita.Log, p_tx);
  }

  public void addLog(EGravita p_sev, String p_tx) {
    addLog(new Date(), p_sev, p_tx);
  }

  public void addLog(Date p_dt, EGravita p_sev, String p_tx) {
    RecordSetTable mod = (RecordSetTable) m_table.getModel();
    LogTableRecord log = new LogTableRecord(p_dt, p_sev, p_tx);
    mod.addLog(log);
  }

  public void adjustColumn() {
    m_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    for (int column = 0; column < 2 /* m_table.getColumnCount() */; column++) {
      TableColumn tableColumn = m_table.getColumnModel().getColumn(column);
      int preferredWidth = tableColumn.getMinWidth();
      int maxWidth = tableColumn.getMaxWidth();
      for (int row = 0; row < m_table.getRowCount(); row++) {
        TableCellRenderer cellRenderer = m_table.getCellRenderer(row, column);
        Component compon = m_table.prepareRenderer(cellRenderer, row, column);
        int width = compon.getPreferredSize().width + m_table.getIntercellSpacing().width;
        preferredWidth = Math.max(preferredWidth, width);
        if (preferredWidth >= maxWidth) {
          preferredWidth = maxWidth;
          break;
        }
      }
      tableColumn.setPreferredWidth(preferredWidth);
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    MioLogger logger = MioLogger.getInst();
    RecordSetTable mod = (RecordSetTable) m_table.getModel();
    if (logger.isEmpty())
      mod.clear();
    else {
      mod.addLog(logger.getLastMessaggio());
    }
    m_table.updateUI();
  }

  @Override
  public void setCursor(Cursor p_cursor) {
    Object obj = this;
    while ( ! (obj instanceof Frame)) {
      if ( ((Component) obj).getParent() == null)
        break;
      obj = ((Component) obj).getParent();
    }

    if (obj != null) {
      if (obj != this)
        ((Component) obj).setCursor(p_cursor);
      else
        super.setCursor(p_cursor);
    }

  }

}
