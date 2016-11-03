package com.garcon.garcon;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.firebase.client.Firebase;
import com.firebase.client.Logger;
import com.firebase.client.core.view.View;
import com.google.android.gms.maps.MapFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class homeactivity extends AppCompatActivity {
    private static final String TAG = homeactivity.class.getName();
    DrawerLayout myDrawerLayout;
    NavigationView myNavigationView;
    FragmentManager myFragmentManager;
    FragmentTransaction myFragmentTransaction;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_homeactivity);
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


        /**
         *Setup the DrawerLayout and NavigationView
         */

        myDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        myNavigationView = (NavigationView) findViewById(R.id.profile);

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        myFragmentManager = getSupportFragmentManager();
        myFragmentTransaction = myFragmentManager.beginTransaction();
        myFragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
        /**
         * Setup click events on the Navigation View Items.
         */

        myNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {



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
                    FragmentTransaction xfragmentTransaction = myFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
                    Log.d(TAG, "menu item clicked");
                    FirebaseAuth.getInstance().signOut();
                    LoginManager.getInstance().logOut();
                    startActivity(new Intent(homeactivity.this, LoginActivity.class));
                    //finishActivity(0);
                }


                myDrawerLayout.closeDrawers();
                return false;
            }
        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, myDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        myDrawerLayout.addDrawerListener(mDrawerToggle);
        //myDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}