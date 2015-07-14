package fp_tree;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.commons.httpclient.HttpStatus;


public class Fp_tree_mapper extends Mapper<Object, Text, Text, IntWritable> {
	private static String last_node = "rootnode";
	private static String now_id = "root";
	private final static IntWritable one = new IntWritable(1); 
	private final static int mintime_threshold = 200; 

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
    /*	1001dd05	POST/m/index.php	68108
    	10036991	POST/m/index.php	68108
    	10041826	POST/login	5511
    	10041826	GET/	4606
    	10041826	GET/fpm_status	51
    	10041826	GET/nginx_status	48
    	10041826	GET/op_apc_stats.php	48
    	10041826	GEThttps://us5.mydlink.com/fpm_ping	24
    	100575f7	POST/signin/	266
    	10058d31	POST/m/index.php	68108*/
   	
    	
    	 String inputStr = value.toString();
    	 String patternStr = "(.+)\\s(.+)\\s(.+)";
    	 Pattern pattern = Pattern.compile(patternStr);
    	 Matcher matcher = pattern.matcher(inputStr);
    	 String keydata = "";
    	 
    	 Text wordK = new Text();
    	 boolean matchFound = matcher.find();
         if(matchFound==true) {
    	

        
    	 String id = matcher.group(1).toString();
    	 String name = matcher.group(2).toString();
    	 String all_times_data = matcher.group(3).toString();
    	// System.out.println(id + "*"+name +"*"+all_times_data);
    	 System.out.println(all_times_data);
    	if ( Integer.valueOf(all_times_data) >= mintime_threshold){
    	 if(now_id.compareTo(id) == 0){
     		String s1 = name+last_node;
          	String s2 = s1.replaceAll("/", "");
         	s2 = s2.replaceAll("\\.", "");
          	byte[] bytes = null;
          	try {
          		if(s2.length()%2 !=0 )
          			s2=s2+"0";
          		bytes = s2.getBytes();
          		} catch (Exception e) {
          			System.out.println("input hash error:"+s2+" ");
          			e.printStackTrace();
          			}
     		 keydata = name+"\t"+last_node+"\t"+Integer.toHexString(hash(bytes, 2));
     		 wordK.set(keydata);	
          	
     		 context.write(wordK, one);
     		//System.out.println("target data:"+ keydata);
     		 last_node = Integer.toHexString(hash(bytes, 2));  
    	 }else{
    		 //System.out.println("----"+now_id);
    		last_node = "rootnode";
    		
    		now_id = id;
    		String s1 = name+last_node;
         	String s2 = s1.replaceAll("/", "");
         	s2 = s2.replaceAll("\\.", "");
         	byte[] bytes = null;
         	try {
         		if(s2.length()%2 !=0 )
         			s2=s2+"0";
         		bytes = s2.getBytes();
         		} catch (Exception e) {
         			System.out.println("input hash error:"+s2+" ");
         			e.printStackTrace();
         			}
    		 keydata = name+"\t"+last_node+"\t"+Integer.toHexString(hash(bytes, 2));
    		 wordK.set(keydata);	
    		 //System.out.println(name + "+" +last_node +"("+bytes+")="+ Integer.toHexString(hash(bytes, 2)));
    		 
    		 context.write(wordK, one);   //Integer.toHexString(hash(bytes, 2))
    		 //System.out.println("target data start :"+ keydata + "-----"+now_id);
    		 last_node = Integer.toHexString(hash(bytes, 2));
    		 
    	 }
         }else{   	
        	 System.out.println("mintime_threshold didn't mach");
            }
         }else{ 
        	 System.out.println("error");
         }
}}
