package com.garcon.garcon;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.Intent;
import android.widget.TextView;

/**
 * Created by siddhiparekh11 on 8/18/17.
 * Activity to display order details.
 */

public class OrderDetail extends AppCompatActivity {

    private static final String TAG = com.garcon.garcon.OrderDetail.class.getName();

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Firebase.setAndroidContext(this);
        }
        setContentView(R.layout.activity_order_detail);
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
        Intent i=getIntent();
        TextView total=(TextView)findViewById(R.id.total_final);
        OrderHistoryCardView o=(OrderHistoryCardView)i.getSerializableExtra("ordobj");
        total.setText(Double.toString(o.getTotal()));

    }
}
