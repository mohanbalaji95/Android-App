package com.garcon.garcon;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.garcon.Constants.Constants;
import com.garcon.Models.Table;
import com.garcon.Models.Ticket;
import com.garcon.garcon.Payment.CheckoutandPay;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TargetApi(21)
/**
 * This activity places user's order to Restaurant. Below are the steps.
 *
 * 1. Open a ticket with Omnivore and obtain the ticket number.
 *      i. fetch and display open tables in dropdown
 *     ii. fetch and display revenue centers
 * 2. Add the user's items to the Ticket Created.
 */
public class MainViewCartActivity extends AppBarActivity {

    static final String LOG_TAG = MainViewCartActivity.class.getSimpleName();
    static ArrayList<MenuItem> listOfItems;
    static CartRowItemAdapter rowAdapter;
    private DatabaseReference mDatabase;
    private View mProgressView;
    private View mScrollView;

    private Boolean done=false;

    String ApiKey=null;
    String locationID="AieMdB5i";
    String ticketID=null;
    Map<Integer,Table> tableMap = null;
    ArrayList<String> tableArray = null;
    SharedPreferences sharedpreferences;
    Ticket ticket;
    Boolean openTicket=false;
    Map<String,String> revenueCenter_map = null;

    Spinner tableSizeSpinner;
    Spinner restaurantAreaSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        listOfItems = OrderSingleton.getInstance().getList();
        Log.i(LOG_TAG, String.valueOf(listOfItems.size()));
        /*
        for(int i = 0; i < listOfItems.size(); i++){
            Log.i(LOG_TAG, ((ArrayList<OrderedItem>) listOfItems).get(i).getName());
        }*/

