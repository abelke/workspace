package demo;
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

public class Main 
{
	public Main() 
	{
	}
	public static void main(String[] args) 
	{
    		try
		{
    			
			IP2Location loc = new IP2Location();
			if (args.length == 2 || args.length == 3)  
			{
				loc.IPDatabasePath = args[0];
				if (args.length == 3)
				{
					loc.IPLicensePath = args[2];
				}
				IPResult rec = loc.IPQuery(args[1]);
				System.out.println(rec.getCountryLong());
				if ("OK".equals(rec.getStatus()))
				{
					System.out.println(rec);
				}
				else if ("EMPTY_IP_ADDRESS".equals(rec.getStatus()))
				{
					System.out.println("IP address cannot be blank.");
				}
				else if ("INVALID_IP_ADDRESS".equals(rec.getStatus()))
				{
					System.out.println("Invalid IP address.");
				}
				else if ("MISSING_FILE".equals(rec.getStatus()))
				{
					System.out.println("Invalid database path.");
				}
				else if ("IPV6_NOT_SUPPORTED".equals(rec.getStatus()))
				{
					System.out.println("This BIN does not contain IPv6 data.");
				}
				else
				{
					System.out.println("Unknown error." + rec.getStatus());
				}
				if (rec.getDelay() == true)
				{
					System.out.println("The last query was delayed for 5 seconds because this is an evaluation copy.");
				}
				System.out.println("Java Component: " + rec.getVersion());
			}
			else
			{
				System.out.println("Usage: Main <IPDatabasePath> <IPAddress> [IPLicensePath]");
				System.out.println(" ");
				System.out.println("   <IPDatabasePath>      Specify BIN data file");
				System.out.println("   <IPAddress>           Specify IP address");
				System.out.println("   [IPLicensePath]       Path of registration license file (optional)");
				System.out.println("                         * Please leave this field empty for unregistered version.");
				System.out.println(" ");
				System.out.println("URL: http://www.ip2location.com");
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
			e.printStackTrace(System.out);
		}
	}
}