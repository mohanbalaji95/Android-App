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

        Button checkoutButton = (Button) v.findViewById(R.id.checkout_button);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startCheckoutActivity = new Intent(getActivity(), MainMenuActivity.class);
                startCheckoutActivity.putExtra("ticketnumber","oTpbBkqT");
                startCheckoutActivity.putExtra("locationID","8cg4k4kc");

                startActivity(startCheckoutActivity);

            }
        });
        return v;



    }




}