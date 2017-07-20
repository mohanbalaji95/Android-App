package com.garcon.garcon;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class homeactivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,SecondFragment.RestaurantClickedListener{
    private static final String TAG = homeactivity.class.getName();
    DrawerLayout myDrawerLayout;
    NavigationView myNavigationView;
    FragmentManager myFragmentManager;
    FragmentTransaction myFragmentTransaction;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private MenuItem mapItem;
    private MenuItem listItem;

    private TabFragment tabFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_homeactivity);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
        tabFragment = new TabFragment();
        myFragmentTransaction = myFragmentManager.beginTransaction();
        myFragmentTransaction.replace(R.id.containerView, tabFragment).commit();
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
                    try {
                        FragmentTransaction xfragmentTransaction = myFragmentManager.beginTransaction();
                        xfragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
                        Log.d(TAG, "menu item clicked");
                        FirebaseAuth.getInstance().signOut();
                        LoginManager.getInstance().logOut();
                        // Firebase sign out
                        mAuth.signOut();
finish();
                        // Google sign out
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient);

                    }
                    catch(Exception e)
                    {
                        String s=e.getMessage();
                    }
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

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, myDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerToggle.setDrawerIndicatorEnabled(true);

        myDrawerLayout.addDrawerListener(mDrawerToggle);
        //myDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        mapItem = menu.findItem(R.id.mapMenu);
        listItem = menu.findItem(R.id.listMenu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.cartMenu:
                //To.Do
                break;
            case R.id.mapMenu:
                mapItem.setVisible(false);
                listItem.setVisible(true);
                openPrimaryFragment();
                break;
            case R.id.listMenu:
                listItem.setVisible(false);
                mapItem.setVisible(true);
                openSecondFragment();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openPrimaryFragment() {
        ((ContainerFragment)tabFragment.getSelectedFragment(0)).switchFragments(true);
    }

    private void openSecondFragment() {
        ((ContainerFragment)tabFragment.getSelectedFragment(0)).switchFragments(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onRestaurantClicked(Restaurant restaurant) {
        ((RestaurantDetailFragment)tabFragment.getSelectedFragment(1)).dataSetup(restaurant);
        tabFragment.switchViewPagerPosition(1);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}