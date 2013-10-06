package com.example.proto.pebblego;

import android.os.Bundle;
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
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        directions(Location, Destination);
    }
    
    public String URL= "http://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&sensor=true&mode=%s&language=%s";
    
    
    //FINAL STRINGS
    public static final String french = "fr";
    public static final String english = "en";
    public static final String walk = "walking";
    public static final String bike = "bicycling";
    
    
    private static String[] directions(String start, String dest) {
    	
    	
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
    
    	String[] arr = {};  
    	return arr;
    }
 }