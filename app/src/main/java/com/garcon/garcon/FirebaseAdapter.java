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
        super();
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
    //DO NOT USE: required by BaseAdapter but cannot return anything besides a long
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
        mViewHolder.tvRating.setText(Double.toString(curRestaurant.getRating()));
        mViewHolder.tvLocation.setText(curRestaurant.getLocation());
        mViewHolder.tvHours.setText(curRestaurant.getHours());
        mViewHolder.tvType.setText(curRestaurant.getTypes());

        return convertView;
    }

    /**
     * This class is for each list item
     * TODO: Implement an image into search results
     */
    private class MyViewHolder {
        TextView tvName, tvPrice, tvRating, tvLocation, tvHours, tvType;

        public MyViewHolder(View item) {
            tvName = (TextView) item.findViewById(R.id.tvName);
            tvPrice = (TextView) item.findViewById(R.id.tvPrice);
            tvRating = (TextView) item.findViewById(R.id.tvRating);
            tvLocation = (TextView) item.findViewById(R.id.tvLocation);
            tvHours = (TextView) item.findViewById(R.id.tvHours);
            tvType = (TextView) item.findViewById(R.id.tvType);

        }
    }
}