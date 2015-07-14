package mydlink_ssl_access;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.commons.httpclient.HttpStatus;


public class access_mapper extends Mapper<Object, Text, Text, Text> {
	
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
    public void map(Object key, Text value, Context context
            ) throws IOException, InterruptedException {
    	
    	
    	 String inputStr = value.toString();
    	 String patternStr = "(.+)\\s(.+)\\s(.+)\\s\\[(..).(.+)\\/.+\\:(.+)\\:(..)\\:.+]\\s\\\"(.+?)\\s(.+?)\\\"(.+?)\\s.+\\\"(.+?)\\\".+\\\"(.+?)\\\"";
    	 Pattern pattern = Pattern.compile(patternStr);
    	 Matcher matcher = pattern.matcher(inputStr);
    	 String actiontarget = "";
    	 String useos = "";
    	 String outdata = "";
    	 
    	 Text wordK = new Text();
    	 Text wordV = new Text();

 
    	 
        boolean matchFound = matcher.find();
        if(matchFound==true) {
        	int a = matcher.group(9).toString().indexOf("?");
        	int b = matcher.group(9).toString().indexOf(" ");
        	if(a>0){
        		actiontarget = matcher.group(9).toString().substring(0, a);
        	}else{
        		actiontarget = matcher.group(9).toString().substring(0, b);
        	}
        	if (actiontarget.indexOf("/device/") != -1)  {
        		String pattern_for_dev = "(.+)\\d{8}\\/(.+)";
        		Pattern pattern_dev = Pattern.compile(pattern_for_dev );
           	    Matcher matcher_dev = pattern_dev.matcher(actiontarget);
           	    boolean matchFound_dev = matcher_dev.find();
           	    if(matchFound_dev==true) {
           	    	actiontarget = matcher_dev.group(1).toString()+matcher_dev.group(2).toString();
           	    }else{
           	    	//System.out.println("device target delete error:"+actiontarget);
           	    }
        	}
		
        	int c = matcher.group(12).toString().indexOf(" ");
        	if(c>0){
        		useos = matcher.group(12).toString().substring(0, c);
        	}else{
        		useos = matcher.group(12).toString();
        	}
        	
        	String ssss =matcher.group(10).toString().replaceAll(" ", "");
        	int i = Integer.parseInt(ssss);
        	String ttt = HttpStatus.getStatusText(i);
        	//System.out.println(i+"=="+ttt);
        	
        	
        	
        	outdata = matcher.group(1).toString()+"\t"+matcher.group(5).toString()+"\t"+matcher.group(4).toString()+"\t"+matcher.group(6).toString()+"\t"+matcher.group(7).toString()+"\t"+matcher.group(8).toString()+"\t"+actiontarget+"\t"+ttt+"\t"+useos;
        	//outdata = matcher.group(1).toString()+" "+matcher.group(5).toString()+" "+matcher.group(4).toString()+" "+matcher.group(6).toString()+" "+matcher.group(7).toString()+" "+actiontarget+" "+matcher.group(9).toString()+" "+useos;
        	
        	//IP, moth, day, hour, action, target, result, os
        	String s = matcher.group(1).toString().replaceAll("\\.", "")+matcher.group(6).toString()+matcher.group(4).toString();//create unique hash code use ip+day+hour
        	String s2 = s.replaceAll("\\D", "");
        	
        	byte[] bytes = null;
        	try {
        		if(s2.length()%2 !=0 )
        			s2=s2+"0";
        		bytes = Hex.decodeHex(s2.toCharArray());
        		} catch (DecoderException e) {
        			System.out.println(s2.replaceAll("\\D", "")+" ");
        			e.printStackTrace();
        			}
        	wordK.set(Integer.toHexString(hash(bytes, 2)));
        	//System.out.println(s2+"("+bytes+")="+ Integer.toHexString(hash(bytes, 2)));
        	wordV.set(outdata);
        	context.write(wordK, wordV);
       
    }else{
    	
    	System.out.println("error");
    }
}}

