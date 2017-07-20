package com.garcon.garcon;

/**
 * Created by kritikagopalakrishnan on 6/2/16.
 */
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

import static android.content.Context.LOCATION_SERVICE;
import static com.google.android.gms.internal.zzs.TAG;


public class PrimaryFragment extends Fragment implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private MapView mapView;
    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;

    SupportMapFragment mapFragment;
    AutoCompleteTextView RestaurantSearch;
    private ArrayAdapter<String> adapter;

    //These values show in autocomplete
    ArrayList<String> item = new ArrayList<>();
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.primary_layout, container, false);
//        RestaurantSearch = (AutoCompleteTextView) v.findViewById(R.id.autoCompleteTextView);
//        RestaurantSearch.setThreshold(1);

        checkPermission();
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

    // SOUMITHRI - check for the GPS to turn the location on
    private void checkPermission() {
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

        }
        else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission. ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            statusCheck((AppCompatActivity) this.getActivity()); // Call the status check method to check whether the GPS is disabled or not
        }
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
        System.out.println("location" + stringb);
        Geocoder coder = new Geocoder(getActivity());
        List<Address> address = null;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(stringa, 5);
            if (address == null) {
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
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

    //** UPDATED onConnected method by SOUMITHRI. Added checkSelfPermission to this method to enable location
    // services for the app in the settings( when the app is installed for the first time)
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        new RestaurantRequest().execute();
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (mLastLocation != null) {
            showLocationOnMap(mLastLocation);
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, createLocationRequest(), new com.google.android.gms.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    showLocationOnMap(location);
                    LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
                }
            });
        }
    }

    private void showLocationOnMap(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        if (googleMap != null) {
            googleMap.addMarker(new MarkerOptions().position(latLng));

            // change the last element to control zoom level
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    private class RestaurantRequest extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference ref = database.getReferenceFromUrl("https://garcondatabase.firebaseio.com/Restaurants");
            final ArrayList listView = new ArrayList<String>();

            ref.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    Log.i("player", player.getKey());
//                        Restaurants restaurants = dataSnapshot1.getValue(Restaurants.class);
//                        String name = (String) ;
                        item.add(dataSnapshot1.child("name").getValue().toString() + ", " + dataSnapshot1.child("location").getValue().toString());

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
            if (RestaurantList != null) {

//                PlaceAutocompleteAdapter mArrayAdapter = new PlaceAutocompleteAdapter(getActivity(), R.layout.primary_layout, RestaurantList);
//                RestaurantSearch.setAdapter(mArrayAdapter);
            }
        }
    }

    // SOUMITHRI - REQUEST for location permission callback method
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG,"yoo");
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

    //******* SOUMITHRI *******//
    // Check the status of the GPS. If it is not turned on call the buildAlertMessageNoGps() method
    public void statusCheck(AppCompatActivity activity) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
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

}