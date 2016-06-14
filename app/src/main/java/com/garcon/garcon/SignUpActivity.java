package com.garcon.garcon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.garcon.Constants.Constants;

import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextAddress;
    private TextView textViewPersons;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        buttonSave = (Button) findViewById(R.id.buttonSave);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);

        textViewPersons = (TextView) findViewById(R.id.textViewPersons);
        Firebase.setAndroidContext(this);

        //Click Listener for button
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating firebase object
                Firebase ref = new Firebase(Constants.FIREBASE_URL);

                //Getting values to store
                String email = editTextName.getText().toString().trim();
                String password = editTextAddress.getText().toString().trim();

                ref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        System.out.println("Successfully created user account with uid: " + result.get("uid"));
                    }
                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // there was an error
                    }
                });
            }
        });
    }
}
