package com.garcon.garcon;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

import java.util.*;

public class MainCartActivity extends AppCompatActivity {

    static final String LOG_TAG = MainCartActivity.class.getSimpleName();
    static CartRowItemAdapter rowAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cart);
        ArrayList<MenuItem> itemsList = OrderSingleton.getInstance().getList();

        //ensures display begins from top of view
        ScrollView sv = (ScrollView) findViewById(R.id.scrollview);
        sv.setFocusableInTouchMode(true);
        sv.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        //table size
        Spinner tableSizeSpinner = (Spinner) findViewById(R.id.tableSize);
        ArrayAdapter<CharSequence> tableSizeAdapter = ArrayAdapter.createFromResource(this, R.array.optionsTableSize,
                android.R.layout.simple_spinner_dropdown_item);
        tableSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tableSizeSpinner.setAdapter(tableSizeAdapter);

        //order date
        Spinner orderDateSpinner = (Spinner) findViewById(R.id.orderDate);
        ArrayAdapter<CharSequence> orderDateAdapter = ArrayAdapter.createFromResource(this, R.array.optionsOrderDate,
                android.R.layout.simple_spinner_dropdown_item);
        orderDateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderDateSpinner.setAdapter(orderDateAdapter);

        //order time
        Spinner timeSpinner = (Spinner) findViewById(R.id.orderTime);
        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this, R.array.optionsOrderTime,
                android.R.layout.simple_spinner_dropdown_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);

        rowAdapter = new CartRowItemAdapter(itemsList, this);
        NonScrollableListView itemListView = (NonScrollableListView) findViewById(R.id.itemsList);
        itemListView.setAdapter(rowAdapter);

        Spinner restaurantAreaSpinner = (Spinner) findViewById(R.id.restaurantArea);
        ArrayAdapter<CharSequence> restaurantAreaAdapter = ArrayAdapter.createFromResource(this,
                R.array.optionsRestaurantArea, android.R.layout.simple_spinner_dropdown_item);
        restaurantAreaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        restaurantAreaSpinner.setAdapter(restaurantAreaAdapter);
    }

    public void view_menu(View view){
        Log.i(LOG_TAG,"see menu");
        Intent intent = new Intent(this, MainMenuActivity.class);
        this.startActivity(intent);
    }

    public void placeOrder(View view){
        Log.i(LOG_TAG,"place order");
        Intent intent = new Intent(this, MainReviewActivity.class);
        intent.putExtra("instructions", ((EditText) findViewById(R.id.specialInstructions)).getText().toString());
        intent.putExtra("table", ((EditText) findViewById(R.id.tableNumber)).getText().toString());
        Spinner area = (Spinner) findViewById(R.id.restaurantArea);
        intent.putExtra("area", area.getItemAtPosition(area.getSelectedItemPosition()).toString());

        this.startActivity(intent);
    }

}