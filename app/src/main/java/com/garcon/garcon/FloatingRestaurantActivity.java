package com.garcon.garcon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Mayank on 7/20/2016.
 */
public class FloatingRestaurantActivity extends Activity {

    TextView tvName, tvPrice, tvRating, tvLocation, tvHours, tvType;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floating_layout);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        dataSetup(b);

        //
        ImageButton quit = (ImageButton) this.findViewById(R.id.exit);
        quit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.floating_layout, container);
        return view;
    }

    public void dataSetup(Bundle b) {
        tvName = (TextView) this.findViewById(R.id.tvName);
        tvPrice = (TextView) this.findViewById(R.id.tvPrice);
        tvRating = (TextView) this.findViewById(R.id.tvRating);
        tvLocation = (TextView) this.findViewById(R.id.tvLocation);
        tvHours = (TextView) this.findViewById(R.id.tvHours);
        tvType = (TextView) this.findViewById(R.id.tvType);

        tvName.setText(b.getString("name"));
        tvPrice.setText(b.getString("price"));
        tvRating.setText(Double.toString(b.getDouble("rating")));
        tvLocation.setText(b.getString("location"));
        tvHours.setText(b.getString("hours"));
        tvType.setText(b.getString("type"));
    }

}