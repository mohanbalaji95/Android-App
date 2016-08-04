package com.garcon.garcon;
import com.garcon.garcon.R;

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
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuItemAdapter extends BaseAdapter implements ListAdapter {

    public final static String LOG_TAG = MenuItemAdapter.class.getSimpleName();
    private ArrayList<MenuItem> list;
    private Context context;
    View view;

    public MenuItemAdapter(ArrayList<MenuItem> list, Context context) {
        this.list = list;
        this.context = context;
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.menu_row, null);
        }

        final TextView listItemText = (TextView) view.findViewById(R.id.rowTextView);
        listItemText.setText(list.get(position).getName());

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
                Log.i(LOG_TAG,"menu item selected");
                Intent intent = new Intent(context, MainItemActivity.class);
                Bundle extras = new Bundle();
                extras.putSerializable("menu-item",list.get(position));
                intent.putExtras(extras);
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });

        notifyDataSetChanged();
        return view;
    }

}