package weatherBot;
import org.jibble.pircbot.*;
import java.io.BufferedReader;
import java.io.IOException; 
import java.io.InputStreamReader; 
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

//Saaket Poray

public class MyBot extends PircBot {
    
    public MyBot() {
        this.setName("weatherNewsBot"); //username for logging in
    }
    
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (message.contains("!weather")) { //if message has "!weather"
        	String location = message.substring(9); //extracts the location
            try {
				sendMessage(channel, sender + ": " + getWeather(location)); //sends the weather in the chat for the particular location
			} catch (IOException e) {
				System.out.println("Error"); //used to debug
				e.printStackTrace();
			}
        }
        if (message.contains("!news")) { //if message has "!news"
        	try {
				sendMessage(channel, sender + ": " + getNews()); //calls getNews() method and sends news back to the user
			} catch (IOException e) {
				System.out.println("Error"); //used to debug
				e.printStackTrace();
			}
         }
        
    }
    
    public String getWeather(String location) throws IOException
    {
    	String APIkey = "&APPID=ENTER API KEY HERE"; //stores API key
		URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + location + "&units=imperial" + APIkey); //forms URL to extract info
		HttpURLConnection con = (HttpURLConnection) url.openConnection(); 
		con.setRequestMethod("GET");
		var jsonData = "";
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())); 
		String inputLine; 
		
		String description = "";
		
		while((inputLine = in.readLine()) != null)
		{
			jsonData = inputLine; //stores data into jsonData
			System.out.println(jsonData);
		}
		
		JSONObject object = null;
		try {
			object = (JSONObject) new JSONParser().parse(jsonData); //parses json data and stores into object
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JSONArray weather = (JSONArray)object.get("weather"); 
		JSONObject first = (JSONObject)weather.get(0); 
		description = first.get("description").toString(); //extracts description of the weather
		
		JSONObject mainW = (JSONObject)object.get("main");
		String temp = mainW.get("temp").toString(); //extracts the temperature

		String returnString = ("The current weather in " + location + " is " + description + " with a temperature of " + temp); //forms string to send back		

		in.close();
		return returnString; //returns weather message
    }
    
    public String getNews() throws IOException
    {
    	String APIkey = "ENTER API KEY HERE"; //stores API key
		URL url = new URL("http://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=" + APIkey); //forms URL to extract info
		HttpURLConnection con = (HttpURLConnection) url.openConnection(); 
		con.setRequestMethod("GET");
		var jsonData = "";
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())); 
		String inputLine; 
		
		String title1 = "";
		String title2 = "";
		
		while((inputLine = in.readLine()) != null)
		{
			jsonData = inputLine; //stores data into jsonData
			System.out.println(jsonData);
		}
		JSONObject object = null;
		try {
			object = (JSONObject) new JSONParser().parse(jsonData); //parses json data and stores into object
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		JSONArray articles = (JSONArray)object.get("articles");
		
		JSONObject first = (JSONObject)articles.get(0);  //gets first title
		title1 = first.get("title").toString();
		
		JSONObject second = (JSONObject)articles.get(1); //gets second title
		title2 = second.get("title").toString();
		
		String returnString = ("Title: "  + title1  + "  Title: " + title2); //forms string to send back		

		in.close();
    	return returnString; //returns news message
    }
    
}