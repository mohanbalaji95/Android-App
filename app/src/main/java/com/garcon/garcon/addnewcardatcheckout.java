package com.garcon.garcon;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class addnewcardatcheckout extends Service {

    public addnewcardatcheckout() {
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
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {

                    //passing the URLs to AsyncTask.
                    new DoBackgroundTask().execute();
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
    class DoBackgroundTask extends AsyncTask<String, Integer, Long> {
        int execute = 0;
        URL url;

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Constants.FIREBASE_URL_USERS);
        protected Long doInBackground(String... urls) {
            int count = 5;

            //for loop to download the array of URLs.

            try {
                System.out.println("in start service");
                System.out.println(mAuth.getCurrentUser().getUid());
                ref.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                // Get user value

                                // [START_EXCLUDE]
                                if (addcardatcheckout.card == null) {
                                    // User is null, error out
//                                    Log.e(TAG, "User " + userId + " is unexpectedly null");
//                                   Toast.makeText(NewPostActivity.this,
//                                            "Error: could not fetch user.",
//                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // Write new post

                                    writeNewCard(addcardatcheckout.card);
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
        protected void onPostExecute(Long result) {
            Toast.makeText(getBaseContext(), "addnewcard done!", Toast.LENGTH_LONG).show();
            stopSelf();
        }
        protected void writeNewCard(cardDetails card) {
            // Create new post at /user-posts/$userid/$postid and at
            // /posts/$postid simultaneousy

            Map<String, Object> postValues = card.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(mAuth.getCurrentUser().getUid()+"/cardDetails/" + card.getCardID(), postValues);
            ref.updateChildren(childUpdates);
        }
    }
}


