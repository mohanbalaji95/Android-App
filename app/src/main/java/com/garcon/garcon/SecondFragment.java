package com.garcon.garcon;

/**
 * Created by kritikagopalakrishnan on 6/2/16.
 * Edited by Mayank Tiwari
 */
import android.app.ListActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class SecondFragment extends Fragment {

    private TextView text;
    private ListView LV;
    private List<String> list;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LV = new ListView(getActivity().getBaseContext());

        listSetup(LV);

        return inflater.inflate(R.layout.second_layout, null);
    }

    public void listSetup(ListView LV){

        LV = (ListView) getActivity().findViewById (android.R.id.list);
        String[] a = {"make", "america", "great", "again"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, a);
        LV.setAdapter(adapter);

    }

   /* public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        return true;
    }
    */


}