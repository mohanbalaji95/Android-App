package com.garcon.garcon;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.garcon.Constants.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import us.monoid.web.Resty;

public class CheckoutActivity extends AppCompatActivity {
    private static final String TAG = CheckoutActivity.class.getName();
    private DatabaseReference mDatabase;
    String ApiKey=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();

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
                                SendOrder so = new SendOrder();
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
    class SendOrder extends AsyncTask<Void, Void, JSONObject> {



        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                JSONObject createTicket = new JSONObject();
                createTicket.put("employee", "MjikgioG");
                createTicket.put("order_type", "KxiAaip5");
                createTicket.put("revenue_center", "LdiqGibo");
                createTicket.put("table", "jLiyniEb");
                createTicket.put("guest_count", "jLiyniEb");
                createTicket.put("table", "jLiyniEb");
                createTicket.put("table", "jLiyniEb");



                URL newURL = new URL(Constants.OMNIVORE_API_BASE_URL);
                HttpURLConnection connection = (HttpURLConnection)newURL.openConnection();
                connection.setRequestMethod("POST");
                connection.addRequestProperty("Api-Key",ApiKey);
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuffer json = new StringBuffer(1024);
                String tmp="";
                while((tmp=reader.readLine())!=null)
                    json.append(tmp).append("\n");
                reader.close();
                Log.d(TAG,"json to string "+json.toString());
                JSONObject data = new JSONObject(json.toString());
                if(connection.getResponseCode()!=200){
                    return null;
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

}
