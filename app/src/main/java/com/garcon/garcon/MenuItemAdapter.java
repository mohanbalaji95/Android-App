package com.garcon.garcon;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.List;
import java.util.ArrayList;
import static android.app.PendingIntent.getActivity;
import static android.content.Intent.getIntent;
/**
 * An adapter for list of the menu items.
 * It is being used on RestaurantDetailActivity.
 */
public class MenuItemAdapter extends BaseAdapter implements ListAdapter {

    public final static String LOG_TAG = MenuItemAdapter.class.getSimpleName();
    private List<MenuItem> list;
    private Context context;
    public Context con;
    View view;
    private String curr_rest;
    private MenuItem item;
    private int numOrdered = 1;
    boolean fromMenu=true;
    private int position;
    String restaurantname;

    public MenuItemAdapter(List<MenuItem> list, Context context) {
        this.list = list;
        this.context = context;
        this.curr_rest="";
    }
    public MenuItemAdapter(List<MenuItem> list, Context context,String curr_rest)
    {
        this.list = list;
        this.context = context;
        this.curr_rest=curr_rest;
    }

    @Override
    public int getCount() {
        //limits the number of rows that appear
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        //return list.get(pos).getId();
        //just return 0 if your list items do not have an Id variable.
        return 0;
    }
    public void setUpButtons(){

        final TextView tv = (TextView) view.findViewById(R.id.numItems);
        tv.setText(String.valueOf(numOrdered));





    }

    public boolean checkMinMax(){

        return true;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.menu_row_item, null);

        }

        final TextView listItemText = (TextView) view.findViewById(R.id.rowTextView);
        final TextView itemprice=(TextView)view.findViewById(R.id.itemprice);

        listItemText.setText(list.get(position).getName());
        itemprice.setText("$"+list.get(position).getPrice());

        if(list.get(position).getNumOrdered() > 0){
            listItemText.setBackgroundColor(Color.GRAY);
        }

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int height = dm.heightPixels;
        listItemText.setHeight((int)(0.12*height));
        listItemText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO blur or dim background
                Log.i(LOG_TAG, "menu item selected");
                Intent intent = new Intent(context, MainItemActivity.class);
                Bundle extras = new Bundle();
                extras.putSerializable("menu-item", list.get(position));
                intent.putExtras(extras);
                intent.putExtra("currentrestaurant",curr_rest);
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });


        notifyDataSetChanged();

        Button addButton = (Button) view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean toast = false;
                if (OrderSingleton.getInstance().getcurrentRestaurent() != null) {
                    if (!OrderSingleton.getInstance().getcurrentRestaurent().equals(curr_rest)) {
                        OrderSingleton.getInstance().clearList();
                        String textToShow = "New restaurant selections. Current cart items cleared.";
                        toast=true;
                        Toast.makeText(context, textToShow, Toast.LENGTH_LONG).show();
                    }
                }

                if (numOrdered > 0 && checkMinMax()) {
                    item = list.get(position);
                    //change num_ordered, special instructions,all modifiers
                    OrderSingleton.getInstance().addItem(item, numOrdered, "", curr_rest);
                    if(!toast) {
                        String textToShow = "Item added";
                        Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });


        notifyDataSetChanged();
        return view;


    }



    public void cartClick(View v)
    {
        //implement code to add item to cart

    }
}
