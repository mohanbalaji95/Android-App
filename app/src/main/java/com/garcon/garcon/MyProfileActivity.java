package com.garcon.garcon;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.garcon.Constants.Constants;
import com.garcon.Models.User;
import com.garcon.Models.cardDetails;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by Bharathi Raja on 7/29/2017.
 */

public class MyProfileActivity extends Fragment{
    private static final String TAG = favorite_activity.class.getName();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    //EditText editusername;
    EditText editfirstname;
    EditText editlastname;
    EditText editphnumber;
    EditText editemailid;
    EditText editpassword;
    EditText addcardholder;
    EditText addcardnumber;
    EditText addexpiry;
    EditText addcvv;
    EditText[] editcardholder;
    EditText[] editcardnumber;
    //    Button savecarddetails;
    Button addcard;
    ImageView editpasswordbutton;
    Button removecard;
    //    Button editcarddetails;
    Button savenewcard;
    Button canceladdnewcard;
    //    Button deletepropic;
//    ImageView profilepic;
    ImageView saveprofile;
    ImageView editprofile;
    Button remove_selected_card;
    TextView nameoncard;
    TextView expiry;
    TextView numberoncard;
    TextView cvv;
    TextView exactwarning;
    TextView expiryformat;
    TextView[] cardholder;
    TextView[] cardnumber;
    TextView cvvdefinition;
    Spinner cardtype;
    CheckBox[] remove = new CheckBox[10];
    LinearLayout container;
    static View[] newcard;
    LayoutInflater mInflater;
    FirebaseStorage storage;
    StorageReference storageRef;
    //    StorageReference profileimage;
    String cardtypestring;
    String[] edits = new String[2];
    static cardDetails card;
    private final static int RESULT_SELECT_IMAGE = 100;
    public static String SEED;
    View focusView = null;


    public static final String MyPREFERENCES = "GarconPref";
    public static final String eMailkey = "emailkey";
    public static final String FirstName = "firstnameKey";
    public static final String Phone = "phoneKey";
    public static final String LastName = "lastnameKey";
    //public static final String UserName = "usernameKey";
    public static final String usercardno = "usercardnumberkey";
    public static final String[] cardID = {"1cardkey", "2cardkey" , "3cardkey", "4cardkey" , "5cardkey", "6cardkey" , "7cardkey", "8cardkey" , "9cardkey", "10cardkey"};
    public static final String[] CardNumber = {"1key", "2key" , "3key", "4key" , "5key", "6key" , "7key", "8key" , "9key", "10key"};
    public static final String[] CardHolder = {"1holderkey", "2holderkey" , "3holderkey", "4holderkey" , "5holderkey", "6holderkey" , "7holderkey", "8holderkey" , "9holderkey", "10holderkey"};
    SharedPreferences sharedpreferences;
    static String[] cardUID = new String[10];
    static int usercardnumber = 0;
    final Boolean[] ischecked = new Boolean[10];
    Uri selectedImage;

    String string = "";
    Handler handler;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    View rootView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.activity_myprofile, container, false);
        initialize();
 editprofile=(ImageView)rootView.findViewById(R.id.editprofile);
        saveprofile=(ImageView)rootView.findViewById(R.id.saveprofile);
        editprofile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               // editusername.setEnabled(true);
                editphnumber.setEnabled(true);
                editlastname.setEnabled(true);
                editfirstname.setEnabled(true);
                editprofile.setVisibility(View.INVISIBLE);
                saveprofile.setVisibility(View.VISIBLE);
//                profilepic.setClickable(true);
//                profilepic.setEnabled(true);
                //editpasswordbutton.setVisibility(View.VISIBLE);
