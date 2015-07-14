package search_ssl_openapi;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpStatus;
//import org.apache.commons.codec.DecoderException;
//import org.apache.commons.codec.binary.Hex;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Ssl_Openapi_Mapper extends Mapper<Object, Text, Text, Text> {
    public void map(Object key, Text value, Context context
            ) throws IOException, InterruptedException {
    	
    	
    	 String inputStr = value.toString();
    	 String patternStr = "(.+)\\s(.+)\\s(.+)\\s\\[(.+\\:..).+]\\s\\\"(.+?)\\s(.+?)\\\"(.+?)\\s.+\\\"(.+?)\\\".+\\\"(.+?)\\\"";
    	 Pattern pattern = Pattern.compile(patternStr);
    	 Matcher matcher = pattern.matcher(inputStr);
    	 String actiontarget = "";
    	 String useos = "";
    	 String outdata = "";
    	 String outkey = "";
    	 
    	 Text wordK = new Text();
    	 Text wordV = new Text();

    	 //"(.+1)\\s(.+2)\\s(.+3)\\s\\[(..4).(.+5)\\/.+\\:(.+6)\\:(..7)\\:.+]\\s\\\"(.+?8)\\s(.+?9)\\\"(.+?10)\\s.+\\\"(.+?11)\\\".+\\\"(.+?12)\\\"";
    	 //"(.+1)\\s(.+2)\\s(.+3)\\s\\[(.+\\:..4).+]\\s\\\"(.+?5)\\s(.+?6)\\\"(.+?7)\\s.+\\\"(.+?8)\\\".+\\\"(.+?9)\\\"";
        boolean matchFound = matcher.find();
        if(matchFound==true) {
        	int a = matcher.group(6).toString().indexOf("?");//分析网址栏位
        	int b = matcher.group(6).toString().indexOf(" ");
        	if(a>0){
        		actiontarget = matcher.group(6).toString().substring(0, a);
        	}else{
        		actiontarget = matcher.group(6).toString().substring(0, b);
        	}
        	if (actiontarget.indexOf("/device/") != -1)  {
        		String pattern_for_dev = "(.+)\\/.+\\/\\d{8}\\/(.+)";//去除网址8码
        		Pattern pattern_dev = Pattern.compile(pattern_for_dev );
           	    Matcher matcher_dev = pattern_dev.matcher(actiontarget);
           	    boolean matchFound_dev = matcher_dev.find();
           	    if(matchFound_dev==true) {
           	    	actiontarget = matcher_dev.group(1).toString()+"/"+matcher_dev.group(2).toString();
           	    }else{
           	    	//System.out.println("device target delete error:"+actiontarget);
           	    }
        	}
		
        	int c = matcher.group(9).toString().indexOf(" ");
        	if(c>0){
        		useos = matcher.group(9).toString().substring(0, c);
        	}else{
        		useos = matcher.group(9).toString();
        	}
        	
        	String ssss =matcher.group(7).toString().replaceAll(" ", "");
        	int i = Integer.parseInt(ssss);
        	String ttt = HttpStatus.getStatusText(i);
        	//System.out.println(i+"=="+ttt);
        	
        	
        	outkey = matcher.group(1).toString();
        	outdata = matcher.group(4).toString()+"\t"+matcher.group(5).toString()+"\t"+actiontarget+"\t"+ttt+"\t"+useos;

        	//outdata = matcher.group(1).toString()+"\t"+matcher.group(5).toString()+"\t"+matcher.group(4).toString()+"\t"+matcher.group(6).toString()+"\t"+matcher.group(7).toString()+"\t"+matcher.group(8).toString()+"\t"+actiontarget+"\t"+ttt+"\t"+useos;
        	//174.254.176.227*1	Sep5	01*4	15*6	51*7	GET*8	/me/user/services	OK	mydlink%20Lite/3.2.2

        	//IP, moth, day, hour, action, target, result, os

        	//System.out.println(s2+"("+bytes+")="+ Integer.toHexString(hash(bytes, 2)));
        	wordK.set(outkey);
        	wordV.set(outdata);
        	context.write(wordK, wordV);
       
    }else{
    	
    	System.out.println("error");
    }
}}


