package nykon.oleg;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import kong.unirest.json.JSONArray;;

@RestController
@RequestMapping("/rate")
public class RateController {
	@GetMapping
	public static int getRate() 	{
	
		try {
			HttpResponse<String> response = Unirest.get("https://api.coinbase.com/v2/prices/BTC-UAH/sell")
				.asString();
			if ((response.getStatus()!=200)){
			throw new RuntimeException("Htpp Response Code = " + response.getStatusText());
			}
			else {
				  JSONObject obj = new JSONObject(response.getBody());
			      int  btcToUAH = (int)obj.getJSONObject("data").getDouble("amount");
			      
		return btcToUAH;
			}
			
		}catch (Exception e ) {
			e.printStackTrace();
		}
		
		return 0;
	}
}
