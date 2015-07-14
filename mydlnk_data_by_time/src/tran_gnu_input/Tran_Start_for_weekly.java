package tran_gnu_input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Tran_Start_for_weekly {//將輸出分類，time(建立花費時間),durring(持續時間),data(傳輸量)
	public static void main(String args[])throws IOException
	{
		Integer[][] output1 = new Integer[24][3];//建立暫存用陣列
		Integer[][] output2 = new Integer[24][3];
		Integer[][] output3 = new Integer[24][3];
		Integer[][] outputZ = new Integer[24][3];
		Integer[][] output4 = new Integer[24][4];
		Integer[][] output5 = new Integer[24][5];
		String[][] date = new String[2][2];
		//String selectday = "day";
//		for(int i=0;i<=23;i++){
//			output[i][0]=i;
//			for(int j=1;j<17;j++){
//				output[i][j]=0;
//			}
//		}
		date[0][1]="1";
		date[1][1]="0";
		for(int i=0;i<=23;i++){//空白陣列寫入小時
			output1[i][0]=i;
			output2[i][0]=i;
			output3[i][0]=i;
			output4[i][0]=i;
			outputZ[i][0]=i;
			output5[i][0]=i;
			for(int j=1;j<3;j++){
				output1[i][j]=0;//清空陣列
				output2[i][j]=0;
				output3[i][j]=0;
				outputZ[i][j]=0;
			}
		}

//     	FileReader FileStream=new FileReader("/home/hduser/Desktop/test/gnutest/input/timeout");//本機測試用位置
//		FileReader FileStream2=new FileReader("/home/hduser/Desktop/test/gnutest/input/duringout");
//		FileReader FileStream3=new FileReader("/home/hduser/Desktop/test/gnutest/input/dataout");
		FileReader FileStream=new FileReader("/mnt/data/out/gpinput/"+args[0]+"/timeout");   //讀取分類完畢檔案的位置
		FileReader FileStream2=new FileReader("/mnt/data/out/gpinput/"+args[0]+"/duringout");
		FileReader FileStream3=new FileReader("/mnt/data/out/gpinput/"+args[0]+"/dataout");

		@SuppressWarnings("resource")
		BufferedReader BufferedStream1=new BufferedReader(FileStream);
		@SuppressWarnings("resource")
		BufferedReader BufferedStream2=new BufferedReader(FileStream2);
		@SuppressWarnings("resource")
		BufferedReader BufferedStream3=new BufferedReader(FileStream3);
        String data;
		 String patternStr ="(.+)\\t(.+)\\t(.+)\\t(.+)";//(.+)\\-(.+\\-.+)\\t(.+)\\t(.+)\\t(.+) //切字串
		 Pattern pattern = Pattern.compile(patternStr);

		 do{ //loading timeout data and all connect data
			 data=BufferedStream1.readLine();
			 //System.out.println(data);
			 if(data==null)
				 break;          // 讀到檔案結束
			 Matcher matcher = pattern.matcher(data);
			 boolean matchFound = matcher.find();
			 if(matchFound==true) {//字串切割正確依序放入陣列
///      Mar-1-05	A	0	25 //輸入檔案的格式					 
				 if(matcher.group(1).toString().substring(0, matcher.group(1).toString().length()-2).equalsIgnoreCase(date[0][0])){//計算該檔中最高天數的相同日期,保留最高及次高天數的日期 （錯誤日期的天數在同一日的log比率極低,先忽略）
					 date[0][1]=String.valueOf(Integer.valueOf(date[0][1])+1);
				 }else if(matcher.group(1).toString().substring(0, matcher.group(1).toString().length()-2).equalsIgnoreCase(date[1][0])){
					 date[1][1]=String.valueOf(Integer.valueOf(date[1][1])+1);
				 }else{
					 if(Integer.valueOf(date[1][1])>Integer.valueOf(date[0][1])){
						 date[0][0]=matcher.group(1).toString().substring(0, matcher.group(1).toString().length()-2);
						 date[0][1]="1";
					 }else{
						 date[1][0]=matcher.group(1).toString().substring(0, matcher.group(1).toString().length()-2);
						 date[1][1]="1";
					 }
				 }

				 
				 if(matcher.group(2).equals("A") && matcher.group(3).equals("0")){//放入陣列中相對的位置 
					 output1[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][1]=output1[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][1]+Integer.valueOf(matcher.group(4).toString());
				 }else if(matcher.group(2).equals("B") && matcher.group(3).equals("0")){
					 output2[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][1]=output2[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][1]+Integer.valueOf(matcher.group(4).toString());
				 }else if(matcher.group(2).equals("A") && matcher.group(3).equals("1")){
					 output1[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][2]=output1[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][2]+Integer.valueOf(matcher.group(4).toString());
				 }else if(matcher.group(2).equals("B") && matcher.group(3).equals("1")){
					 output2[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][2]=output2[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][2]+Integer.valueOf(matcher.group(4).toString());
					 
				 }		 
//			 }else{} 
			 }else{
				   	System.out.println("error2");
				   }
			 } while(true);
		 
		 for(int i=0;i<=23;i++){
				int alltime_http=output1[i][1]+output2[i][1];
				int alltime_legacy=output1[i][2]+output2[i][2];
				
				int alltime_less=(int)((((double)output1[i][1]+(double)output1[i][2])));//計算比率
				int alltime_more=(int)((((double)output2[i][1]+(double)output2[i][2])));
				output2[i][1]=(int)((double)(alltime_less)/(double)(alltime_less+alltime_more)*100);
				output2[i][2]=(int)((double)(alltime_more)/(double)(alltime_less+alltime_more)*100);
				output1[i][1]=alltime_http;
				output1[i][2]=alltime_legacy;
				

		}

//		 PrintWriter out2 = new PrintWriter(new File("/home/hduser/Desktop/test/gnutest/input/timeoutA"));
//		 PrintWriter out1 = new PrintWriter(new File("/home/hduser/Desktop/test/gnutest/input/totalAA"));
		 PrintWriter out2 = new PrintWriter(new File("/mnt/data/out/gpinput/"+args[0]+"/timeoutA"));//檔案輸出
		 PrintWriter out1 = new PrintWriter(new File("/mnt/data/out/gpinput/"+args[0]+"/total"));
		 PrintWriter out4 = new PrintWriter(new File("/mnt/data/out/gpinput/"+args[0]+"/timeoutA_week"));
		 PrintWriter out0 = new PrintWriter(new File("/mnt/data/out/gpinput/"+args[0]+"/total_week"));
		 
		 for(int i=0;i<=23;i++){

				for(int j=0;j<3;j++){
					out2.print(output2[i][j]+";");
					out1.print(output1[i][j]+";");

				}
				out2.println("");
				out1.println("");
			}
			String today = ""; 
			
			if(Integer.valueOf(date[1][1])>Integer.valueOf(date[0][1])){
				today=date[1][0];
			 }else{
				 today=date[0][0];
			 }
			today=today.replaceAll("--", "-");
			
			//System.out.println(today+"+"+date[0][0]+"="+date[0][1]+"**"+date[1][0]+"="+date[1][1]);
			for(int i=0;i<=23;i++){
				out4.print(today);
				out0.print(today);
				for(int j=0;j<3;j++){
					out4.print(output2[i][j]+";");
					out0.print(output1[i][j]+";");
				}
				out4.println("");
				out0.println("");
			}
			
			
			out2.close();
			out1.close();
			out0.close();
			out4.close();
			
			for(int i=0;i<=23;i++){
				output1[i][0]=i;
				output2[i][0]=i;
				for(int j=1;j<3;j++){
					output1[i][j]=0;
					output2[i][j]=0;
				}
			}
		 
		 do{ //以下作法同上,差別為讀入的資料不同,而放到不同的輸出位置
			 data=BufferedStream2.readLine();

			 if(data==null)
				 break;          // 讀到檔案結束
			 Matcher matcher = pattern.matcher(data);
			 boolean matchFound = matcher.find();
			 if(matchFound==true) {
			 /*System.out.println("m01=="+matcher.group(1));//day
	    	   System.out.println("m02=="+matcher.group(2));//hour
	    	   System.out.println("m03=="+matcher.group(3));// input type ()
	    	   System.out.println("m04=="+matcher.group(4));// http(0) or Legacy(1)
	    	   System.out.println("m05=="+matcher.group(5));// type data*/  
			 
//			 if(matcher.group(1).endsWith(selectday)){//select day (22 only for test)
				 
				 if(matcher.group(2).equals("A") && matcher.group(3).equals("0")){
					 output1[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][1]=output1[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][1]+Integer.valueOf(matcher.group(4).toString());
				 }else if(matcher.group(2).equals("B") && matcher.group(3).equals("0")){
					 output2[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][1]=output2[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][1]+Integer.valueOf(matcher.group(4).toString());
				 }else if(matcher.group(2).equals("C") && matcher.group(3).equals("0")){
					 output3[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][1]=output3[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][1]+Integer.valueOf(matcher.group(4).toString());
				 }else if(matcher.group(2).equals("A") && matcher.group(3).equals("1")){
					 output1[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][2]=output1[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][2]+Integer.valueOf(matcher.group(4).toString());
				 }else if(matcher.group(2).equals("B") && matcher.group(3).equals("1")){
					 output2[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][2]=output2[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][2]+Integer.valueOf(matcher.group(4).toString());
				 }else if(matcher.group(2).equals("C") && matcher.group(3).equals("1")){
					 output3[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][2]=output3[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][2]+Integer.valueOf(matcher.group(4).toString());
				 }		 
//			 }else{} 
			 }else{
				   	System.out.println("error3");
				   }
			 } while(true);
		 
		 for(int i=0;i<=23;i++){
			double duringA=output1[i][1]+output1[i][2];
			double duringB=output2[i][1]+output2[i][2];
			double duringC=output3[i][1]+output3[i][2];
			double total = duringA+duringB+duringC;
			output4[i][1]=(int)(duringA/(total)*100);
			output4[i][2]=(int)(duringB/(total)*100);
			output4[i][3]=(int)(duringC/(total)*100);	
			}
		 

		 
		 PrintWriter out3 = new PrintWriter(new File("/mnt/data/out/gpinput/"+args[0]+"/duringoutA"));
		 PrintWriter out5 = new PrintWriter(new File("/mnt/data/out/gpinput/"+args[0]+"/duringoutA_week"));
//		 PrintWriter out3 = new PrintWriter(new File("/home/hduser/Desktop/test/gnutest/input/duringoutA"));
			for(int i=0;i<=23;i++){
				for(int j=0;j<4;j++){
					out3.print(output4[i][j]+";");
				}
				out3.println("");
			}
		

			for(int i=0;i<=23;i++){
				out5.print(today);
				for(int j=0;j<4;j++){
					out5.print(output4[i][j]+";");
				}
				out5.println("");
			}
			out3.close();
			out5.close();
		 
			for(int i=0;i<=23;i++){
				output1[i][0]=i;
				output2[i][0]=i;
				output3[i][0]=i;
				for(int j=1;j<3;j++){
					output1[i][j]=0;
					output2[i][j]=0;
					output3[i][j]=0;
					output4[i][j]=0;
				}
			}
			for(int i=0;i<=23;i++){
				output4[i][3]=0;
			}
	 
			
			
		 do{ 
			 data=BufferedStream3.readLine();
			 if(data==null)
				 break;          // 讀到檔案結束
			 Matcher matcher = pattern.matcher(data);
			 boolean matchFound = matcher.find();
			 if(matchFound==true) {
			 /*System.out.println("m01=="+matcher.group(1));//day
	    	   System.out.println("m02=="+matcher.group(2));//hour
	    	   System.out.println("m03=="+matcher.group(3));// input type ()
	    	   System.out.println("m04=="+matcher.group(4));// http(0) or Legacy(1)
	    	   System.out.println("m05=="+matcher.group(5));// type data*/  
			 
//			 if(matcher.group(1).endsWith(selectday)){//select day (22 only for test)
				 
				 if(matcher.group(2).equals("A") && matcher.group(3).equals("0")){
					 output1[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][1]=output1[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][1]+Integer.valueOf(matcher.group(4).toString());
				 }else if(matcher.group(2).equals("B") && matcher.group(3).equals("0")){
					 output2[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][1]=output2[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][1]+Integer.valueOf(matcher.group(4).toString());
				 }else if(matcher.group(2).equals("C") && matcher.group(3).equals("0")){
					 output3[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][1]=output3[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][1]+Integer.valueOf(matcher.group(4).toString());
				 }else if(matcher.group(2).equals("Z") && matcher.group(3).equals("0")){
					 outputZ[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][1]=outputZ[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][1]+Integer.valueOf(matcher.group(4).toString());
				 }else if(matcher.group(2).equals("A") && matcher.group(3).equals("1")){
					 output1[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][2]=output1[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][2]+Integer.valueOf(matcher.group(4).toString());
				 }else if(matcher.group(2).equals("B") && matcher.group(3).equals("1")){
					 output2[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][2]=output2[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][2]+Integer.valueOf(matcher.group(4).toString());
				 }else if(matcher.group(2).equals("C") && matcher.group(3).equals("1")){
					 output3[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][2]=output3[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][2]+Integer.valueOf(matcher.group(4).toString());
				 }else if(matcher.group(2).equals("Z") && matcher.group(3).equals("1")){
					 outputZ[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][2]=outputZ[Integer.valueOf(matcher.group(1).toString().substring(matcher.group(1).toString().length()-2))][2]+Integer.valueOf(matcher.group(4).toString());
				 }		 
//			 }else{
//				 //System.out.println(matcher.group(1));
//			 } 
			 }else{
				   	System.out.println("error4");
				   }
			 } while(true);
		 
		 for(int i=0;i<=23;i++){
			double dataA=output1[i][1]+output1[i][2];
			double dataB=output2[i][1]+output2[i][2];
			double dataC=output3[i][1]+output3[i][2];
			double dataZ=outputZ[i][1]+outputZ[i][2];
			double total = dataA+dataB+dataC+dataZ;
			output5[i][1]=(int)(dataA/(total)*100);
			output5[i][2]=(int)(dataB/(total)*100);
			output5[i][3]=(int)(dataC/(total)*100);	
			output5[i][4]=(int)(dataZ/(total)*100);	
			}
		 
		// PrintWriter out6 = new PrintWriter(new File("/user/abel/login_data_mining/gnutest/input/dataoutA"));
		 PrintWriter out6 = new PrintWriter(new File("/mnt/data/out/gpinput/"+args[0]+"/dataoutA"));
		 PrintWriter out7 = new PrintWriter(new File("/mnt/data/out/gpinput/"+args[0]+"/dataoutA_week"));
//		 PrintWriter out6 = new PrintWriter(new File("/home/hduser/Desktop/test/gnutest/input/dataoutA"));
			for(int i=0;i<=23;i++){
				for(int j=0;j<5;j++){
					out6.print(output5[i][j]+";");
				}	
				out6.println("");
			}
			out6.close();
			
			for(int i=0;i<=23;i++){
				out7.print(today);
				for(int j=0;j<5;j++){
					out7.print(output5[i][j]+";");
				}
				out7.println("");
			}
			out7.close();



//		 System.out.println("==============================================================");
//	        PrintWriter pw1 = new PrintWriter(new File("/user/abel/login_data_mining/gnutest/input/inputforgnu"));
//	        
//			for(int i=0;i<=23;i++){
//				for(int j=0;j<17;j++){
//					System.out.print(output[i][j]+" ");
//					pw1.print(output[i][j]+" ");
//				}
//				System.out.println(" ");
//				pw1.println(" ");
//			}
//			pw1.close();
		 }
	    }
	
