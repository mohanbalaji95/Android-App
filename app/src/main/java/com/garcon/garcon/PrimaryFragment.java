package com.garcon.garcon;

/**
 * Created by kritikagopalakrishnan on 6/2/16.
 */

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import com.garcon.Models.*;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class PrimaryFragment extends Fragment implements OnMapReadyCallback, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, LocationListener {
    private MapView mapView;
    private GoogleMap googleMap;
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
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
//        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        String bestProvider = locationManager.getBestProvider(criteria, true);
//        Location location = locationManager.getLastKnownLocation(bestProvider);
//        if (location != null) {
//            onLocationChanged(location);
//        }
//        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
        // Check if we were successful in obtaining the map.
//        if (googleMap != null) {
//
//
//            googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//
//                @Override
//                public void onMyLocationChange(Location arg0) {
//                    // TODO Auto-generated method stub
//
//                    googleMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));
//                }
//            });
//        }
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
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
