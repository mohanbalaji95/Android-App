package com.garcon.garcon;
import com.garcon.garcon.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * Created by raja on 7/23/2016.
 */
public class MainCheckoutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_main_checkout);
        ArrayList<Integer> values = getIntent().getIntegerArrayListExtra("values");

        for(int i = 0; i < values.size(); i++)
            Log.i("information ",i+" "+values.get(i));

        TextView subtotal = (TextView) findViewById(R.id.subtotal_final);
        subtotal.setText(String.valueOf(values.get(0)));

        TextView tip = (TextView) findViewById(R.id.tip_final);
        tip.setText(String.valueOf(values.get(1)));

        TextView total = (TextView) findViewById(R.id.total_final);
        total.setText(String.valueOf(values.get(2)));

    }

}