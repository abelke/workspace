package ddd_mining_connect_success;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class Ddd_connect_mapper extends Mapper<Object, Text, Text, Text> {
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
    	 //DateFormat df = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss");
    	 String splitsym ="^";
    	 String inputStr = value.toString();//輸入的完整字串
    	 
    	// Cutstr g01 = new Cutstr(inputStr, "logtime="); //分離dddlog中各項資料 參考cutstr定義
    	 Cutstr g02 = new Cutstr(inputStr, "no=");
    	 Cutstr g03 = new Cutstr(inputStr, "id=");
    	// Cutstr g04 = new Cutstr(inputStr, "type=");
    	 Cutstr g05 = new Cutstr(inputStr, "state=");//非連線成功與否 連線的頁面狀態
    	 Cutstr g06 = new Cutstr(inputStr, "status=");
    	 Cutstr g07 = new Cutstr(inputStr, "ctype=");
    	// Cutstr g08 = new Cutstr(inputStr, "browser=");
 	 
    	 
    	 Build_hash changehash = new Build_hash(g02.exchange(splitsym).toString(),g03.exchange(splitsym).toString());//建立NO和ID的HASH
    	 Text wordK = new Text();
    	 Text wordV = new Text();
    	 
    	 String aa="fail";
    	// String swapdata = "";
    	        	 
    	 
    	 //swapdata=matcher.group(1).toString(); 
    	
         if(g06.exchange(splitsym).toString().indexOf("error_data")==-1 && g05.exchange(splitsym).toString().equals("0")) {//status不為錯誤資料 而為確定單筆資料只計數一次所以計算STATE
    		 
    		 try {
    			 aa = changehash.exchange();//設定KEY值
    			 } catch (Exception e) {
	// TODO Auto-generated catch block
    				 e.printStackTrace();
    				 }                
    		 wordK.set(aa); //+"	"+g07.exchange(splitsym).toString()+"	"+g02.exchange(splitsym).toString()+"	"+g03.exchange(splitsym).toString()
    		 //wordV.set(g06.exchange(splitsym).toString());
    		 //one.set(Integer.valueOf(g06.exchange(splitsym).toString()));
    		// System.out.println(aa);

    		  System.out.println(aa+"****"+g02.exchange(splitsym).toString()+"=="+g07.exchange(splitsym).toString()+"	"+g06.exchange(splitsym).toString());

    		 wordV.set(g07.exchange(splitsym).toString()+"	"+g06.exchange(splitsym).toString());//連線類型和成功狀態傳入reduce
    		 context.write(wordK, wordV);
         }else{
         }
    	//System.out.println("error");
    }
}