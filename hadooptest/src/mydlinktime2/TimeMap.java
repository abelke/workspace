package mydlinktime2;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class TimeMap extends Mapper<LongWritable, Text, Text, IntWritable> {
	//int a;
	private final static IntWritable wordK = new IntWritable(1); 

    public void map(LongWritable key, Text value, Context context
                    ) throws IOException, InterruptedException {
    	
    	
    	 String inputStr = value.toString();
    	 String patternStr = "(.+)\\:(.+)\\:\\s(.+?)\\s(.+?)\\s(.+?)\\s(.+)";
    	 Pattern pattern = Pattern.compile(patternStr);
    	 Matcher matcher = pattern.matcher(inputStr);
    	 
        boolean matchFound = matcher.find();
        if(matchFound==true) {
        	
            	 for(int i = 3; i <= 6; i++) {
            		//String groupStr = matcher.group(i+3);
            		
            		Text wordV = new Text();
            		
            	switch(i) {  
                case 3: 
                	wordV.set(matcher.group(3).toString());
                	//context.write(wordV, wordK);
                	//System.out.println(wordK+ ":" + matcher.group(i));
                    break; 
                case 4: 
                	long t2 = Long.parseLong(matcher.group(4).toString());
                	java.util.Date time=new java.util.Date(t2*1000);
                	String inputStrhour = time.toString();
                    String patternStrhour = "(.+)\\s(.+)\\s(.+)\\s(.+)\\:(.+)\\:(.+)\\s(.+)";
                    Pattern patternhour = Pattern.compile(patternStrhour);
                    Matcher matcherhour = patternhour.matcher(inputStrhour); 
                    matcherhour.find();
                	wordV.set(matcherhour.group(4).toString());
                	context.write(wordV, wordK);  
                	//System.out.println(wordK+ ":" + matcher.group(i));
                	break; 
                case 5:  
                	//System.out.println(wordK+ ":" + matcher.group(i)+"X");
                	break; 
                case 6: 
                	
                	int duringtime = Integer.valueOf(matcher.group(6).toString());
                	
                	if(duringtime<60)
                		wordV.set("A");
                	else if(60<=duringtime && duringtime<600)
                		wordV.set("B");
                	else if(600<=duringtime && duringtime<1800)
                		wordV.set("C");
                	else 
                		wordV.set("D");
                	
                	context.write(wordV, wordK);
                	//System.out.println(Integer.toHexString(hash(bytes, 1))+ ":" + matcher.group(i));
                    break; 
                    }
            }
 
             
    }else{
    	System.out.println("error");
    }
}}

