package com.garcon.garcon;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;

public class SecondFragment extends Fragment {


    ArrayList<Restaurant> myList;
    private ListView restaurantList;
    private Firebase ref;

    private RestaurantClickedListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Firebase.setAndroidContext(this.getActivity().getBaseContext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.second_layout, null);

        restaurantList = (ListView) view.findViewById(R.id.fblist);

        connectFirebase();

        restaurantList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Restaurant cur = myList.get(position);
                ((RestaurantClickedListener)getContext()).onRestaurantClicked(cur);
            }
        });

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
                listSetup();
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RestaurantClickedListener) {
            listener = (RestaurantClickedListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface RestaurantClickedListener {
        void onRestaurantClicked(Restaurant cur);
    }
}