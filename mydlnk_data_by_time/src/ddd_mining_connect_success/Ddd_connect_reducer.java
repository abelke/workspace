package ddd_mining_connect_success;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class Ddd_connect_reducer extends Reducer<Text, Text, Text, Text> {
 // private Text result = new Text();
	
  public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
	  int localsum = 0;//計算各種連線成功數的統計
	  int relaysum = 0;
	  int upnpsum = 0;
	  int othersum = 0;
	  int Flocalsum = 0;//失敗數目的統計
	  int Frelaysum = 0;
	  int Fupnpsum = 0;
	  int Fothersum = 0;
	  
	  Text outwordV = new Text();

	  System.out.println("----"+key+"----"+values.toString());  //for test

	  
	  for (Text val : values) {
		  String patternStr = "(.+)\\s(.+)";//切割輸入字串
		  Pattern pattern = Pattern.compile(patternStr);
		  Matcher matcher = pattern.matcher(val.toString());
		  boolean matchFound = matcher.find();
		  System.out.println("qwe");//for test
		  System.out.println("**"+key+"--"+val.toString());
		  System.out.println("qwe");
		  System.out.println("@@"+matcher.group(1).toString()+"-"+matcher.group(2).toString());//group(1)為連線類型,group(2)為成功與否
		  if(matchFound==true) {//切割成功進入判斷 ,作連線類型及成功與否的數目統計
	
		  if(matcher.group(1).toString().indexOf("Local") != -1 || matcher.group(1).toString().equals("1")){
			  if(Integer.valueOf(matcher.group(2).toString())!=0)
				  localsum = localsum+Integer.valueOf(matcher.group(2).toString());
			  else if(Integer.valueOf(matcher.group(2).toString())==0)
				  Flocalsum = Flocalsum+1;			  	
		  }else if(matcher.group(1).toString().indexOf("UPnP")!= -1 || matcher.group(1).toString().equals("2")){
			  if(Integer.valueOf(matcher.group(2).toString())!=0)
				  upnpsum = upnpsum+Integer.valueOf(matcher.group(2).toString());
			  else if(Integer.valueOf(matcher.group(2).toString())==0)
				  Fupnpsum = Fupnpsum+1;
		  }else if(matcher.group(1).toString().indexOf("Relay")!= -1 || matcher.group(1).toString().equals("4") ){
			  if(Integer.valueOf(matcher.group(2).toString())!=0)
				  relaysum = relaysum+Integer.valueOf(matcher.group(2).toString());
			  else if(Integer.valueOf(matcher.group(2).toString())==0)
				  Frelaysum = Frelaysum+1;
		  }else{
			  if(Integer.valueOf(matcher.group(2).toString())!=0)
				  othersum = othersum+Integer.valueOf(matcher.group(2).toString());
			  else if(Integer.valueOf(matcher.group(2).toString())==0)
				  Fothersum = Fothersum+1;
		 }
        }else{
        	 System.out.println("error");
        }
//System.out.println("**"+key+"--"+val.toString());
//System.out.println("**"+matcher.group(1).toString()+"-"+matcher.group(2).toString());
//System.out.println("matcher.group(1):"+matcher.group(1).toString()+"&"+localsum+"*"+upnpsum+"*"+relaysum+"*"+othersum+"--"+Flocalsum+"*"+Fupnpsum+"*"+Frelaysum+"*"+Fothersum);



}
	  System.out.println(key);//for test
	  System.out.println("_____________________________________");
	  System.out.println("@"+localsum+"*"+upnpsum+"*"+relaysum+"*"+othersum);
	  System.out.println("@---"+Flocalsum+"*"+Fupnpsum+"*"+Frelaysum+"*"+Fothersum);
	  System.out.println("=======================================");
	  if(localsum>0){//統計個性數值並加總
		  outwordV.set("Local	1	"+localsum);//計算完的數字輸出
	  }else if(upnpsum>0){
		  outwordV.set("UPnP	1	"+upnpsum);
	  }else if(relaysum>0){
		  outwordV.set("Relay	1	"+relaysum);
	  }else if(othersum>0){
		  outwordV.set("unknown	1	"+othersum);
	  }else if(Flocalsum>0){
		  outwordV.set("Local	0	"+Flocalsum);
	  }else if(Fupnpsum>0){
		  outwordV.set("UPnP	0	"+Fupnpsum);
	  }else if(Frelaysum>0){
		  outwordV.set("Relay	0	"+Frelaysum);
	  }else if(Fothersum>0){
		  outwordV.set("unknown	0	"+Fothersum);
	  }else{
		  System.out.println("error");
		  outwordV.set("unconnect	0	0");
	  }
		
	  localsum = 0;
	  relaysum = 0;
	 upnpsum = 0;
	  othersum = 0;
	  Flocalsum = 0;
	  Frelaysum = 0;
	 Fupnpsum = 0;
	  Fothersum = 0;
	  System.out.println(key+"****"+outwordV);
	  System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	  context.write(key, outwordV);
	  outwordV.set("");


  }
	}
