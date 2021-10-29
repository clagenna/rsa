package sm.clagenna.crypt.swing.log;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import sm.clagenna.crypt.util.Utils;

public class Messaggio {
  @Getter @Setter
  private Date     momento;
  @Getter @Setter
  private EGravita gravita;
  @Getter @Setter
  private String   msg;

  public Messaggio(String p_msg) {
    momento = new Date();
    gravita = EGravita.Log;
    msg = p_msg;
  }

  public Messaggio(EGravita g, String p_msg) {
    momento = new Date();
    gravita = g;
    msg = p_msg;
  }

  public Messaggio(Date p_dt, EGravita p_g, String p_msg) {
    momento = p_dt;
    gravita = p_g;
    msg = p_msg;
  }

  @Override
  public String toString() {
    return Utils.s_fmtY4MDHMS.format(momento) + "[" + String.valueOf(getGravita()) + "]\t" + msg;
  }

}
