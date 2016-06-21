package com.garcon.garcon;

/**
 * Created by kritikagopalakrishnan on 6/2/16.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class ThirdFragment extends Fragment {
    private Button beaconButton;
    @Nullable



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.third_layout, null);
        beaconButton = (Button) v.findViewById(R.id.beacon_button);
        beaconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startBeaconActivity = new Intent(getActivity(), BeaconActivity.class);
                startActivity(startBeaconActivity);

            }
        });
        return v;



    }




}