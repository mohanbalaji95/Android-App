package com.garcon.garcon.Payment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;

import android.nfc.Tag;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import java.io.UnsupportedEncodingException;

import java.security.KeyFactory;


import java.security.PublicKey;
import java.security.Security;

import javax.crypto.Cipher;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.garcon.Constants.Constants;
import com.garcon.garcon.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.garcon.garcon.R.id.url;

/**
 * Created by kusumasri on 7/14/17.
 * Modified by Akshay on 7/28/17.
 */

public class OnPayment extends AppCompatActivity{

    EditText et_cardholderName,et_cardNumber,et_expirymonth,et_expiryyear,et_cvv;
    Totalobject totalobj;
    String cardHName,expirymonth,expiryyear,cardNumber,cvv;
    String encode;
    Context context;
    byte[] plainText;
    String ticketNumber;
    String firebaseKey;
    String ccLast4Digits;
    String amountPaid;

    private DatabaseReference mDatabase;

    public static final String TAG="Payment";

    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    String public_key= "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtnqQa7uxS3Cmo8VDPYrf\n" +
            "BkVj0d/dTHPdI2LBeIqw6Bz+qw4u9BzSZwUDHQ78hVhkLIKUkmrjiirY9G4Gvn6+\n" +
            "/E8hSMBC1NEz5uTYQwAjzXCLXOkV9UzfWKSt5OccBq/Iyas9tM/xGBEnThwTnyv9\n" +
            "B+n6P+kuCo5uUXBwOLJcRXyZ/u7fFMko6f1U6DjR+XOnfStgPcksI+6rmnQooi87\n" +
            "ncSckYH1lU5HFoShPzJBhODCsUM8hL2q4mEDUwGpTHJswZxcRigt2VqabxssEq4E\n" +
            "XIU/46aeLmi5ME7O1NtkkoDBDX9OsOZI8TkFGFNSz5SvZRgl2Q9EZPOaAMuJLv9V\n" +
            "sQIDAQAB\n" +
            "-----END PUBLIC KEY-----";

    private void stripHeaders(){

        public_key = public_key.replace("-----BEGIN PUBLIC KEY-----", "");
        public_key = public_key.replace("-----END PUBLIC KEY-----", "");

    }

    public byte[] encryptWithPublicKey(String encrypt) throws Exception {
        byte[] message = encrypt.getBytes("UTF-8");
        stripHeaders();
        PublicKey apiPublicKey= getRSAPublicKeyFromString();
        Cipher rsaCipher = Cipher.getInstance("RSA/None/PKCS1Padding", "SC");
        rsaCipher.init(Cipher.ENCRYPT_MODE, apiPublicKey);
        return rsaCipher.doFinal(message);
    }

