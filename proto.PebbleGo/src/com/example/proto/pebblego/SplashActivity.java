package com.example.proto.pebblego;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;

public class SplashActivity extends Activity {

        // Set the display time, in milliseconds (or extract it out as a configurable parameter)
        private final int SPLASH_DISPLAY_LENGTH = 2500;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);
        }

        @Override
        protected void onResume() {
            super.onResume();

            new Handler().postDelayed(new Runnable() {
	            @Override
	            public void run() {
	            //Finish the splash activity so it can't be returned to.
	            SplashActivity.this.finish();
	            // Create an Intent that will start the main activity.
	            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
	            SplashActivity.this.startActivity(mainIntent);
	            }
	        }, SPLASH_DISPLAY_LENGTH);
	    }
}
