package com.garcon.garcon;
import com.garcon.garcon.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by raja on 7/27/2016.
 */
/**
 * An adapter for various modifiers of the menu item categories.
 */
public class ModifierAdapter extends BaseAdapter implements ListAdapter {

    public final static String LOG_TAG = ModifierAdapter.class.getSimpleName();
    private Context context;
    View view;
    private MenuItem.ModifierGroup mgrp;

    public ModifierAdapter(MenuItem.ModifierGroup mgrp, Context context) {
        this.mgrp = mgrp;
        this.context = context;
    }

    public MenuItem.ModifierGroup getModifierGroup(){return  this.mgrp;}

    @Override
    public int getCount() {
        return mgrp.getModifierList().size();
    }

    @Override
    public Object getItem(int pos) {
        return mgrp.getModifierList().get(pos);
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
            view = inflater.inflate(R.layout.modifier_row, null);
        }

        final CheckedTextView mod_name = (CheckedTextView) view.findViewById(R.id.mod_name);
        mod_name.setText(mgrp.getModifierList().get(position).getName());

        if(mgrp.getModifierList().get(position).isAdded())
            mod_name.setChecked(true);
        else
            mod_name.setChecked(false);

        final TextView mod_price = (TextView) view.findViewById(R.id.mod_price);
        mod_price.setText(String.valueOf(mgrp.getModifierList().get(position).getPricePerUnit()));
        //TODO price levels list

        return view;
    }

}