package com.garcon.garcon;
import com.garcon.garcon.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raja on 7/6/2016.
 */

public class MainItemActivity extends Activity {

    private final static String LOG_TAG = MainItemActivity.class.getSimpleName();
    private MenuItem item;
    private int numOrdered = 1;
    boolean fromMenu;
    private int position;
    String locationName;
    String restaurantname;
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        Bundle extras = getIntent().getExtras();
        item = (MenuItem) extras.getSerializable("menu-item"); //comes from MainMenuActivity -> MenuItemAdapter -> listItemText
        restaurantname=getIntent().getStringExtra("currentrestaurant");
        fromMenu = true;
        if(item==null){ // comes from MainCartActivity -> CartRowItemAdapter -> editButton
            position = extras.getInt("position");
            item = OrderSingleton.getInstance().getList().get(position);
            numOrdered = item.getNumOrdered();
            fromMenu = false;
        }
        setContentView(R.layout.activity_main_item);

        //ensures display begins from top of view
        ScrollView scrollview = (ScrollView) findViewById(R.id.scrollview);
        scrollview.setFocusableInTouchMode(true);
        scrollview.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        setUpButtons();
        setUpSizes();
        //setUpFoodImage();
        setUpFoodInformation(item);

        RelativeLayout description = (RelativeLayout) findViewById(R.id.foodInformation);
        //Log.i("clash","num mod groups: "+item.getModifierGroups().size());
        for(int nMod = 0; nMod < item.getModifierGroups().size(); nMod++) {
            // nMod = 0;1;2
            // id(textview,listview) = 1,2;3,4;5,6

            //mod group name
            TextView mod_title = new TextView(this);
            RelativeLayout.LayoutParams p_title = new RelativeLayout.LayoutParams
                    (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mod_title.setId(2*nMod+1);
            Log.i("titleid",mod_title.getId()+"");
            if(nMod==0)
                p_title.addRule(RelativeLayout.BELOW, R.id.item_description);
            else{
                p_title.addRule(RelativeLayout.BELOW, description.findViewById(2*nMod).getId());
            }

            String text = item.getModifierGroups().get(nMod).getName();
            int max = item.getModifierGroups().get(nMod).getMaximum();
            int min = item.getModifierGroups().get(nMod).getMinimum();
            boolean required = item.getModifierGroups().get(nMod).isRequired();

            if(required){
                mod_title.setText(text + ": Chose at least " + min +" and at most " + max + " (required)");
            }
            else{
                mod_title.setText(text + ": Chose at least " + min +" and at most " + max + " (not required)");
            }

            mod_title.setTextSize(20f);
            mod_title.setBackgroundColor(Color.rgb(201,0,0));
            mod_title.setTextColor(Color.WHITE);
            mod_title.setLayoutParams(p_title);

            description.addView(mod_title);

            //options
            final ListView lv = new NonScrollableListView(this);
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams
                    (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lv.setId(mod_title.getId()+1);
            p.addRule(RelativeLayout.BELOW, description.findViewById(mod_title.getId()).getId());
            lv.setLayoutParams(p);
            int color;
            switch (nMod%2) {
                case 0:
                    color = Color.WHITE;
                    break;
                case 1:
                    color = Color.rgb(200,200,100);
                    break;
                default:
                    color = Color.RED;
                    break;
            }
            lv.setBackgroundColor(color);

            MenuItem.ModifierGroup mgrp = item.getModifierGroups().get(nMod);
            final ModifierAdapter adapter = new ModifierAdapter(mgrp, this);
            lv.setAdapter(adapter);
            lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

            description.addView(lv);

            mod_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggle_contents(lv);
                }
            });

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,  int position, long id) {

                    MenuItem.ModifierGroup.ItemModifier mMod = (MenuItem.ModifierGroup.ItemModifier) adapter.getItem(position);
                    boolean state = mMod.isAdded();

                    if(state) { //from true to false
                        mMod.setAdded(false);
                        adapter.notifyDataSetChanged();
                    }
                    else{ //from false to true
                        int num_checked = 0;
                        for(MenuItem.ModifierGroup.ItemModifier im : adapter.getModifierGroup().getModifierList()){
                            if(im.isAdded()){
                                num_checked++;
                            }
                        }
                        int max = adapter.getModifierGroup().getMaximum();
                        if(num_checked>=max){
                            Toast.makeText(MainItemActivity.this,
                                    "You can only select "+max+" option(s).", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            mMod.setAdded(true);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            });

        }

        if(item.getModifierGroups().size()>0){
            TextView tv = (TextView) findViewById(R.id.specialInstructions);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv.getLayoutParams();
            params.addRule(RelativeLayout.BELOW, item.getModifierGroups().size()*2);
            tv.setLayoutParams(params);
        }

    }

    void setUpSizes() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        //getWindow().setLayout((int) (width * 0.875), (int) (height * 0.875));

        TextView itemName = (TextView) findViewById(R.id.item_name);
        itemName.setWidth((int) (0.5 * width));

        /*ImageView foodImage = (ImageView) findViewById(R.id.foodImage);
        foodImage.setMaxHeight((int) (0.4 * height)); */

    }

  /*  public void setUpFoodImage() {
        ImageView foodPic = (ImageView) findViewById(R.id.foodImage);
        foodPic.setScaleType(ImageView.ScaleType.FIT_XY);
    } */

    public void setUpFoodInformation(MenuItem item) {
        TextView foodPic = (TextView) findViewById(R.id.item_name);
        foodPic.setText(item.getName());
        locationName=item.getName();
        TextView item_price = (TextView) findViewById(R.id.item_price);
        item_price.setText(String.valueOf(item.getPrice()));
    }
    public void addToFav(View view){
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);
        String value = sharedPreferences.getString("favmenu3_string",null);
        if(value!=null){
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            favcardview favcardarray[] = gson.fromJson(value, favcardview[].class);

            List<favcardview> favcardviews;
            favcardviews = new ArrayList<>();
            for(int i =0 ;i<favcardarray.length;i++){
                favcardviews.add(new favcardview(favcardarray[i].name,R.drawable.salad,favcardarray[i].extra));
            }
            favcardviews.add(new favcardview(locationName,R.drawable.salad,restaurantname));
            SharedPreferences.Editor spEditor = sharedPreferences.edit();
            spEditor.putString("favmenu3_string",gson.toJson(favcardviews));
            spEditor.commit();
        }
        else{
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            List<favcardview> favcardviews;
            favcardviews = new ArrayList<>();
            favcardviews.add(new favcardview(locationName,R.drawable.salad,restaurantname));
            SharedPreferences.Editor spEditor = sharedPreferences.edit();
            spEditor.putString("favmenu3_string",gson.toJson(favcardviews));
            spEditor.commit();

        }
        Toast.makeText(this,"Added to your Favorites List",Toast.LENGTH_LONG).show();
    }

    public void setUpButtons(){

        final TextView tv = (TextView) findViewById(R.id.numItems);
        tv.setText(String.valueOf(numOrdered));

        final EditText userText = (EditText) findViewById(R.id.userText);

        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add item to viewcart

                if(fromMenu){
                    if (numOrdered > 0 && checkMinMax()) {
                        //change num_ordered, special instructions,all modifiers
                        OrderSingleton.getInstance().addItem(item,numOrdered,userText.getText().toString());
                        Log.i(LOG_TAG, "item added");
                    }
                }
                else{
                    item.setNumOrdered(numOrdered);
                    item.setSpecialInstructions(userText.getText().toString());
                    OrderSingleton.getInstance().getList().set(position,item);
                    Log.i(LOG_TAG, "order edited");
                    MainCartActivity.rowAdapter.notifyDataSetChanged();
                }
                finish();
                //Intent intent = new Intent(MainItemActivity.this, MainMenuActivity.class);
                //MainItemActivity.this.startActivity(intent);

            }
        });

        Button decNumOrderedButton = (Button) findViewById(R.id.decNumOrderedButton);
        decNumOrderedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //decrease number ordered by one until numOrdered = 0
                if (numOrdered > 1)
                    numOrdered--;
                tv.setText(String.valueOf(numOrdered));
            }
        });

        Button incNumOrderedButton = (Button) findViewById(R.id.incNumOrderedButton);
        incNumOrderedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //increase number ordered by one
                numOrdered++;
                tv.setText(String.valueOf(numOrdered));
            }
        });
    }

    public boolean checkMinMax(){
        ArrayList<MenuItem.ModifierGroup> mgList = item.getModifierGroups();
        for(int i = 0; i < mgList.size(); i++){
            MenuItem.ModifierGroup mgrp = mgList.get(i);
            int min = mgrp.getMinimum();
            int max = mgrp.getMaximum();

            int num_checked = 0;
            for(MenuItem.ModifierGroup.ItemModifier im : mgrp.getModifierList())
                if(im.isAdded())
                    num_checked++;

            if((num_checked<min || num_checked>max) && mgrp.isRequired())
                return false;
        }
        return true;
    }

    public void toggle_contents(View view) {
        //no effects
        /*
        options.setVisibility(view.isShown()
                ? View.GONE
                : View.VISIBLE);
        */
        if (view.isShown()) {
            UtilityFX.slide_up(this, view);
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            UtilityFX.slide_down(this, view);
        }
    }

}