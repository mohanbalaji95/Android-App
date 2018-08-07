package com.garcon.garcon.Payment;


import android.content.Context;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import com.garcon.garcon.R;


import java.io.Serializable;

public class customTip extends Fragment {

    EditText addTipT;
    Button addTipB;
    String ctip;


    public customTip() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_custom_tip, container, false);

        addTipB = (Button) v.findViewById(R.id.addTipB);
        addTipT = (EditText) v.findViewById(R.id.addTipT);





        addTipB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ctip = addTipT.getText().toString();
                addTip.tipCalc(view, ctip);
                getFragmentManager().beginTransaction().remove(customTip.this).commit();

            }
        });



        return v;
    }
}

