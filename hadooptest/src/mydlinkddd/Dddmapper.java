package mydlinkddd;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class Dddmapper extends Mapper<Object, Text, Text, Text> {
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
    	 DateFormat df = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss");
    	 String splitsym ="^";
    	 String inputStr = value.toString();
    	 
    	 Cutstr g01 = new Cutstr(inputStr, "logtime=");
    	 Cutstr g02 = new Cutstr(inputStr, "no=");
    	 Cutstr g03 = new Cutstr(inputStr, "id=");
    	 Cutstr g04 = new Cutstr(inputStr, "type=");
    	 Cutstr g05 = new Cutstr(inputStr, "state=");
    	 Cutstr g06 = new Cutstr(inputStr, "status=");
    	 Cutstr g07 = new Cutstr(inputStr, "ctype=");
    	 Cutstr g08 = new Cutstr(inputStr, "browser=");
/*Nov 17 09:43:05 10.32.100.195 403 <168>1 2014-11-17T09:41:56+08:00 signal signalingd 6330 - [meta sequenceId="10393655"] [1416188516] Do Something: Portal log, 
 * logtime=2014-11-17 09:41:56^
 * no=-^
 * id=obj_1416188568752^
 * type=1^
 * state=11^
 * status=^
 * ctype=^
 * message=[begin][applet][com.dlink.app.TsaApplet.class]^
 * browser=Mozilla/5.0%20(Windows%20NT%206.1;%20WOW64;%20rv:33.0)%20Gecko/20100101%20Firefox/33.0,remote_ip=60.251.172.83,remote_port=34465    	
 * 
 *  Nov 17 09:43:27 10.32.100.195 359 <168>1 2014-11-17T09:42:18+08:00 signal signalingd 6330 - [meta sequenceId="10407261"] [1416188538] Do Something: Portal log, 
 *  logtime=2014-11-17 09:42:18^
 *  no=30716299^
 *  id=1416188590899^
 *  type=1^
 *  state=2^
 *  status=^
 *  ctype=^
 *  message=^b
 *  rowser=Mozilla/5.0%20(Windows%20NT%206.1;%20WOW64;%20rv:33.0)%20Gecko/20100101%20Firefox/33.0,remote_ip=60.251.172.83,remote_port=34529
 *  
 *  Nov 17 10:33:33 10.32.100.195 453 <168>1 2014-11-17T10:32:24+08:00 signal signalingd 6330 - [meta sequenceId="12300225"] [1416191544] Do Something: Portal log, 
 *  logtime=2014-11-17 10:32:24^
 *  no=71024542^
 *  id=1416191584982^
 *  type=1^
 *  state=3^
 *  status=1^
 *  ctype=1^
 *  message=http://192.168.0.100:80,https://192.168.0.100:443^browser=Mozilla/5.0%20(Windows%20NT%206.1;%20WOW64)%20AppleWebKit/537.36%20(KHTML,%20like%20Gecko)%20Chrome/38.0.2125.111%20Safari/537.36,remote_ip=60.251.172.83,remote_port=40967
    	*/ 
    	
    	 
    	 Text wordK = new Text();
    	 Text wordV = new Text();
    	 String outdata = "";
    	 String swapdata = "";
    	 wordK.set(g03.exchange(splitsym));       	 

    	 String patternStr =".+\\s(\\d{2})\\:.+";
    	 Pattern pattern = Pattern.compile(patternStr);
    	 Matcher matcher = pattern.matcher(inputStr);
    	 boolean matchFound = matcher.find();
         if(matchFound==true)
    	 swapdata=matcher.group(1).toString(); 
    	 outdata = "logtime="+g01.exchange(splitsym)+"	no="+g02.exchange(splitsym)+"	type="+g04.exchange(splitsym)+"	state="+g05.exchange(splitsym)+"	status="+g06.exchange(splitsym)+"	ctype="+g07.exchange(splitsym)+"	browser="+g08.exchange(" ");
    	   	

    	 wordV.set(outdata);
    	 
    	 context.write(wordK, wordV);
 
             

    	//System.out.println("error");
    }
}

