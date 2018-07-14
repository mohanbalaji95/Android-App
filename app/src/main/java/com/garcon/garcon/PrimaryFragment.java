package com.garcon.garcon;

/**
 * Created by kritikagopalakrishnan on 6/2/16.
 *
 * Edited :
 *
 * Name | Changes | Date
 * Kusuma | Map to represent all the restaurents -setAllRestaurents() method | June 23 rd
 *
 */

import android.content.Intent;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.LOCATION_SERVICE;
import static com.google.android.gms.internal.zzs.*;

//TODO:fix spelling of restaurants: its not "restaurents"

public class PrimaryFragment extends Fragment implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private MapView mapView;
    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;
    SupportMapFragment mapFragment;
    private final static String TAG = PrimaryFragment.class.getSimpleName();

    AutoCompleteTextView RestaurantSearch;
    private ArrayAdapter<String> adapter;

    //"item" has the restaurants to show in search in an adapter
    ArrayList<String> item = new ArrayList<>();
	
	public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    //Array to get all restaurents from async task on implementing an interface Intertogetrestaurents
    public ArrayList<Restaurant> array_restaurent=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.primary_layout, container, false);
//        RestaurantSearch = (AutoCompleteTextView) v.findViewById(R.id.autoCompleteTextView);
//        RestaurantSearch.setThreshold(1);
        Log.d(TAG,"onCreate");

        // Create an instance of GoogleAPIClient.
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        RestaurantSearch = (AutoCompleteTextView) v.findViewById(R.id.autoCompleteTextView);

        // Inflate the layout for this fragment
        return v;
    }

    private void setupMarkers(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        ref=ref.child("Restaurants");
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                item.clear();
                array_restaurent.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Restaurant restaurent = dataSnapshot1.getValue(Restaurant.class);
                    //Getting restaurents objects and storing it in an array
//                    Restaurant restaurent = new Restaurant();
//                    restaurent.setId(dataSnapshot1.child("ID").getValue().toString());
//                    restaurent.setHours(dataSnapshot1.child("hours").getValue().toString());
//                    restaurent.setLat(Double.parseDouble(dataSnapshot1.child("lat").getValue().toString()));
//                    restaurent.setLongt(Double.parseDouble(dataSnapshot1.child("longt").getValue().toString()));
//                    restaurent.setLocation(dataSnapshot1.child("location").getValue().toString());
//                    restaurent.setType(dataSnapshot1.child("Type").getValue().toString());
//                    restaurent.setName(dataSnapshot1.child("name").getValue().toString());
//                    restaurent.setPhone(dataSnapshot1.child("phone").getValue().toString());
//                    restaurent.setPrice(dataSnapshot1.child("price").getValue().toString());
//                    restaurent.setWebsite(dataSnapshot1.child("website").getValue().toString());
                    array_restaurent.add(restaurent);
                    item.add(restaurent.getName()+", "+restaurent.getLocation());

                }
                setAllRestaurents(array_restaurent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        RestaurantSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestaurantSearch.setText("");
                setupMarkers();
                googleMap.clear();
            }
        });


        //Create adapter
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, item);
        RestaurantSearch.setThreshold(1);

        //Set adapter to AutoCompleteTextView
        RestaurantSearch.setAdapter(adapter);
        RestaurantSearch.setOnItemSelectedListener(this);
        RestaurantSearch.setOnItemClickListener(this);
    }

    // SOUMITHRI - check for the GPS to turn the location on
    private void checkPermission() {
        Log.d(TAG,"checking for permission");
        if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d(TAG,"permission not granted... requesting...");
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

        }
        else {
            /*ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission. ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);*/
            statusCheck((AppCompatActivity) this.getActivity()); // Call the status check method to check whether the GPS is disabled or not
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void setupMap(){
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
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setMyLocationEnabled(true);
        setupMarkers();
        //We have to add add a listener for the marker clicking
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);
    }

    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        googleMap.moveCamera(update);
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

        //implementation relies on geocoder coordinates instead of using firebase's data
        //might be better to use database?
        //issue with goecoder giving slightly different latitude and longitude

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
        String stringb = str_array[1]+", "+str_array[2]+", "+str_array[3];
        System.out.println("location"+stringb);
        Geocoder coder = new Geocoder(getActivity());
        List<Address> address;
        LatLng p1;

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
    public  void onResume()
    {

        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }


    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkPermission();
    }

    private void showLocationOnMap(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        if(googleMap!=null) {
            googleMap.addMarker(new MarkerOptions().position(latLng));

            //TODO: For Later Implementation - TO show restaurents in a range
            //Adding a circle around the current location with a radius
           /* googleMap.addCircle(new CircleOptions().center(latLng).radius(3000).strokeColor(Color.LTGRAY)
                    .fillColor(Color.TRANSPARENT));*/

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

    public void onInfoWindowClick(Marker marker) {
        onMarkerClick(marker);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        //Call connectFirebase() to get the restaurants into the ArrayList from Firebase. Uses the Firebase Reference to do it
        //connectFirebase();

        //Get the position of the marker. So then we can compare it to the restaurant.
        LatLng markerPos = marker.getPosition();

        //index for for loop
        int index = 0;

        //Do a for each loop to check the restauarnt info with the Marker info. And open the page for it.
        //Make an Intent to open the specified restaurant.

        //for loop for this
        //if the marker's latitude equals the arrayList's latitude AND if marker's longitude equals arrayList's longitude, then open that restaurant's activity
        //for loop for this
        for (index = 0; index < array_restaurent.size(); index++) {
            Log.d("In for", "In the for loop");

            //changed from comparing lat and long to addresses
            //geocoder was not giving same precision of coordinates as were in database

            //if ((markerPos.latitude == array_restaurent.get(index).getLat() && (markerPos.longitude == array_restaurent.get(index).getLongt()))) {
            if (marker.getSnippet().equals(array_restaurent.get(index).getLocation())){
                //MAKE INTENT TO START ACTIVITY OF THAT RESTAURANT
                Intent chosenRestaurant = new Intent(getContext(), RestaurantDetailActivity.class);
                chosenRestaurant.putExtra("RestaurantObject", array_restaurent.get(index));  //may not need this
                startActivity(chosenRestaurant);
                break;





                //return true;
                // this is if your querying for the latLong child


                //NOTE THIS
                //if (val.child("hotel").getValue(String.class).contains("hotel")) {

                //}
            }
        }

        return false;
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
                    item.clear();
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

   // SOUMITHRI - REQUEST for location permission callback method
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.d(TAG,"onRequestPermissionsResult...");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this.getActivity(),
                            Manifest.permission. ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG,"permission accepted");
                        getLocationFromService();
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d(TAG,"permission denied");
                }
                return;
            }
        }
    }

    private void getLocationFromService(){
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
        setupMap();
    }

    //******* SOUMITHRI *******//
    // Check the status of the GPS. If it is not turned on call the buildAlertMessageNoGps() method
    public void statusCheck(AppCompatActivity activity) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }

        setupMap();
    }

    //****** SOUMITHRI ******//
    // This method shows the dialog window saying that GPS is disabled
    // and Open the GPS location services by starting an intent.
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
	
	
	
    //Below Function will show all the restaurents preent in Firebase in GoogleMap
    public void setAllRestaurents(ArrayList<Restaurant> res)
    {
        for(int i=0;i<res.size();i++)
        {
            if(googleMap !=null)
            {
                googleMap.addMarker(new MarkerOptions().position(new LatLng(res.get(i).getLat(),res.get(i).getLongt())).title(res.get(i).getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).showInfoWindow();

            }
       }
       if(googleMap !=null)
           googleMap.animateCamera(CameraUpdateFactory.zoomTo(8));

    }

}