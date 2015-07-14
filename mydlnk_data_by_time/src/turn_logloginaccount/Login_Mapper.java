package turn_logloginaccount;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class Login_Mapper extends Mapper<Object, Text, Text, Text> {
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
//Dec 22 04:31:44 usrelay1 relay_server[3133]: [Legacy]   68.52.189.141,   68.52.189.141, 308536980,      1, Mon Dec 22 04:31:43 2014 , Mon Dec 22 04:31:44 2014 ,      1,          0
//Dec 22 04:31:44 usrelay1 relay_server[3133]: [HTTP]   74.197.20.140,   74.197.20.140, 261396110, 261396111,      0, Mon Dec 22 04:31:15 2014 , Mon Dec 22 04:31:44 2014 ,     29,       1024           
    	String inputStr = value.toString(); //(.+)\\[.+\\](.+)\\,(.+)\\,(.+)\\,\\s.+\\s(.+\\s.+)\\:.+\\:.+\\,(.+)\\,(.+)\\,(.+)
    	String patternStr =	"(.+)\\[.+\\](.+)\\,(.+)\\,(.+)\\,\\s.+\\s(.+\\s.+\\s.+)\\:.+\\:.+\\,(.+)\\,(.+)\\,(.+)";//切字串 取要的部份
	   	Pattern pattern = Pattern.compile(patternStr);
	   	Matcher matcher = pattern.matcher(inputStr);
	   	Text wordK = new Text();
	   	Text wordV = new Text();
	   	String connecttime;
	   	String duringtime;
	   	String passdata;

	   	boolean matchFound = matcher.find();
	       if(matchFound==true) {

	    	   String[] mat = new String[8];
	    	   mat[0]=matcher.group(1).toString().replaceAll(" ", "");// 時間 類型 不會用到
	    	   mat[1]=matcher.group(2).toString().replaceAll(" ", "");// IP 8碼
	    	   mat[2]=matcher.group(3).toString().replaceAll(" ", "");//8碼
	    	   mat[3]=matcher.group(4).toString().replaceAll(" ", "");//連線成功花費時間
	    	   mat[4]=matcher.group(5).toString();                    //連線日期
	    	   mat[5]=matcher.group(6).toString().replaceAll(" ", "");//結束時間
	    	   mat[6]=matcher.group(7).toString().replaceAll(" ", "");//在線時間
	    	   mat[7]=matcher.group(8).toString().replaceAll(" ", "");//傳輸量
	    	   
	    	   Build_hash changehash = new Build_hash(matcher.group(2).toString(),matcher.group(3).toString()+matcher.group(6).toString());//用資料的唯一值建立hash
	       	
	    	   String aa="fail";
	    	   try {
	    		   aa = changehash.exchange();
	    		   } catch (Exception e) {
	    			   e.printStackTrace();   
	    		   }
	    	   
	    	   for(int i=0;i<8;i++){
	    		   
	    		   if(mat[i].equals("18446744073709551615")){//欄位中會包含的錯誤資料,進行替換以必面後續步驟出錯
	    			   
	    			   mat[i]="0";
	    			   
	    		     }
	    	   }
	    	   
	    	   if(Integer.parseInt(mat[3].replaceAll(" ", ""))>2)//when connect time ">=3" let Counter to "B"  else to A
	    		   connecttime="B";    
	    	   else
	    		   connecttime="A";
	    	   
	    	   if(Integer.parseInt(mat[6].replaceAll(" ", ""))<60)//when during time "<1min" let Counter to "A"  ">6min" to C between 1~6min to B
	    		   duringtime="A";
	    	   else if(Integer.parseInt(mat[6].replaceAll(" ", ""))>360)
	    		   duringtime="C";
	    	   else
	    		   duringtime="B";
	    	   double passdata01;
	    	   double duringtime01;
	    	   if(Float.parseFloat(mat[6].replaceAll(" ", ""))==0 || Float.parseFloat(mat[6].replaceAll(" ", ""))>86400)//針對再現時間的數字作除錯處理
	    		   duringtime01=1.0;
	    	   else
	    		   duringtime01=Float.parseFloat(mat[6].replaceAll(" ", ""));
	    	   
	    	   if(Float.parseFloat(mat[7].replaceAll(" ", ""))==0)
	    		   passdata01=1.0;
	    	   else
	    		   passdata01=Float.parseFloat(mat[7].replaceAll(" ", ""));
	    	   
	    	   if((int)((passdata01/duringtime01)/1024.0)>99)//每時間單位传输量分级
	    		   passdata="C";
	    	   else if((int)((passdata01/duringtime01)/1024.0)<30 && (int)((passdata01/duringtime01)/1024.0)!=0)
	    		   passdata="A";
	    	   else if((int)((passdata01/duringtime01)/1024.0)==0)
	    		   passdata="Z";
	    	   else
	    		   passdata="B";
	    	   
	    	   wordK.set(aa);
	    	   if(inputStr.indexOf("HTTP")!=-1){
	    		   wordV.set("0"+"	"+mat[4].replaceAll(" ", "-")+"	"+mat[3]+"	"+connecttime+"	"+mat[6]+"	"+duringtime+"	"+mat[7]+"	"+passdata);
	    	   }else if(inputStr.indexOf("Legacy")!=-1){
	    		   wordV.set("1"+"	"+mat[4].replaceAll(" ", "-")+"	"+mat[3]+"	"+connecttime+"	"+mat[6]+"	"+duringtime+"	"+mat[7]+"	"+passdata);
	    	   }              //0	22	09	0	A	8	A	13312	A 結果輸出給reducer
	       	context.write(wordK, wordV);
	      
	   }else{
	   	
	   	System.out.println("error");
	   }
    	  
    }
}
