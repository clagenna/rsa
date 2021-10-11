package sm.clagenna.crypt.gcd;

import java.math.BigInteger;
import java.text.NumberFormat;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import lombok.Getter;

public class GcdMain {
  private static final String PRIME1 = "prime1";
  private static final String PRIME2 = "prime2";
  private static final String DEBUG2 = "debug";
  private static final String P2     = "p";
  private static final String Q2     = "q";
  private static final String D2     = "d";
  private static final String GCD    = "gcd";
  private static final String LCM    = "lcm";

  private static GcdMain      s_inst;

  @Getter private Options     opts;

  @Getter private BigInteger  p;
  @Getter private BigInteger  q;
  private CommandLine         cmd;
  private String              tipo;

  public GcdMain() {
    if (s_inst != null)
      throw new UnsupportedOperationException("GcdMain gia' istanziata !");
    s_inst = this;
  }

  public static GcdMain getInst() {
    return s_inst;
  }

  public static void main(String[] args) {
    GcdMain gcd = new GcdMain();
    gcd.creaOptions();
    gcd.parseOptions(args);
    gcd.doTheJob();

  }

  private void doTheJob() {
    if (cmd.hasOption(DEBUG2))
      Gcd.setDebug(true);
    GcdRec res = null;
    NumberFormat fmt = NumberFormat.getIntegerInstance();
    if (tipo.equals(GCD)) {
      res = Gcd.gcd(p, q);
      BigInteger res2 = p.multiply(res.x()).add(q.multiply(res.y()));
      System.out.printf("gcd(%s, %s)=%s\tx=%s y=%s\tBezout=%s\n", //
          fmt.format(p), //
          fmt.format(q), //
          fmt.format(res.resto()), //
          fmt.format(res.x()), //
          fmt.format(res.y()), //
          fmt.format(res2));
    } else {
      BigInteger ii = Gcd.lcm(p, q);
      System.out.printf("Min. Com. Mul (%s,%s)=%s\n", //
          fmt.format(p), //
          fmt.format(q), //
          fmt.format(ii) //
      );
    }

    //    res = Gcd.gcdnor(p, q);
    //    res2 = p.multiply(res.x()).add(q.multiply(res.y()));
    //    System.out.printf("gcdNoRecurse(%s, %s)=%s\tx=%s y=%s\tBezout=%s\n", //
    //        fmt.format(p), //
    //        fmt.format(q), //
    //        fmt.format(res.resto()), //
    //        fmt.format(res.x()), //
    //        fmt.format(res.y()), //
    //        fmt.format(res2));

  }

  private void creaOptions() {
    opts = new Options();

    // --prime1
    Option opt = Option.builder(P2) //
        .longOpt(PRIME1) //
        .hasArg() //
        .required(true) //
        .desc("Il primo numero primo (p)") //
        .build();
    opts.addOption(opt);

    // --prime2
    opt = Option.builder(Q2) //
        .longOpt(PRIME2) //
        .hasArg() //
        .required(true) //
        .desc("Il secondo numero primo (q)") //
        .build();
    opts.addOption(opt);

    // -mcm
    opt = Option.builder(GCD) //

        .desc("Massimo Comun Divisore") //
        .build();
    opts.addOption(opt);

    // -mcm
    opt = Option.builder(LCM) //

        .desc("Minimo Comune multiplo") //
        .build();
    opts.addOption(opt);

    // --debug
    opt = Option.builder(D2) //
        .longOpt(DEBUG2) //
        .desc("modalita debug") //
        .build();
    opts.addOption(opt);

  }

  private void parseOptions(String[] args) {
    CommandLineParser parse = new DefaultParser();
    HelpFormatter helper = new HelpFormatter();
    tipo = GCD;
    p = null;
    q = null;

    try {
      cmd = parse.parse(opts, args);
      if (cmd.hasOption(P2)) {
        String szP = cmd.getOptionValue(PRIME1);
        szP = szP.replace(".", "");
        szP = szP.replace("_", "");
        p = new BigInteger(szP);
      }
      if (cmd.hasOption(Q2)) {
        String szQ = cmd.getOptionValue(PRIME2);
        szQ = szQ.replace(".", "");
        szQ = szQ.replace("_", "");
        q = new BigInteger(szQ);
      }
      if (cmd.hasOption(GCD))
        tipo = GCD;
      if (cmd.hasOption(LCM))
        tipo = LCM;
    } catch (NumberFormatException | ParseException e) {
      System.out.println("Errore:" + e.getMessage());
    }
    if (p == null || q == null) {
      helper.printHelp("Usage:", opts);
      System.exit(0);
    }

  }
}
