package com.garcon.garcon;

/**
 * Created by kritikagopalakrishnan on 6/2/16.
 * Edited by Mayank Tiwari
 */
import com.firebase.client.Firebase;
import com.garcon.Constants.Constants;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.garcon.garcon.Restaurant;


public class SecondFragment extends Fragment {


    private ListView LV;

    @Override
    public void onCreate(Bundle savedInstanceState){
        Firebase ref = new Firebase(Constants.FIREBASE_URL);
        Firebase.setAndroidContext(this.getActivity());
        ref.addChildEventListener(new ChildEventListener()){
            @Override
            public void onChildAdded(DataSnapshot snap, String previousChildKey){
                Restaurant newRest = snap.getValue(Restaurant.class);
            }
        });
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState== null){
            Firebase.setAndroidContext(this.getActivity().getBaseContext());
        }
        return inflater.inflate(R.layout.second_layout, null);
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        LV = (ListView) getActivity().findViewById(android.R.id.list);

        listSetup(LV, ref);
    }



    //function to populate ListView with values
    //currently a simple array as dummy data
    //TODO: implement pipeline to database
    public void listSetup(ListView LV, Firebase ref){



        String[] a = { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2" , "asdf", "beep", "boop", "hello", "my", "name", "jeff"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, a);
        LV.setAdapter(adapter);
    }
}