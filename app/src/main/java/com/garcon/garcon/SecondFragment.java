package com.garcon.garcon;

/**
 * Created by kritikagopalakrishnan on 6/2/16.
 * Edited by Mayank Tiwari
 */
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.garcon.Constants.Constants;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class SecondFragment extends Fragment {


    private ListView LV;
    private Firebase ref;
    ArrayList<Restaurant> myList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState== null){
            Firebase.setAndroidContext(this.getActivity().getBaseContext());
        }



    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.second_layout, null);

        ref= new Firebase(Constants.FIREBASE_RESTAURANT_URL);
        myList = new ArrayList<Restaurant>();
        //final TextView textbox = (TextView) getActivity().findViewById(R.id.text2);

        //Add data into myList
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnap : dataSnapshot.getChildren()){
                    Restaurant res = dataSnap.getValue(Restaurant.class);
                    myList.add(res);
                }
            }

            public void onCancelled(FirebaseError firebaseError) {}
        });

        //LayoutInflater inflater = getLayoutInflater(savedInstanceState);
        //LV = (ListView) inflater.inflate(R.layout.second_layout, null).findViewById(R.id.fblist);
        LV = (ListView) view.findViewById(R.id.fblist);
        listSetup(LV, savedInstanceState);


        //FirebaseAdapter fba = new FirebaseAdapter(this.getContext(), myList);
        //LV.setAdapter(fba);
        return view;
    }


    //function to populate ListView with values
    public void listSetup(ListView LV, Bundle savedInstanceState){
        FirebaseAdapter fba = new FirebaseAdapter(this.getContext(), myList);
        LV.setAdapter(fba);
    }
}