package com.garcon.garcon;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garcon.garcon.R;

/**
 * Created by raja on 7/29/2016.
 */
public class ModifierFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.modifier_row, null);
        return v;
    }

}
