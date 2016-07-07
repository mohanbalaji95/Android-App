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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState== null){
            Firebase.setAndroidContext(this.getActivity().getBaseContext());
        }
        ref= new Firebase(Constants.FIREBASE_RESTAURANT_URL);

        myList = new ArrayList<Restaurant>();
        final TextView textbox = (TextView) getActivity().findViewById(R.id.text2);

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

        FirebaseAdapter fba = new FirebaseAdapter(this.getContext(), myList);
        LV = (ListView) this.getActivity().findViewById(android.R.id.list);

        listSetup(LV, fba);
        View view = inflater.inflate(R.layout.second_layout, null);
        return view;
    }

    //function to populate ListView with values
    //TODO: implement pipeline to database
    public void listSetup(ListView LV, FirebaseAdapter FBAdapter){

        /**String[] a = { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2" , "asdf", "beep", "boop", "hello", "my", "name", "jeff"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, a);
        **/

        LV.setAdapter(FBAdapter);
    }
}