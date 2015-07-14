package mydlinktest;

import java.io.*;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.io.LongWritable;



public class mydlinkmapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	private final static IntWritable one = new IntWritable(1); // type of output value
    private Text word = new Text();   // type of output key
      
    public void map(LongWritable key, Text value, Context context
                    ) throws IOException, InterruptedException {
    	StringTokenizer itr = null;
    	String[] splitresult = null;
    	if(value.toString().contains("from")==true){
    		System.out.print(value.toString().contains("from"));
    		int start=value.toString().indexOf("{");
    		int end=value.toString().indexOf("}");
    		String stopC = " "+(char)34+(char)44+(char)58;
    		
    		itr = new StringTokenizer(value.toString().substring(start+1, end),stopC);	
    		  int i =0;
    		while (itr.hasMoreTokens()) {
    			  
    			  splitresult[i]=itr.nextToken().toString();
    			  i++;
    			  
    			  
    		        word.set(itr.nextToken());    // set word as each input keyword
    		        context.write(word, one);     // create a pair <keyword, 1> 
    		      }
    		
    		  
    		  
    		  
    		  
    		  
    	}else{
    		System.out.println("*");
    		
    	}
    	
    	
    	
    
    }
}
