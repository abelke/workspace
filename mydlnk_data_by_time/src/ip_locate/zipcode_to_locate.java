package ip_locate;
/** Main.java
* --------------------------------------------------------------------------
* Title        : IP2Location Java Component Sample Code
* Description  : This Java code lookup the IP2Location info from an IP address.
*                Things that we can do with this script.
*                1. Redirect based on country 
*                2. Digital rights management 
*                3. Web log stats and analysis 
*                4. Auto-selection of country on forms 
*                5. Filter access from countries you do not do business with 
*                6. Geo-targeting for increased sales and click-through
*                7. And much, much more! 
*
* Requirements : Java SDK 1.4 or later
* Installation : a. Extract ip2location-java-component.zip to your local machine.
*                b. Open command prompt.
*                c. Execute command below to compile the sample code (both Windows & Linux)
*                      javac -classpath ../libs/ip2location.jar Main.java
*                d. Execute command below to get IP result (Windows)
*                      java -cp ../libs/ip2location.jar; Main IP2LOCATION-LITE-DB1.BIN 200.0.0.1 license.key
*                e. Execute command below to get IP result (Linux)
*                      java -cp ../libs/ip2location.jar: Main IP2LOCATION-LITE-DB1.BIN 200.0.0.1 license.key
*
* Author       : IP2Location.com
* URL          : http://www.ip2location.com
* Email        : sales@ip2location.com
*
* Copyright (c) 2002-2014 IP2Location.com
* ---------------------------------------------------------------------------
*/

import com.ip2location.*;


public class zipcode_to_locate {
	String ipliblocate="/home/hduser/Desktop/159/IP-COUNTRY-REGION-CITY-LATITUDE-LONGITUDE-ZIPCODE-TIMEZONE.BIN";
	String ipstring;
    String Country;
    String City;
    String Region;
    float Latitude=0;
    float Longitude; 


	
    zipcode_to_locate(String input) {
        this.ipstring = input;
           }
    
    
    String outlocate() throws Exception {    	
        IP2Location loc = new IP2Location();
    	loc.IPDatabasePath = ipliblocate;
    	IPResult rec = loc.IPQuery(String.valueOf(ipstring));
    	
    	Country = rec.getCountryShort();
    	City= rec.getCity().replaceAll(" ", "_");
    	Latitude=rec.getLatitude();
    	Longitude=rec.getLongitude();
    	Region = rec.getRegion();
    	
    	String result ;
    	result = Country+"	"+City+"	"+Region+"	"+Latitude+"	"+Longitude;
		return result;
    }

    

}
