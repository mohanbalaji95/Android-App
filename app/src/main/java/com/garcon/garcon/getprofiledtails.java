package com.garcon.garcon;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class getprofiledtails extends Service {
//    User userget;
    Lock lock;
    public getprofiledtails() {
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
                    new UpdateUI().execute();
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
    public class UpdateUI extends AsyncTask<Void, Void, String[]> {
        User userget;

        @Override
        protected String[] doInBackground(Void... params) {
            final String[] string = new String[5];
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Constants.FIREBASE_URL_USERS);
            try {
                System.out.println("in start service");
                System.out.println(mAuth.getCurrentUser().getUid());
                ref.child(mAuth.getCurrentUser().getUid()).addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                System.out.println("datasnapshot getprofile");

                                        userget = dataSnapshot.getValue(User.class);

                                cardDetails card = dataSnapshot.getValue(cardDetails.class);
                                if (userget == null) {
                                    System.out.println("user is null");

                                    // User is null, error out

                                } else {
string[0] = userget.getUserName();
//                                    editusername.setText(userget.getUserName());
//                                    editphnumber.setText(userget.getPhoneNumber());
//                                    editfirstname.setText(userget.getFirstName());
//                                    editlastname.setText(userget.getLastName());
//                                    editemailid.setText(userget.geteMail());
//                                    editpassword.setText("*******************");

                                }
//                                for(int i=0; i<usercardnumber; i++)
                                while(card!=null)
                                {
//                                    newcard[usercardnumber] = mInflater.inflate(R.layout.newcard, container, false);
//                                    remove[usercardnumber] = (CheckBox) newcard[usercardnumber].findViewById(R.id.remove);
//                                    cardholder[usercardnumber] = (TextView) newcard[usercardnumber].findViewById(R.id.cardholder);
//                                    cardnumber[usercardnumber] = (TextView) newcard[usercardnumber].findViewById(R.id.cardnumber);
//                                    editcardholder[usercardnumber] = (EditText) newcard[usercardnumber].findViewById(R.id.editablecardholder);
//                                    editcardholder[usercardnumber].setText(card.getCardHolder());
//                                    editcardnumber[usercardnumber] = (EditText) newcard[usercardnumber].findViewById(R.id.editablecardnumber);
//                                    editcardnumber[usercardnumber].setText(card.getCardNumber().toString());
//                                    remove[usercardnumber].setVisibility(View.INVISIBLE);
//                                    editcardholder[usercardnumber].setEnabled(false);
//                                    editcardnumber[usercardnumber].setEnabled(false);
//                                    container.addView(newcard[usercardnumber]);
                                }


                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
            } catch (Exception e) {

            }
            return string;
        }

        //for loop to download the array of URLs.

        // final toast that notifies completion of download.
        @Override
        protected void onPostExecute(String[] userget) {
ProfileSettings obj = new ProfileSettings();
            obj.editusername.setText(userget[0]);
//            obj.editphnumber.setText(userget[1]);
//            obj.editfirstname.setText(userget[2]);
//            obj.editlastname.setText(userget[3]);
//            obj.editemailid.setText(userget[4]);
//            obj.editpassword.setText("*******************");

        }

    }
}


