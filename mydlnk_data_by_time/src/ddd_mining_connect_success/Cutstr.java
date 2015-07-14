package ddd_mining_connect_success;

public class Cutstr {//切割出需求
	
    String input;
    String find;
    String result;
    Cutstr(String input2, String find) {
        this.input = input2;//輸入的ddd字串
        this.find =find;//要尋找的標記
           }
    
    String exchange(String splitchar) { //切割字串部份
        int finstart = input.indexOf(find);//標記搜尋位置
        String splitstart = "=";//切割符號
        int cutfromsym = input.indexOf(splitstart, finstart);//標記位置用的兩個變數
        int cutendsym = input.indexOf(splitchar, finstart);
        String mydlinklite = "mydlink";
        
    	if (finstart != -1 && cutfromsym >=0 && cutendsym >=0) {//找到切割位置時
    		//input.indexOf(splitchar, input.indexOf(find));
    		
    		result = input.substring(cutfromsym+1, cutendsym);//剪出內容
    		if(result.length() != 0){//內容不惟0
    			if(result.equals(mydlinklite)){
    				result = input.substring(cutfromsym+1, input.indexOf(",", finstart));
    				return result;
    				}else if(find.equals("ctype=")&& result.length() == 1){
    					
    					return result;
    				}else
    					return result;		
    		}else{//內容為0的判斷
    			
    			if (find.equals("ctype="))//根據不同切割標記
    				result="ctype undefined";
    			else if(find.equals("state="))
    				result="0";
    			else
    				result="error_data";
    			return result;
    		}
    	}else{
    		this.result = "error_from";//未找到切割結果時的輸出
    		return result;
    	}
    		
    }
}