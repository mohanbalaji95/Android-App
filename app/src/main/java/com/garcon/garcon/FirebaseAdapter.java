package com.garcon.garcon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mayank on 7/6/2016.
 */
public class FirebaseAdapter extends BaseAdapter {
    ArrayList<Restaurant> myList = new ArrayList<Restaurant>();
    LayoutInflater inflater;
    Context context;


    public FirebaseAdapter(Context context, ArrayList myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Restaurant getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listpiece, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        Restaurant curRestaurant = getItem(position);

        mViewHolder.tvName.setText(curRestaurant.getName());
        mViewHolder.tvPrice.setText(curRestaurant.getPrice());
        mViewHolder.tvRating.setText(curRestaurant.getRating());

        return convertView;
    }

    private class MyViewHolder {
        TextView tvName, tvPrice, tvRating;

        public MyViewHolder(View item) {
            tvName = (TextView) item.findViewById(R.id.tvName);
            tvPrice = (TextView) item.findViewById(R.id.tvPrice);
            tvRating = (TextView) item.findViewById(R.id.tvRating);
        }
    }
}