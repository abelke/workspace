package Set_input_id;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class Set_id_Mapper extends Mapper<Object, Text, Text, Text> {
	private static String last_id = "rootnode";
	private static String now_id = "root";
	private static int tree_node_no = 0;

	private final static int time_step = 1200; //兩分鐘
	DateFormat df = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss");
	private static Date date1;
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
    	/*
    	 * 1.0.135.120	01/Sep/2014:21:33:01	GET	/m/index.php
1.0.186.112	01/Sep/2014:05:36:05	GET	/query_ipdb.php
1.10.218.85	01/Sep/2014:18:14:40	GET	/
1.10.218.85	01/Sep/2014:18:14:41	GET	/query_ipdb.php
1.121.174.208	01/Sep/2014:01:22:39	GET	/entrance
1.121.174.208	01/Sep/2014:01:22:45	GET	/profile/getBasicInfo
1.121.174.208	01/Sep/2014:01:23:05	GET	/check_account
1.121.174.208	01/Sep/2014:01:23:06	GET	/check_account
    	 * 
    	 * */

    	 String inputStr = value.toString();
    	 String inputkey = key.toString();
    	 String patternStr = "(.+)\\t(.+)\\t(.+)\\t(.+)";
    	 Pattern pattern = Pattern.compile(patternStr);
    	 Matcher matcher = pattern.matcher(inputStr);
    	 String keydata = "";
    	 String valuedata = "";
    	 String date_string ;
    	 java.util.Date date2 = null;
    	 


    	 Text wordK = new Text();
    	 Text wordV = new Text();
    	 
    	 boolean matchFound = matcher.find();
         if(matchFound==true && (matcher.group(4).toString().toLowerCase().contains("faq") || matcher.group(4).toString().toLowerCase().contains("download"))) {//檢定網頁的條件
        	 //&& !matcher.group(4).toString().equals("/m/index.php") && !matcher.group(4).toString().equals("/8D/tssm/tssml.php") //一般情況 剔除非網頁瀏覽紀錄
        	 
//        	 System.out.println("***************************");
//        	 System.out.println(inputkey);
//        	 System.out.println("--------------------");
//        	 System.out.println(inputStr+"="+matcher.group(1).toString()+"(1)+"+matcher.group(2).toString()+"(2)+"+matcher.group(3).toString()+"+"+matcher.group(4).toString());
//        	 System.out.println("***************************");
    		 try {
 				date2=df.parse(matcher.group(2).toString());
 			} catch (ParseException e) {
 				// TODO Auto-generated catch block
 				System.out.println("error to load date");
 				e.printStackTrace();
 			}

    	 
    	 inputkey = matcher.group(1).toString();
    	 String action = matcher.group(3).toString();
    	 String target = matcher.group(4).toString();
    	// System.out.println(id + "*"+name +"*"+all_times_data);
    	 if(inputkey.compareTo(now_id)==0){
    		 long between=(date2.getTime()-date1.getTime())/1000;//除以1000是为了转换成秒
    		 if (between>time_step){
    			 tree_node_no =0;//計算每筆消費者紀錄走訪網頁的順序
    			 System.out.println(date1.getTime()+"+"+date2.getTime()+"="+between);
    			 now_id = inputkey;
        		 date_string = matcher.group(2).toString();
        		 String s1 = date_string+now_id;
        		 s1 = s1.replaceAll(":", "");
        		 s1 = s1.replaceAll(" ", "");
        		 s1 = s1.replaceAll("-", "");
        		 byte[] bytes = null;
        		 try {
              		if(s1.length()%2 !=0 )
              			s1=s1+"0";
              		bytes = s1.getBytes();
              		} catch (Exception e) {
              			System.out.println("input hash error:"+s1+" ");
              			e.printStackTrace();
              			}
         		 keydata = Integer.toHexString(hash(bytes, 2));
         		 valuedata = action+"\t"+target+"\t"+tree_node_no;
         		 wordK.set(keydata);	
         		 wordV.set(valuedata);
         	 	 tree_node_no=tree_node_no+1;
        		 
        		 context.write(wordK, wordV);   //Integer.toHexString(hash(bytes, 2))
        		 last_id = keydata;
        		 try {
    				date1=df.parse(matcher.group(2).toString());
    			} catch (ParseException e) {
    				// TODO Auto-generated catch block
    				System.out.println("error to load date");
    				e.printStackTrace();
    			}
    		 }else{
    			 
    			 now_id = inputkey;
         		 keydata = last_id;
         		 valuedata = action+"\t"+target+"\t"+tree_node_no;//+"----"+inputkey+"*"+date1+"-"+date2+"="+between
         		 wordK.set(keydata);	
         		 wordV.set(valuedata);
          		 tree_node_no=tree_node_no+1;
         		
        		 context.write(wordK, wordV);   //Integer.toHexString(hash(bytes, 2))
        		 try {
    				date1=df.parse(matcher.group(2).toString());
    			} catch (ParseException e) {
    				// TODO Auto-generated catch block
    				System.out.println("error to load date");
    				e.printStackTrace();
    			}
    		 } 
    	 }else{
    		 now_id = inputkey;
    		 tree_node_no =0;//計算每筆消費者紀錄走訪網頁的順序
    		 date_string = matcher.group(2).toString();
    		 String s1 = date_string+now_id;
    		 s1 = s1.replaceAll(":", "");
    		 s1 = s1.replaceAll(" ", "");
    		 s1 = s1.replaceAll("-", "");
    		 byte[] bytes = null;
    		 try {
          		if(s1.length()%2 !=0 )
          			s1=s1+"0";
          		bytes = s1.getBytes();
          		} catch (Exception e) {
          			System.out.println("input hash error:"+s1+" ");
          			e.printStackTrace();
          			}
     		 keydata = Integer.toHexString(hash(bytes, 2));
     		 valuedata = action+"\t"+target+"\t"+tree_node_no;//+"----"+inputkey+"*"+date_string
     		 wordK.set(keydata);	
     		 wordV.set(valuedata);
     		
	


     		 
     		 last_id = keydata;
     		 tree_node_no=tree_node_no+1;
     		 
    		 context.write(wordK, wordV);   //Integer.toHexString(hash(bytes, 2))

    		 try {
				date1=df.parse(matcher.group(2).toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				System.out.println("error to load date");
				e.printStackTrace();
			}
    		 
    		 
    	 }
    	
         }else{ 
        	 System.out.println("error");
        	 System.out.println("/m/index.php");
         }
}}