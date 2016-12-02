package com.garcon.garcon;

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

public class ViewCartRowItemAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<OrderedItem> list;
    private Context context;
    View view;
    public final static String LOG_TAG = ViewCartRowItemAdapter.class.getSimpleName();
    public static final String ORDERED_LIST = "orderedItems";
    public static final String EDIT_ORDER = "editOrder";

    public ViewCartRowItemAdapter(ArrayList<OrderedItem> list, Context context) {
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
            view = inflater.inflate(R.layout.row_item_layout, null);
        }

        //Handle TextView and display string from your list
        final TextView listItemText = (TextView) view.findViewById(R.id.menu_item_string);
        listItemText.setText(list.get(position).getName() + " x" + list.get(position).getNumOrdered());

        // edits order on click of menu item name
        /*
        listItemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(LOG_TAG,"details requested");
                Intent intent = new Intent(context, MainItemActivity.class);
                Bundle extras = new Bundle();
                extras.putString("activity", LOG_TAG);
                extras.putString("name",listItemText.getText().toString());
                intent.putExtras(extras);
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        } );
        */

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        listItemText.setWidth((int)(0.5*width));

        //Handle buttons and add onClickListeners
        Button deleteButton = (Button)view.findViewById(R.id.delete_btn);
        Button moreButton = (Button)view.findViewById(R.id.more_btn);

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
                                // ArrayOutOfBounds Exception, fixed
                                Log.i(LOG_TAG, Integer.toString(position)+" position");
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
                /*
                list.remove(position);
                notifyDataSetChanged();
                */
            }
        });

        moreButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO blur or dim background
                //v.getBackground().setAlpha(256);
                Log.i(LOG_TAG,"more button: edit order");
                Intent intent = new Intent(context, MainItemActivity.class);

                Bundle extras = new Bundle();
                extras.putString("activity", LOG_TAG);
                extras.putString("name",listItemText.getText().toString());
                intent.putExtras(extras);

                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });

        notifyDataSetChanged();
        return view;
    }

}