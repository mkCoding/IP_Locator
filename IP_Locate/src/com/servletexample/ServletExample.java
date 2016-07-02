package com.servletexample;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.webservicex.GeoIP;
import net.webservicex.GeoIPService;
import net.webservicex.GeoIPServiceSoap;

public class ServletExample extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		/*
		 * make a call to webservice
		 * http://www.webservicex.net/geoipservice.asmx?WSDL
		 */

		//this ensures that null ip address are not passed in for processing
		req.setAttribute("ipAddress", req.getParameter("ipAddress"));

		// holds the IP passed in by user
		String userIP = req.getParameter("ipAddress");
		
		//class used to take IP address for processing
		GeoIP geoIP = null;
		
		try {
			if (userIP.length() == 1 ) {
				System.out.println("You need to pass in one IP address");

			} else {

				// to access remote service call
				// we need a service endpoint interface
				// stub.getCountryName (ipAddress)

				// use method of this class to get instance of port
				GeoIPService ipService = new GeoIPService();

				// this is the stub/ port-> local object representing a remote
				// service
				GeoIPServiceSoap geoIPServiceSoap = ipService.getGeoIPServiceSoap();

				// this returns return type
				/*sometimes a vaild IP will not be processed because of webservice problem*/
				geoIP = geoIPServiceSoap.getGeoIP(userIP);

				// displays country name in console
				System.out.println(geoIP.getCountryName());
				System.out.println(geoIP.getCountryCode());
				
				
				//this variable will store the location based on given ip 
				String ipLocation = geoIP.getCountryName();

				//this will handle Taiwan and omit ROC string
				if (ipLocation.contains("Taiwan; Republic of China (ROC)"))
				{
					ipLocation ="Taiwan";
				}
				
				/*
				 * when the user submits the IP address the follwing page is returned
				 */
				resp.getWriter().println("<html>");
				resp.getWriter().println("<head>");
				resp.getWriter().println("<title> This is the response</title>");
				
				/*this will write IP address to the web page*/
				resp.getWriter().println("IP Address Entered: " + userIP); 
				
				/*ensure all country names will be displayed correctly
				*before presenting to the UI
				*/
				if (ipLocation.contains("Russian Federation"))
				{
					ipLocation ="Russia";
					
				}
				
				if(ipLocation.contains("Viet Nam")){
					ipLocation ="Vietnam";
				}
				if(ipLocation.contains("Korea Republic of")){
					ipLocation ="South Korea";
				}
				
				
				
				/*display location of IP address to the user on new page*/
				resp.getWriter().println("<br><p class =\"posReturnLocation\">Your IP location is:<br><b>"+ ipLocation + "</b> </p>");
				
				
				
				//creates button used to return to homepage
				String backButton = "<br><input id =\"back\" Type=\"submit\" VALUE=\"Back\" onClick=\"history.go(-1);return true;\"/>";
				resp.getWriter().println(backButton);
				
				//if IP is found but location is reserved display the reserve pic
				if (ipLocation==null || ipLocation.contains("Reserved"))
				{
					ipLocation = "question2";
				}
				
				//set ipLocation to method replacing spaces with underscore(_)
				if(ipLocation.contains(" ")){
					ipLocation = displayImagesWithSpaces(ipLocation);	
					ipLocation = ipLocation.replace("_", " ");
				}
				
				
				//if IP location is found display the flag of the country
				String flagImage ="<img id =\"flagImage\" src=\"Countries/"+ipLocation+".png\" alt=\"Flag Image\" style=\"width:304px;height:200px; position:relative;left:850px;\">";
				resp.getWriter().println(flagImage);
				resp.getWriter().println("<link rel =\"stylesheet\" type =\"text/css\" href=\"styles.css\">");
				resp.getWriter().println("</head>");
//				resp.getWriter().println("<body><div class=\"wrap\"><div><img src=\"world_transparent.gif\" width=\"600\" height=\"600\"></div></div></body>");
				resp.getWriter().println("</html>");
			}
		} catch (NullPointerException npe) { /*if ip country return is null or ip not found show custom error page*/
			resp.getWriter().println("<html>");
			resp.getWriter().println("<head>");
			resp.getWriter().println("<title> Problem Processing IP</title>");

			resp.getWriter().println("Your IP is: " + userIP);
			
			String ipErrorString ="<p id=\"ipErrorFormat\">The IP address:<br> <b>"+userIP+"</b> <br> could not be processed </p>";
			resp.getWriter().println(ipErrorString);
			
			resp.getWriter().println("<br><p class =\"tryAgain\">Try another IP address</p><br>");
			
			String backButton = "<br><input id =\"backButtonNoProcess\" Type=\"submit\" VALUE=\"Back\" onClick=\"history.go(-1);return true;\"/>";
			resp.getWriter().println(backButton);
			
			resp.getWriter().println("<link rel =\"stylesheet\" type =\"text/css\" href=\"styles.css\">");
			resp.getWriter().println("</head>");
			resp.getWriter().println("</html>");
			
			
//			System.out.println(e.getMessage());
//			System.out.println(userIP.length());
		}
		catch(Exception se) /*the webservice is not able to process this IP*/
		{
			resp.getWriter().println("<html>");
			resp.getWriter().println("<head>");
			resp.getWriter().println("<title> Problem Processing IP</title>");

			resp.getWriter().println("Your IP is: " + userIP);
			
			String ipErrorString ="<p id=\"ipErrorFormat\">The IP address:<br> <b>"+userIP+"</b> <br> could not be processed </p>";
			resp.getWriter().println(ipErrorString);
			
			resp.getWriter().println("<br><p class =\"tryAgain\">Try another IP address</p><br>");
			
			String backButton = "<br><input id =\"backButtonNoProcess\" Type=\"submit\" VALUE=\"Back\" onClick=\"history.go(-1);return true;\"/>";
			resp.getWriter().println(backButton);
			
			resp.getWriter().println("<link rel =\"stylesheet\" type =\"text/css\" href=\"styles.css\">");
			resp.getWriter().println("</head>");
			resp.getWriter().println("</html>");
			
//			System.out.println(se.getMessage());
//			System.out.println(userIP.length());
		}

	
	}
	
	/*the image look up does not like spaces when pulling up img png
	 * so there for spaces removed (special cases)
	 */
	public String displayImagesWithSpaces(String ipLocation){
		
		String resultLocation ="";
		
		switch (ipLocation) {
	   
	    case "Antigua and Barbuda":
	        resultLocation = "Antigua_and_Barbuda";
	        break;
	    case "Burkina Faso":
	        resultLocation = "Burkina_Faso.png";
	        break;
	    case "Cape Verde":
	        resultLocation = "Cape_Verde";
	        break;
	    case "Central African Republic":
	        resultLocation = "Central_African_Republic";
	        break;
	    case "Costa Rica":
	        resultLocation = "Costa_Rica";
	        break;
	    case "Czech Republic":
	        resultLocation = "Czech_Republic";
	        break;
	    case "Democratic Republic of Congo":
	        resultLocation = "Democratic_Republic_of_Congo";
	        break;
	    case "Dominican Republic":
	        resultLocation = "Dominican_Republic";
	        break;
	    case "East Timor":
	        resultLocation = "East_Timor";
	        break;
	    case "European Union":
	        resultLocation = "European_Union";
	        break;
	    case "Equatorial Guinea":
	        resultLocation = "Equatorial_Guinea.png";
	        break;
	    case "El Salvador":
	        resultLocation = "El_Salvador";
	        break;
	    case "Ivory Coast":
	        resultLocation = "Ivory_Coast";
	        break;
	    case "Marshall Islands":
	        resultLocation = "Marshall_Islands";
	        break;
	    case "New Zealand":
	        resultLocation = "New_Zealand";
	        break;
	    case "North Korea":
	        resultLocation = "North_Korea";
	        break;
	    case "Papua New Guinea":
	        resultLocation = "Papua_New_Guinea";
	        break;
	    case "Puerto Rico":
	        resultLocation = "Puerto_Rico";
	        break;
	    case "Saint Vincent and the Grenadines":
	        resultLocation = "Saint_Vincent_and_the_Grenadines";
	        break;
	    case "Saint Kitts and Nevis":
	        resultLocation = "Saint_Kitts_and_Nevis";
	        break;     
	    case "San Marino":
	        resultLocation = "San_Marino";
	        break;     
	    case "Sao Tome and Principe":
	        resultLocation = "Sao_Tome_and_Principe";
	        break;     
	    case "Saudi Arabia":
	        resultLocation = "Saudi_Arabia";
	        break;     
	    case "Sierra Leone":
	        resultLocation = "Sierra_Leone";
	        break;     
	    case "Solomon Islands":
	        resultLocation = "Solomon_Islands";
	        break;
	    case "South Africa":
	        resultLocation = "South_Africa";
	        break;
	    case "South Korea":
	        resultLocation = "South_Korea";
	        break;
	    case "Sri Lanka.png":
	        resultLocation = "Sri_Lanka";
	        break;
	    case "Trinidad and Tobago":
	        resultLocation = "Trinidad_and_Tobago";
	        break;
	    case "United Arab Emirates":
	        resultLocation = "United_Arab_Emirates";
	        break;
	    case "United Kingdom":
	        resultLocation = "United_Kingdom";
	        break;
	    case "United States":
	        resultLocation = "United_States";
	        break;
	 
	    case "Western Sahara":
	        resultLocation = "Western_Sahara";
	        break;
	} 
		
		
		return resultLocation;
	}
}
