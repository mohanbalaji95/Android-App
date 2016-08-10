package com.garcon.garcon;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
/**
 * Created by kushaldileep on 6/21/2016.
 */
public class favcardadapter extends RecyclerView.Adapter<favcardadapter.favcardadapterholder> {
    private Context context;
    List<favcardview> favcardviews;

    favcardadapter(List<favcardview> favcardviews){
        this.favcardviews = favcardviews;
    }
    public static class favcardadapterholder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView restaurantname;
        ImageView personPhoto;

        favcardadapterholder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            restaurantname = (TextView)itemView.findViewById(R.id.restaurant_name);
            personPhoto = (ImageView)itemView.findViewById(R.id.imageView2);
        }
    }


    @Override
    public int getItemCount() {
        return favcardviews.size();
    }
    @Override
    public favcardadapterholder onCreateViewHolder(ViewGroup viewGroup, int i) {
    //    View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fav_card, viewGroup, false);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fav_card, viewGroup, false);
        favcardadapterholder pvh = new favcardadapterholder(view);
        return pvh;
    }
    @Override
    public void onBindViewHolder(favcardadapterholder favcardadapterholders, final int position) {
        favcardadapterholders.restaurantname.setText(favcardviews.get(position).name);
        favcardadapterholders.personPhoto.setImageResource(favcardviews.get(position).photoid);



    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