        setContentView(R.layout.activity_main_cart);
        mProgressView = (ProgressBar) findViewById(R.id.checkout_progress);
        mScrollView = (ScrollView) findViewById(R.id.scrollview);
        mScrollView.setVisibility(View.GONE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Api-Key").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        setKey(dataSnapshot.getValue().toString());
                        Log.d(LOG_TAG,"The Key is--> " + getKey());
                        fetchTables fT = new fetchTables();
                        fT.execute((Void) null);
                        FetchRevenueCenters fetchRevenueCenters = new FetchRevenueCenters();
                        fetchRevenueCenters.execute();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(LOG_TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });




    }

    public Integer getNumItemsOrdered(){
        return listOfItems.size();
    }

    public void view_menu(View view){
        Log.i(LOG_TAG,"request to see menu");
        Intent intent = new Intent(this, MainMenuActivity.class);
        this.startActivity(intent);
    }
    public void setKey(String key){
        ApiKey = key;
    }
    public String getKey(){
        return ApiKey;
    }

    //async task to fetch revenue centers at the location.
    class FetchRevenueCenters extends AsyncTask<Void, Void, Map<String, String>>{


        @Override
        protected Map<String, String> doInBackground(Void... params) {
            String urlfetchRevenueCenters = String.format(Constants.OMNIVORE_API_BASE_URL+"%s/revenue_centers/",locationID);
            try
            {
                URL newURL = new URL(urlfetchRevenueCenters);
                HttpURLConnection connection = (HttpURLConnection)newURL.openConnection();
                connection.setRequestMethod("GET");
                connection.addRequestProperty("Api-Key",ApiKey);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuffer json = new StringBuffer(1024);
                String tmp="";
                while((tmp=reader.readLine())!=null)
                    json.append(tmp).append("\n");
                reader.close();
                Log.d(LOG_TAG,"Fetch Revenue String--> "+json.toString());
                JSONObject data = new JSONObject(json.toString());
                JSONArray revenueCenters = data.getJSONObject("_embedded").getJSONArray("revenue_centers");
                Map<String,String> revCenters = new HashMap<>();
                for (int i =0;i<revenueCenters.length();i++){

                    JSONObject revenueJSONObject = (JSONObject)revenueCenters.get(i);
                    revCenters.put(revenueJSONObject.getString("name"),revenueJSONObject.getString("id"));
                    Log.d(LOG_TAG,"The revenue center IDs are -->  "+revenueJSONObject.get("id"));

                }
                if(connection.getResponseCode()!=200){
                    return null;
                }
                else{
                    return revCenters;
                }



            }
            catch (Exception e){
                Log.d(LOG_TAG,"Error:"+e.getMessage());
                return null;
            }
            //return null;
        }



        @Override
        protected void onPostExecute(Map<String, String> stringStringMap) {
            super.onPostExecute(stringStringMap);
            revenueCenter_map = stringStringMap;
            restaurantAreaSpinner = (Spinner) findViewById(R.id.restaurantArea);
            List<String> revenueCenterNames = new ArrayList<>();
            for (String revenue_center_id : revenueCenter_map.keySet()){
                revenueCenterNames.add(revenue_center_id);

            }
            ArrayAdapter<String> restaurantAreaAdapter = new ArrayAdapter<String>(MainViewCartActivity.this,android.R.layout.simple_spinner_dropdown_item,revenueCenterNames);
            restaurantAreaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            restaurantAreaSpinner.setAdapter(restaurantAreaAdapter);
        }
    }

    //async task to fetch available table.
    class fetchTables extends AsyncTask<Void, Void, ArrayList<Table>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressView.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Table> doInBackground(Void... params) {

            String urlfetchTables = String.format(Constants.OMNIVORE_API_BASE_URL+"%s/tables/",locationID);
            try
            {
                URL newURL = new URL(urlfetchTables);
                HttpURLConnection connection = (HttpURLConnection)newURL.openConnection();
                connection.setRequestMethod("GET");
                connection.addRequestProperty("Api-Key",ApiKey);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuffer json = new StringBuffer(1024);
                String tmp="";
                while((tmp=reader.readLine())!=null)
                    json.append(tmp).append("\n");
                reader.close();
                Log.d(LOG_TAG,"json to string "+json.toString());
                JSONObject data = new JSONObject(json.toString());
                JSONArray tableArray = data.getJSONObject("_embedded").getJSONArray("tables");
                Log.d(LOG_TAG,"The tables are -->  "+tableArray);
                ArrayList<Table> tables = new ArrayList<Table>();
                for (int i =0;i<tableArray.length();i++){

                    JSONObject tableJSONObject = (JSONObject)tableArray.get(i);
                    Table newTable = new Table(tableJSONObject.getString("id"),tableJSONObject.getBoolean("available"),tableJSONObject.getString("name"),tableJSONObject.getInt("number"),tableJSONObject.getInt("seats"));
                    Log.d(LOG_TAG,"The tables IDs are -->  "+tableJSONObject.get("id"));
                    tables.add(newTable);

                }
                if(connection.getResponseCode()!=200){
                    return null;
                }
                else{
                    return tables;
                }



            }
            catch (Exception e){
                Log.d(LOG_TAG,"Error:"+e.getMessage());
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<Table> tables) {
            super.onPostExecute(tables);

            setTableData(tables);
            mProgressView.setVisibility(View.GONE);
            mScrollView.setVisibility(View.VISIBLE);
            //done=true;
            setupUI();
        }
    }
    //async task to add items to the omnivore ticket ID.
    class SendItems extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... params) {
            Log.d(LOG_TAG,"***Sending Items to Omnivore*** >" +listOfItems.size());

            try{
                JSONArray itemArray = new JSONArray();

                for (MenuItem menuItem : listOfItems){
                    JSONObject item = new JSONObject();
                    Log.d(LOG_TAG,"Item ID--> "+menuItem.getId());
                    Log.d(LOG_TAG,"Item Quantity--> "+menuItem.getNumOrdered());
                    Log.d(LOG_TAG,"Item Price Lvl--> "+menuItem.getPrice_levels().get(0).getId());
                    Log.d(LOG_TAG,"Item Comments--> "+menuItem.getSpecialInstructions());

                    item.put("menu_item",menuItem.getId());
                    item.put("quantity",menuItem.getNumOrdered());
                    item.put("price_level",menuItem.getPrice_levels().get(0).getId());
                    item.put("comment",menuItem.getSpecialInstructions());

                    itemArray.put(item);
                }





                //JSONArray modifierArray = new JSONArray();
                //item1.put("modifiers",modifierArray);


                /*JSONObject item2 = new JSONObject();
                item2.put("menu_item","doTaLTyg");
                item2.put("quantity",3);
                item2.put("price_level","L4iqKid8");
                item2.put("comment","Bring it on!");
                JSONArray modifierArray2 = new JSONArray();
                item2.put("modifiers",modifierArray2);
                itemArray.put(item2);*/

                String newMenuItemString = String.format(Constants.OMNIVORE_API_BASE_URL+"%s/tickets/%s/items/",locationID,ticketID);

                URL newURL = new URL(newMenuItemString);
                HttpURLConnection connection = (HttpURLConnection)newURL.openConnection();
                connection.setRequestMethod("POST");
                connection.addRequestProperty("Api-Key",ApiKey);
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(itemArray.toString());
                dStream.flush(); // Flushes the data output stream.
                dStream.close();




                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuffer json = new StringBuffer(1024);
                String tmp="";
                while((tmp=reader.readLine())!=null)
                    json.append(tmp).append("\n");
                reader.close();
                Log.d(LOG_TAG,"json to string "+json.toString());
                JSONObject data = new JSONObject(json.toString());
                Log.d(LOG_TAG,"The ticket ID is -->  "+data.getString("id"));
                SharedPreferences sp = getSharedPreferences("garconpref",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("ticketnumber", data.getString("id"));
                editor.commit();

                if(connection.getResponseCode()!=200){
                    return null;
                }
                Log.d(LOG_TAG,"json object to string "+data.toString());

                Log.d(LOG_TAG,"Adding data to FireBase");
                sharedpreferences = getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);
                String userUUID = sharedpreferences.getString(getString(R.string.User_UUID_Key),null);
                ticket = new Ticket(ticketID,"MjikgioG","KxiAaip5","LdiqGibo","jLiyniEb",2,"FirebaseTicket",true,userUUID,locationID);
                ticket.setOpen(data.getBoolean("open"));
                ticket.setOpened_at(data.getLong("opened_at"));
                Map<String,Object> ticketValues = ticket.toMap();
                String key = mDatabase.child("orders").push().getKey();
                Log.d(LOG_TAG,"The key is --> "+key);
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/orders/" + key, ticketValues);
                Log.d(LOG_TAG,"The updateChildren Object is --> "+childUpdates.toString());
                mDatabase.updateChildren(childUpdates);
                mDatabase.child("orders").child(key).child("menu_items").setValue(listOfItems);

                Intent intent = new Intent(MainViewCartActivity.this, CheckoutandPay.class);
                //intent.putExtra("instructions", ((EditText) findViewById(R.id.specialInstructions)).getText().toString());
                //intent.putExtra("table", ((EditText) findViewById(R.id.tableNumber)).getText().toString());
                //Spinner area = (Spinner) findViewById(R.id.restaurantArea);
                //intent.putExtra("area", area.getItemAtPosition(area.getSelectedItemPosition()).toString());
                intent.putExtra("ticketID", ticketID);
                intent.putExtra("firebasekey",key);
                editor.putString("firebasekey",key);
                editor.commit();
                OrderSingleton.getInstance().clearList();
                startActivity(intent);
                finish();
                return data;


            }
            catch (Exception e){
                Log.e(LOG_TAG,e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            //listOfItems.clear();
        }
    }
    //async task to open a ticket at omnivore and save the ticket number.
    class OpenTicket extends AsyncTask<String, Void, JSONObject> {



        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                String tablenumber = params[0];
                String revenuecenter = params[1];
                JSONObject createTicket = new JSONObject();
                createTicket.put("employee", "MjikgioG");
                createTicket.put("order_type", "KxiAaip5");
                createTicket.put("revenue_center", revenuecenter);
                createTicket.put("table", tablenumber);
                createTicket.put("guest_count",2);
                createTicket.put("name", "New Ticket");
                createTicket.put("auto_send", true);

                Log.d(LOG_TAG,"New Ticket JSON-->"+createTicket.toString());

                String newTicketString = String.format(Constants.OMNIVORE_API_BASE_URL+"%s/tickets/",locationID);
                URL newURL = new URL(newTicketString);
                HttpURLConnection connection = (HttpURLConnection)newURL.openConnection();
                connection.setRequestMethod("POST");
                connection.addRequestProperty("Api-Key",ApiKey);
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(createTicket.toString());
                dStream.flush(); // Flushes the data output stream.
                dStream.close();




                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuffer json = new StringBuffer(1024);
                String tmp="";
                while((tmp=reader.readLine())!=null)
                    json.append(tmp).append("\n");
                reader.close();
                Log.d(LOG_TAG,"json to string "+json.toString());
                SharedPreferences sp = getApplicationContext().getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);
                SharedPreferences.Editor spEditor = sp.edit();
                spEditor.putString("ticketString",json.toString());
                spEditor.commit();
                Log.d(LOG_TAG,"json in sharedpref "+sp.getString("ticketString",null));
                JSONObject data = new JSONObject(json.toString());
                Log.d(LOG_TAG,"The ticket ID is -->  "+data.getString("id"));
                ticketID = data.getString("id");
                openTicket = true;
                if(connection.getResponseCode()!=201){
                    return null;
                }
                else{
                    Log.d(LOG_TAG,"Adding data to FireBase");
                    sharedpreferences = getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);
                    String userUUID = sharedpreferences.getString(getString(R.string.User_UUID_Key),null);
                    ticket = new Ticket(ticketID,"MjikgioG","KxiAaip5","LdiqGibo","jLiyniEb",2,"FirebaseTicket",true,userUUID,locationID);
                    ticket.setOpen(data.getBoolean("open"));
                    ticket.setOpened_at(data.getLong("opened_at"));
                    Map<String,Object> ticketValues = ticket.toMap();
                    String key = mDatabase.child("orders").push().getKey();
                    Log.d(LOG_TAG,"The key is --> "+key);
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/orders/" + key, ticketValues);
                    Log.d(LOG_TAG,"The updateChildren Object is --> "+childUpdates.toString());
                    mDatabase.updateChildren(childUpdates);



                }
                Log.d(LOG_TAG,"json object to string "+data.toString());
                return data;
            }catch(Exception e){
                //return null;
                System.out.println(LOG_TAG+"Error:"+e.getMessage());
                //Log.e(TAG,);
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            if(openTicket){
                SendItems si = new SendItems();
                si.execute();
            }

        }
    }

    public void setTableData(ArrayList<Table> tables){
        tableMap = new HashMap<Integer, Table>();
        tableArray = new ArrayList<String>();
        //int i =0;
        for (Table table : tables)  {
            //if(table.getAvailable()){
                tableMap.put(table.getNumber(),table);
                Log.d(LOG_TAG,"Table numbers are --> "+table.getNumber());
                //tableArray[i] = table.getNumber()+"";
                tableArray.add(table.getNumber()+"");
                //i++;
            //}


        }
        Log.d(LOG_TAG,"Size of table array is --> "+tableMap.size());

    }

    public Map<Integer,Table> getTableData (){
        return tableMap;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
            mScrollView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void setupUI(){
        //ensures display begins from top of view
        ScrollView scrollview = (ScrollView) findViewById(R.id.scrollview);
        scrollview.setFocusableInTouchMode(true);
        scrollview.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);


        tableSizeSpinner = (Spinner) findViewById(R.id.tableSize);
        /*ArrayAdapter<CharSequence> tableSizeAdapter = ArrayAdapter.createFromResource(this, R.array.optionsTableSize,
                android.R.layout.simple_spinner_dropdown_item);*/
        if(tableArray.size()>0){
            ArrayAdapter<String> tableSizeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,tableArray);
            tableSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            tableSizeSpinner.setAdapter(tableSizeAdapter);

        }
        else{
            tableArray.add("No Table Available");
            ArrayAdapter<String> tableSizeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,tableArray);
            tableSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            tableSizeSpinner.setAdapter(tableSizeAdapter);
        }


        /*Spinner orderDateSpinner = (Spinner) findViewById(R.id.orderDate);
        ArrayAdapter<CharSequence> orderDateAdapter = ArrayAdapter.createFromResource(this, R.array.optionsOrderDate,
                android.R.layout.simple_spinner_dropdown_item);
        orderDateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderDateSpinner.setAdapter(orderDateAdapter);

        Spinner timeSpinner = (Spinner) findViewById(R.id.orderTime);
        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this, R.array.optionsOrderTime,
                android.R.layout.simple_spinner_dropdown_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);*/

    /*  listOfItems.add("REALLY LONG TEXT STRING TO TEST IF IT PROPERLY WRAPS FOR THE TABLET THE TABLET THE TABLET...");
        listOfItems.add("Old Fashioned Buttermilk Pancakes");
        listOfItems.add("A for Apple");
        */

        //instantiate custom adapter
        rowAdapter = new CartRowItemAdapter(listOfItems, this);
        //handle ListView and assign adapter
        NonScrollableListView itemListView = (NonScrollableListView) findViewById(R.id.itemsList);
        itemListView.setAdapter(rowAdapter);


        // Create an ArrayAdapter using the string array and a default spinner layout
        //ArrayAdapter<CharSequence> restaurantAreaAdapter = ArrayAdapter.createFromResource(this, R.array.optionsRestaurantArea, android.R.layout.simple_spinner_dropdown_item);

        /*
        Button placeOrder = (Button) findViewById(R.id.placeOrderButton);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectedTable = tableSizeSpinner.getSelectedItem().toString();
                Log.d(LOG_TAG,"The selected table is--> " + selectedTable);

                if(openTicket){
                    SendItems si = new SendItems();
                    si.execute();
                }
                else{
                    String selectedTableNumber = tableMap.get(Integer.parseInt(tableSizeSpinner.getSelectedItem().toString())).getTableID();
                    String selectedRevCenter = revenueCenter_map.get(restaurantAreaSpinner.getSelectedItem().toString());
                    OpenTicket ot = new OpenTicket();
                    ot.execute(selectedTableNumber,selectedRevCenter);
                    SendItems si = new SendItems();
                    si.execute();

                }
            }
        });
        */

    }

    /* This method executes the Send items async task to add items to the open ticket.
       if the ticket is not open due to tables being unavailable the appropriate message is displayed.
       otherwise the open ticket async task is called to open a ticket with omnivore.
    */
    public void placeOrder(View view){
        Log.i(LOG_TAG,"place order");

        String selectedTable = tableSizeSpinner.getSelectedItem().toString();
        Log.d(LOG_TAG,"The selected table is--> " + selectedTable);

        if(openTicket){
            SendItems si = new SendItems();
            si.execute();
        }
        else{
            if(tableSizeSpinner.getSelectedItem().toString().equalsIgnoreCase("No Table Available")){
                Toast.makeText(this,"Tables unavailable",Toast.LENGTH_LONG).show();
                return;
            }
            else{
                String selectedTableNumber = tableMap.get(Integer.parseInt(tableSizeSpinner.getSelectedItem().toString())).getTableID();
                String selectedRevCenter = revenueCenter_map.get(restaurantAreaSpinner.getSelectedItem().toString());
                OpenTicket ot = new OpenTicket();
                ot.execute(selectedTableNumber,selectedRevCenter);
            }
            //String selectedTableNumber = tableMap.get(Integer.parseInt(tableSizeSpinner.getSelectedItem().toString())).getTableID();



        }


        //Take this out to stop the Place ORder crashing error
        /*Intent intent = new Intent(this, MainReviewActivity.class);
        intent.putExtra("instructions", ((EditText) findViewById(R.id.specialInstructions)).getText().toString());
        intent.putExtra("table", ((EditText) findViewById(R.id.tableNumber)).getText().toString());
        Spinner area = (Spinner) findViewById(R.id.restaurantArea);
        intent.putExtra("area", area.getItemAtPosition(area.getSelectedItemPosition()).toString());
        intent.putExtra("ticketID", ticketID);

        this.startActivity(intent);*/
    }

}