package com.garcon.garcon;
import com.garcon.garcon.R;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.*;

public class ReviewRowItemAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<MenuItem> list;
    private Context context;
    View view;
    public final static String LOG_TAG = CartRowItemAdapter.class.getSimpleName();

    public ReviewRowItemAdapter(ArrayList<MenuItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
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
            view = inflater.inflate(R.layout.review_row_item_layout, null);
        }

        //Handle TextView and display string from your list
        final TextView listItemText = (TextView) view.findViewById(R.id.menu_item_string);
        listItemText.setText(list.get(position).getName());

        final TextView price = (TextView) view.findViewById(R.id.price);
        MenuItem m = list.get(position);
        Double base = m.getPrice();
        for(MenuItem.ModifierGroup mgrp : m.getModifierGroups()){
            for(MenuItem.ModifierGroup.ItemModifier im : mgrp.getModifierList()){
                if(im.isAdded()){
                    base+=im.getPricePerUnit();
                }
            }
        }
        Double subtotal = base*m.getNumOrdered();
        price.setText(String.valueOf(subtotal));

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        listItemText.setWidth((int)(0.5*width));

        final TextView number_ordered = (TextView) view.findViewById(R.id.number_ordered);
        number_ordered.setText(String.valueOf(list.get(position).getNumOrdered()));

        notifyDataSetChanged();
        return view;
    }

}