//                deletepropic.setVisibility(View.VISIBLE);


            }

        });
        saveprofile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean cancel = false;
                /*if (TextUtils.isEmpty(editusername.getText().toString())) {
                    editusername.setError("username cannot be empty");
                    focusView = editusername;
                    cancel = true;
                    cancelling(cancel);
                }*/

                 if(TextUtils.isEmpty(editphnumber.getText().toString()))
                {
                    editphnumber.setError("phone number cannot be empty");
                    focusView = editphnumber;
                    cancel = true;
                    cancelling(cancel);
                }
                else if (!isPhoneNumberValid(editphnumber.getText().toString())) {
                    editphnumber.setError("Enter a valid phone number");
                    focusView = editphnumber;
                    cancel = true;
                    cancelling(cancel);
                }
                else {
                    Toast.makeText(getApplicationContext(), "saved changes", Toast.LENGTH_SHORT).show();
                    //editusername.setEnabled(false);
                    editphnumber.setEnabled(false);
                    editfirstname.setEnabled(false);
                    editlastname.setEnabled(false);
                    editprofile.setVisibility(View.VISIBLE);
                    saveprofile.setVisibility(View.INVISIBLE);
//                    profilepic.setClickable(false);
//                    profilepic.setEnabled(false);
//                    deletepropic.setVisibility(View.INVISIBLE);
                   // editpasswordbutton.setVisibility(View.INVISIBLE);

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Phone, editphnumber.getText().toString());
                    //editor.putString(UserName, editusername.getText().toString());
                    editor.putString(FirstName, editfirstname.getText().toString());
                    editor.putString(LastName, editlastname.getText().toString());
                    editor.commit();
                    Intent editprofiledetails = new Intent(getApplicationContext(), editprofiledetails.class);
                    editprofiledetails.putExtra("edits", edits);

                    //startService(editprofiledetails);
                }
            }


        });
        /*editpasswordbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent editpassword = new Intent(getApplicationContext(), EditPassword.class);
                startActivity(editpassword);
            }
        });*/
        return rootView;


    }
    public void cancelling(Boolean cancel)
    {
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
    }

    private  boolean isPhoneNumberValid(String phoneNumber){

        Pattern phonePattern = Pattern.compile("[0-9]{3}-[0-9]{3}-[0-9]{4}");
        Matcher phoneMatcher = phonePattern.matcher(phoneNumber);

        return phoneMatcher.matches();
    }
    public void initialize() {

        container = (LinearLayout)rootView.findViewById(R.id.container);
        editpassword = (EditText) rootView.findViewById(R.id.editablepassword);
        editemailid = (EditText) rootView.findViewById(R.id.editableemailid);
        editfirstname = (EditText) rootView.findViewById(R.id.editablefirstname);
        editlastname = (EditText) rootView.findViewById(R.id.editablelastname);
        editphnumber = (EditText) rootView.findViewById(R.id.editablephnumber);
      //  editusername = (EditText) rootView.findViewById(R.id.editableusername);
        editemailid.setEnabled(false);
       // editusername.setEnabled(false);
        editpassword.setEnabled(false);
        editfirstname.setEnabled(false);
        editlastname.setEnabled(false);
        editphnumber.setEnabled(false);
        mInflater = (LayoutInflater) getApplicationContext()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        storage = FirebaseStorage.getInstance();
//        deletepropic = (Button) findViewById(R.id.deletepropic);
//        deletepropic.setVisibility(View.GONE);
        // Create a storage reference from our app
        storageRef = storage.getReferenceFromUrl("gs://garcondatabase.appspot.com");
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if(sharedpreferences.getString(eMailkey, null)==null)
        {
            Reinstallation reinstall = new Reinstallation();
            reinstall.execute();

        }
        else {
            editemailid.setText(sharedpreferences.getString(eMailkey, null));
  //          editusername.setText(sharedpreferences.getString(UserName, null));
            editfirstname.setText(sharedpreferences.getString(FirstName, null));
            editlastname.setText(sharedpreferences.getString(LastName, null));
            editphnumber.setText(sharedpreferences.getString(Phone, null));
            editpassword.setText("*************");
        }


    }
    class Reinstallation extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL_USERS);
            try {
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
                                    //editusername.setText(user.getUserName());
                                    editphnumber.setText(user.getPhoneNumber());
                                    editfirstname.setText(user.getFirstName());
                                    editlastname.setText(user.getLastName());
                                    editemailid.setText(user.geteMail());
                                    editpassword.setText("************");
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString(Phone, editphnumber.getText().toString());
                                    //editor.putString(UserName, editusername.getText().toString());
                                    editor.putString(FirstName, editfirstname.getText().toString());
                                    editor.putString(LastName, editlastname.getText().toString());
                                    editor.commit();
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
