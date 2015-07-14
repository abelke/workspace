package ip_locate;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class IP_con {
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration(); 
	    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs(); // get all args
	    if (otherArgs.length != 2) {
	      System.err.println("Usage: WordCount <in> <out>");
	      System.exit(2);
	    }

	    // create a job with name "wordcount"
	    Job job = new Job(conf, "Timecount");
	    job.setJarByClass(IP_con.class);
	    job.setMapperClass(IP_mapper.class);
	    System.out.println("13131321");
	    //job.setReducerClass(IP_reducer.class);
	    System.out.println("45645654645");
	    // uncomment the following line to add the Combiner
	   // job.setCombinerClass(IP_reducer.class);
	     

	    // set output key type   
	    job.setOutputKeyClass(Text.class);
	    
	    // set output value type
	    job.setOutputValueClass(Text.class);
	    
	    
	    
	    //set the HDFS path of the input data
	    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
	    // set the HDFS path for the output
	    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

	    //Wait till job completion
	    System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
	}
