package com.garcon.garcon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by cesar_000 on 11/6/2016.
 */

public class termsandconditions extends AppCompatActivity {
    private static final String TAG = termsandconditions.class.getName();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private DrawerLayout myDrawerLayout;
    private NavigationView myNavigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_terms_conditions);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        myDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        myNavigationView = (NavigationView) findViewById(R.id.profile);
//        setupDrawerContent(myNavigationView);
    }
//        myNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Define the listener
//        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                // Do something when action item collapses
//                return true;  // Return true to collapse action view
//            }
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                // Do something when expanded
//                return true;  // Return true to expand action view
//            }
//        };
//
//        // Get the MenuItem for the action item
//        MenuItem actionMenuItem = menu.findItem(R.id.action_cart);
//
//        // Assign the listener to that action item
//        MenuItemCompat.setOnActionExpandListener(actionMenuItem, expandListener);
//
//        // Any other things you have to do when creating the options menu…

        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_cart:
//                // User chose the "Settings" item, show the app settings UI...
//                return true;
//
//            case R.id.action_checkout:
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                return true;
//
//            default:
//                // If we got here, the user's action was not recognized.
//                // Invoke the superclass to handle it.
//                return super.onOptionsItemSelected(item);
//
//        }
//    }
}

//            @Override
//            public boolean onOptionsItemSelected(MenuItem item) {
//                // The action bar home/up action should open or close the drawer.
//                switch (item.getItemId()) {
//                    case android.R.id.home:
//                        mDrawer.openDrawer(GravityCompat.START);
//                        return true;
//                }
//                return super.onOptionsItemSelected(item);
//            }
//        }




















//            @Override
//            public boolean onNavigationItemSelected(android.view.MenuItem menuItem) {
//
//
//                if (menuItem.getItemId() == R.id.nav_profilesettings) {
//                    Intent profile_settings = new Intent(getApplicationContext(), ProfileSettings.class);
//                    startActivity(profile_settings);
//                }
//                if (menuItem.getItemId() == R.id.nav_item_sent) {
//                    Intent fav_activity = new Intent(getApplicationContext(), favorite_activity.class);
//                    startActivity(fav_activity);
//                }
//
//                if (menuItem.getItemId() == R.id.nav_history) {
//                    Intent history = new Intent(getApplicationContext(), History.class);
//                    startActivity(history);
//                }
//
//                if (menuItem.getItemId() == R.id.nav_settings) {
//                    Intent settings = new Intent(getApplicationContext(), Settings.class);
//                    startActivity(settings);
//                }
//
//                if (menuItem.getItemId() == R.id.nav_item_inbox) {
////                    FragmentTransaction xfragmentTransaction = myFragmentManager.beginTransaction();
////                    xfragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
//                    Log.d(TAG, "menu item clicked");
//                    FirebaseAuth.getInstance().signOut();
//                    LoginManager.getInstance().logOut();
//                    startActivity(new Intent(termsandconditions.this, LoginActivity.class));
//                    //finishActivity(0);
//                }
//
//
//                myDrawerLayout.closeDrawers();
//                return false;
//            }
//        });
//
//        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.terms_toolbar);
//        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, myDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
//        myDrawerLayout.addDrawerListener(mDrawerToggle);
//
//        mDrawerToggle.syncState();
//
//
//    }
////    android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.terms_toolbar);
//}
