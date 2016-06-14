package com.garcon.garcon;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mayank on 6/13/2016.
 */
public class listActivity extends ListActivity {
    private List<String> list;
    private ListView LV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LV = (ListView) findViewById(R.id.listV);
        list = new ArrayList<String>();
        list.add("hi");
        list.add("hi");
        list.add("hi");
        list.add("hi");
        list.add("hi");
        list.add("hi");
        list.add("hi");
        list.add("hi");
        list.add("hi");

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.listText, list);
        //LV.setAdapter(adapter);
        setListAdapter(adapter);
    }

}
