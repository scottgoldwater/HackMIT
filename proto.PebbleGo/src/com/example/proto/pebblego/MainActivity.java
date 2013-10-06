package com.example.proto.pebblego;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.view.Menu;

import android.app.Activity;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements View.OnClickListener  {

	private final String TAG = "MainActivity";
    private String Location, Destination ;
    Button sendButton ;
    EditText LocationText,DestinationText;
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        Button sendButton = (Button) findViewById(R.id.Send);
        LocationText = (EditText) findViewById(R.id.Start_position_input);
        DestinationText = (EditText) findViewById(R.id.destination_input);

        sendButton.setOnClickListener(this);
/*
        MapView mapView = (MapView) findViewById(R.id.mapview); //or you can declare it directly with the API key
        Route route = directions(new GeoPoint((int)(26.2*1E6),(int)(50.6*1E6)), new GeoPoint((int)(26.3*1E6),(int)(50.7*1E6)));
        RouteOverlay routeOverlay = new RouteOverlay(route, Color.BLUE);
        mapView.getOverlays().add(routeOverlay);
        mapView.invalidate();*/

    }

    public void onClick(View v) {
        v.performHapticFeedback(350);
        Location = LocationText.getText().toString();
        Destination = DestinationText.getText().toString();
        //do action 
        try {
			directions(Location, Destination,"test","test");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    //public static String URLString= "http://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&sensor=true&mode=%s&language=%s";
    
    
    //FINAL STRINGS
    public static final String french = "fr";
    public static final String english = "en";
    public static final String walk = "walking";
    public static final String bike = "bicycling";
    
    
    private static journey directions(String start, String dest, String language, String mode) throws IOException, JSONException {
    	
    	String URLString = "http://maps.googleapis.com/maps/api/directions/json?origin=345%20Marlborough%20Street%20Boston,%20MA%2002115&destination=30%20Fairfield%20Street%20Boston,%20MA%2002116&sensor=true&mode=walking&language=en";
    //     Parser parser;
    //     //https://developers.google.com/maps/documentation/directions/#JSON <- get api
    //     String jsonURL = "http://maps.googleapis.com/maps/api/directions/json?";
    //     final StringBuffer sBuf = new StringBuffer(jsonURL);
    //     sBuf.append("origin=");
    //     sBuf.append(start.getLatitudeE6()/1E6);
    //     sBuf.append(',');
    //     sBuf.append(start.getLongitudeE6()/1E6);
    //     sBuf.append("&destination=");
    //     sBuf.append(dest.getLatitudeE6()/1E6);
    //     sBuf.append(',');
    //     sBuf.append(dest.getLongitudeE6()/1E6);
    //     sBuf.append("&sensor=true&mode=driving");
    //     parser = new GoogleParser(sBuf.toString());
    //     Route r =  parser.parse();
    	//String.format(URLString, start,dest,mode,language);
    	
    	URL url = new URL(URLString);

    	
    	HttpURLConnection conn = (HttpURLConnection)url.openConnection();
    	  if (conn.getResponseCode() != 200) {
    		    throw new IOException(conn.getResponseMessage());
    		  }
    	  
    	  //GET REQUEST
    	  BufferedReader rd = new BufferedReader(
    		      new InputStreamReader(conn.getInputStream()));
    		  StringBuilder sb = new StringBuilder();
    		  String line;
    		  while ((line = rd.readLine()) != null) {
    		    sb.append(line);
    		  }
    		  rd.close();
    		  conn.disconnect();
    	
    		  
    		  JSONObject jObj = new JSONObject(sb.toString());
    		  
    		  JSONArray route = jObj.getJSONArray("routes");
    		  
    		  JSONArray legs = route.getJSONObject(0).getJSONArray("legs");
    		  
    		  JSONObject leg = legs.getJSONObject(0);
    		  
    		  JSONArray steps = leg.getJSONArray("steps");
    		  
    		  journey trip = new journey (leg.getJSONObject("distance").getString("text"), leg.getString("start_address"), leg.getString("end_address"));
    		  
    		  android.util.Log.i("Scott", trip.toString());
    		  for(int i = 0; i<steps.length(); i++)
    		  {
    			  
    			  JSONObject temp = steps.getJSONObject(i);
    			  android.util.Log.i("Scott", temp.toString());
    			  Step step = new Step(Float.parseFloat(temp.getJSONObject("start_location").getString("lat")),
    					  Float.parseFloat(temp.getJSONObject("start_location").getString("lng")),
    					  Float.parseFloat(temp.getJSONObject("end_location").getString("lat")),
    					  Float.parseFloat(temp.getJSONObject("end_location").getString("lng")),
    					  temp.getString("html_instructions"),temp.getJSONObject("duration").getInt("value"));
    			  trip.addStep(step);
    		  }
    		  android.util.Log.i("Scott", trip.toString());
    	return trip;
    }
 }