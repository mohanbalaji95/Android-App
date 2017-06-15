package com.garcon.garcon;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.garcon.Constants.Constants;
import com.garcon.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PayAppCardDetails extends Service {
    public PayAppCardDetails() {
    }

    private static String TAG = "MyService";
    DoeditBackgroundTask obj = new DoeditBackgroundTask();
//        private Handler handler;
//        private Runnable runnable;
//        private final int runTime = 5000;

    //        @Override
//        public void onCreate() {
//            super.onCreate();
//            Log.i(TAG, "onCreate");
//
////            handler = new Handler();
////            runnable = new Runnable() {
////                @Override
////                public void run() {
////
////                    handler.postDelayed(runnable, runTime);
////                }
////            };
////            handler.post(runnable);
//        }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");

        Thread t = new Thread() {
            @Override
            public void run() {
                try {

//                    Object[] objedits = (Object[]) intent.getExtras().get("edits");
//                    String[] edits = new String[objedits.length];
//                    for (int i = 0; i < objedits.length - 1; i++) {
//                        edits[i] = (String) objedits[i];
//                    }
                    //passing the URLs to AsyncTask.
                    obj.execute();
                } catch (Exception e) {
                }
            }
        };
        t.start();
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {


        return Service.START_NOT_STICKY;

    }

    //        @SuppressWarnings("deprecation")
//        @Override
//        public void onStart(Intent intent, int startId) {
//            super.onStart(intent, startId);
//            Log.i(TAG, "onstart");
//
//
//        }
    //AsyncTask class.
    class DoeditBackgroundTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            final FirebaseAuth mAuth = FirebaseAuth.getInstance();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL_USERS);
            try {
                ref.child(mAuth.getCurrentUser().getUid()).addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(!isCancelled()) {
                                    MainCheckoutActivity.cards.clear();
//                                        MainReviewActivity.cards.add("\nADD NEW CARD\n");

                                    System.out.println("datasnapshot editprofile");


                                    int i = 0;
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("cardDetails").getChildren()) {

                                        if (dataSnapshot1 == null) {
                                            System.out.println("I'm null no cards left");
                                        } else {
                                            System.out.println("datasnapshot editprofile");
//                                            System.out.println("datasnapshot editprofile = " + dataSnapshot1.child("cardID").getValue().toString());
                                            String SEED = mAuth.getCurrentUser().getUid();
//
                                            try {
                                                String decryptedcardno = encrypt.decrypt(SEED, dataSnapshot1.child("cardNumber").getValue().toString());
                                                System.out.println("decryptedcardno " + decryptedcardno);
                                                decryptedcardno = "*************" + decryptedcardno.substring(decryptedcardno.length() - 4);
                                                MainCheckoutActivity.cards.add(dataSnapshot1.child("cardType").getValue().toString()+"\n \n"+decryptedcardno+"\n \n"+dataSnapshot1.child("cardHolder").getValue().toString());

//                                System.out.println("encrypted value"+card.getCardNumber());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                            i = i + 1;
                                        }

                                    }
                                    obj.cancel(true);
                                    stopSelf();


                                    System.out.println("setting done true");

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {


                            }

                        });
            }
            catch(Exception e)
            {

            }
            return null;
        }
    }
}


