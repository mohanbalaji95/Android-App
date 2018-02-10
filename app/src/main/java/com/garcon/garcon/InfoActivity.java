package com.garcon.garcon;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity to display extra information about a restaurant.
 * The activity pulls the data about the restaurant from Firebase.
 */
public class InfoActivity extends AppCompatActivity {

    TextView tvPhone,tvWebsite,tvDogsAllowed,tvBikeParking,tvWheelChairAccessible,tvOutdoorSeating;
    TextView tvWaiterService, tvTakeOut,tvReservations,tvAlcohol, tvAmbience,tvCaters, tvDelivery;
    TextView tvParking, tvTelevision, tvWifi,tvHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setupWidgets();
        Bundle bundle = getIntent().getExtras();
        Restaurant restaurant = null;
        if(bundle!=null) {
            restaurant = (Restaurant) bundle.getSerializable("RestaurantObject");
        }

        if(restaurant!=null) {
            Log.d("InfoActivity", "Dogs Allowed= " + restaurant.getDogsAllowed());
            bindData(restaurant);
        }

    }

    private void setupWidgets(){
        tvPhone = (TextView) findViewById(R.id.phone_number);
        tvWebsite = (TextView) findViewById(R.id.website_link);
        tvDogsAllowed = (TextView) findViewById(R.id.dogs);
        tvBikeParking = (TextView) findViewById(R.id.bike_parking);
        tvWheelChairAccessible = (TextView) findViewById(R.id.wheelchair_accessible);
        tvOutdoorSeating = (TextView) findViewById(R.id.outdoor_seating);
        tvWaiterService = (TextView) findViewById(R.id.waiter_service);
        tvTakeOut = (TextView) findViewById(R.id.tvTakeoutStatus);
        tvReservations = (TextView) findViewById(R.id.reservation1);
        tvAlcohol = (TextView) findViewById(R.id.alcoholStatus);
        tvAmbience = (TextView) findViewById(R.id.ambience);
        tvCaters = (TextView) findViewById(R.id.caters);
        tvDelivery = (TextView) findViewById(R.id.delivery1);
        tvParking = (TextView) findViewById(R.id.parking);
        tvTelevision = (TextView) findViewById(R.id.television);
        tvWifi = (TextView) findViewById(R.id.wifi);
        tvHours = (TextView) findViewById(R.id.hours1);
    }

    private void bindData(Restaurant restaurant){
        tvPhone.setText(restaurant.getPhone());
        tvWebsite.setText(restaurant.getWebsite());
        tvDogsAllowed.setText(restaurant.getDogsAllowed());
        tvBikeParking.setText(restaurant.getBikeParking());
        tvWheelChairAccessible.setText(restaurant.getWheelchairAccessible());
        tvOutdoorSeating.setText(restaurant.getOutdoorSeating());
        tvWaiterService.setText(restaurant.getWaiterService());
        tvTakeOut.setText(restaurant.getTakeout());
        tvReservations.setText(restaurant.getReservations());
        tvAlcohol.setText(restaurant.getAlcohol());
        tvAmbience.setText(restaurant.getAmbience());
        tvCaters.setText(restaurant.getCaters());
        tvDelivery.setText(restaurant.getDelivery());
        tvParking.setText(restaurant.getParking());
        tvTelevision.setText(restaurant.getTelevision());
        tvWifi.setText(restaurant.getWifi());
        tvHours.setText(restaurant.getHours().replace("; ","\n"));
    }
}
