package com.garcon.garcon;
import com.garcon.garcon.R;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuCategoryAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<Category> list;
    private Context context;
    View view;
    public final static String LOG_TAG = MenuCategoryAdapter.class.getSimpleName();

    public MenuCategoryAdapter(ArrayList<Category> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        //sets hard limit on the number of rows that appear
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
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int height = dm.heightPixels;
        listItemText.setHeight((int)(0.275*height));
        //duplicate command
        //listItemText.setWidth((int)(0.5*width));

        //if category is clicked, then items are correspondingly synchronized
        listItemText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG,"category selected");
                if(context instanceof MainMenuActivity){
                    ((MainMenuActivity)context).categorySelectedSync(position);
                    notifyDataSetChanged();
                }
            }
        });

        return view;
    }

}