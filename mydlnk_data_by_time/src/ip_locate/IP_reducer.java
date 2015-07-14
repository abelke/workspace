package ip_locate;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class IP_reducer extends Reducer<Text, Text, Text, Text> {
	
	
  public void reduce(Text key, Text values, Context context) throws IOException, InterruptedException {
	  
	   System.out.println("**"+key);
	  context.write(key, values);
	  System.out.println("---"+key+"	"+values);
	  
  }
	}
