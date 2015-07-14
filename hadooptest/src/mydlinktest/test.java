package mydlinktest;
import java.security.MessageDigest;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mydlinkddd.Cutstr;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.hadoop.io.Text;

 
/**
 * This is a very fast, non-cryptographic hash suitable for general hash-based
 * lookup.  See http://murmurhash.googlepages.com/ for more details.
 * 
 * <p>The C version of MurmurHash 2.0 found at that site was ported
 * to Java by Andrzej Bialecki (ab at getopt org).</p>
 */
public class test{
	
  public static int hash(byte[] data, int seed) {
    int m = 0x5bd1e995;
    int r = 24;

    int h = seed ^ data.length;

    int len = data.length;
    int len_4 = len >> 2;

    for (int i = 0; i < len_4; i++) {
      int i_4 = i << 2;
      int k = data[i_4 + 3];
      k = k << 8;
      k = k | (data[i_4 + 2] & 0xff);
      k = k << 8;
      k = k | (data[i_4 + 1] & 0xff);
      k = k << 8;
      k = k | (data[i_4 + 0] & 0xff);
      k *= m;
      k ^= k >>> r;
      k *= m;
      h *= m;
      h ^= k;
    }

    int len_m = len_4 << 2;
    int left = len - len_m;

    if (left != 0) {
      if (left >= 3) {
        h ^= (int) data[len - 3] << 16;
      }
      if (left >= 2) {
        h ^= (int) data[len - 2] << 8;
      }
      if (left >= 1) {
        h ^= (int) data[len - 1];
      }

      h *= m;
    }

    h ^= h >>> 13;
    h *= m;
    h ^= h >>> 15;

    return h;
  }
  
  /* Testing ...
  
  
  public static void main(String[] args) {
    byte[] bytes = new byte[4];
    for (int i = 0; i < NUM; i++) {
      bytes[0] = (byte)(i & 0xff);
      bytes[1] = (byte)((i & 0xff00) >> 8);
      bytes[2] = (byte)((i & 0xff0000) >> 16);
      bytes[3] = (byte)((i & 0xff000000) >> 24);
      System.out.println(Integer.toHexString(i) + " " + Integer.toHexString(hash(bytes, 1)));
    }
  } */
  public static String bytesToHex(byte[] b) {

      StringBuffer sb = new StringBuffer();
      String stmp = "";
      for (int n = 0; n < b.length; n++) {
          stmp = (Integer.toHexString(b[n] & 0XFF));
          if (stmp.length() == 1) {
              sb.append("0").append(stmp);
          } else {
              sb.append(stmp);
          }
          if (n < b.length - 1) {
              sb.append(":");
          }
      }
      return sb.toString().toUpperCase();
  }


