package com.garcon.garcon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.garcon.Constants.Constants;
import com.garcon.Models.Table;
import com.garcon.Models.Ticket;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {
    private static final String TAG = CheckoutActivity.class.getName();
    private DatabaseReference mDatabase;
    SharedPreferences sharedpreferences;

    String ApiKey=null;
    String locationID;
    String ticketID;
    Ticket ticket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        Bundle extras = intent.getExtras();
        locationID = extras.getString("locationID");
        ticketID = extras.getString("ticketnumber");

        setContentView(R.layout.activity_checkout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button btn_openTab = (Button) findViewById(R.id.open_tab_btn);
        btn_openTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Api-Key").addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                setKey(dataSnapshot.getValue().toString());
                                Log.d(TAG,"The Key is--> " + getKey());
                                OpenTicket ot = new OpenTicket();
                                ot.execute((Void) null);



                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                            }
                        });

            }
        });

        Button btn_submitOrder = (Button) findViewById(R.id.submit_order);
        btn_submitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child("Api-Key").addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                setKey(dataSnapshot.getValue().toString());
                                Log.d(TAG,"The Key is--> " + getKey());
                                SendItems so = new SendItems();
                                so.execute((Void) null);



                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                            }
                        });

            }
        });



    }
    public void setKey(String key){
        ApiKey = key;
    }
    public String getKey(){
        return ApiKey;
    }
    class OpenTicket extends AsyncTask<Void, Void, JSONObject> {



        @Override
        protected JSONObject doInBackground(Void... params) {
            try {

                JSONObject createTicket = new JSONObject();
                createTicket.put("employee", "MjikgioG");
                createTicket.put("order_type", "KxiAaip5");
                createTicket.put("revenue_center", "LdiqGibo");
                createTicket.put("table", "jLiyniEb");
                createTicket.put("guest_count",2);
                createTicket.put("name", "New Ticket");
                createTicket.put("auto_send", true);

                Log.d(TAG,"New Ticket JSON-->"+createTicket.toString());

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
                Log.d(TAG,"json to string "+json.toString());
                SharedPreferences sp = getApplicationContext().getSharedPreferences(getString(R.string.shared_pref_file_name),Context.MODE_PRIVATE);
                SharedPreferences.Editor spEditor = sp.edit();
                spEditor.putString("ticketString",json.toString());
                spEditor.commit();
                Log.d(TAG,"json in sharedpref "+sp.getString("ticketString",null));
                JSONObject data = new JSONObject(json.toString());
                Log.d(TAG,"The ticket ID is -->  "+data.getString("id"));
                ticketID = data.getString("id");
                if(connection.getResponseCode()!=201){
                    return null;
                }
                else{
                    Log.d(TAG,"Adding data to FireBase");
                    sharedpreferences = getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);
                    String userUUID = sharedpreferences.getString(getString(R.string.User_UUID_Key),null);
                    ticket = new Ticket(ticketID,"MjikgioG","KxiAaip5","LdiqGibo","jLiyniEb",2,"FirebaseTicket",true,userUUID,locationID);
                    ticket.setOpen(data.getBoolean("open"));
                    ticket.setOpened_at(data.getLong("opened_at"));
                    Map<String,Object> ticketValues = ticket.toMap();
                    String key = mDatabase.child("orders").push().getKey();
                    Log.d(TAG,"The key is --> "+key);
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/orders/" + key, ticketValues);
                    Log.d(TAG,"The updateChildren Object is --> "+childUpdates.toString());
                    mDatabase.updateChildren(childUpdates);

                }
                Log.d(TAG,"json object to string "+data.toString());
                return data;
            }catch(Exception e){
                //return null;
                System.out.println(TAG+"Error:"+e.getMessage());
                //Log.e(TAG,);
            }
            return null;
        }
    }

    class SendItems extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... params) {

            try{
                JSONArray itemArray = new JSONArray();
                JSONObject item1 = new JSONObject();
                item1.put("menu_item","gki84ia9");
                item1.put("quantity",2);
                item1.put("price_level","g4T4dTBj");
                item1.put("comment","Bring it on!");
                JSONArray modifierArray = new JSONArray();
                item1.put("modifiers",modifierArray);
                itemArray.put(item1);

                JSONObject item2 = new JSONObject();
                item2.put("menu_item","doTaLTyg");
                item2.put("quantity",3);
                item2.put("price_level","L4iqKid8");
                item2.put("comment","Bring it on!");
                JSONArray modifierArray2 = new JSONArray();
                item2.put("modifiers",modifierArray2);
                itemArray.put(item2);

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
                Log.d(TAG,"json to string "+json.toString());
                JSONObject data = new JSONObject(json.toString());
                Log.d(TAG,"The ticket ID is -->  "+data.getString("id"));
                if(connection.getResponseCode()!=200){
                    return null;
                }
                Log.d(TAG,"json object to string "+data.toString());
                return data;


            }
            catch (Exception e){
                Log.e(TAG,e.getMessage());
            }
            return null;
        }
    }


}
