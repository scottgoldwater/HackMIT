package com.example.proto.pebblego;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.PebbleKit.PebbleDataReceiver;
import com.getpebble.android.kit.util.PebbleDictionary;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements View.OnClickListener  {

	private final String TAG = "MainActivity";
    private String Location, Destination ;
    Button sendButton ;
    EditText LocationText,DestinationText;
    private ToggleButton langugeTog, bikeTog; 
    private static final int GO_ICON_KEY = 0;
    private static final int GO_DIRECTION_KEY = 3;
    private static final int GO_TIME_KEY = 4;
    private static final int CMD_KEY = 5;
    private static journey jour = null;

    private static final UUID GO_UUID = UUID.fromString("566288F3-B46E-4BED-A160-9257F525CF7D");
    private static int index = -1;

    
    public enum Directions {
        Left, Right 
    }
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        Button sendButton = (Button) findViewById(R.id.Send);
        LocationText = (EditText) findViewById(R.id.Start_position_input);
        DestinationText = (EditText) findViewById(R.id.destination_input);
        langugeTog = (ToggleButton) findViewById(R.id.languangeToggle); 
        bikeTog = (ToggleButton) findViewById(R.id.Walktoggle);
        sendButton.setOnClickListener(this);
        
        // Pebble event handler
        
        PebbleDataReceiver receiver = new PebbleDataReceiver(GO_UUID) {
			@Override
			public void receiveData(Context context, int transactionId,
					PebbleDictionary data) {
				Long direction = data.getUnsignedInteger(CMD_KEY);
				if (direction == 6)
				{
					// Key Up // Previous
					if (index <= 0) {
						sendDiretcionDataToWatch(2, jour.distance, "Departing from " + jour.getStartPoint());
					}
					if (index > 0) {
						index --;
						Step step = jour.steps.get(index);
						int dirIcon = getIconFromDirection(getDirectionFromString(step.manuver));
						sendDiretcionDataToWatch(dirIcon, step.duration, step.getDescr());
					}
				}
				else {
					// Key Down // Next
					if (index >= jour.steps.size()-1 )
					{
						sendDiretcionDataToWatch(3, "0 miles", "You reached " + jour.getEndPoint());
					}
					else{
						index ++;
						Step step = jour.steps.get(index);
						int dirIcon = getIconFromDirection(getDirectionFromString(step.manuver));
						sendDiretcionDataToWatch(dirIcon, step.duration, step.getDescr());
					}
				}

			}
		};
        PebbleKit.registerReceivedDataHandler(getApplicationContext(), receiver);
        
        
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
        String lang = "en";
        String mode = "walking";
        boolean isFrench = langugeTog.isChecked(); 
        boolean isBike = bikeTog.isChecked(); 
        if(isFrench)
        {
        	lang = "fr";
        }
        
        if(isBike)
        {
        	mode = "bicycling";
        }
        
        try {
			jour = directions(Location, Destination, lang, mode);
			updateDirection(v);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    
    
    //FINAL STRINGS
    public static final String french = "fr";
    public static final String english = "en";
    public static final String walk = "walking";
    public static final String bike = "bicycling";
    
    
    private static journey directions(String start, String dest, String language, String mode) throws IOException, JSONException {
    	String URLString= "http://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&sensor=true&mode=%s&language=%s";	
    	//String URLString = "http://maps.googleapis.com/maps/api/directions/json?origin=77%20Massachusetts%20Ave%20Cambridge,%20MA%2002139&destination=1%20Silber%20Way%20Boston%20MA%2002215&sensor=true&mode=walking&language=en";
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
    	start = URLEncoder.encode(start, "ISO-8859-1");
    	dest = URLEncoder.encode(dest, "ISO-8859-1");
    	mode = URLEncoder.encode(mode, "ISO-8859-1");
    	language = URLEncoder.encode(language, "ISO-8859-1");
    	  URLString = String.format(URLString, start, dest, mode, language);
    	   
    	  android.util.Log.i("Scott", URLString);
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
    		  
    		  JSONArray route = jObj.optJSONArray("routes");
    		  if(route==null)
    		  {
    			  return new journey ("404 address not found", "", "");
    			  
    		  }
    		  
    		  JSONArray legs = route.getJSONObject(0).getJSONArray("legs");
    		  
    		  JSONObject leg = legs.getJSONObject(0);
    		  
    		  JSONArray steps = leg.getJSONArray("steps");
    		  
    		  journey trip = new journey (leg.getJSONObject("distance").getString("text"), leg.getString("start_address"), leg.getString("end_address"));
    		  
    		  
    		  for(int i = 0; i<steps.length(); i++)
    		  {
    			  
    			  JSONObject temp = steps.getJSONObject(i);
    			  android.util.Log.i("Scott", temp.toString());
    			  Step step = new Step(Float.parseFloat(temp.getJSONObject("start_location").getString("lat")),
    					  Float.parseFloat(temp.getJSONObject("start_location").getString("lng")),
    					  Float.parseFloat(temp.getJSONObject("end_location").getString("lat")),
    					  Float.parseFloat(temp.getJSONObject("end_location").getString("lng")),
    					  temp.getString("html_instructions"),temp.getJSONObject("duration").getString("text"),temp.optString("maneuver"));
    			  trip.addStep(step);
    		  }
    		  
    	return trip;
    }
    
    public void updateDirection(View view) {
		doStepUpdate();
    }

    public void sendDiretcionDataToWatch(int directionIcon, String ditance , String dirc) {
        PebbleDictionary data = new PebbleDictionary();
        data.addUint8(GO_ICON_KEY, (byte) directionIcon);
        data.addString(GO_TIME_KEY, ditance);
        data.addString(GO_DIRECTION_KEY, dirc);

        PebbleKit.sendDataToPebble(getApplicationContext(), GO_UUID, data);
    }

    public void doStepUpdate() {
    	
        int directionIcon = getIconFromDirection(Directions.Left);
        sendDiretcionDataToWatch(directionIcon, jour.distance, "Press the side toggle buttons to go back and forth");
    }
  
    private Directions getDirectionFromString(String manuve) {
    	if (manuve.contains("right")) {
    		return Directions.Right;
    	}
    	else
    		return Directions.Left;
    	
    }

    private int getIconFromDirection(Directions dir) {
       switch(dir) {
       case Left :
    	   return 0;
       case Right :
    	   return 1;
       }
	return -1;
    }
 }