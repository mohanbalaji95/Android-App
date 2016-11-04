package com.garcon.garcon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mayank Tiwari on 7/20/2016.
 */
public class FloatingRestaurantActivity extends Activity {

    TextView tvName, tvPrice, tvLocation, tvHours, tvType;

    Button btn_Menu;
    String locationName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floating_layout);

//        WindowManager.LayoutParams params = getWindow().getAttributes();
//
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//
//        int width = size.x-(int)(0.075*size.x);
//        int height = size.y;
//        Log.d("Floating Activity","The width is--> "+size.x);
//        params.height = height;
//        params.width = width;
//
//        this.getWindow().setAttributes(params);

       Bundle bundle = getIntent().getExtras();
        dataSetup(bundle);

        btn_Menu = (Button) findViewById(R.id.menu);
        btn_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startCheckoutActivity = new Intent(FloatingRestaurantActivity.this, MainMenuActivity.class);
                startCheckoutActivity.putExtra("ticketnumber","oTpbBkqT");
                startCheckoutActivity.putExtra("locationID","AieMdB5i");
                startActivity(startCheckoutActivity);
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
        tvLocation = (TextView) this.findViewById(R.id.tvLocation);
        tvHours = (TextView) this.findViewById(R.id.tvHours);
        tvType = (TextView) this.findViewById(R.id.tvType);

        tvName.setText(b.getString("name"));
        tvPrice.setText(b.getString("price"));
        tvLocation.setText(b.getString("location"));
        tvHours.setText( parseHours(b.getString("hours")) );
        tvType.setText(b.getString("type"));

        locationName = b.getString("name");
    }

    public String parseHours(String hours){
        return "hours";
    }

    public void addToFav(View view){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);
        String value = sharedPreferences.getString("fav_string",null);
        if(value!=null){
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            favcardview favcardarray[] = gson.fromJson(value, favcardview[].class);

            List<favcardview> favcardviews;
            favcardviews = new ArrayList<>();
            for(int i =0 ;i<favcardarray.length;i++){
                favcardviews.add(new favcardview(favcardarray[i].name,R.drawable.la_vic));
            }
            favcardviews.add(new favcardview(locationName,R.drawable.la_vic));
            SharedPreferences.Editor spEditor = sharedPreferences.edit();
            spEditor.putString("fav_string",gson.toJson(favcardviews));
            spEditor.commit();
        }
        else{
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            List<favcardview> favcardviews;
            favcardviews = new ArrayList<>();
            favcardviews.add(new favcardview(locationName,R.drawable.la_vic));
            SharedPreferences.Editor spEditor = sharedPreferences.edit();
            spEditor.putString("fav_string",gson.toJson(favcardviews));
            spEditor.commit();

        }
        Toast.makeText(this,"Added to your Favorites List",Toast.LENGTH_LONG).show();
    }

}