package mydlinktest;

public class cuttest {
	
	    String input;
	    String find;
	    String result;
	    cuttest(String input2, String find) {
	        this.input = input2;
	        this.find =find;
	           }
	    
	    String exchange(String splitchar) {  // 兌換紅利點數時呼叫的方法
	        int finstart = input.indexOf(find);
	        String mydlinklite = "mydlink";
	        String splitstart = "=";
			
	    	if (finstart != -1) {
	    		//input.indexOf(splitchar, input.indexOf(find));
	    		
	    		result = input.substring(input.indexOf(splitstart, finstart)+1, input.indexOf(splitchar, finstart));
	    		if(result.length() != 0){
	    			if(result.equals(mydlinklite)){
	    				result = input.substring(input.indexOf(splitstart, finstart)+1, input.indexOf(",", finstart));
	    				return result;
	    		}else{
	    				return result;
	    			}
	    		}else{
	    			result="sdfsdfsdfsdfsdf";
	    			return result;
	    		}
	    			
	    	}else{
	    		this.result = "error";
	    		return result;
	    	}
	    		
	    }
}