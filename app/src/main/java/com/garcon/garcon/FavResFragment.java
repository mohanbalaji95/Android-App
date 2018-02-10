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
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.garcon.Constants.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * Created by siddhiparekh11 on 7/24/17.
 * This fragment displays user's favourite restaurants with support for swipe to dismiss.
 */

public class FavResFragment extends Fragment {

    private Firebase ref;
    private ArrayList<String> mItems;
    //for populating the restaurants
    ArrayList<Restaurant> myList;
    private ListView restaurantList;
    //    private CardViewAdapter mAdapter;
    private List<favcardview> favcardviews;

    favcardview favcardarray[];
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Firebase.setAndroidContext(this.getActivity().getBaseContext());
        }
        /*restaurantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                Intent appInfo = new Intent(getContext(), RestaurantDetailActivity.class);
                appInfo.putExtra("RestaurantObject",myList.get(position));
                startActivity(appInfo);
            }
        });*/
        connectFirebase();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_favorite_activity, null);
        Intent intent = getActivity().getIntent();
        RecyclerView rv = (RecyclerView)view.findViewById(R.id.recycleview);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext().getApplicationContext());
        rv.setLayoutManager(llm);





        SharedPreferences sharedPreferences = getContext().getApplicationContext().getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);
        String value = sharedPreferences.getString("fav2_string",null);
        if(value!=null){
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
             favcardarray = gson.fromJson(value, favcardview[].class);


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
        rv.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent appInfo = new Intent(getContext(), RestaurantDetailActivity.class);
                        Iterator<Restaurant> itr=myList.iterator();
                        int count=0;
                        while(itr.hasNext())
                        {

                            if(favcardarray[position].name.equals(itr.next().getName()))
                            {
                                appInfo.putExtra("RestaurantObject",myList.get(count));
                            }
                            count++;
                        }

                        startActivity(appInfo);
                    }
                })
        );


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
                                    spEditor.putString("fav2_string",gson.toJson(favcardviews));
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
                                    spEditor.putString("fav2_string",gson.toJson(favcardviews));
                                    spEditor.commit();
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });

        rv.addOnItemTouchListener(swipeTouchListener);
        return view;

    }
    //connects to firebase and populates myList with data
    public void connectFirebase(){
        ref = new Firebase(Constants.FIREBASE_RESTAURANT_URL);
        myList = new ArrayList<Restaurant>();

        //Add data into myList
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                    Restaurant res = dataSnap.getValue(Restaurant.class);
                    myList.add(res);
                }
                //listSetup();
            }

            public void onCancelled(FirebaseError firebaseError) {
            }
        });

    }
    //function to populate ListView with values from myList
    public void listSetup() {
        FirebaseAdapter fba = new FirebaseAdapter(this.getContext(), myList);
        restaurantList.setAdapter(fba);
    }
}