	static int NUM = 1000;
  public static void main(String[] args) throws Throwable {
    String in1 = "Jun 16 00:00:07 signal1 signalingd[2013]: [1402902007] Do Something: Portal log, logtime=2014-06-16 00:00:07^no=30047544^id=d9c1eb93-c0d8-41a6-8722-0d7fa07d3688^type=16^state=21^status=1^ctype=LOCAL^message=2014-06-16 00:00:06: ^browser=Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; InfoPath.2; BRI/1; .NET4.0C; .NET4.0E; .NET CLR 1.1.4322),remote_ip=24.17.141.83,remote_port=1356";
    String in2="Jun 16 00:01:07 signal1 signalingd[2013]: [1402902007] Do Something: Portal log, logtime=2014-06-16 00:00:07^no=30047544^id=d9c1eb93-c0d8-41a6-8722-0d7fa07d3688^type=16^state=21^status=1^ctype=LOCAL^message=2014-06-16 00:00:06: ^browser=Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; InfoPath.2; BRI/1; .NET4.0C; .NET4.0E; .NET CLR 1.1.4322),remote_ip=24.17.141.83,remote_port=1356";
    String in3="Jun 16 00:01:11 signal1 signalingd[2013]: [1402902011] Do Something: Portal log, logtime=2014-06-16 00:00:11^no=42754084^id=b5b60f43-d4cc-4160-996a-f8399b42d1c2^type=16^state=21^status=0^ctype=RELAY^message=2014-06-16 09:00:09: ^browser=Mozilla/5.0 (Windows NT 6.1; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0,remote_ip=82.57.38.100,remote_port=16220";
    String patternStr =".+\\s(\\d{2})\\:.+";
   //String patternStr = ".+logtime=.+\\s(\\d{2})\\:.+\\^no=(\\d{8})\\^id=(.*)\\^type=(.*)\\^state=(.*)\\^status=(.*)\\^ctype=(.*)\\^message=(.*)\\^browser=(.*)\\/.+\\,remote_ip.+";
    Pattern pattern = Pattern.compile(patternStr);
    Matcher matcher = pattern.matcher(in2);
    boolean matchFound = matcher.find();
    cuttest c01 = new cuttest(in1, "browser=");
    System.out.println(c01.input);
    System.out.println(c01.find);
    System.out.println(c01.exchange(" "));
    System.out.println(in1.substring(0,15));
	 
    String hh = "20140616070101";  
    System.out.println(hh+"##"+hh.length()+"##");
	 byte[] bytes = null;
	try {
		bytes = Hex.decodeHex(hh.toCharArray());
	} catch (DecoderException e) {
		// TODO Auto-generated catch block
		System.out.println(hh+"@@"+hh.length()+"@@");
		e.printStackTrace();
	}
	 //System.out.println(Integer.toHexString(hash(bytes, 1))+"***-**/-");
	/*2014 06 16 00 00 08 31228555
	
	2014 06 16 07 01 01 0*/
	 
	 System.out.println(Integer.toHexString(hash(bytes, 1)));
    
    
    
    cuttest g01 = new cuttest(in1, "logtime=");
    cuttest g02 = new cuttest(in1, "no=");
    cuttest g03 = new cuttest(in1, "id=");
    cuttest g04 = new cuttest(in1, "type=");
    cuttest g05 = new cuttest(in1, "state=");
    cuttest g06 = new cuttest(in1, "status=");
    cuttest g07 = new cuttest(in1, "ctype=");
    cuttest g08 = new cuttest(in1, "browser=");
    
    String splitsym ="^";
    
    String outdata = "";
    outdata = matcher.group(1)+"	"+g02.exchange(splitsym)+"	"+g03.exchange(splitsym)+"	"+g04.exchange(splitsym)+"	"+g05.exchange(splitsym)+"	"+g06.exchange(splitsym)+"	"+g07.exchange(splitsym)+"	"+g08.exchange(" ");
   
    String s=g01.exchange(splitsym)+g02.exchange(splitsym);

    System.out.println(s.replaceAll("\\D", "=")+"@@~");

   
    
    
   // byte[] bytes = new byte[4];
   // String s = "Mon Jun 16 19:49:18 PDT 2014";    
   // byte[] bytes = Hex.decodeHex(s.toCharArray());
      
     
      //System.out.println(" " + Integer.toHexString(hash(bytes, 1)));
    
      
   // 程式執行前須先將 Provider 加入
     // Security.addProvider(new gnu.crypto.jce.GnuCrypto());

      // 初始化測試資料
   /*   String stringToDigest = "28107B085EFE1402899947";
      byte[] bytesToDigest = stringToDigest.getBytes();

      // Hash 範例：MD5
      // 要用 SHA, WHIRLPOOL 的話只要把 "MD5" 改為 "SHA"、"WHIRLPOOL"
      try {
          // 設定 Hash 演算法
          MessageDigest messageDigest = MessageDigest.getInstance("MD5");
          messageDigest.update(bytesToDigest);
          byte[] digest = messageDigest.digest();

          // 輸出 MD5 Hash：18:50:1F:84:0C:FE:2A:10:CE:89:B0:94:8E:FA:A0:42
          System.out.println(bytesToHex(digest)+"erererer");
      } catch (Exception e) {
          e.printStackTrace();
      }*/
      
      
      
      
    while(matchFound) {
    	
    	Date dt=new Date();
    	long t=dt.getTime()/1000;
    	System.out.println(t+"--"+dt);
    	
    	long t2 = 1402893323;
    	java.util.Date time=new java.util.Date((long)t2*1000);
    	
    	System.out.println(time+"-**-");
    	
    	
    	java.util.Date timet=new java.util.Date(t2*1000);
    	
    	String inputStr2 = "Sun Jun 15 21:35:23 PDT 2014";
    	System.out.println(t2+"++-");
    	
        String patternStr2 = "(.+?)\\s(.+?)\\s(.+?)\\s(.+?)\\:(.+?)\\:(.+?)\\s(.+)";
        Pattern pattern2 = Pattern.compile(patternStr2);
        Matcher matcher2 = pattern2.matcher(timet.toString());
    	 
      //System.out.println(matcher.start() + "-" + matcher.end());
      for(int i = 0; i <= matcher.groupCount(); i++) {
        String groupStr = matcher.group(i);
        System.out.println("g"+i + ":" + groupStr);
      }
      if(matcher.end() + 1 <= in1.length()) {
    	  
        matchFound = matcher.find(matcher.end());
          }else{
            break;
          }
      }
  }
}