    private PublicKey getRSAPublicKeyFromString() throws Exception{
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "SC");
        byte[] publicKeyBytes = Base64.decode(public_key.getBytes("UTF-8"), Base64.DEFAULT);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(x509KeySpec);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addcardatcheckout);
        et_cardholderName=(EditText)findViewById(R.id.addnameoncardatcheckout);
        et_cardNumber=(EditText)findViewById(R.id.cardnumberatcheckout);
        et_expirymonth=(EditText)findViewById(R.id.addexpirymonthatcheckout);
        et_expiryyear=(EditText)findViewById(R.id.addexpiryyearatcheckout);
        et_cvv=(EditText)findViewById(R.id.addcvvatcheckout);
        Intent intent = getIntent();
        totalobj=(Totalobject)intent.getExtras().getSerializable("totalobject");
        ticketNumber = intent.getExtras().getString("ticketnumber");
        firebaseKey = intent.getExtras().getString("firebasekey");
        context=getApplicationContext();


        /*Comment the below line to set card details,
            using it so that i do not have to enter card again and again
         */
        String omnivorecardnumber="5454545454545454";
        String omnivorecvv="123";
        int omnivoremonth=6;
        int omnivoreyear=2019;
        et_cardNumber.setText(omnivorecardnumber);
        et_expirymonth.setText(String.valueOf(omnivoremonth));
        et_expiryyear.setText(String.valueOf(omnivoreyear));
        et_cvv.setText(String.valueOf(omnivorecvv));

        mDatabase = FirebaseDatabase.getInstance().getReference().child("orders").child(firebaseKey);

    }

    //getting the information from card and trying to modify into json and encrypt.

    public void onclickpay(View view)
    {
        //Need to encrypt and send it to omnivore..
        cardHName=et_cardholderName.getText().toString();
        cardNumber=et_cardNumber.getText().toString();
        expirymonth=et_expirymonth.getText().toString();
        expiryyear=et_expiryyear.getText().toString();
        cvv=et_cvv.getText().toString();

        //Storing dummy data in card
        /*String omnivorecardnumber="5454545454545454";
        String omnivorecvv="123";
        int omnivoremonth=6;
        int omnivoreyear=2019;*/
        String omnivorecardnumber=cardNumber;
        String omnivorecvv=cvv;
        int omnivoremonth=Integer.parseInt(expirymonth);
        int omnivoreyear=Integer.parseInt(expiryyear);

        ccLast4Digits = omnivorecardnumber.substring(omnivorecardnumber.length()-4,omnivorecardnumber.length());

                //Adding a progress bar






        //Uncomment it while using the data from UI .
      /*  String[] date= expiry.split("-");

        cardInfo cardinfo=new cardInfo(cardNumber,cvv,Integer.parseInt(date[0]),Integer.parseInt(date[1]));*/
        cardInfo cardinfo=new cardInfo(omnivorecardnumber,omnivorecvv,omnivoremonth,omnivoreyear);
        Gson gson = new Gson();
        String jsonstr= gson.toJson(cardinfo);
        /*try {

            plainText = "jsonstr".getBytes("UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/

        //Try to get public key from assert manager instead of using string..
        AssetManager am = context.getAssets();

        List<String> records = new ArrayList<String>();
        try {


            byte[] result=encryptWithPublicKey(jsonstr);
            System.out.print(result);
            encode=Base64.encodeToString(result,Base64.DEFAULT);
            System.out.println(encode);

        }
        catch(Exception ex)
        {
            System.out.println(ex.getStackTrace());
        }


        PostDataOmnivore(encode,totalobj);

    }


    //Sending post request to omnivore for paymentand getting reponse.
    public void PostDataOmnivore(String encryptiondata,Totalobject totalobj) {



        JsonObject payload = new JsonObject();
        payload.addProperty("type", "card_not_present");

        JsonObject cardinfo = new JsonObject();
        cardinfo.addProperty("encrypted_data", encryptiondata);
        cardinfo.addProperty("encryption_key_id", Constants.Encryptionkey);

        payload.add("card_info",cardinfo);
        payload.addProperty("amount",totalobj.getDue());
        Log.d(TAG,"Amount due--> "+totalobj.getDue());
        payload.addProperty("tip",0);
        amountPaid = String.valueOf(totalobj.getDue());


        //Uncomment totalobj.get total and totalobj.getTax when you are testing with ticket data and amount >50 and tip>0
       /* payload.addProperty("amount",totalobj.getTotal());

        payload.addProperty("tip",totalobj.getTax());*/

        System.out.println(payload);

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            //for v1.0
            //String url = "https://api.omnivore.io/1.0/locations/AieMdB5i/tickets/"+ticketNumber+"/payments";
            //for v0.1
            String url = "https://api.omnivore.io/0.1/locations/AieMdB5i/tickets/"+ticketNumber+"/payments";
            final String requestbody=payload.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    Log.i("LOG_VOLLEY", response);
                    if(response.equals("200")){
                        Toast.makeText(OnPayment.this, "Successfully Paid", Toast.LENGTH_LONG).show();
                        SharedPreferences sp = getSharedPreferences("garconpref",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("ticketnumber", null);
                        editor.putString("firebasekey", null);
                        editor.commit();
                        mDatabase.child("totalAmount").setValue(amountPaid);
                        mDatabase.child("ccLast4Digits").setValue(ccLast4Digits);

                        finish();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

                    Log.e("LOG_VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestbody == null ? null : requestbody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestbody, "utf-8");
                        return null;
                    }
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> headers = new HashMap<>();
                    headers.put("Api-Key", "590c27de8c41473b8014af7553bb85c4");
                    headers.put("Content-Type", "application/json");

                    return headers;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {

                        responseString = String.valueOf(response.statusCode);

                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);

        }catch (Exception e)
        {

            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            Log.d(TAG+"error",e.toString());
        }


    }


}











