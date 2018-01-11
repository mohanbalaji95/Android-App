package com.garcon.garcon.Payment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.garcon.garcon.R;

import java.util.ArrayList;

/**
 * Created by akshaymathur on 7/12/17.
 */

public class OrderedItemsAdapter extends RecyclerView.Adapter<OrderedItemsAdapter.MyViewHolder> {

    private ArrayList<OmnivoreOrderItems> mOrderItems;

    public OrderedItemsAdapter(ArrayList<OmnivoreOrderItems> myDataset) {
        mOrderItems = myDataset;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public OrderedItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_row,parent,false);
        /*TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);*/
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        OmnivoreOrderItems item = mOrderItems.get(position);
        holder.mItemName.setText(item.getItemname());
        holder.mItemQuantity.setText(String.valueOf(item.getQuantity()));
        holder.mItemPrice.setText(String.valueOf(item.getPrice()));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mOrderItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView mItemName;
        public TextView mItemQuantity;
        public TextView mItemPrice;
        public MyViewHolder(View v) {
            super(v);
            mItemName = (TextView) v.findViewById(R.id.order_item_name);
            mItemQuantity = (TextView) v.findViewById(R.id.order_item_quantity);
            mItemPrice = (TextView) v.findViewById(R.id.order_item_price);
        }
    }
}
