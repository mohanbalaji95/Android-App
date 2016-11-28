package com.garcon.garcon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class favorite_activity extends AppCompatActivity {
    private static final String TAG = favorite_activity.class.getName();
    DrawerLayout myDrawerLayout;
    NavigationView myNavigationView;
    private ArrayList<String> mItems;
//    private CardViewAdapter mAdapter;
    private List<favcardview> favcardviews;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_activity);
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
                Intent mapIntent = new Intent(favorite_activity.this, homeactivity.class);
                favorite_activity.this.startActivity(mapIntent);
            }
        });
        restaurantbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent restaurantIntent = new Intent(favorite_activity.this, SecondFragment.class);
                favorite_activity.this.startActivity(restaurantIntent);
            }
        });


        Intent intent = getIntent();
        RecyclerView rv = (RecyclerView)findViewById(R.id.recycleview);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);
        String value = sharedPreferences.getString("fav_string",null);
        if(value!=null){
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            favcardview favcardarray[] = gson.fromJson(value, favcardview[].class);


            favcardviews = new ArrayList<>();
            for(int i =0 ;i<favcardarray.length;i++){
                favcardviews.add(new favcardview(favcardarray[i].name,R.drawable.la_vic));
            }
        }
        else{
            favcardviews = new ArrayList<>();
        }

        /*favcardviews.add(new favcardview("LA VIC",R.drawable.la_vic));
        favcardviews.add(new favcardview("Panda Express",R.drawable.la_vic));
        favcardviews.add(new favcardview("Chat Cuisinie", R.drawable.la_vic));
        favcardviews.add(new favcardview("sagar samrat", R.drawable.la_vic));
        favcardviews.add(new favcardview("kamat", R.drawable.la_vic));
        favcardviews.add(new favcardview("peacock", R.drawable.la_vic));
        favcardviews.add(new favcardview("bangalore cafe", R.drawable.la_vic));
        favcardviews.add(new favcardview("star bucks", R.drawable.la_vic));
        favcardviews.add(new favcardview("Chat Cuisinie", R.drawable.la_vic));*/
        final favcardadapter adapter = new favcardadapter(favcardviews);
        rv.setAdapter(adapter);


        /*SharedPreferences.Editor spEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        spEditor.putString("fav_string",gson.toJson(favcardviews));
        spEditor.commit();*/

        /****************/
        swipablerecyclerview swipeTouchListener =
                new swipablerecyclerview(rv,
                        new swipablerecyclerview.SwipeListener() {
                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
//                                    Toast.makeText(MainActivity.this, mItems.get(position) + " swiped left", Toast.LENGTH_SHORT).show();
                                    favcardviews.remove(position);
                                    adapter.notifyItemRemoved(position);
                                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);
                                    SharedPreferences.Editor spEditor = sharedPreferences.edit();
                                    GsonBuilder gsonb = new GsonBuilder();
                                    Gson gson = gsonb.create();
                                    spEditor.putString("fav_string",gson.toJson(favcardviews));
                                    spEditor.commit();
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
//                                    Toast.makeText(MainActivity.this, mItems.get(position) + " swiped right", Toast.LENGTH_SHORT).show();
                                    favcardviews.remove(position);
                                    adapter.notifyItemRemoved(position);
                                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);
                                    SharedPreferences.Editor spEditor = sharedPreferences.edit();
                                    GsonBuilder gsonb = new GsonBuilder();
                                    Gson gson = gsonb.create();
                                    spEditor.putString("fav_string",gson.toJson(favcardviews));
                                    spEditor.commit();
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });

        rv.addOnItemTouchListener(swipeTouchListener);

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
                    startActivity(new Intent(favorite_activity.this, LoginActivity.class));
                    //finishActivity(0);
                }


                myDrawerLayout.closeDrawers();
                return false;
            }
        });

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.fav_toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, myDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        myDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();


    }


}
