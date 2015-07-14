package ddd_mining_ctype;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class Ddd_Ctype_Reducer extends Reducer<Text, Text, Text, Text> {
 // private Text result = new Text();
  
  public void reduce(Text key, Text values, Context context) throws IOException, InterruptedException {
	  context.write(key, values);
    
  }
	}
