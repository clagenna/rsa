package sm.clagenna.crypt.util;

import java.text.SimpleDateFormat;

public class Utils {

  public final static char[]          hexArray                = "0123456789ABCDEF".toCharArray();

  public static final SimpleDateFormat s_fmtY4MDHMSm           = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
  public static final SimpleDateFormat s_fmtY4MDHMS            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  public static final SimpleDateFormat s_fmtY4MD               = new SimpleDateFormat("yyyy-MM-dd");
  public static final SimpleDateFormat s_fmtDMY4               = new SimpleDateFormat("dd/MM/yyyy");

}
