package sm.clagenna.crypt.util;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.List;

public class ProvaB64 {
  private List<Character> liChars = new ArrayList<Character>();

  public static void main(String[] args) {
    ProvaB64 b64 = new ProvaB64();
    b64.provalo();
  }

  public void provalo() {
    System.out.println("Java Version: " + System.getProperty("java.runtime.version"));
    char[] arr = { 54, 10, 145, 225, 242, 135, 6, 130, 20, 113, 109, 35, 33, 7, 1, 38, 152, 100, 134, 25, 111, 6, 160, 125, 116 };

    String szPrimo = new String(arr);
    Encoder enc = Base64.getEncoder();
    String coded = new String(enc.encode(szPrimo.getBytes(StandardCharsets.UTF_8)));
    System.out.println(coded);

    Decoder dec = Base64.getDecoder();
    String szSecond = new String(dec.decode(coded.getBytes(StandardCharsets.UTF_8)));
    System.out.println(szSecond);
    if ( !szPrimo.equals(szSecond))
      System.err.println("Strings are different !!!");
    else
      System.out.println("Are equals !");
  }
}
