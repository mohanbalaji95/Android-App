package com.garcon.garcon;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.garcon.Constants.Constants;
import com.garcon.Models.User;
import com.garcon.Models.cardDetails;
import com.garcon.garcon.ProfileSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class removecard extends Service {

    public removecard() {
    }
    private static String TAG = "MyService";
//    private Handler handler;
//    private Runnable runnable;
//    private final int runTime = 5000;

//    @Override
//    public void onCreate() {
//        super.onCreate();
//        Log.i(TAG, "onCreate");
//
////        handler = new Handler();
////        runnable = new Runnable() {
////            @Override
////            public void run() {
////
////                handler.postDelayed(runnable, runTime);
////            }
////        };
////        handler.post(runnable);
//    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    Object objUID = intent.getExtras().get("cardUID");
                    String[] cardUID = new String[1];
                    cardUID[0] = (String) objUID;
                    //passing the URLs to AsyncTask.
                    new DoBackgroundTask().execute(cardUID);
                } catch (Exception e) {
                }
            }
        };
        t.start();
        stopSelf();
        return Service.START_STICKY;
    }

//    @SuppressWarnings("deprecation")
//    @Override
//    public void onStart(Intent intent, int startId) {
//        super.onStart(intent, startId);
//        Log.i(TAG, "onstart");
//
//
//    }

    //AsyncTask class.
    class DoBackgroundTask extends AsyncTask<String, Void, Boolean> {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Constants.FIREBASE_URL_USERS);
        protected Boolean doInBackground(String... cardUID) {
        final String cardID = cardUID[0];
            //for loop to download the array of URLs.

            try {
                System.out.println("in start service");
                System.out.println("cardIDinbackground"+cardID);
                System.out.println(mAuth.getCurrentUser().getUid());
                ref.child(mAuth.getCurrentUser().getUid()).child("cardDetails").child(cardID).addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                System.out.println("datasnapshot editprofile");
                                cardDetails card = dataSnapshot.getValue(cardDetails.class);
                                if (card == null) {
                                    System.out.println("user is null");

                                    // User is null, error out
//                                    Log.e(TAG, "User " + userId + " is unexpectedly null");
//                                   Toast.makeText(NewPostActivity.this,
//                                            "Error: could not fetch user.",
//                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    System.out.println("I'm not null");
                                    card.setCardNumber(null);
                                    card.setCardHolder(null);
                                    card.setCardID(null);
                                    card.setExpiry(null);
                                    card.setcvv(null);
                                    card.setCardType(null);
//
//                                    // Write new post
                                    removeCard(card, cardID);
                                }

                                // Finish this Activity, back to the stream
                                // [END_EXCLUDE]
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
////                                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                            }
                        });
            } catch (Exception e) {

            }
            stopSelf();
            return null;

        }

        // final toast that notifies completion of download.
        @Override
        protected void onPostExecute(Boolean result) {
            Toast.makeText(getBaseContext(), "removecard done!", Toast.LENGTH_LONG).show();
            stopSelf();
        }
        protected void removeCard(cardDetails card, String cardID) {
            // Create new post at /user-posts/$userid/$postid and at
            // /posts/$postid simultaneously

            System.out.println("I'm in remove card"+cardID);

            System.out.println(ref.child(mAuth.getCurrentUser().getUid()).child("cardDetails").child(cardID));
            ref.child(mAuth.getCurrentUser().getUid()).child("cardDetails").child(cardID).setValue(card);


        }
    }
}


