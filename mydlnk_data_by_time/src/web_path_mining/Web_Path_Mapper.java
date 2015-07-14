package web_path_mining;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
//import org.apache.hadoop.mapreduce.Mapper.Context;
//import org.apache.commons.httpclient.HttpStatus;
//import org.apache.commons.codec.DecoderException;
//import org.apache.commons.codec.binary.Hex;

public class Web_Path_Mapper extends Mapper<Object, Text, Text, IntWritable> {
	private static String last_node = "rootnode";  //fffffffDDDDDDDDD
	private static ArrayList<String> pathlist = new ArrayList<String>();
	private static ArrayList<Integer> pathlistno = new ArrayList<Integer>();
	private final static IntWritable one = new IntWritable(1); 



	/*public static int hash(byte[] data, int seed) {
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
		  }*/
    
	public static String webpath_out(int start, int end) {
		
		String keydataF = "";
		String last_nodeF = "rootnode";
		if(start== end){
			String s0 = pathlist.get(end);
			String s1 = s0+last_nodeF;
	       	String s2 = s1.replaceAll("/", "");
	      	s2 = s2.replaceAll("\\.", "");
	      	keydataF = s0+"\t"+last_nodeF+"\t"+s2.hashCode();
	  		return keydataF;
		}else{
			String s0="";
			String s1;
	       	String s2;
	       	String last_nodeFV2="";//迴圈輸出上層節點雜湊
			for(int ins=start; ins<=end;ins++){
				last_nodeFV2= last_nodeF ;
				s0 = pathlist.get(ins);
				s1 = s0+last_nodeF;
		       	s2 = s1.replaceAll("/", "");
		      	s2 = s2.replaceAll("\\.", "");
	
		      	last_nodeF = Integer.toString(s2.hashCode());
			}
	      	keydataF = s0+"\t"+last_nodeFV2+"\t"+last_nodeF;
	      	return keydataF;
		}
	}
	
	public void map(Object key, Text value, Context context
            ) throws IOException, InterruptedException {
    /*	
100a5c4	GET	/	0
100a5c4	GET	/query_ipdb.php	1
100a79a4	GET	/device	0
100a79a4	GET	/entrance	1
100b416d	GET	/	0
100b416d	GET	/entrance	1
100b416d	GET	/query_ipdb.php	2
100b416d	GET	/entrance	3
100d697b	GET	/browserconfig.xml	0
100db458	GET	/8D/tssm/tssml.php	0
100db458	GET	/8D/tssm/tssml.php	1
100db458	GET	/8D/tssm/tssml.php	2
100dd67a	GET	/8D/tssm/tssml.php	0
100dd67a	GET	/device/en/cameraSet	1
100dd67a	GET	/device/event	2
100dd67a	GET	/8D/tssm/tssml.php	3
100dd67a	POST	/device/set_unverified	4
*/
   	
    	 String inputStr = value.toString();
    	 
    	 String patternStr = "(.+)\\s(.+)\\s(.+)\\s(.+)";
    	 Pattern pattern = Pattern.compile(patternStr);
    	 Matcher matcher = pattern.matcher(inputStr);
    	 String keydata = "";
    	 
    	 Text wordK = new Text();
    	 boolean matchFound = matcher.find();
         if(matchFound==true) {
    	    
    	 String id = matcher.group(1).toString();
    	 String action = matcher.group(2).toString();
    	 String web_page = matcher.group(3).toString();
    	 int web_path_no = Integer.valueOf(matcher.group(4).toString());
    	// System.out.println(id + "*"+name +"*"+all_times_data);
    	

    	 if(web_path_no == 0){
    		 //System.out.println("----"+now_id);
    		 pathlist.clear();
    		last_node = "rootnode";
    		String s0 = action+web_page;
    		String s1 = s0+last_node;
    		String s2 = s1.replaceAll("/", "");
         	
         	s2 = s2.replaceAll("\\.", "");
        
         	
    		 keydata = s0+"\t"+last_node+"\t"+s2.hashCode();
    		 wordK.set(keydata);	
    		 //System.out.println(name + "+" +last_node +"("+bytes+")="+ Integer.toHexString(hash(bytes, 2)));
    		 
    		 context.write(wordK, one);   //Integer.toHexString(hash(bytes, 2))
    		 //System.out.println("target data start :"+ keydata + "-----"+now_id);
    		 last_node = Integer.toString(s2.hashCode());
    		 pathlist.add(s0);
    		 pathlistno.add(web_path_no);
    	}else{
    		String s0 = action+web_page;
    		String s1 = s0+last_node;
           	String s2 = s1.replaceAll("/", "");
          	s2 = s2.replaceAll("\\.", "");

      		 keydata = s0+"\t"+last_node+"\t"+s2.hashCode();
      		 wordK.set(keydata);	
           	
      		 context.write(wordK, one);
      		//System.out.println("target data:"+ keydata);
      		 last_node = Integer.toString(s2.hashCode());
    		 pathlist.add(s0);
    		 pathlistno.add(web_path_no);
      		 if(web_path_no>1){//
      			keydata = webpath_out(web_path_no-1,web_path_no-1);//前一位置的ROOT
      			wordK.set(keydata);	
              	context.write(wordK, one);
      			
              	for(int inputP =1; inputP<web_path_no;inputP++ ){
          			keydata = webpath_out(inputP,web_path_no);//起點位置
          			wordK.set(keydata);	
                  	context.write(wordK, one);
              	}
      			 
      			
      		 }else{
      			//System.out.println("path only have two node");
      		 }

    		 
    	 }
         }else{ 
        	 System.out.println("error");
         }
}}