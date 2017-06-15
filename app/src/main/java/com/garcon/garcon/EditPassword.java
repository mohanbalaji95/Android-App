package com.garcon.garcon;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.garcon.Constants.Constants;
import com.garcon.Models.User;
import com.garcon.Models.cardDetails;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditPassword extends AppCompatActivity {

    EditText getpassword;
    EditText getnewpassword;
    EditText getreenter;
    TextView password;
    TextView newpassword;
    TextView reenter;
    Button next;
    Button savepassword;
    String newpasswordstring = new String();
    private FirebaseAuth mAuth;
    FirebaseUser user;

    public static String oldpassword;
    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20})";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        initialize();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                oldpassword = getpassword.getText().toString();

                mAuth.signInWithEmailAndPassword(user.getEmail(), oldpassword)
                        .addOnCompleteListener(EditPassword.this, new OnCompleteListener<AuthResult>() {
                            boolean cancel = false;
                            View focusView = null;

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                System.out.println("signInWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    System.out.println("signInWithEmail" + task.getException());
                                    Toast.makeText(EditPassword.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    getpassword.setError("invalid password");
                                    focusView = getnewpassword;
                                    cancel = true;

                                    //System.out.println("Error Code:"+firebaseError.getCode());
                                    //System.out.println("Error:"+ firebaseError.getMessage());
                                    //updateErrorCode(firebaseError.getCode());
                                } else {
                                    //System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                                    System.out.println("Successful login");


                                }

                                if (cancel) {
                                    // There was an error; don't attempt login and focus the first
                                    // form field with an error.
                                    focusView.requestFocus();
                                } else {
                                    password.setVisibility(View.INVISIBLE);
                                    getpassword.setVisibility(View.INVISIBLE);
                                    next.setVisibility(View.INVISIBLE);
                                    getreenter.setVisibility(View.VISIBLE);
                                    getnewpassword.setVisibility(View.VISIBLE);
                                    newpassword.setVisibility(View.VISIBLE);
                                    reenter.setVisibility(View.VISIBLE);
                                    savepassword.setVisibility(View.VISIBLE);
                                }
                            }
                        });
            }
        });
        savepassword.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                newpasswordstring = getnewpassword.getText().toString();
                String confirmPassword = getreenter.getText().toString();
                boolean cancel = false;
                View focusView = null;
                // Check for a valid password, if the user entered one.
                if (!TextUtils.isEmpty(newpasswordstring) && !isPasswordValid(newpasswordstring)) {
                    getnewpassword.setError(getString(R.string.error_invalid_password));
                    focusView = getnewpassword;
                    cancel = true;
                }

                if (!confirmPassword.equals(newpasswordstring)) {
                    getreenter.setError(getString(R.string.error_incorrect_confirmpassword));
                    focusView = getreenter;
                    cancel = true;
                }
                if (newpasswordstring.equals(oldpassword)) {
                    getnewpassword.setError("New password cannot be same as the previous password");
                    focusView = getnewpassword;
                    cancel = true;
                }
                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    user.updatePassword(newpasswordstring);
                    finish();
                }

            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher passwordMatcher = passwordPattern.matcher(password);

        return passwordMatcher.matches();
    }

    public void initialize() {
        getnewpassword = (EditText) findViewById(R.id.getnewpassword);
        getpassword = (EditText) findViewById(R.id.getpassword);
        getreenter = (EditText) findViewById(R.id.getreenter);
        password = (TextView) findViewById(R.id.password);
        newpassword = (TextView) findViewById(R.id.newpassword);
        reenter = (TextView) findViewById(R.id.reenetr);
        next = (Button) findViewById(R.id.next);
        savepassword = (Button) findViewById(R.id.savepassword);
        getreenter.setVisibility(View.INVISIBLE);
        getnewpassword.setVisibility(View.INVISIBLE);
        newpassword.setVisibility(View.INVISIBLE);
        reenter.setVisibility(View.INVISIBLE);
        savepassword.setVisibility(View.INVISIBLE);


    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "EditPassword Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.garcon.garcon/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "EditPassword Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.garcon.garcon/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

}
