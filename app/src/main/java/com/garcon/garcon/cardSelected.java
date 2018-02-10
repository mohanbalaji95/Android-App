package com.garcon.garcon;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.garcon.Constants.Constants;
import com.garcon.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.javacc.jjtree.Main;
/**
 * Related to store and retrieve credit cards
 * No longer used.
 */
public class cardSelected extends Service {
    public cardSelected() {
    }

    private static String TAG = "MyService";
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
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

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
                    new DoinBackgroundTask().execute();
                } catch (Exception e) {
                }
            }
        };
        t.start();
        stopSelf();
        return Service.START_STICKY;

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
    class DoinBackgroundTask extends AsyncTask<Void, Void, String>
    {
        @Override
        protected String doInBackground(Void... params) {
            if(!isCancelled()) {

                final String card[] = MainCheckoutActivity.selectedcard.split("\\r?\\n");
                System.out.println("card[0] " + card[0]);
                System.out.println("card[1] " + card[1]);
                System.out.println("card[2] " + card[2]);
                System.out.println("card[3] " + card[3]);
                System.out.println("card[4] " + card[4]);
                MainCheckoutActivity.selectedcard = card[0]+","+card[1]+","+card[2]+","+card[3]+","+card[4];

                final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL_USERS);
                try {
                    ref.child(mAuth.getCurrentUser().getUid()).addValueEventListener(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String[] expmnth = new String[1];
                                    String[] expyear = new String[1];
                                    String[] decryptedselectedcard = new String[1];
                                    String[] decryptedselcetedcvv = new String[1];
                                    final String MyPREFERENCES = "GarconPref";
                                    final String tempCardexpm = "tempexpmkey";
                                    final String tempCardexpy = "tempexpykey";
                                    final String tempCardno = "tempcardnokey";
                                    final String tempCardcvv = "tempcardcvvkey";
                                    SharedPreferences sharedpreferences;
                                    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

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
                                                String decryptedcardno1 = "*************" + decryptedcardno.substring(decryptedcardno.length() - 4);
                                                if (decryptedcardno1.equals(card[2]) && card[0].equals(dataSnapshot1.child("cardType").getValue().toString())) {
                                                    decryptedselectedcard[0] = encrypt.decrypt(SEED, dataSnapshot1.child("cardNumber").getValue().toString());
                                                    decryptedselcetedcvv[0] = encrypt.decrypt(SEED, dataSnapshot1.child("cvv").getValue().toString());
                                                    expmnth[0] = dataSnapshot1.child("expiry").getValue().toString().split("\\-")[0];
                                                    expyear[0] = dataSnapshot1.child("expiry").getValue().toString().split("\\-")[1];
                                                    System.out.println("expmonth" + expmnth[0]);
                                                    System.out.println("expmonth" + expyear[0]);
                                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                                    editor.putString(tempCardexpm, expmnth[0]);
                                                    editor.putString(tempCardexpy, expyear[0]);
                                                    editor.putString(tempCardcvv, decryptedselcetedcvv[0]);
                                                    editor.putString(tempCardno, decryptedselectedcard[0]);

                                                    editor.apply();


                                                }

//                                System.out.println("encrypted value"+card.getCardNumber());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                            i = i + 1;
                                        }

                                    }
                                    System.out.println("setting done true");
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }

                            });

                } catch (Exception e) {

                }
            }
            stopSelf();

            return null;

        }

    }
}

