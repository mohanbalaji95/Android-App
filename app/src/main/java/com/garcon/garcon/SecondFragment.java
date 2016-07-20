package com.garcon.garcon;

/**
 * Created by kritikagopalakrishnan on 6/2/16.
 * Edited by Mayank Tiwari
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Logger;
import com.firebase.client.ValueEventListener;
import com.garcon.Constants.Constants;

import java.util.ArrayList;


public class SecondFragment extends Fragment {


    ArrayList<Restaurant> myList;
    private ListView LV;
    private Firebase ref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Firebase.setAndroidContext(this.getActivity().getBaseContext());
        }
        connectFirebase();
        onCreateView(getLayoutInflater(savedInstanceState), LV , savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.second_layout, null);

        LV = (ListView) view.findViewById(R.id.fblist);
        listSetup(LV);
        LV.bringToFront();
        return view;
    }

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
                    Toast.makeText(getActivity().getBaseContext(), res.getName(), Toast.LENGTH_SHORT).show();
                }
            }

            public void onCancelled(FirebaseError firebaseError) {
            }
        });

    }

    //function to populate ListView with values
    public void listSetup(final ListView LV) {
        LV.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Restaurant item = (Restaurant) LV.getItemAtPosition(position);
                Toast.makeText(getContext(),"You selected : " + item.getName(),Toast.LENGTH_SHORT).show();
            }
        });

        FirebaseAdapter fba = new FirebaseAdapter(this.getContext(), myList);
        LV.setAdapter(fba);
        fba.notifyDataSetChanged();
    }
}