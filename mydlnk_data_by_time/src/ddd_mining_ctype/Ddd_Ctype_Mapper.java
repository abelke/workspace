package ddd_mining_ctype;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class Ddd_Ctype_Mapper extends Mapper<Object, Text, Text, Text> {
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
    	 
    	 String splitsym ="^";
    	 String inputStr = value.toString();
    	 
    	// Cutstr g01 = new Cutstr(inputStr, "logtime=");
    	 Cutstr g02 = new Cutstr(inputStr, "no=");
    	 Cutstr g03 = new Cutstr(inputStr, "id=");
    	// Cutstr g04 = new Cutstr(inputStr, "type=");
    	 Cutstr g05 = new Cutstr(inputStr, "state=");
    	 Cutstr g06 = new Cutstr(inputStr, "status=");
    	 Cutstr g07 = new Cutstr(inputStr, "ctype=");
    	// Cutstr g08 = new Cutstr(inputStr, "browser=");
    	 
    	 Build_hash changehash = new Build_hash(g02.exchange(splitsym).toString(),g03.exchange(splitsym).toString());
	 
    	 Text wordK = new Text();
    	 Text wordV = new Text();
 
        	 if(g05.exchange(splitsym).toString().equals("0") && g06.exchange(splitsym).toString().indexOf("0")!=0) 
        	 {
	//one.set(Integer.valueOf(g06.exchange(splitsym).toString()));  
	//int a = Integer.valueOf(g07.exchange(splitsym).toString());
        		 String aa="fail";
        		 try {
        			 aa = changehash.exchange();
        			 } catch (Exception e) {
		// TODO Auto-generated catch block
        				 e.printStackTrace();
        				 }                
        		 wordK.set(aa);
	//wordV.set(g07.exchange(splitsym).toString()+"*"+g06.exchange(splitsym).toString()+"*"+g05.exchange(splitsym).toString()+"*"+g02.exchange(splitsym).toString()+"+++"+aa);
        		 wordV.set(g07.exchange(splitsym).toString());
        		 context.write(wordK, wordV);

	}
    	//System.out.println("error");
    }
}