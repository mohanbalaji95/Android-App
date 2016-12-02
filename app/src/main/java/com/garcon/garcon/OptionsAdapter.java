package com.garcon.garcon;
import com.garcon.garcon.R;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class OptionsAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<String> list;
    private Context context;
    View view;
    public final static String LOG_TAG = OptionsAdapter.class.getSimpleName();

    public OptionsAdapter(ArrayList<String> list, Context context) {
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
            view = inflater.inflate(R.layout.card_row, null);
        }

        final TextView listItemText = (TextView) view.findViewById(R.id.rowTextView);
        //listItemText.setText(list.get(position));
        listItemText.setText(list.get(position));
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        listItemText.setWidth((int)(0.5*width));

        return view;
    }

}