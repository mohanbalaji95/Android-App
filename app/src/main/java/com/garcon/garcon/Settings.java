package com.garcon.garcon;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by cesar_000 on 11/2/2016.
 */

public class Settings extends AppBarActivity {
    private static final String TAG = Settings.class.getName();

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    Switch switchButton;
    TextView textview;
    BluetoothAdapter bluetoothadapter;
    int i = 1;
    Intent bluetoothIntent;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_settings);


        /*final Button termsandconditionsbutton = (Button) findViewById(R.id.seeTermsandConditions);
        switchButton = (Switch) findViewById(R.id.switch1);
        textview = (TextView) findViewById(R.id.textView1);
        */
        bluetoothadapter = BluetoothAdapter.getDefaultAdapter();
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

        /*termsandconditionsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent termsIntent = new Intent(Settings.this, termsandconditions.class);
                Settings.this.startActivity(termsIntent);
            }
        });*/
        /*switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub

                if (isChecked) {
                    BluetoothEnable();
                } else {
                    BluetoothDisable();
                }
            }
        });*/
        ListView listView = (ListView) findViewById(R.id.listView);
        final TextView txtPhoneCall = (TextView) findViewById(R.id.txtCallPhone);
        final TextView txtSendEmail = (TextView) findViewById(R.id.txtSendEmail);
        txtSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.parse("mailto:garcon@garconllc.com?subject=" + "" + "&body=" + "");
                    intent.setData(data);
                    startActivity(intent);
                } catch (Exception e) {
                    String msg = e.getMessage().toString();
                }
            }
        });
        txtPhoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);

                    callIntent.setData(Uri.parse("tel:" + txtPhoneCall.getText().toString()));
                    startActivity(callIntent);
                } catch (Exception e) {
                    String msg = e.getMessage().toString();
                }
            }
        });
        ListView list = (ListView) findViewById(R.id.listView);
        customadapter ca = new customadapter();
        list.setAdapter(ca);
    }

    public void BluetoothEnable() {
        bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(bluetoothIntent, i);
        textview.setText("Bluetooth ON");
    }

    public void BluetoothDisable() {
        bluetoothadapter.disable();
        textview.setText("Bluetooth OFF");
    }
    class customadapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return Settings.length;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub

            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }


        public View getView(final int position, View convertview, ViewGroup arg2) {
            // TODO Auto-generated method stub
            LayoutInflater inflater = getLayoutInflater();
            convertview = inflater.inflate(R.layout.settingsrow, null);

            TextView tv = (TextView) convertview.findViewById(R.id.textView2);
            Switch sw=(Switch) convertview.findViewById(R.id.switchButton);

            tv.setText(Settings[position]);



            return convertview;
        }

    }

    String[] Settings = {"Email :", "In App :", "Lock Screen Notification :", "Bluetooth :"};

}