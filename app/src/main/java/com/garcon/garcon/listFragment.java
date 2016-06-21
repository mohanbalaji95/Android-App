package com.garcon.garcon;

import android.app.ListActivity;
import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mayank on 6/13/2016.
 */
public class listFragment extends ListFragment {
    private ListView LV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
            android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    /**
        LV = (ListView) findViewById(android.R.id.list);
        String[] a = {"make", "america", "great", "again"};

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, a);
        LV.setAdapter(adapter);
 **/
    }

}
