package com.garcon.garcon;

/**
 * Created by kritikagopalakrishnan on 6/2/16.
 * Edited by Mayank Tiwari
 */
import com.firebase.client.Firebase;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SecondFragment extends Fragment {


    private ListView LV;
    private Firebase ref;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState== null){
            Firebase.setAndroidContext(this.getActivity().getBaseContext());
            ref= new Firebase(Constants.FIREBASE_RESTAURANT_URL);
        }
        View view = inflater.inflate(R.layout.second_layout, null);
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        LV = (ListView) getActivity().findViewById(android.R.id.list);
        Activity disActivity = this.getActivity();
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference();
        FirebaseListAdapter<Restaurant> adapter = new FirebaseListAdapter<Restaurant>(disActivity, Restaurant.class, android.R.layout.activity_list_item, reff)
        {
            @Override
            protected void populateView(View view, Restaurant temp, int position) {
                ((TextView) view.findViewById(R.id.text1)).setText(temp.getName());
            }
        };
        listSetup(LV, adapter);
    }



    //function to populate ListView with values
    //currently a simple array as dummy data
    //TODO: implement pipeline to database
    public void listSetup(ListView LV, FirebaseListAdapter FBAdapter){

        /**String[] a = { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2" , "asdf", "beep", "boop", "hello", "my", "name", "jeff"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, a);
        **/
        LV.setAdapter(FBAdapter);
    }
}