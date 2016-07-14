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
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
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
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.second_layout, null);

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
            }

            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        LV = (ListView) view.findViewById(R.id.fblist);
        listSetup(LV);

        return view;
    }

    //function to populate ListView with values
    public void listSetup(ListView LV) {
        FirebaseAdapter fba = new FirebaseAdapter(this.getContext(), myList);
        LV.setAdapter(fba);
    }
}