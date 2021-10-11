package test.rsa;

import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;

import org.junit.Test;

import sm.clagenna.crypt.primi.PrimiFactory;
import sm.clagenna.crypt.rsa.CodeFrase;
import sm.clagenna.crypt.rsa.RsaObj;


public class TestCodifica {
  static final String riga   = "------------------------------------------------------";

  static final String FRASE  = "Lascia lente le briglie del tuo ippogrifo, o Astolfo,\n"
                                 + "e sfrena il tuo volo dove più ferve l'opera dell'uomo.\n"
                                 + "Però non ingannarmi con false immagini" + "ma lascia che io veda la verità\n"
                                 + "e possa poi toccare il giusto.\n" + "Da qui, messere, si domina la valle"
                                 + " ciò che si vede, è.\n" + "Ma se l'imago è scarna al vostro occhio"
                                 + " scendiamo a rimirarla da più in basso\n" + "e planeremo in un galoppo alato"
                                 + " entro il cratere ove gorgoglia il tempo.";

  static final String FRASE2 = "  Kh,::o 55i*o 5xItLoMVWA=2B2CoOjo,PB:JBoi\\2B'w5X2GKfV3zBp"
                                 + "PIaBAvmXD2CoZOvmQosmn;u -=7aSCx;,2bI?\\-Cua=2BzobZLx3-ldVt2O.F6x_eiyLyhjw"
                                 + "ODKF6x3C-i\\nVG=4qE:8mBH'Sdgl1,OH+*c+WcG/V0Hpr_Pt*4XBQJHemvjOHemg;B+O7ND?"
                                 + "mXD=FAG1Iw8-Z6SG9e/brdaFZqipE1oS3'-Z AXr1*zcE'J NdLw+ 5P=oS3M-rpE1O7jhpQ6"
                                 + "bi2bIqilFF5k--F* F_I;z.lN-jA'A+fj-G_z?nb71mbnrHD2yd.= b-Hpr/V0-=76b;d9_V:"
                                 + "MBQJ\\D0fU/jh;Ojof06aQ:\\t9,PBlT6?iB';/jA'mXDuB4/bd*3;eZf/Gyc+R*lyg_?Tx;q" + "jF:ac";

  long[]              arrl   = { 5614310, 5217813, 5716712, 10210381, 5716712, 9827902, 4489061, 8968590, 2752923, 6950921,
      7604168, 10583931, 5397038, 73003, 1898550, 7870768, 6311261, 8283547, 7552436, 8919650, 5244234, 6818203, 6950921, 9518865,
      7885697, 9662400, 3236763, 4604700, 6899195, 66718, 7757183, 4999935, 1693052, 2752923, 1202850, 9087783, 4584539, 9603185,
      6616175, 7956085, 3723913, 3691175, 506203, 4190529, 7956085, 9006636, 2134612, 4256340, 7125572, 9252828, 2729462 };

  long[]              arrl2  = { 5244234, 6899195 };

  long[]              arrl3  = { 1527765, 1321683, 402521, 1724885, 2106091, 5002530, 327075, 1725866, 7543855, 374800 };

  public void testCodice() {
    RsaObj rsa = creaRSA();
    List<Long> vec = new ArrayList<Long>();
    for (long ll : arrl2) {
      vec.add(ll);
    }
    CodeFrase cf = new CodeFrase(rsa.getPQ());
    String sz = cf.decode(vec);
    List<Long> vec2 = cf.encode(sz);
    confronta(vec, vec2, "dopo encode(string)");
  }

  public void testCodifica() {
    RsaObj rsa = creaRSA();
    CodeFrase cf = new CodeFrase(rsa.getPQ());
    String sz2 = null;

    // ------------------------
    List<Long> vec = cf.encode(FRASE);
    sz2 = cf.decode(vec);
    sout(FRASE);
    sout(sz2);
    // ------------------------
    List<Long> vec2 = cf.encode(sz2);
    confronta(vec, vec2, "dopo encode(string)");
  }

  @Test
  public void testCodificaRSA()  {
    RsaObj rsa = creaRSA();
    sout(FRASE);

    // Codifica in chiaro
    CodeFrase cf = new CodeFrase(rsa.getPQ());
    List<Long> vecC = cf.encode(FRASE);
    // codifica RSA
    List<Long> vecCK = null;
    List<Long> vecCKSK = null;
    
    vecCK = rsa.encode(vecC);
    // codifica criptata
    String szCKS = cf.encode(vecCK);
    sout(szCKS);

    // da kript String a Crypt List
    vecCKSK = cf.decode(szCKS);
    // da Crypt_String a Crypt_vec
    confronta(vecCK, vecCKSK, "da K-s-K");

    // da Crypt_vec a Clear_Vec
    List<Long> vecCKSKC = rsa.decode(vecCK);
    confronta(vecC, vecCKSKC,  "da C K C");

    vecCKSKC = rsa.decode(vecCKSK);
    confronta(vecC, vecCKSKC,  "da C K-s-K C");

    // da clear_vec a String
    String sz4 = cf.decode(vecCKSKC);
    sout(sz4);
  }

  public void testSingolaCodificaRSA() {
    RsaObj rsa = creaRSA();
    CodeFrase cf = new CodeFrase(rsa.getPQ());
    List<Long> vecC = cf.encode(FRASE);
    int k = 0;
    for (Long ll : vecC) {
      BigInteger bi = BigInteger.valueOf(ll);
      BigInteger bi1 = rsa.esponenteE(bi);
      BigInteger bi2 = rsa.esponenteD(bi1);
      if ( !bi.equals(bi2))
        System.out.println("k:" + k + " Cod1=" + bi + "\tcod2=" + bi2 + "\tPQ=" + rsa.getPQ());
      k++;
    }
  }

  private void confronta(List<Long> vec, List<Long> vec2, String head) {
    boolean bOk = true;
    int n1 = vec.size();
    int n2 = vec2.size();
    bOk = n1 == n2;
    if ( !bOk)
      System.out.println(head + " : vec1.size=" + n1 + " <> vec2.size=" + n2);
    int diff = 0;
    for (int i = 0; i < n1 && diff < 10; i++) {
      if ( !vec.get(i).equals(vec2.get(i))) {
        System.out.println(String.format("vec1(%d)=%9d\t<> vec2=%9d", Integer.valueOf(i), vec.get(i), vec2.get(i)));
        diff++;
        bOk = false;
      }
    }
    if (bOk)
      System.out.println(head + " :Vec1==Vec2!");
  }

  private void sout(String sz1) {
    System.out.println(riga);
    String sz = sz1.replaceAll("\\\\n", "\n");
    System.out.println(sz);
  }

  private RsaObj creaRSA() {
    RsaObj rsa = new RsaObj();
    PrimiFactory pf = rsa.getPrimiFactory();
    int min = 200;
    int max = 700;
    int i1 = (int) (Math.random() * (max - min) + min);
    int i2 = (int) (Math.random() * (max - min) + min);
    long p = pf.getList().get(i1);
    long q = pf.getList().get(i2);
    rsa.setP(p);
    rsa.setQ(q);
    return rsa;
  }
}
