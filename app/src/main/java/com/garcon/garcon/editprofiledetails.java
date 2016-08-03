package com.garcon.garcon;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import android.os.Handler;

import com.garcon.Constants.Constants;
import com.garcon.Models.User;
import com.garcon.garcon.ProfileSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;

public class editprofiledetails extends Service {

    public editprofiledetails() {
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
                    new DoeditBackgroundTask().execute();
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
    class DoeditBackgroundTask extends AsyncTask<Void, Void, Boolean> {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL_USERS);
        public static final String MyPREFERENCES = "GarconPref";
        public static final String Phone = "phoneKey";
        public static final String UserName = "usernameKey";
        public static final String LastName = "lastnameKey";
        public static final String FirstName = "firstnameKey";
        SharedPreferences sharedpreferences;

        protected Boolean doInBackground(final Void ... Params) {
//            final String name = edits[0];
//            final String number = edits[1];
            //for loop to download the array of URLs.

            try {

                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                System.out.println("in editprofikedetails service");
                System.out.println(mAuth.getCurrentUser().getUid());
                ref.child(mAuth.getCurrentUser().getUid()).addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                System.out.println("datasnapshot editprofile");
                                User user = dataSnapshot.getValue(User.class);
                                if (user == null) {
                                    System.out.println("user is null");

                                    // User is null, error out
//                                    Log.e(TAG, "User " + userId + " is unexpectedly null");
//                                   Toast.makeText(NewPostActivity.this,
//                                            "Error: could not fetch user.",
//                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // Write new post
                                    user.setUserName(sharedpreferences.getString(UserName, null));
                                    user.setPhoneNumber(sharedpreferences.getString(Phone, null));
                                    user.setFirstName(sharedpreferences.getString(FirstName, null));
                                    user.setLastName(sharedpreferences.getString(LastName, null));
                                    writeNewPost(sharedpreferences.getString(UserName, null), sharedpreferences.getString(Phone, null), sharedpreferences.getString(LastName, null), sharedpreferences.getString(FirstName, null));
                                }



                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {
//                                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                            }
                        });
            }
            catch(Exception e)
            {

            }
            stopSelf();
            return null;


        }

// final toast that notifies completion of download.
        protected void onPostExecute(Boolean result) {
            Toast.makeText(getBaseContext(), "editprofiledetails done!", Toast.LENGTH_LONG).show();
            stopSelf();
        }
        protected void writeNewPost(String name, String phone, String firstname, String lastname) {
            // Create new post at /user-posts/$userid/$postid and at
            // /posts/$postid simultaneously
            ref.child(mAuth.getCurrentUser().getUid()).child("userName").setValue(name);
            ref.child(mAuth.getCurrentUser().getUid()).child("phoneNumber").setValue(phone);
            ref.child(mAuth.getCurrentUser().getUid()).child("lasName").setValue(lastname);
            ref.child(mAuth.getCurrentUser().getUid()).child("firstName").setValue(firstname);


//        Map<String, Object> postValues = user.toMap();
//        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put(firebaseuser.getUid(), postValues);
//        ref.updateChildren(childUpdates);
//        System.out.println("in the end usernamee=" + user.getUserName());

        }
    }
}


