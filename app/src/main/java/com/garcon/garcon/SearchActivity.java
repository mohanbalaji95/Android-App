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

        Intent intent = getIntent();
        //intent to search
        //TODO: implement search with database
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            search(query);
        }
    }

    protected void search (String query){
        //TODO: search based on data storage
    }
}
