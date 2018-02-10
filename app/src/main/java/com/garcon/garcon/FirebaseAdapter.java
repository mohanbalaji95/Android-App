package com.garcon.garcon;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mayank on 7/6/2016.
 * This adapter displays the list of restaurants with few details on one of the tabs on home screen.
 */
public class FirebaseAdapter extends BaseAdapter {
    ArrayList<Restaurant> myList = new ArrayList<Restaurant>();
    LayoutInflater inflater;
    Context context;

    public FirebaseAdapter(Context context, ArrayList myList) {
        super();
        this.myList = myList;
        this.context = context;
        assert(context!=null);
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
        RestaurantViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.restaurant_list_item, parent, false);
            mViewHolder = new RestaurantViewHolder(convertView);

            //Drawable progressStar =  mViewHolder.ratingRestaurant.getProgressDrawable();
            //DrawableCompat.setTint(progressStar, Color.RED);
            //mViewHolder.ratingRestaurant.setProgressDrawable(progressStar);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (RestaurantViewHolder) convertView.getTag();
        }

        Restaurant curRestaurant = getItem(position);

        mViewHolder.tvName.setText(curRestaurant.getName());
        mViewHolder.tvPrice.setText(curRestaurant.getPrice());
        //mViewHolder.ratingRestaurant.setRating((float) curRestaurant.getRating());
        mViewHolder.tvLocation.setText(curRestaurant.getLocation().trim());
        mViewHolder.tvType.setText(curRestaurant.getType());

        return convertView;
    }

    /**
     * This class is for each list item
     * TODO: Implement an image into search results
     */
    private class RestaurantViewHolder {
        TextView tvName, tvPrice, tvLocation, tvHours, tvType;
        ImageView ivRestaurant;
        //RatingBar ratingRestaurant;

        public RestaurantViewHolder(View item) {
            tvName = (TextView) item.findViewById(R.id.tvName);
            tvPrice = (TextView) item.findViewById(R.id.tvPrice);
            //ratingRestaurant = (RatingBar) item.findViewById(R.id.ratingRestaurant);
            tvLocation = (TextView) item.findViewById(R.id.tvLocation);
            tvType = (TextView) item.findViewById(R.id.tvType);
            ivRestaurant = (ImageView)item.findViewById(R.id.ivRestaurant);
        }
    }
}
