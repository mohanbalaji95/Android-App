package com.garcon.garcon;
import com.garcon.Constants.Constants;
import com.garcon.garcon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by raja on 7/23/2016.
 */
public class MainCheckoutActivity extends Activity {
    private final static String LOG_TAG = MainReviewActivity.class.getSimpleName();
    PayAppCardDetails cardvalue = new PayAppCardDetails();
    static Boolean done = false;
    static ArrayList<String> cards = new ArrayList<String>();
    boolean iscardselected = false;
    String addthiscard;
    static JSONObject item1 = new JSONObject();
    static JSONObject item2 = new JSONObject();
    public static final String MyPREFERENCES = "GarconPref";
    public static final String usercardno = "usercardnumberkey";
    public static final String[] CardNumber = {"1key", "2key", "3key", "4key", "5key", "6key", "7key", "8key", "9key", "10key"};
    public static final String[] CardHolder = {"1holderkey", "2holderkey", "3holderkey", "4holderkey", "5holderkey", "6holderkey", "7holderkey", "8holderkey", "9holderkey", "10holderkey"};
    public static final String tempCardexpm = "tempexpmkey";
    public static final String tempCardexpy = "tempexpykey";
    public static final String tempCardno = "tempcardnokey";
    public static final String tempCardcvv = "tempcardcvvkey";


    SharedPreferences sharedpreferences;
    private RelativeLayout payment_information;
    ArrayList<Integer> values;
    String ticketID=null;
    String locationID="8cg4k4kc";
    String ApiKey=null;
    private DatabaseReference mDatabase;
    Button processPayment;
    Sendpayment send = new Sendpayment();
    static String selectedcard = null;

