package com.garcon.garcon;

import java.util.ArrayList;
import java.util.List;

import android.app.SearchManager;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;



/**
 * Created by Mayank Tiwari on 6/9/2016.
 */
public class SearchActivity extends ListActivity {

    private TextView text;
    private ListView listView;
    private List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);
        listView = (ListView) findViewById(R.id.list);
        list = new ArrayList<String>();
        text = (TextView) findViewById(R.id.actionText);
        list.add("hi");
        list.add("hi");
        list.add("hi");
        list.add("hi");list.add("hi");
        list.add("hi");
        list.add("hi");


        ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, R.layout.second_layout,list);
        listView.setAdapter(adapter);


        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            search(query);
        }
    }

    protected void onListItemClick(ListView list, View view, int position, long id){
        super.onListItemClick(list, view, position, id);

        String selectedItem = (String) getListView().getItemAtPosition(position);
        text.setText("clicked " + selectedItem + " at position " + position);

    }

    protected void search (String query){
        //TODO: search based on data storage
    }

}
