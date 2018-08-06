package com.garcon.garcon.Payment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import com.garcon.garcon.R;

import org.json.JSONObject;

import java.util.ArrayList;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by kusumasri on 7/6/17.
 * Modified by Akshay on 7/28/17.
 */

public class CheckoutandPay extends AppCompatActivity {

    Button mPayButton, fifPercent, eighttPercent, twentyPercent, twentyfPercent, customPercent, addTipB;
    public final String TAG = "CheckoutandPay";
    TextView mSubTotal, mTax, mTotal, mDiscount, mDue, addTipT;
    ArrayList<OmnivoreOrderItems> mOrderItems;
    private RecyclerView mRecyclerView;
    public Totalobject totalobj = new Totalobject();
    public String ticketNumber = null;
    public String firebaseKey = null;
    RelativeLayout mLayoutTax, mLayoutSubTotal, mLayoutTotal, mLayoutDiscount, mLayoutDue, mLayoutServiceCharge, mLayoutTip;
    static TextView mTip;
    int gratuity;
    TextView mServiceCharge;
    String gratuityN;
    int gratuityI;
    int tipI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_checkout);

        //Order object needs to be used to get the data from omnivore with ticketid
        //getting details from omnivore using volley..

        //Getting references to UI elements
        mSubTotal = (TextView) findViewById(R.id.subtotal_final);
        mTax = (TextView) findViewById(R.id.tax_final);
        mTotal = (TextView) findViewById(R.id.total_final);
        //mDue = (TextView) findViewById(R.id.due_final); Due was being used as the total in activity_main_Checkout.xml; the total is not the total
        mTip = (TextView) findViewById(R.id.tip_final);
        mServiceCharge = (TextView) findViewById(R.id.service_charge_final);
        addTipT = (TextView) findViewById(R.id.addTipT);
        addTipB = (Button) findViewById(R.id.addTipB);
        mRecyclerView = (RecyclerView) findViewById(R.id.orderlist);
        mLayoutTax = (RelativeLayout) findViewById(R.id.tax);
        //mLayoutDue = (RelativeLayout) findViewById(R.id.due); renamed to total
        mLayoutTotal = (RelativeLayout) findViewById(R.id.total);
        mLayoutSubTotal = (RelativeLayout) findViewById(R.id.subtotal);
        mLayoutServiceCharge = (RelativeLayout) findViewById(R.id.service_charge);
        mLayoutTip = (RelativeLayout) findViewById(R.id.tip);
        mOrderItems = new ArrayList<>();


        if (getIntent().getExtras() != null) {
            ticketNumber = getIntent().getExtras().getString("ticketID");
            firebaseKey = getIntent().getExtras().getString("firebasekey");
            Log.d(TAG, "Ticket NUmber from intent-->" + ticketNumber);
        }
        if (ticketNumber == null) {
            SharedPreferences sharedPref = getSharedPreferences("garconpref", MODE_PRIVATE);
            ticketNumber = sharedPref.getString("ticketnumber", null);
            firebaseKey = sharedPref.getString("firebasekey", null);
        }
        Log.d(TAG, "Ticket NUmber is-->" + ticketNumber);
        if (ticketNumber != null) {
            //fetchTicketTotals(); // for v1.0 API
            fetchTicketTotalsOldVersion(); // for 0.1 API
        } else {
            mLayoutTax.setVisibility(View.GONE);
            mLayoutDue.setVisibility(View.GONE);
            mLayoutTotal.setVisibility(View.GONE);
            mLayoutSubTotal.setVisibility(View.GONE);

            //display dialog if there are no pending orders.
            AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutandPay.this);
            builder.setMessage("There are no pending order to pay.")
                    .setTitle("Payments").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }



        mPayButton = (Button) findViewById(R.id.process);


        mPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processPayment();
            }
        });


        fifPercent = (Button) findViewById(R.id.fifPercent);


        fifPercent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String finalTip = addTip.tipCalc(view, 0.15, mSubTotal);
                tipI = Integer.parseInt(finalTip);
                mTip.setText(finalTip);
                totalobj.setTip(tipI);
                mTotal.setText(String.valueOf(totalobj.getTotal()));
            }
        });


        eighttPercent = (Button) findViewById(R.id.eighttPercent);


        eighttPercent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String finalTip = addTip.tipCalc(view, 0.18, mSubTotal);
                tipI = Integer.parseInt(finalTip);
                mTip.setText(finalTip);
                totalobj.setTip(tipI);
                mTotal.setText(String.valueOf(convertSubtotal() + convertTax() + convertTip() + convertServiceCharge()));
            }
        });


        twentyPercent = (Button) findViewById(R.id.twentyPercent);


        twentyPercent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String finalTip = addTip.tipCalc(view, 0.2, mSubTotal);
                tipI = Integer.parseInt(finalTip);
                mTip.setText(finalTip);
                totalobj.setTip(tipI);
                mTotal.setText(String.valueOf(totalobj.getTotal()));
            }
        });


        twentyfPercent = (Button) findViewById(R.id.twentyfPercent);


        twentyfPercent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String finalTip = addTip.tipCalc(view, 0.25, mSubTotal);
                tipI = Integer.parseInt(finalTip);
                mTip.setText(finalTip);
                totalobj.setTip(tipI);
                mTotal.setText(String.valueOf(totalobj.getTotal()));
            }
        });


        customPercent = (Button) findViewById(R.id.customPercent);


        customPercent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                customTip Customtip = new customTip();
                fragmentTransaction.add(R.id.mainCheckout, Customtip);
                fragmentTransaction.commit();

            }
        });
    }

    private void processPayment() {
        if (mTip != null){
            totalobj.setTip(Integer.parseInt(mTip.getText().toString()));
        }
        totalobj.setTotal(totalobj.getTotal());
        Intent intent = new Intent(this, OnPayment.class);
        //fetchTicketTotals();
        intent.putExtra("totalobject", totalobj);
        intent.putExtra("ticketnumber", ticketNumber);
        intent.putExtra("firebasekey", firebaseKey);
        startActivity(intent);
        finish();


    }

    protected int serviceChargeCalc() {

        gratuity = (int) (Integer.parseInt(mSubTotal.getText().toString()) * .01);
        gratuityN = String.valueOf(gratuity);
        mServiceCharge.setText(gratuityN);
        gratuityI = Integer.parseInt(gratuityN);
        return gratuityI;
    }

    public int convertSubtotal() {
        int subtotal = Integer.parseInt(mSubTotal.getText().toString());
        return subtotal;
    }

    public int convertTax() {
        int tax = Integer.parseInt(mTax.getText().toString());
        return tax;
    }

    public int convertTip() {
        int tip = Integer.parseInt(mTip.getText().toString());
        return tip;
    }

    public int convertServiceCharge() {
        int serviceCharge = Integer.parseInt(mServiceCharge.getText().toString());
        return serviceCharge;
    }


    private void getOrderItems() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://api.omnivore.io/1.0/locations/AieMdB5i/tickets/" + ticketNumber + "/items";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //mTxtDisplay.setText("Response: " + response.toString());
                        //Log.d(TAG,response.toString());
                        try {
                            JSONArray itemsArray = response.getJSONObject("_embedded").getJSONArray("items");
                            //Log.d(TAG,itemsArray.get(0).toString());
                            for (int nItems = 0; nItems < itemsArray.length(); nItems++) {
                                JSONObject itemDetails = itemsArray.getJSONObject(nItems).getJSONObject("_embedded").getJSONObject("menu_item");
                                int itemID = itemDetails.getInt("id");
                                String itemName = itemDetails.getString("name");
                                int itemPrice = itemDetails.getInt("price_per_unit");
                                Log.d(TAG, "Item id->" + itemID);
                                Log.d(TAG, "Item name->" + itemName);
                                Log.d(TAG, "Item price->" + itemPrice);

                                OmnivoreOrderItems omnivoreItem = new OmnivoreOrderItems(itemName, itemPrice, 1);
                                mOrderItems.add(omnivoreItem);
                            }


                        } catch (Exception e) {
                            Log.d(TAG, "Error:" + e.toString());
                        }

                        setDataInView();


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // something to do here ??

                Map<String, String> headers = new HashMap<>();
                headers.put("Api-Key", "2fc36f61e6dd442c9879ec7071f46635");
                headers.put("Content-Type", "application/json");

                return headers;
            }
        };
        requestQueue.add(jsObjRequest);

    }

    private void getOrderItemsOldVersion() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://api.omnivore.io/0.1/locations/AieMdB5i/tickets/" + ticketNumber + "/items";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //mTxtDisplay.setText("Response: " + response.toString());
                        //Log.d(TAG,response.toString());
                        try {
                            JSONArray itemsArray = response.getJSONObject("_embedded").getJSONArray("items");
                            //Log.d(TAG,itemsArray.get(0).toString());
                            for (int nItems = 0; nItems < itemsArray.length(); nItems++) {
                                JSONObject itemDetails = itemsArray.getJSONObject(nItems);
                                String itemID = itemDetails.getString("id");
                                String itemName = itemDetails.getString("name");
                                int itemPrice = itemDetails.getInt("price_per_unit");
                                int itemQuantity = itemDetails.getInt("quantity");
                                Log.d(TAG, "Item id->" + itemID);
                                Log.d(TAG, "Item name->" + itemName);
                                Log.d(TAG, "Item price->" + itemPrice);
                                Log.d(TAG, "Item quantity->" + itemQuantity);

                                OmnivoreOrderItems omnivoreItem = new OmnivoreOrderItems(itemName, itemPrice, itemQuantity);
                                mOrderItems.add(omnivoreItem);
                            }


                        } catch (Exception e) {
                            Log.d(TAG, "Error:" + e.toString());
                        }

                        setDataInView();


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // something to do here ??

                Map<String, String> headers = new HashMap<>();
                headers.put("Api-Key", "590c27de8c41473b8014af7553bb85c4");
                headers.put("Content-Type", "application/json");

                return headers;
            }
        };
        requestQueue.add(jsObjRequest);

    }

    public void setDataInView() {
        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // specify an adapter (see also next example)
        OrderedItemsAdapter mAdapter = new OrderedItemsAdapter(mOrderItems);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void fetchTicketTotals() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://api.omnivore.io/1.0/locations/AieMdB5i/tickets/" + ticketNumber + "/";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //mTxtDisplay.setText("Response: " + response.toString());
                        Log.d(TAG,response.toString());
                        try {
                            JSONObject totals = response.getJSONObject("totals");
                            //Log.d(TAG,itemsArray.get(0).toString());
                            Log.d(TAG, "Totals--> " + totals.toString());
                            mSubTotal.setText("$" + totals.getInt("sub_total"));
                            mTax.setText("$" + totals.getInt("tax"));
                            mTotal.setText("$" + totals.getInt("total"));
                            mDue.setText(String.valueOf(totals.getInt("due")));

                            totalobj.setSubtotal(totals.getInt("sub_total"));
                            totalobj.setTax(totals.getInt("tax"));
                            totalobj.setTotal(totals.getInt("total"));
                            totalobj.setDue(totals.getInt("due"));

                        } catch (Exception e) {
                            Log.d(TAG, "Error:" + e.toString());
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // something to do here ??

                Map<String, String> headers = new HashMap<>();
                headers.put("Api-Key", "2fc36f61e6dd442c9879ec7071f46635");
                headers.put("Content-Type", "application/json");

                return headers;
            }
        };
        requestQueue.add(jsObjRequest);

    }

    private void fetchTicketTotalsOldVersion() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://api.omnivore.io/0.1/locations/AieMdB5i/tickets/" + ticketNumber + "/";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //mTxtDisplay.setText("Response: " + response.toString());
                        //Log.d(TAG,response.toString());
                        try {
                            JSONObject totals = response.getJSONObject("totals");
                            //Log.d(TAG,itemsArray.get(0).toString());
                            Log.d(TAG, "Totals--> " + totals.toString());
                            mSubTotal.setText("" + totals.getInt("sub_total"));
                            mTax.setText("" + totals.getInt("tax"));
                            serviceChargeCalc();
                            mTotal.setText("" + totals.getInt("total"));
                            //mDue.setText(String.valueOf(totals.getInt("due")));          //due is now total


                            totalobj.setSubtotal(totals.getInt("sub_total"));
                            totalobj.setTax(totals.getInt("tax"));
                            totalobj.setTotal(totals.getInt("total"));
                            totalobj.setDue(totals.getInt("due"));
                            totalobj.setServiceCharge(gratuityI);

                            mTotal.setText(String.valueOf(totalobj.getTotal())); // adds service charge to total

                            if (totalobj.getDue() <= 0) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutandPay.this);
                                builder.setMessage("There are no pending order to pay.")
                                        .setTitle("Payments").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();

                            } else {
                                //getOrderItems(); //for v1.0 API
                                getOrderItemsOldVersion(); //for v0.1 API
                            }

                        } catch (Exception e) {
                            Log.d(TAG, "Error:" + e.toString());
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // something to do here ??

                Map<String, String> headers = new HashMap<>();
                headers.put("Api-Key", "590c27de8c41473b8014af7553bb85c4");
                headers.put("Content-Type", "application/json");

                return headers;
            }
        };
        requestQueue.add(jsObjRequest);

    }

    public ArrayList<OmnivoreOrderItems> getorder() {
        ArrayList<OmnivoreOrderItems> omnivoreresponse = new ArrayList<>();


        return omnivoreresponse;
    }

}
