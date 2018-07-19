package com.garcon.garcon;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.login.LoginManager;
import com.garcon.garcon.Payment.CheckoutandPay;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

public class AppBarActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = AppBarActivity.class.getName();

    FragmentManager myFragmentManager;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private TabFragment tabFragment;


    private FrameLayout view_stub; //This is the framelayout to keep your content view
    private NavigationView navigation_view; // The new navigation view from Android Design Library. Can inflate menu resources. Easy
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Menu drawerMenu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_app_bar);// The base layout that contains your navigation drawer.
        view_stub = (FrameLayout) findViewById(R.id.view_stub);
        navigation_view = (NavigationView) findViewById(R.id.navigation_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0);
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerMenu = navigation_view.getMenu();
        for(int i = 0; i < drawerMenu.size(); i++) {
            drawerMenu.getItem(i).setOnMenuItemClickListener(this);
        }
        // and so on...
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /* Override all setContentView methods to put the content view to the FrameLayout view_stub
     * so that, we can make other activity implementations looks like normal activity subclasses.
     */
    @Override
    public void setContentView(int layoutResID) {
        if (view_stub != null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            View stubView = inflater.inflate(layoutResID, view_stub, false);
            view_stub.addView(stubView, lp);
        }
    }

    @Override
    public void setContentView(View view) {
        if (view_stub != null) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            view_stub.addView(view, lp);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (view_stub != null) {
            view_stub.addView(view, params);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        int id = item.getItemId();
        Intent intent = new Intent(getApplicationContext(), CheckoutandPay.class);
        Intent intent2 = new Intent(getApplicationContext(), MainViewCartActivity.class);
        switch (id) {
            case R.id.cartMenu:
                startActivity(intent2);
                //To Do
                break;
            case R.id.pay:
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent homeactivity = new Intent(getApplicationContext(), homeactivity.class);
                startActivity(homeactivity);
                break;

            case R.id.nav_profilesettings:
                Intent profile_settings = new Intent(getApplicationContext(), manageProfileActivity.class);
                startActivity(profile_settings);
                break;

            case R.id.nav_item_sent:
                Intent fav_activity = new Intent(getApplicationContext(), FavoriteActivity.class);
                startActivity(fav_activity);
                break;

            case R.id.nav_history:
                Intent history = new Intent(getApplicationContext(), OrderHistoryActivity.class);
                startActivity(history);
                break;

            case R.id.nav_settings:
                Intent settings = new Intent(getApplicationContext(), Settings.class);
                startActivity(settings);
                break;

            case R.id.nav_item_inbox:
                AlertDialog.Builder alert = new AlertDialog.Builder(AppBarActivity.this);


                alert.setMessage("Are you sure you want to logout ?");
                alert.setTitle("Confirmation");


                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //What ever you want to do with the value

                        try {
                            FragmentTransaction xfragmentTransaction = myFragmentManager.beginTransaction();
                            xfragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
                            Log.d(TAG, "menu item clicked");

                            // Firebase sign out
                            FirebaseAuth.getInstance().signOut();
                            mAuth.signOut();
                            finish();

                            // Google sign out
                            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                            finish();

                            //facebook sign out
                            LoginManager.getInstance().logOut();
                            finish();

                        } catch (Exception e) {
                            Log.d(TAG, e.getMessage());
                        }
                        startActivity(new Intent(AppBarActivity.this, LoginActivity.class));
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                        Log.d(TAG, "Cancel clicked");

                    }
                });

                alert.show();
                break;

        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
