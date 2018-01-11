package com.garcon.garcon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.garcon.Constants.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created by siddhiparekh11 on 8/10/17.
 */

public class OrderHistoryActivity extends AppCompatActivity {


    private static final String TAG = com.garcon.garcon.OrderHistoryActivity.class.getName();
       // private Toolbar toolbar;
        private ArrayList<String> mItems;
        //    private CardViewAdapter mAdapter;
        private ArrayList<OrderHistoryCardView> orderhistorycardviews;
    private ArrayList<Restaurant1> restaurants;
        private FirebaseAuth.AuthStateListener mAuthListener;
        private FirebaseAuth mAuth;
    private Firebase ref;
    private Firebase ref1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (savedInstanceState == null) {
                Firebase.setAndroidContext(this);
            }
            setContentView(R.layout.activity_order_history);
            /*toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Order History");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

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

            orderhistorycardviews = new ArrayList<OrderHistoryCardView>();
            restaurants=new ArrayList<>();
            connectFirebase();










            }


    //connects to firebase and populates myList with data
    public void connectFirebase(){


        ref1=new Firebase(Constants.FIREBASE_RESTAURANT1_URL);


        //getting the restaurants
        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnap : dataSnapshot.getChildren())
                {
                    Restaurant1 r= dataSnap.getValue(Restaurant1.class);
                    restaurants.add(r);

                }

              int siz=restaurants.size();
               connectFirebase2();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });












    }
    public void connectFirebase2()
    {
        ref = new Firebase(Constants.FIREBASE_ORDER1_URL);

        //Add data into myList
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               Restaurant1 temp,r2;
                temp=r2=null;

                    for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                        OrderHistoryView res = dataSnap.getValue(OrderHistoryView.class);
                        String s=res.getOmnivore_loc_id();

                        Iterator<Restaurant1> i = restaurants.iterator();
                        while (i.hasNext()) {
                            temp = i.next();
                            String t=temp.getid();
                            if (temp.getid().equals(res.getOmnivore_loc_id())) {

                                r2 = temp;
                                break;
                            }
                        }

                       orderhistorycardviews.add(new OrderHistoryCardView(r2.getName(), Calendar.getInstance().getTime(), Double.valueOf(res.getTotalAmount()), r2.getLocation(),res.getOrderid()));


                    }

              //  System.out.println(orderhistorycardviews.size());

            }

            public void onCancelled(FirebaseError firebaseError) {

                Log.d("Error",firebaseError.toString());
            }
        });

        final OrderHistoryCardAdapter adapter = new OrderHistoryCardAdapter(orderhistorycardviews);

        RecyclerView rv = (RecyclerView) findViewById(R.id.recycleview);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);


        rv.setAdapter(adapter);
        rv.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent appInfo = new Intent(getApplicationContext(), OrderDetail.class);
                        appInfo.putExtra("ordobj",orderhistorycardviews.get(position));

                        startActivity(appInfo);
                    }
                })
        );


    }


    }

