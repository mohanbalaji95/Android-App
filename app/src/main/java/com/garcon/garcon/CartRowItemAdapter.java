package com.garcon.garcon;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.*;

public class CartRowItemAdapter extends BaseAdapter implements ListAdapter {

    public final static String LOG_TAG = CartRowItemAdapter.class.getSimpleName();
    private ArrayList<MenuItem> list;
    private Context context;
    private View view;

    public CartRowItemAdapter(ArrayList<MenuItem> list, Context context) {
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

        Log.i("here","get view called "+getCount());

        view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.cart_row_item_layout, null);
        }

        RelativeLayout root = (RelativeLayout) view.findViewById(R.id.root);
        MenuItem m = list.get(position);
        Log.i("here",m.getName()+" "+m.getNumOrdered());

        final TextView listItemText = (TextView) view.findViewById(R.id.menu_item_string);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        listItemText.setWidth((int)(0.5*width));
        listItemText.setText(m.getName());

        //TODO does not update immediately when item is deleted/added (notifyDataSetChanged doesn't fix)
        /*
        ArrayList<String> modNames = new ArrayList<>();
        for(MenuItem.ModifierGroup mgrp : m.getModifierGroups()){
            for(MenuItem.ModifierGroup.ItemModifier im : mgrp.getModifierList()){
                if(im.isAdded()){ //&& im.getPricePerUnit()>0){
                    modNames.add(im.getName());
                    Log.i("here","added: "+im.getName());
                }
            }
        }
        int i;
        for(i = 0; i < modNames.size(); i++){
            TextView tv = new TextView(context);
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams
                    (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv.setId(i+1);
            if(i==0){
                param.addRule(RelativeLayout.BELOW, R.id.menu_item_string);
            }
            else{
                param.addRule(RelativeLayout.BELOW, root.findViewById(i).getId());
            }
            int dp = 16;
            int px = (int)(dp*dm.density+0.5f);

            tv.setPadding(px,0,0,0);
            tv.setText(modNames.get(i));
            tv.setWidth((int) (0.5 * width));
            tv.setBackgroundColor(Color.YELLOW);
            tv.setLayoutParams(param);
            notifyDataSetChanged();
            root.addView(tv);
        }

        TextView tv = new TextView(context);
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv.setId(i+1);
        if(i==0){
            param.addRule(RelativeLayout.BELOW, R.id.menu_item_string);
        }
        else{
            param.addRule(RelativeLayout.BELOW, root.findViewById(i).getId());
        }
        int dp = 16;
        int px = (int)(dp*dm.density+0.5f);

        tv.setPadding(px,0,0,0);
        tv.setText(((MenuItem) getItem(position)).getSpecialInstructions());
        tv.setWidth((int) (0.5 * width));
        tv.setBackgroundColor(Color.YELLOW);
        tv.setLayoutParams(param);
        notifyDataSetChanged();
        root.addView(tv);
        */

        listItemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //same as edit
            }
        } );


        final TextView number_ordered = (TextView) view.findViewById(R.id.number_ordered);
        number_ordered.setText(String.valueOf(list.get(position).getNumOrdered()));

        Button deleteButton = (Button)view.findViewById(R.id.delete_btn);
        Button editButton = (Button)view.findViewById(R.id.edit_btn);

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Confirm MenuItem Removal");
                alertDialogBuilder
                        .setMessage("Are you sure you want to remove this item?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //delete item
                                Log.i("here", Integer.toString(position)+" position");
                                list.remove(position);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //just close the dialog box and do nothing
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create(); //create AD
                alertDialog.show(); //show AD

                //remove alert dialog and include bottom code if you do not want confirmation
                //list.remove(position);
                //notifyDataSetChanged();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO blur or dim background, possibly v.getBackground().setAlpha(256);
                Log.i("here","edit order");
                Intent intent = new Intent(context, MainItemActivity.class);
                intent.putExtra("position",position);
                //Bundle extras = new Bundle();
                //extras.putSerializable("ordered-item",list.get(position));
                //extras.putInt("position",position);
                //intent.putExtras(extras);
                context.startActivity(intent);
            }
        });

        return view;
    }

}