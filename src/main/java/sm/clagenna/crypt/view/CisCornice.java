package sm.clagenna.crypt.view;

public interface CisCornice {
  public String m_szRes="Resources/";
  // Per salvataggio posizione nel file .ini
  /** posizione x angolo alto sx della frame */
  public static final String FRAME_POSX = "px";
  /** posizione y angolo alto sx della frame */
  public static final String FRAME_POSY = "py";
  /** dimensione x della frame */
  public static final String FRAME_CX = "cx";
  /** dimensione y della frame */
  public static final String FRAME_CY = "cy";
  /** tab attiva della frame */
  public static final String PROP_ACTIVETAB = "nTab";
  /** posizione dello splitter fra dati e conn */
  public static final String PROP_SPLITDATI = "dati";

}
