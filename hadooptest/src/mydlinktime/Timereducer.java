package mydlinktime;

import java.io.IOException;
//import org.apache.hadoop.io.IntWritable;
//import org.apache.hadoop.io.Text;


import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class Timereducer extends Reducer<Text, Text, Text, Text> {
    //private IntWritable result = new IntWritable();

    public void reduce(Text key, Text value, 
                       Context context
                       ) throws IOException, InterruptedException {
      //int sum = 0; // initialize the sum for each keyword
      

      context.write(key, value); // create a pair <keyword, number of occurences>
    }
	}
