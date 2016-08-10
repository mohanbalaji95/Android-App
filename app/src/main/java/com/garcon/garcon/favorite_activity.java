package com.garcon.garcon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.DefaultItemAnimator;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class favorite_activity extends AppCompatActivity {
    private ArrayList<String> mItems;
    //private CardViewAdapter mAdapter;
    private List<favcardview> favcardviews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        RecyclerView rv = (RecyclerView)findViewById(R.id.recycleview);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);
        String value = sharedPreferences.getString("fav_string",null);
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        favcardview favcardarray[] = gson.fromJson(value, favcardview[].class);


        favcardviews = new ArrayList<>();
        for(int i =0 ;i<favcardarray.length;i++){
            favcardviews.add(new favcardview(favcardarray[i].name,favcardarray[i].photoid));
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
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
//                                    Toast.makeText(MainActivity.this, mItems.get(position) + " swiped right", Toast.LENGTH_SHORT).show();
                                    favcardviews.remove(position);
                                    adapter.notifyItemRemoved(position);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });

        rv.addOnItemTouchListener(swipeTouchListener);


    }


}
