package com.garcon.garcon;

import java.util.ArrayList;
import java.util.List;

import android.app.SearchManager;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;



/**
 * Created by Mayank Tiwari on 6/9/2016.
 */
public class SearchActivity extends ListActivity {

    private ListView LV;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);

        LV = (ListView) findViewById(android.R.id.list);
        String[] a = {"make", "america", "stupid", "again"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, a);
        //LV.setAdapter(adapter);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            search(query);
            LV.setAdapter(adapter);
        }


    }

    protected void search (String query){
        //TODO: search based on data storage
    }

    }
