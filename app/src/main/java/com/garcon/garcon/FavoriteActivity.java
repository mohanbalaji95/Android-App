package com.garcon.garcon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by siddhiparekh11 on 7/24/17.
 * Activity which displays the favourite items of the user.
 */

public class FavoriteActivity extends AppCompatActivity implements SecondFragment.RestaurantClickedListener{
    private static final String TAG = homeactivity.class.getName();
    DrawerLayout myDrawerLayout;
    NavigationView myNavigationView;
    FragmentManager myFragmentManager;
    FragmentTransaction myFragmentTransaction;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    private android.view.MenuItem mapItem;
    private android.view.MenuItem listItem;

    private FavFragment tabFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.favtoolbar);
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

        /*Setup the DrawerLayout and NavigationView
                */

        myDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        myNavigationView = (NavigationView) findViewById(R.id.profile);

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        myFragmentManager = getSupportFragmentManager();
        tabFragment = new FavFragment();
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
                    FragmentTransaction xfragmentTransaction = myFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
                    Log.d(TAG, "menu item clicked");
                    FirebaseAuth.getInstance().signOut();
                    LoginManager.getInstance().logOut();
                    startActivity(new Intent(FavoriteActivity.this, LoginActivity.class));
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
    private void openPrimaryFragment() {
       // ((ContainerFragment)tabFragment.getSelectedFragment(0)).switchFragments(true);
    }

    private void openSecondFragment() {
       // ((ContainerFragment)tabFragment.getSelectedFragment(0)).switchFragments(false);
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
    @Override
    public void onRestaurantClicked(Restaurant restaurant) {
        // ((RestaurantDetailFragment)tabFragment.getSelectedFragment(1)).dataSetup(restaurant);

        tabFragment.switchViewPagerPosition(0);
    }
}
