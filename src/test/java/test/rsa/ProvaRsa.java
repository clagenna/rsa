package test.rsa;

import org.junit.Test;

import sm.ciscoop.crypt.primi.PrimiFactory;
import sm.ciscoop.crypt.rsa.RsaObj;

public class ProvaRsa {

  @Test
  public void provaEsp() {
    
    RsaObj gg = new RsaObj();
   
    
//    prova(23432, 5, 1023);
//    prova(3352, 235, 1023);
    
    PrimiFactory pf = new PrimiFactory();
    pf.buildPrimi(200);
    
    int nP = pf.getList().get(55).intValue();
    int nQ = pf.getList().get(77).intValue();
    
    gg.setP(nP);
    gg.setQ(nQ);
  }

//  private void prova(long vv, long exp, long mm) {
//    RsaObj gg = new RsaObj();
//    BigInteger res = gg.esponente(BigInteger.valueOf(vv), BigInteger.valueOf(exp), BigInteger.valueOf(mm));
//
//    String sz = String.format("esponente(%d, %d, %d)=%d", vv, exp, mm, res);
//    System.out.println(sz);
//  }
}
