package fp_tree;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*World [label = rtrtrtr]
World -> World11 [label = 123]; */

public class Turn_Graphviz {
    public static void main(String [] argv) throws IOException {
    	FileReader fr = new FileReader("/home/hduser/Desktop/test/webpathtree4/part-r-00000");
    	FileWriter fw = new FileWriter("/home/hduser/Desktop/test/webpathtree4/graphviv_out_ssl.txt");
    	BufferedReader br = new BufferedReader(fr);
    	/*
    	 * IOException {
        FileWriter fw = new FileWriter("test.txt");
        fw.write("test");
        fw.flush();
        fw.close();
    }
    	 * 
    	 * */
    	fw.write("digraph G {"+"\r\n");
    	while (br.ready()) {
    		String inputStr = br.readLine();
    		String patternStr = "(.+)\\s(.+)\\s(.+)\\s(.+)";
    		Pattern pattern = Pattern.compile(patternStr);
    		Matcher matcher = pattern.matcher(inputStr);
       	 boolean matchFound = matcher.find();
         if(matchFound==true) {
        	 if(Integer.valueOf(matcher.group(4).toString())>100){
         		String s1 = "\"" + matcher.group(3).toString()+"\""+" [label = \""+matcher.group(1).toString()+" "+matcher.group(4).toString()+"\"]";
         		fw.write(s1+"\r\n");
         		
         		if(!matcher.group(2).toString().equalsIgnoreCase("rootnode")){
         		String s2 = "\"" + matcher.group(2).toString()+"\""+" -> "+"\"" + matcher.group(3).toString()+"\"";
         		fw.write(s2+"\r\n"); }
        	 }else{
        		 //System.out.println(matcher.group(1).toString()+"+"+matcher.group(2).toString()+"+"+matcher.group(3).toString()+"+"+matcher.group(4).toString());
        	 }
    		
         }else{ 
        	 System.out.println("error");
         }
    		}
    	fw.write("}");
    	fw.close();
        fr.close();
    }
}
