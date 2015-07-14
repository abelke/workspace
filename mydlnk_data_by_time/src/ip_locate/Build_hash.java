package ip_locate;
import java.security.MessageDigest;


public class Build_hash {

	String mydlinkno;
    String sessionid;
    String thedigest;
    Build_hash(String input2, String find) {
        this.mydlinkno = input2;
        this.sessionid =find;
           }
    
    String exchange() throws Exception{    	
		String yourString = mydlinkno+sessionid;
		yourString = yourString.replaceAll("-", "");
		yourString = yourString.replaceAll("_", "");
    	
    	 
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(yourString.getBytes());
 
        byte byteData[] = md.digest();
 
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        //System.out.println("Digest(in hex format):: " + sb.toString());
        //System.out.println(yourString+"=="+sb.toString());
        
        String result = sb.toString();
		return result;
        
    }
}
