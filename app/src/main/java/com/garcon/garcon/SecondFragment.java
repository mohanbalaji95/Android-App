package com.garcon.garcon;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.garcon.Constants.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Connects to Firebase to retrieve the list of partner restaurants and display them in a list.
 */
public class SecondFragment extends Fragment {


    ArrayList<Restaurant> myList;
    List<Restaurant> Li;
    private ListView restaurantList;
    private Firebase ref;
    private EditText sv;
    FirebaseAdapter fba;
    ArrayList<String> slist= new ArrayList<String>();
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
        sv=(EditText) view.findViewById(R.id.search);
        sv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @TargetApi(Build.VERSION_CODES.N)
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.equals("")) connectFirebase();
                else connectFirebaseSS(s);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //restaurantList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
        restaurantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //  Restaurant cur = myList.get(position);
            //((RestaurantClickedListener)getContext()).onRestaurantClicked(cur);
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                Intent appInfo = new Intent(getContext(), RestaurantDetailActivity.class);

                appInfo.putExtra("RestaurantObject",myList.get(position));
                startActivity(appInfo);

            }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void connectFirebaseSS(final CharSequence sequence) {
        List<Restaurant> thingsToBeAdd = new ArrayList<Restaurant>();
        for(Iterator<Restaurant> it = myList.iterator(); it.hasNext();) {
            Restaurant element = it.next();
            if((element.getName().toLowerCase().contains(sequence.toString().toLowerCase()))||(element.getType().toLowerCase().contains(sequence.toString().toLowerCase()))) {
                thingsToBeAdd.add(element);
            }
        }
        Li= (ArrayList<Restaurant>) thingsToBeAdd;
        liSetup();
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
                    slist.add(res.getName());


                }
                listSetup();
            }

            public void onCancelled(FirebaseError firebaseError) {
            }
        });

    }

    //function to populate ListView with values from myList
    public void listSetup() {
        fba = new FirebaseAdapter(this.getContext(), myList);
        restaurantList.setAdapter(fba);

    }

    public void liSetup() {
        fba = new FirebaseAdapter(this.getContext(), (ArrayList) Li);
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