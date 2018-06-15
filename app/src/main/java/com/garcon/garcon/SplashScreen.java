package com.garcon.garcon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Displays a splash screen when the app is launched.
 * Opens the login screen after 1000 milliseconds.
 */
public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent myIntent = new Intent(SplashScreen.this, homeactivity.class);
            startActivity(myIntent);
            finish();

        } else {
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    /* Create an Intent that will start the Login-Activity. */
                    Intent mainIntent = new Intent(SplashScreen.this,LoginActivity.class);
                    SplashScreen.this.startActivity(mainIntent);
                    SplashScreen.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
