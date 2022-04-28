import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.catalyst.advanced.CatalystAdvancedIOHandler;
import com.zc.component.object.ZCObject;
import com.zc.component.object.ZCRowObject;
import com.zc.component.zcql.ZCQL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ShopAIO implements CatalystAdvancedIOHandler {
	private static final Logger LOGGER = Logger.getLogger(ShopAIO.class.getName());
	
	private static String TABLENAME = "Location";
	private static String COLUMNNAME = "Place";
	JSONObject responseData = new JSONObject();
	static String GET = "GET";
	static String POST = "POST";
	
	@Override
	@SuppressWarnings("unchecked")
    public void runner(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String url = request.getRequestURI();
			String method = request.getMethod();

			//The GET API that checks the table for an alien encounter in that city
			if ((url.equals("/shop")) && method.equals(GET)) {
				String cityName = request.getParameter("city_name");

				//Queries the Catalyst Data Store table and checks whether a row is present for
				//the given city
				int length = getShopCountFromCatalystDataStore(cityName);
				if (length > 0) {
					responseData.put("message", "Uh oh! We have a store in your location! Enjoy Shopping!");
				} else {
					responseData.put("message", "Oops! We dont have a store in your location! Choose some other location..");
				}
			}
			//The POST API that reports the alien encounter for a particular city
			else if ((url.equals("/shop")) && method.equals(POST)) {
				//Gets the request body and parses it
				ServletInputStream requestBody = request.getInputStream();
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(requestBody, "UTF-8"));

				String cityName = (String) jsonObject.get("city_name");

				//Queries the Catalyst Data Store table and checks whether a row is present for
				//the given city
				int length = getShopCountFromCatalystDataStore(cityName);
				if (length > 0) {
					responseData.put("message",
							"Thank You!");
				}

				//If the row is not present, then a new row is inserted
				else {
					ZCRowObject row = ZCRowObject.getInstance();
					row.set("Place", cityName);
					ZCObject.getInstance().getTableInstance(TABLENAME).insertRow(row);
					responseData.put("message", "Thanks!");
				}
			}
			else if ((url.equals("/webhook")) && method.equals(POST)) {
				System.out.println("Testing done");
			}
			else {
			//The actions are logged. You can check the logs from Catalyst Logs.
				LOGGER.log(Level.SEVERE, "Error. Invalid Request");
				responseData.put("error", "Request Endpoint not found");
				response.setStatus(404);
			}

			//Sends the response back to the Client
			response.setContentType("application/json");
			response.getWriter().write(responseData.toString());
			response.setStatus(200);
		}
		catch(Exception e) {
			LOGGER.log(Level.SEVERE, "Exception in AlienCityAIO", e);
			responseData.put("error", "Internal server error occurred. Please try again in some time.");
			response.getWriter().write(responseData.toString());
			response.setStatus(500);
		}
        
	}
	
	private int getShopCountFromCatalystDataStore(String shopName) throws Exception {
		String query = "select * from " + TABLENAME + " where " + COLUMNNAME + " = " + shopName;

		//Gets the ZCQL instance and executes query using the query string
		ArrayList<ZCRowObject> rowList = ZCQL.getInstance().executeQuery(query);
		return rowList.size();
	}
	
}