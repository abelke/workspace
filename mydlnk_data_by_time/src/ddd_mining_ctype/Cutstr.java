package ddd_mining_ctype;

public class Cutstr {
	
    String input;
    String find;
    String result;
    Cutstr(String input2, String find) {
        this.input = input2;
        this.find =find;
           }
    
    String exchange(String splitchar) { 
        int finstart = input.indexOf(find);
        String splitstart = "=";
        int cutfromsym = input.indexOf(splitstart, finstart);
        int cutendsym = input.indexOf(splitchar, finstart);
        String mydlinklite = "mydlink";
        
    	if (finstart != -1 && cutfromsym >=0 && cutendsym >=0) {
    		//input.indexOf(splitchar, input.indexOf(find));
    		
    		result = input.substring(cutfromsym+1, cutendsym);
    		if(result.length() != 0){
    			if(result.equals(mydlinklite)){
    				result = input.substring(cutfromsym+1, input.indexOf(",", finstart));
    				return result;
    				}else{
    					if (find.equals("ctype=")&&result.endsWith("1")){
    						result = "Local";
    						return result;
    					}else if(find.equals("ctype=")&&result.endsWith("2")){
    						result = "UPnP";
    						return result;
    					}else if(find.equals("ctype=")&&result.endsWith("4")){
    						result = "Relay";
    						return result;
    					}else
    					return result;
    				}
    						
    		}else{
    			if (find.equals("ctype="))
    				result="no_ctype_data";
    			else if(find.equals("state="))
    				result="0";
    			else
    				result="error_data";
    			return result;
    		}
    	}else{
    		this.result = "error_from";
    		return result;
    	}
    		
    }
}