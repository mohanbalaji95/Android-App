package com.garcon.garcon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;


public class RestaurantDetailFragment extends Fragment {

    TextView tvName, tvPrice, tvLocation, tvHours, tvType;

    Button btn_Menu;
    Button btn_Call;
    Button btn_DineIn;
    Button btn_TakeOut;
    Button btn_Info;
    String locationName;
    String phoneNumber;
    double lat, longt;
    GoogleMap Map;
    private View restaurantLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.floating_layout, null);
        initializeUI(view);

        return view;
    }

    public void dataSetup(Restaurant restaurant) {

        if(restaurant!=null) {
            restaurantLayout.setVisibility(View.VISIBLE);
            tvName.setText(restaurant.getName());
            tvPrice.setText(restaurant.getPrice());
            tvLocation.setText(restaurant.getLocation());
            tvHours.setText(parseHours(restaurant.getHours()));
            tvType.setText(restaurant.getTypes());
            phoneNumber = restaurant.getPhone();
            locationName = restaurant.getName();
            lat = restaurant.getLat();
            longt = restaurant.getLongt();
        }
    }

    private void initializeUI(View v)
    {
        restaurantLayout = (View) v.findViewById(R.id.restaurantLayout);
        tvName = (TextView) v.findViewById(R.id.tvName);
        tvPrice = (TextView) v.findViewById(R.id.tvPrice);
        tvLocation = (TextView) v.findViewById(R.id.tvLocation);
        tvHours = (TextView) v.findViewById(R.id.tvHours);
        tvType = (TextView) v.findViewById(R.id.tvType);


        btn_Menu = (Button) v.findViewById(R.id.menu);
        btn_Call = (Button) v.findViewById(R.id.btnCall);
        btn_DineIn = (Button) v.findViewById(R.id.btnDineIn);
        btn_TakeOut = (Button) v.findViewById(R.id.btbTakeOut);

        btn_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startCheckoutActivity = new Intent(getContext(), MainMenuActivity.class);
                startCheckoutActivity.putExtra("ticketnumber","oTpbBkqT");
                startCheckoutActivity.putExtra("locationID","AieMdB5i");
                startActivity(startCheckoutActivity);
            }
        });

        btn_Call.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                Intent callActivity = new Intent(Intent.ACTION_CALL);
                callActivity.setData(Uri.parse("tel:"+phoneNumber));
                if (callActivity.resolveActivity(getActivity().getPackageManager()) != null)
                {
                    startActivity(callActivity);
                }
                //Snackbar.make(v,phoneNumber,Snackbar.LENGTH_LONG).show();
            }
        });
        btn_DineIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent startCheckoutActivity = new Intent(getContext(), MainMenuActivity.class);
                startCheckoutActivity.putExtra("ticketnumber","oTpbBkqT");
                startCheckoutActivity.putExtra("locationID","AieMdB5i");
                startActivity(startCheckoutActivity);
            }

        });

        btn_TakeOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent startCheckoutActivity = new Intent(getContext(), MainMenuActivity.class);
                startCheckoutActivity.putExtra("ticketnumber","oTpbBkqT");
                startCheckoutActivity.putExtra("locationID","AieMdB5i");
                startActivity(startCheckoutActivity);
            }
        });
        tvLocation.setOnClickListener(new View.OnClickListener(){
            //int zoomLevel = 12;
            @Override
            public  void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setPackage("com.google.android.apps.maps");
                String data = String.format("geo:%s,%s",+lat , +longt);
                data = String.format("%s?q=%s,%s", data, +lat,+longt+"("+locationName+")");


                intent.setData(Uri.parse(data));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }


        });
    }

    public String parseHours(String hours){
        return "hours";
    }

    public void addToFav(View view){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);
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
        Toast.makeText(getContext(),"Added to your Favorites List",Toast.LENGTH_LONG).show();
    }
}