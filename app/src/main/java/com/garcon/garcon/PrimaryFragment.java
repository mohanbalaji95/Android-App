package com.garcon.garcon;

/**
 * Created by kritikagopalakrishnan on 6/2/16.
 */

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PrimaryFragment extends Fragment implements OnMapReadyCallback, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private MapView mapView;
    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;

    SupportMapFragment mapFragment;
    AutoCompleteTextView RestaurantSearch;
    private ArrayAdapter<String> adapter;

    //These values show in autocomplete
    ArrayList<String> item = new ArrayList<String>();

    //    mylist.add(mystring);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.primary_layout, container, false);
//        RestaurantSearch = (AutoCompleteTextView) v.findViewById(R.id.autoCompleteTextView);
//        RestaurantSearch.setThreshold(1);

        // Create an instance of GoogleAPIClient.
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        new RestaurantRequest().execute();
        RestaurantSearch = (AutoCompleteTextView) v.findViewById(R.id.autoCompleteTextView);
        RestaurantSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestaurantSearch.setText("");
            }
        });
        //Create adapter
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, item);

        RestaurantSearch.setThreshold(1);

        //Set adapter to AutoCompleteTextView
        RestaurantSearch.setAdapter(adapter);
        RestaurantSearch.setOnItemSelectedListener(this);
        RestaurantSearch.setOnItemClickListener(this);
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentByTag("mapFragment");
        if (mapFragment == null) {
            mapFragment = new SupportMapFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.mapFragmentContainer, mapFragment, "mapFragment");
            ft.commit();
            fm.executePendingTransactions();
        }
        mapFragment.getMapAsync(this);

    }

    //    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState)
//{
//    mapView = (MapView) view.findViewById(R.id.map);
//    mapView.onCreate(savedInstanceState);
//    mapView.onResume();
//    mapView.getMapAsync(this);
//}
//    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setMyLocationEnabled(true);
    }

    public void onDestroyView() {
        super.onDestroyView();
//        if (googleMap != null) {
//            getFragmentManager().beginTransaction().remove(mapFragment).commit();
////            getFragmentManager()
////                    .beginTransaction()
////                    .remove(getFragmentManager().findFragmentById(R.id.mapFragmentContainer))
////                    .commit();
//
//        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        // Show Alert
        googleMap.clear();
//        Toast.makeText(getActivity(), "Position:" + arg2 + " Month:" + arg0.getItemAtPosition(arg2),
//                Toast.LENGTH_LONG).show();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        String search = arg0.getItemAtPosition(arg2).toString();
        String[] str_array = search.split(", ");
        String stringa = str_array[0];
        String stringb = str_array[1];
        System.out.println("location"+stringb);
        Geocoder coder = new Geocoder(getActivity());
        List<Address> address = null;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(stringb, 5);
            if (address == null) {
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
// Show the current location in Google Map
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(p1));

            // Zoom in the Google Map
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title(stringa).snippet(stringb)).showInfoWindow();
        } catch (Exception ex) {

            ex.printStackTrace();
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (mLastLocation != null) {
            showLocationOnMap(mLastLocation);
        }else {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, createLocationRequest(), new com.google.android.gms.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    showLocationOnMap(location);
                    LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
                }
            });
        }
    }

    private void showLocationOnMap(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        if(googleMap!=null) {
            googleMap.addMarker(new MarkerOptions().position(latLng));

            // change the last element to control zoom level
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));
        }
    }

    private LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000);
        mLocationRequest.setFastestInterval(30000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        return mLocationRequest;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class RestaurantRequest extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference ref = database.getReferenceFromUrl("https://garcondatabase.firebaseio.com/Restaurants");
            final ArrayList listView= new ArrayList<String>();

            ref.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    Log.i("player", player.getKey());
//                        Restaurants restaurants = dataSnapshot1.getValue(Restaurants.class);
//                        String name = (String) ;
                        item.add(dataSnapshot1.child("name").getValue().toString()+", "+dataSnapshot1.child("location").getValue().toString());

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
            return item;
        }

        @Override
        protected void onPostExecute(ArrayList<String> RestaurantList) {
            super.onPostExecute(RestaurantList);
            if (RestaurantList != null ) {

//                PlaceAutocompleteAdapter mArrayAdapter = new PlaceAutocompleteAdapter(getActivity(), R.layout.primary_layout, RestaurantList);
//                RestaurantSearch.setAdapter(mArrayAdapter);
            }
        }
    }

}