    cardSelected cardselected = new cardSelected();

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_main_checkout);
        values = getIntent().getIntegerArrayListExtra("values");
        ticketID = getIntent().getStringExtra("ticketID");
        System.out.println("ticketID = " + ticketID);
        for(int i = 0; i < values.size(); i++)
            Log.i("information ",i+" "+values.get(i));

        TextView subtotal = (TextView) findViewById(R.id.subtotal_final);
        subtotal.setText(String.valueOf(values.get(0)));

        TextView tip = (TextView) findViewById(R.id.tip_final);
        tip.setText(String.valueOf(values.get(1)));

        TextView total = (TextView) findViewById(R.id.total_final);
        total.setText(String.valueOf(values.get(2)));

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Api-Key").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        setKey(dataSnapshot.getValue().toString());
                        Log.d(LOG_TAG,"The Key is--> " + getKey());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(LOG_TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
        processPayment = (Button) findViewById(R.id.process);
        processPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedcard==null) {
                    AlertDialog.Builder alertDialog;
                    alertDialog = new AlertDialog.Builder(MainCheckoutActivity.this);
                    alertDialog.setMessage("PLease select a card to process payment");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                    alertDialog.show();
                }
                else {
                    send.execute();
                    Intent intent = new Intent(getApplicationContext(), ReceiptActivity.class);
                    intent.putIntegerArrayListExtra("values", values);
                    intent.putExtra("billingCard", selectedcard);
                    startActivity(intent);
                    finish();
                }


            }
        });


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        BaseAdapter cardAdapter = new OptionsAdapter(cards, this);
        NonScrollableListView cardsLV = (NonScrollableListView) findViewById(R.id.credit_cards);
        cardsLV.setAdapter(cardAdapter);
        payment_information = (RelativeLayout) findViewById(R.id.payment_information);
        payment_information.setVisibility(View.GONE);

        toggle_contents(payment_information);
        RadioGroup choosePayment = (RadioGroup) findViewById(R.id.paymentOption);
        choosePayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.payStore && payment_information.isShown()) {
                    toggle_contents(payment_information);
                } else if (checkedId == R.id.payApp) {
                    toggle_contents(payment_information);
                }
            }
        });


        findViewById(R.id.addCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int usercardnumber = sharedpreferences.getInt(usercardno, 0);
                System.out.println("in mainreview activity initially " + usercardnumber);
                if (usercardnumber == 10) {
                    AlertDialog.Builder alertDialog;
                    alertDialog = new AlertDialog.Builder(MainCheckoutActivity.this);
                    alertDialog.setTitle("Cannot add a new card");
                    alertDialog.setMessage("User can add upto a maximum of 10 cards. Please delete a card in Profile Settings to add another");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                    alertDialog.show();
                } else {

                    Intent addcardintent = new Intent(getApplicationContext(), addcardatcheckout.class);
                    addcardintent.putIntegerArrayListExtra("values", values);
                    addcardintent.putExtra("ticketID", ticketID);
                    startActivity(addcardintent);
//                    Intent i = getIntent();
//                    addthiscard = i.getExtras().getString("MainDate");
//                    System.out.println("maindate" + addthiscard);
//                    cards.add(addthiscard);

//                    cards.add(sharedpreferences.getString(CardAtCheckoutType, null)+"\n \n"+sharedpreferences.getString(CardNumber[usercardnumber], null)+"\n \n"+sharedpreferences.getString(CardHolder[usercardnumber], null));

                }
            }

        });
    }
    public void toggle_contents(View view) {
        //no effects
        /*
        options.setVisibility(optionsListView.isShown()
                ? View.GONE
                : View.VISIBLE);
        */
        if (payment_information.isShown()) {
            UtilityFX.slide_up(this, payment_information);
            payment_information.setVisibility(View.GONE);
        } else {
            payment_information.setVisibility(View.VISIBLE);
            UtilityFX.slide_down(this, payment_information);
        }

    }
    public void cardSelected(View view) {
        //TODO integrate with selecting an existing card
        ColorDrawable buttonColor = (ColorDrawable) view.getBackground();
        int colorId = buttonColor.getColor();
        if (colorId == (getResources().getColor(R.color.garcon))) {
            view.setBackgroundColor(getResources().getColor(R.color.white));
            iscardselected = false;
        }
        if (!iscardselected) {
            Log.i(LOG_TAG, ((TextView) view).getText() + "card selected");

            if (colorId == (getResources().getColor(R.color.white))) {
                view.setBackgroundColor(getResources().getColor(R.color.garcon));
                selectedcard = ((TextView) view).getText().toString();
//                if (MainCheckoutActivity.selectedcard.equals(null)) {
//                    AlertDialog.Builder alertDialog;
//                    alertDialog = new AlertDialog.Builder(MainCheckoutActivity.this);
//                    alertDialog.setMessage("PLease select a card to process payment");
//                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.dismiss();
//                        }
//                    });
//                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
//                    alertDialog.show();
//                } else {
                Intent cardselected = new Intent(getApplicationContext(), cardSelected.class);
                startService(cardselected);
                iscardselected = true;
            } else {
                view.setBackgroundColor(getResources().getColor(R.color.white));
                selectedcard = null;
            }


        }

    }
    public void setKey(String key){
        ApiKey = key;
    }

    public String getKey(){
        return ApiKey;
    }

    class Sendpayment extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... params) {
            try{
                System.out.println("expmonthout" + sharedpreferences.getString(tempCardexpm, null));
                item2.put("exp_month", Integer.parseInt(sharedpreferences.getString(tempCardexpm, null)));
                item2.put("exp_year", 2000+Integer.parseInt(sharedpreferences.getString(tempCardexpy, null)));
                item2.put("cvc2",sharedpreferences.getString(tempCardcvv, null));
                item2.put("number",sharedpreferences.getString(tempCardno, null));
                item1.put("type","card_not_present");
                item1.put("amount",String.valueOf(values.get(0)));
                item1.put("tip",String.valueOf(values.get(1)));
                item1.put("card_info",item2);
//                JSONArray modifierArray = new JSONArray();
//                itemArray.put(item1);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(tempCardexpm, null);
                editor.putString(tempCardexpy, null);
                editor.putString(tempCardcvv, null);
                editor.putString(tempCardno, null);

                editor.apply();

                String newMenuItemString = String.format(Constants.OMNIVORE_API_BASE_URL+"%s/tickets/%s/payments/",locationID,ticketID);

                URL newURL = new URL(newMenuItemString);
                HttpURLConnection connection = (HttpURLConnection)newURL.openConnection();
                connection.setRequestMethod("POST");
                connection.addRequestProperty("Api-Key",ApiKey);
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(item1.toString());
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
                if(connection.getResponseCode()!=200){
                    return null;
                }
                Log.d(LOG_TAG,"json object to string "+data.toString());
                return data;


            }
            catch (Exception e){
                Log.e(LOG_TAG,e.getMessage());
            }
            return null;
        }
    }


}