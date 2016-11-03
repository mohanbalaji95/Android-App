package com.garcon.garcon;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.garcon.Constants.Constants;
import com.garcon.Models.Receipt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class History extends ListActivity {
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems = new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    //RECORDING HOW MANY TIMES THE BUTTON HAS BEEN CLICKED
    int clickCounter = 0;
    private static final String TAG = favorite_activity.class.getName();
    DrawerLayout myDrawerLayout;
    NavigationView myNavigationView;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_history);
        final Button mapbutton = (Button) findViewById(R.id.seeViewMap);
        final Button restaurantbutton = (Button) findViewById(R.id.seeViewRestaurant);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //System.out.println("onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    //System.out.println("onAuthStateChanged:signed_out");
                    finish();
                }
            }
        };
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        setListAdapter(adapter);
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL_USERS);

        try {
            ref.child(mAuth.getCurrentUser().getUid()).child("Receipt").addValueEventListener(
                    new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            listItems.clear();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                Receipt receipt = dataSnapshot1.getValue(Receipt.class);
                                listItems.add(receipt.getdateAndTime()+"\n \nAmount Spent \n \nSubtotal - " + receipt.getsubTotal() + "\nTip - " + receipt.getTip() + "\nTotal - " + receipt.getTotal() + "\n \n Payment Method Used \n \n Card Type - " + receipt.getcardTypeUsed() + "\n Account# - " + receipt.getcardNumberUsed() + "\n Card Holder - " + receipt.getcardHolderUsed() + "\n");
                                adapter.notifyDataSetChanged();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });


        } catch (Exception e) {

        }
        mapbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(History.this, homeactivity.class);
                History.this.startActivity(mapIntent);
            }
        });
        restaurantbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent restaurantIntent = new Intent(History.this, homeactivity.class);
                History.this.startActivity(restaurantIntent);
            }
        });

        myDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        myNavigationView = (NavigationView) findViewById(R.id.profile);
        myNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(android.view.MenuItem menuItem) {


                if (menuItem.getItemId() == R.id.nav_profilesettings) {
                    Intent profile_settings = new Intent(getApplicationContext(), ProfileSettings.class);
                    startActivity(profile_settings);
                }
                if (menuItem.getItemId() == R.id.nav_item_sent) {
                    Intent fav_activity = new Intent(getApplicationContext(), favorite_activity.class);
                    startActivity(fav_activity);
                }

                if (menuItem.getItemId() == R.id.nav_history) {
                    Intent history = new Intent(getApplicationContext(), History.class);
                    startActivity(history);
                }

                if (menuItem.getItemId() == R.id.nav_settings) {
                    Intent settings = new Intent(getApplicationContext(), Settings.class);
                    startActivity(settings);
                }

                if (menuItem.getItemId() == R.id.nav_item_inbox) {
//                    FragmentTransaction xfragmentTransaction = myFragmentManager.beginTransaction();
//                    xfragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
                    Log.d(TAG, "menu item clicked");
                    FirebaseAuth.getInstance().signOut();
                    LoginManager.getInstance().logOut();
                    startActivity(new Intent(History.this, LoginActivity.class));
                    //finishActivity(0);
                }


                myDrawerLayout.closeDrawers();
                return false;
            }
        });

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.his_toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, myDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        myDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();


    }
}

//public class History extends AppCompatActivity {
//    FirebaseStorage storage;
//    StorageReference storageRef;
//    StorageReference imagesRef;
//    FirebaseAuth mAuth;
//    private Integer images[] = {R.drawable.logo, R.drawable.salad};
//    public static ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();
//    public static ArrayList<String> UriArray = new ArrayList<String>();
//    public static ArrayList<String> key = new ArrayList<String>();
//
//    static LinearLayout imageGallery;
//    static ImageView[] imageView;
//    int maxScrollX;
//    static int j;
//    private RelativeLayout payment_information;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        UriArray.clear();
//        bitmapArray.clear();
//        key.clear();
//        setContentView(R.layout.activity_history);
//        storage = FirebaseStorage.getInstance();
//        storageRef = storage.getReferenceFromUrl("gs://garcondatabase.appspot.com");
//        mAuth = FirebaseAuth.getInstance();
//        imagesRef = storageRef.child("UserReceipts").child(mAuth.getCurrentUser().getUid());
////        imageGallery= (LinearLayout) findViewById(R.id.imageGallery);
////        imageView = new ImageView[20];
////        j=0;
////        for(int i =0 ; i<10; i++) {
////            imageView[i] = new ImageView(this);
////            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
////            lp.setMargins(0, 0, 20, 0);
////            imageView[i].setLayoutParams(lp);
////            imageGallery.addView(imageView[i]);
////
////        }
//
//        payment_information = (RelativeLayout) findViewById(R.id.payment_information);
//        payment_information.setVisibility(View.GONE);
////
//        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
////            FirebaseStorage storage;
////            StorageReference storageRef;
////            StorageReference ReceiptsRef;
//        final DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL_USERS);
//
//        try {
//            ref.child(mAuth.getCurrentUser().getUid()).child("Receipt").addValueEventListener(
//                    new ValueEventListener() {
//
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            History.UriArray.clear();
//
//                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//
//                                Receipt receipt= dataSnapshot1.getValue(Receipt.class);
//                                    History.UriArray.add("Amount Spent \n \n Subtotal - "+receipt.getsubTotal()+"\nTip - "+receipt.getTip()+"\nTotal - "+receipt.getTotal()+"\n \n Payment Method Used \n \n Card Type - "+receipt.getcardTypeUsed()+"\n Account# - "+receipt.getcardNumberUsed()+"\n Card Holder - "+receipt.getcardHolderUsed()+"\n");
//
//                            }
////                            if(UriArray.size()>20)
////                            {
////                                int size = UriArray.size();
////                                for(int i = 0; i<size-20; i++ )
////                                {
////                                    ref.child(mAuth.getCurrentUser().getUid()).child("ReceiptsUrl").setValue(null);
////
////                                    UriArray.remove(i);
////                                }
////                            }
//                            try {
//
//                                BaseAdapter cardAdapter = new OptionsAdapter(UriArray, getApplicationContext());
//                                NonScrollableListView cardsLV = (NonScrollableListView) findViewById(R.id.credit_cards);
//                                cardsLV.setAdapter(cardAdapter);
////                                System.out.println("Uriarraysize = " + History.UriArray.size());
////                                Intent intent = new Intent(getApplicationContext(), getReceipt.class);
////                                startService(intent);
////
////
//
//                            } catch (Exception e) {
//
//                            }
//                        }
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//
//                    });
//
//
//
//
//        } catch (Exception e) {
//
//        }
//
//
//
//    }
//
//
////    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
////        @Override
////        public void onReceive(Context context, Intent intent) {
////            updateUI(intent);
////        }
////    };
//
//
//    protected static void updateUI(ArrayList<Bitmap> bmp) {
//        System.out.println("Uriarray size bitmap "+bitmapArray.size());
//        for(int j = 0; j<bmp.size();j++) {
//            imageView[j].setImageBitmap(bmp.get(j));
//        }
//
//    }
//}