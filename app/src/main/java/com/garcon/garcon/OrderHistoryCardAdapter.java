package com.garcon.garcon;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by siddhiparekh11 on 8/10/17.
 */

public class OrderHistoryCardAdapter extends RecyclerView.Adapter<OrderHistoryCardAdapter.OrderHistoryCardAdapterHolder> {
    private Context context;
    List<OrderHistoryCardView> ordhistorycardviews;

    OrderHistoryCardAdapter(List<OrderHistoryCardView> ordhistorycardviews){
        this.ordhistorycardviews = ordhistorycardviews;
    }
    public static class OrderHistoryCardAdapterHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView resname;
        TextView orderdate;
        TextView reslocation;
        TextView ordertotal;



        OrderHistoryCardAdapterHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            resname = (TextView)itemView.findViewById(R.id.restaurant_name);
            orderdate=(TextView)itemView.findViewById(R.id.order_date);
            reslocation=(TextView)itemView.findViewById(R.id.restaurant_location);
            ordertotal=(TextView)itemView.findViewById(R.id.order_total);
        }
    }


    @Override
    public int getItemCount() {
        return ordhistorycardviews.size();
    }
    @Override
    public OrderHistoryCardAdapter.OrderHistoryCardAdapterHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //    View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fav_card, viewGroup, false);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_receipt_card, viewGroup, false);
        OrderHistoryCardAdapter.OrderHistoryCardAdapterHolder pvh = new OrderHistoryCardAdapter.OrderHistoryCardAdapterHolder(view);
        return pvh;
    }
    @Override
    public void onBindViewHolder(OrderHistoryCardAdapter.OrderHistoryCardAdapterHolder orderhistorycardadapterholders, final int position) {
        orderhistorycardadapterholders.resname.setText(ordhistorycardviews.get(position).getResname());
        orderhistorycardadapterholders.orderdate.setText(ordhistorycardviews.get(position).getOrderdate().toString());
        orderhistorycardadapterholders.reslocation.setText(ordhistorycardviews.get(position).getReslocation());
        orderhistorycardadapterholders.ordertotal.setText(Double.toString(ordhistorycardviews.get(position).getTotal()));



    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

