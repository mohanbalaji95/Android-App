package com.garcon.garcon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.Firebase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siddhiparekh11 on 7/24/17.
 */

public class FavMenuFragment extends Fragment {
    private Firebase ref;
    private ArrayList<String> mItems;
    //    private CardViewAdapter mAdapter;
    private List<favcardview> favcardviews;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Firebase.setAndroidContext(this.getActivity().getBaseContext());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_favoritemenu, null);
        Intent intent = getActivity().getIntent();
        RecyclerView rv = (RecyclerView)view.findViewById(R.id.recycleview);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext().getApplicationContext());
        rv.setLayoutManager(llm);

        SharedPreferences sharedPreferences = getContext().getApplicationContext().getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);
        String value = sharedPreferences.getString("favmenu3_string",null);
        if(value!=null){
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            favcardview favcardarray[] = gson.fromJson(value, favcardview[].class);


            favcardviews = new ArrayList<>();
            for(int i =0 ;i<favcardarray.length;i++){
                favcardviews.add(new favcardview(favcardarray[i].name,R.drawable.restaurant_thumbnail,favcardarray[i].extra));
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
                                    SharedPreferences sharedPreferences = getContext().getApplicationContext().getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);
                                    SharedPreferences.Editor spEditor = sharedPreferences.edit();
                                    GsonBuilder gsonb = new GsonBuilder();
                                    Gson gson = gsonb.create();
                                    spEditor.putString("favmenu3_string",gson.toJson(favcardviews));
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
                                    SharedPreferences sharedPreferences = getContext().getApplicationContext().getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);
                                    SharedPreferences.Editor spEditor = sharedPreferences.edit();
                                    GsonBuilder gsonb = new GsonBuilder();
                                    Gson gson = gsonb.create();
                                    spEditor.putString("favmenu3_string",gson.toJson(favcardviews));
                                    spEditor.commit();
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });

        rv.addOnItemTouchListener(swipeTouchListener);
        return view;

    }
}
