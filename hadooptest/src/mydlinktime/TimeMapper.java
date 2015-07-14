package mydlinktime;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class TimeMapper extends Mapper<Object, Text, Text, Text> {
	int a;
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
    	 String patternStr = "(.+)\\s(\\d{2})\\:(.+)\\:\\s(.+?)\\s(.+?)\\s(.+?)\\s(.+)";
    	 Pattern pattern = Pattern.compile(patternStr);
    	 Matcher matcher = pattern.matcher(inputStr);
        
        boolean matchFound = matcher.find();
        if(matchFound==true) {
        	
            	 String s = matcher.group(4).toString()+matcher.group(5).toString();    
            	 byte[] bytes = null;
				try {
					bytes = Hex.decodeHex(s.toCharArray());
				} catch (DecoderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	 //System.out.println(Integer.toHexString(hash(bytes, 1))+"***-**/-");
            	 
            	 Text wordK = new Text();
            	 wordK.set(Integer.toHexString(hash(bytes, 1)));
            	 
            	 Text wordV = new Text();
            	 String outdata = "";
            	 String swapdata = "";
            	 for(int i = 2; i <= 7; i++) {
            		//String groupStr = matcher.group(i+3);
            		
            		
            		 
            	switch(i) { 
            	case 2:
            		swapdata=matcher.group(2).toString();  //online time at this version
                case 3: 
                	//wordV.set(matcher.group(3).toString());  //group(3) was mean MAC at before version 
                	//context.write(wordK, wordV);
                	//outdata=outdata+matcher.group(3).toString();
                	//System.out.println(Integer.toHexString(hash(bytes, 1))+ ":" + matcher.group(i));
                    break; 
                case 4: 
                	/*long t2 = Long.parseLong(matcher.group(4).toString());    //group(4) was mean online time at before version, so need to transform linuX timestamp
                	java.util.Date time=new java.util.Date(t2*1000);            //now MAC
                	String inputStrhour = time.toString();
                    String patternStrhour = "(.+)\\s(.+)\\s(.+)\\s(.+)\\:(.+)\\:(.+)\\s(.+)";
                    Pattern patternhour = Pattern.compile(patternStrhour);
                    Matcher matcherhour = patternhour.matcher(inputStrhour); 
                    matcherhour.find();
                	//wordV.set(matcherhour.group(4).toString());
                	//context.write(wordK, wordV);
                	outdata=outdata+"	"+matcherhour.group(4).toString();
                	//System.out.println(Integer.toHexString(hash(bytes, 1))+ ":" + matcher.group(i));*/
                	outdata=outdata+matcher.group(4).toString();
                	outdata=outdata+"	"+swapdata;
                	
                	break; 
                case 5:  
                	//System.out.println(a+ ":" + matcher.group(i)+"X");
                	break; 
                case 6: 
                	
                	int duringtime = Integer.valueOf(matcher.group(7).toString());
                	
                	if(duringtime<60)
                		outdata=outdata+"	A";//wordV.set("A");
                	else if(60<=duringtime && duringtime<=360)
                		outdata=outdata+"	B";//wordV.set("B");
                	else if(360<duringtime && duringtime<600)
                		outdata=outdata+"	C";
                	else if(600<=duringtime && duringtime<1800)
                		outdata=outdata+"	D";//wordV.set("C");
                	else 
                		outdata=outdata+"	E";//wordV.set("D");
                	
                	
                	//System.out.println(Integer.toHexString(hash(bytes, 1))+ ":" + matcher.group(i));
                    break; 
                    }
            }
            	 
            	 wordV.set(outdata);
            	 context.write(wordK, wordV);
 
             
    }else{
    	System.out.println("error");
    }
}}
