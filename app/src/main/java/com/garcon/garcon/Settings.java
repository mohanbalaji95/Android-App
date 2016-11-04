package com.garcon.garcon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by cesar_000 on 11/2/2016.
 */

public class Settings extends AppCompatActivity {
    private static final String TAG = Settings.class.getName();
    DrawerLayout myDrawerLayout;
    NavigationView myNavigationView;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_settings);
        final Button mapbutton = (Button) findViewById(R.id.seeViewMap);
        final Button restaurantbutton = (Button) findViewById(R.id.seeViewRestaurant);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //System.out.println("onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    //System.out.println("onAuthStateChanged:signed_out");
                    finish();
                }
            }
        };
        mapbutton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent mapIntent = new Intent(Settings.this, homeactivity.class);
            Settings.this.startActivity(mapIntent);
        }
    });

        restaurantbutton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent restaurantIntent = new Intent(Settings.this, homeactivity.class);
            Settings.this.startActivity(restaurantIntent);
        }
    });

    myDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
    myNavigationView = (NavigationView) findViewById(R.id.profile);
    myNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(android.view.MenuItem menuItem) {


            if (menuItem.getItemId() == R.id.nav_profilesettings) {
                Intent profile_settings = new Intent(getApplicationContext(), ProfileSettings.class);
                startActivity(profile_settings);
            }
            if (menuItem.getItemId() == R.id.nav_item_sent) {
                Intent fav_activity = new Intent(getApplicationContext(), favorite_activity.class);
                startActivity(fav_activity);
            }

            if (menuItem.getItemId() == R.id.nav_history) {
                Intent history = new Intent(getApplicationContext(), History.class);
                startActivity(history);
            }

            if (menuItem.getItemId() == R.id.nav_settings) {
                Intent settings = new Intent(getApplicationContext(), Settings.class);
                startActivity(settings);
            }

            if (menuItem.getItemId() == R.id.nav_item_inbox) {
//                    FragmentTransaction xfragmentTransaction = myFragmentManager.beginTransaction();
//                    xfragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
                Log.d(TAG, "menu item clicked");
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                startActivity(new Intent(Settings.this, LoginActivity.class));
                //finishActivity(0);
            }


            myDrawerLayout.closeDrawers();
            return false;
        }
    });

    android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.set_toolbar);
    ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, myDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
    myDrawerLayout.addDrawerListener(mDrawerToggle);

    mDrawerToggle.syncState();


}
}
