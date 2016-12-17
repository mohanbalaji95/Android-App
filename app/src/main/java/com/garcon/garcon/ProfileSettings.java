package com.garcon.garcon;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.garcon.Constants.Constants;
import com.garcon.Models.User;
import com.garcon.Models.cardDetails;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ProfileSettings extends AppCompatActivity {
    private static final String TAG = favorite_activity.class.getName();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    EditText editusername;
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
    public static final String UserName = "usernameKey";
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
//    private userupdate muserupdate = null;
//    private uiupdate muiupdate = null;


    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_profile_settings);
        initialize();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

//        StorageReference profilepicdownload = storageRef.child("profileic/" + firebaseuser.getUid());

        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        final File finalLocalFile = localFile;
//        profilepicdownload.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                // Local temp file has been created
////                profilepic.setImageURI(Uri.parse(String.valueOf(Uri.fromFile(finalLocalFile))));
//                BitmapFactory.Options o = new BitmapFactory.Options();
//                o.inJustDecodeBounds = true;
//                try {
//                    BitmapFactory.decodeStream(getApplicationContext().getContentResolver().openInputStream(Uri.parse(String.valueOf(Uri.fromFile(finalLocalFile)))), null, o);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//                int width_tmp = o.outWidth, height_tmp = o.outHeight;
//                int scale = 1;
//
//                while (true) {
//                    if (width_tmp / 2 < 600 || height_tmp / 2 < 600)
//                        break;
//                    width_tmp /= 2;
//                    height_tmp /= 2;
//                    scale *= 2;
//                }
//
//                BitmapFactory.Options o2 = new BitmapFactory.Options();
//                o2.inSampleSize = scale;
//                try {
//                    profilepic.setImageBitmap(BitmapFactory.decodeStream(getApplicationContext().getContentResolver().openInputStream(Uri.parse(String.valueOf(Uri.fromFile(finalLocalFile)))), null, o2));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });
        //service intent along with the URLs.


        cardtypestring = cardtype.getSelectedItem().toString();


        editprofile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editusername.setEnabled(true);
                editphnumber.setEnabled(true);
                editlastname.setEnabled(true);
                editfirstname.setEnabled(true);
                editprofile.setVisibility(View.INVISIBLE);
                saveprofile.setVisibility(View.VISIBLE);
//                profilepic.setClickable(true);
//                profilepic.setEnabled(true);
                editpasswordbutton.setVisibility(View.VISIBLE);
//                deletepropic.setVisibility(View.VISIBLE);


            }

        });

//        profilepic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    //Pick Image From Gallery
//                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(i, RESULT_SELECT_IMAGE);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

//        deletepropic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                profilepic.setImageBitmap(null);
                // Create a reference to the file to delete
//                StorageReference deleteref = storageRef.child("profileic/"+firebaseuser.getUid());
                // Delete the file
//                deleteref.delete().addOnSuccessListener(new OnSuccessListener() {
//                    @Override
//                    public void onSuccess(Object o) {
//
//                    }
//
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Uh-oh, an error occurred!
//                    }
//                });
//            }
//        });

        saveprofile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean cancel = false;
                if (TextUtils.isEmpty(editusername.getText().toString())) {
                    editusername.setError("username cannot be empty");
                    focusView = editusername;
                    cancel = true;
                    cancelling(cancel);
                }

                else if(TextUtils.isEmpty(editphnumber.getText().toString()))
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
                    editusername.setEnabled(false);
                    editphnumber.setEnabled(false);
                    editfirstname.setEnabled(false);
                    editlastname.setEnabled(false);
                    editprofile.setVisibility(View.VISIBLE);
                    saveprofile.setVisibility(View.INVISIBLE);
//                    profilepic.setClickable(false);
//                    profilepic.setEnabled(false);
//                    deletepropic.setVisibility(View.INVISIBLE);
                    editpasswordbutton.setVisibility(View.INVISIBLE);

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Phone, editphnumber.getText().toString());
                    editor.putString(UserName, editusername.getText().toString());
                    editor.putString(FirstName, editfirstname.getText().toString());
                    editor.putString(LastName, editlastname.getText().toString());
                    editor.commit();
                    Intent editprofiledetails = new Intent(getApplicationContext(), editprofiledetails.class);
                    editprofiledetails.putExtra("edits", edits);
                    startService(editprofiledetails);
                }
            }


        });
        editpasswordbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent editpassword = new Intent(getApplicationContext(), EditPassword.class);
                startActivity(editpassword);
            }
        });
        canceladdnewcard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                addcard.setVisibility(View.VISIBLE);
                addcardholder.setText(null);
                addcardnumber.setText(null);
                addexpiry.setText(null);
                addcvv.setText(null);
                addcardholder.setVisibility(View.INVISIBLE);
                addcardnumber.setVisibility(View.INVISIBLE);
                addcvv.setVisibility(View.INVISIBLE);
                addexpiry.setVisibility(View.INVISIBLE);
                savenewcard.setVisibility(View.INVISIBLE);
                expiryformat.setVisibility(View.INVISIBLE);
                exactwarning.setVisibility(View.INVISIBLE);
                nameoncard.setVisibility(View.INVISIBLE);
                expiry.setVisibility(View.INVISIBLE);
                numberoncard.setVisibility(View.INVISIBLE);
                cvv.setVisibility(View.INVISIBLE);
                cvvdefinition.setVisibility(View.INVISIBLE);
//                    savecarddetails.setEnabled(false);
                cardtype.setVisibility(View.INVISIBLE);
                canceladdnewcard.setVisibility(View.INVISIBLE);


            }

        });


        addcard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (usercardnumber == 10) {
                    AlertDialog.Builder alertDialog;
                    alertDialog = new AlertDialog.Builder(ProfileSettings.this);
                    alertDialog.setTitle("Cannot add a new card");
                    alertDialog.setMessage("User can add upto a maximum of 10 cards. Please delete a card to add another");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                    alertDialog.show();
                } else {
                    addcard.setVisibility(View.INVISIBLE);
                    addcardholder.setVisibility(View.VISIBLE);
                    addcardnumber.setVisibility(View.VISIBLE);
                    addcvv.setVisibility(View.VISIBLE);
                    addexpiry.setVisibility(View.VISIBLE);
                    savenewcard.setVisibility(View.VISIBLE);
                    expiryformat.setVisibility(View.VISIBLE);
                    exactwarning.setVisibility(View.VISIBLE);
                    nameoncard.setVisibility(View.VISIBLE);
                    expiry.setVisibility(View.VISIBLE);
                    numberoncard.setVisibility(View.VISIBLE);
                    cvv.setVisibility(View.VISIBLE);
                    canceladdnewcard.setVisibility(View.VISIBLE);
                    cvvdefinition.setVisibility(View.VISIBLE);
                    cardtype.setVisibility(View.VISIBLE);
                }

            }

        });



        savenewcard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Boolean cancel = false;
                if (TextUtils.isEmpty(addcardnumber.getText().toString())) {
                    addcardnumber.setError("cardnumber cannot be empty");
                    focusView = addcardnumber;
                    cancel = true;
                    cancelling(cancel);
                } else if (!iscardnumbervalid(addcardnumber.getText().toString())) {
                    addcardnumber.setError("Enter a valid cardnumber");
                    focusView = addcardnumber;
                    cancel = true;
                    cancelling(cancel);
                } else if (TextUtils.isEmpty(addcardholder.getText().toString())) {
                    addcardholder.setError("card holder cannot be empty");
                    focusView = addcardholder;
                    cancel = true;
                    cancelling(cancel);
                } else if (TextUtils.isEmpty(addcvv.getText().toString())) {
                    addcvv.setError("cvv holder cannot be empty");
                    focusView = addcvv;
                    cancel = true;
                    cancelling(cancel);
                } else if (!iscvvvalid(addcvv.getText().toString())) {
                    addcvv.setError("Enter valid cvv");
                    focusView = addcvv;
                    cancel = true;
                    cancelling(cancel);
                } else if (TextUtils.isEmpty(addexpiry.getText().toString())) {
                    addexpiry.setError("expiry holder cannot be empty");
                    focusView = addexpiry;
                    cancel = true;
                    cancelling(cancel);
                } else if (!isexpiryvalid(addexpiry.getText().toString())) {
                    addexpiry.setError("Expiry format 01-17");
                    focusView = addexpiry;
                    cancel = true;
                    cancelling(cancel);
                } else {
                    addcardholder.setVisibility(View.INVISIBLE);
                    addcardnumber.setVisibility(View.INVISIBLE);
                    addcvv.setVisibility(View.INVISIBLE);
                    addexpiry.setVisibility(View.INVISIBLE);
                    savenewcard.setVisibility(View.INVISIBLE);
                    canceladdnewcard.setVisibility(View.INVISIBLE);
                    expiryformat.setVisibility(View.INVISIBLE);
                    exactwarning.setVisibility(View.INVISIBLE);
                    addcard.setVisibility(View.VISIBLE);
                    nameoncard.setVisibility(View.INVISIBLE);
                    expiry.setVisibility(View.INVISIBLE);
                    numberoncard.setVisibility(View.INVISIBLE);
                    cvv.setVisibility(View.INVISIBLE);
                    cvvdefinition.setVisibility(View.INVISIBLE);
                    addcard.setVisibility(View.VISIBLE);
//                    savecarddetails.setEnabled(true);
                    cardtype.setVisibility(View.INVISIBLE);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ProfileSettings.this);
                    final EditText edittext = new EditText(getApplicationContext());
                    edittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edittext.setTextColor(Color.BLACK);
                    final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    card = new cardDetails();
//                    builder.setTitle("Enter Password");
//                    builder.setMessage("Enter password for "+ mAuth.getCurrentUser().getEmail());
//                    builder.setView(edittext);
//                    builder.setNegativeButton("Cancel",
//                            new DialogInterface.OnClickListener()
//                            {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which)
//                                {
//                                }
//                            });
//                    builder.setPositiveButton("Save",
//                            new DialogInterface.OnClickListener()
//                            {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which)
//                                {
//                                    //Do nothing here because we override this button later to change the close behaviour.
//                                    //However, we still need this because on older versions of Android unless we
//                                    //pass a handler the button doesn't get instantiated
//                                }
//                            });
//                    final AlertDialog dialog = builder.create();
//                    dialog.show();
                    newcard[usercardnumber] = mInflater.inflate(R.layout.newcard, container, false);
                    remove[usercardnumber] = (CheckBox) newcard[usercardnumber].findViewById(R.id.remove);
                    cardholder[usercardnumber] = (TextView) newcard[usercardnumber].findViewById(R.id.cardholder);
                    cardnumber[usercardnumber] = (TextView) newcard[usercardnumber].findViewById(R.id.cardnumber);
                    editcardholder[usercardnumber] = (EditText) newcard[usercardnumber].findViewById(R.id.editablecardholder);
//                editcardholder[usercardnumber].setText(card.getCardHolder());
                    editcardnumber[usercardnumber] = (EditText) newcard[usercardnumber].findViewById(R.id.editablecardnumber);
//                editcardnumber[usercardnumber].setText(card.getCardNumber().toString());
                    remove[usercardnumber].setVisibility(View.INVISIBLE);


                    editcardholder[usercardnumber].setEnabled(false);
                    editcardnumber[usercardnumber].setEnabled(false);
//Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
//                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
//                    {
//                        @Override
//                        public void onClick(View v) {
//                            final Boolean[] wantToCloseDialog = {false};
//                            //Do stuff, possibly set wantToCloseDialog to true then...
//                            mAuth.signInWithEmailAndPassword(mAuth.getCurrentUser().getEmail(), edittext.getText().toString())
//                                    .addOnCompleteListener(ProfileSettings.this, new OnCompleteListener<AuthResult>() {
//                                        boolean cancel = false;
//                                        View focusView = null;
//                                        String tempcardnumholder = addcardnumber.getText().toString();
//                                        String tempcardHolder = addcardholder.getText().toString();
//                                        @Override
//                                        public void onComplete(@NonNull Task<AuthResult> task) {
//                                            System.out.println("signInWithEmail:onComplete:" + task.isSuccessful());
//
//                                            // If sign in fails, display a message to the user. If sign in succeeds
//                                            // the auth state listener will be notified and logic to handle the
//                                            // signed in user can be handled in the listener.
//                                            if (!task.isSuccessful()) {
//                                                System.out.println("signInWithEmail" + task.getException());
//                                                Toast.makeText(ProfileSettings.this, "Authentication failed.",
//                                                        Toast.LENGTH_SHORT).show();
//                                                edittext.setError("invalid password");
//                                                focusView = edittext;
//                                                cancel = true;
//
//                                                //System.out.println("Error Code:"+firebaseError.getCode());
//                                                //System.out.println("Error:"+ firebaseError.getMessage());
//                                                //updateErrorCode(firebaseError.getCode());
//                                            } else {
//                                                //System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
//                                                System.out.println("Successful login");
//
//
//                                            }
//
//                                            if (cancel) {
//                                                // There was an error; don't attempt login and focus the first
//                                                // form field with an error.
//                                                focusView.requestFocus();
//                                            } else {
//                                                dialog.dismiss();
                    SEED = mAuth.getCurrentUser().getUid();

                    try {
                        card.setCardNumber(encrypt.encryption(SEED, addcardnumber.getText().toString()));
//                                System.out.println("encrypted value"+card.getCardNumber());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                            try {
//                                String decMsg = encrypt.decrypt(SEED, card.getCardNumber());
//                                System.out.println("decrypted value"+decMsg);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
                    try {
                        card.setcvv(encrypt.encryption(SEED, addcvv.getText().toString()));
//                                System.out.println("encrypted value"+card.getCardNumber());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    card.setCardHolder(addcardholder.getText().toString());
                    final DatabaseReference ref = FirebaseDatabase.getInstance()
                            .getReferenceFromUrl(Constants.FIREBASE_URL_USERS);
                    String key = ref.push().getKey();
                    card.setCardID(key);
                    cardUID[usercardnumber] = card.getCardID();
                    removecard.setVisibility(View.VISIBLE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(cardID[usercardnumber], cardUID[usercardnumber]);
                    editor.apply();
                    System.out.println("cardID in savenewcard " + sharedpreferences.getString(cardID[usercardnumber], null));
                    System.out.println("cardid" + cardUID[usercardnumber]);
                    card.setCardType(cardtypestring);
                    card.setExpiry(addexpiry.getText().toString());
                    editor.putString(CardNumber[usercardnumber], "*************" + addcardnumber.getText().toString().substring(addcardnumber.getText().toString().length() - 3));
                    editor.putString(CardHolder[usercardnumber], card.getCardHolder());
                    editor.commit();
                    editcardholder[usercardnumber].setText(sharedpreferences.getString(CardHolder[usercardnumber], null));
                    editcardnumber[usercardnumber].setText(sharedpreferences.getString(CardNumber[usercardnumber], null));
                    System.out.println("usercardnumber in savenewcard" + usercardnumber);
                    Intent addnewcard = new Intent(getApplicationContext(), com.garcon.garcon.addnewcard.class);
                    startService(addnewcard);
                    container.addView(newcard[usercardnumber]);
                    usercardnumber++;
                    editor.putInt(usercardno, usercardnumber);
                    editor.commit();
                    addcardholder.setText(null);
                    addcardnumber.setText(null);
                    addexpiry.setText(null);
                    addcvv.setText(null);
                }
            }
//                                    });
//                            if(wantToCloseDialog[0])
//                            {
//                                System.out.println("in want to close dialog");
//                                dialog.dismiss();
//                            }
            //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.

        });


        removecard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                for (int i = 0; i < usercardnumber; i++) {

                    ischecked[i] = false;
                    remove[i].setVisibility(View.VISIBLE);
                    final int finalI = i;
                    remove_selected_card.setVisibility(View.VISIBLE);
                    remove[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b) {
                                ischecked[finalI] = true;
                                System.out.println("cardholder in ischecked true "+finalI);
                            }
                            else {
                                ischecked[finalI] = false;
                                System.out.println("cardholder in ischecked false "+finalI);

                            }

                        }

                    });
                }

            }
        });

        remove_selected_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intstart = new Intent(ProfileSettings.this, removecard.class);
                int i=0;
                for ( i = 0; i < usercardnumber; i++) {
                    System.out.println("cardholder in the for loop");
                    if (ischecked[i]) {
                        System.out.println("cardholder in ischecked");
                        intstart.putExtra("cardUID", cardUID[i]);
                        startService(intstart);
                        newcard[i].setVisibility(View.GONE);
                        usercardnumber--;
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putInt(usercardno, usercardnumber);
                        editor.apply();
                        if((i==usercardnumber-1) && ischecked[i+1]) {
                            intstart.putExtra("cardUID", cardUID[i + 1]);
                            startService(intstart);
                            newcard[i+1].setVisibility(View.GONE);
                            usercardnumber--;
                            editor.putInt(usercardno, usercardnumber);
                            editor.apply();
                            System.out.println("usercardnumber in removecard" + usercardnumber);
                            for (int j = i+1; j < usercardnumber; j++) {
                                ischecked[j] = ischecked[j + 1];
                                cardUID[j] = cardUID[j + 1];
                                editor.putString(CardNumber[j], sharedpreferences.getString(CardNumber[j + 1], null));
                                editor.putString(CardHolder[j], sharedpreferences.getString(CardHolder[j + 1], null));
                                editor.putString(cardID[j], sharedpreferences.getString(cardID[j + 1], null));
                                editor.apply();

                                System.out.println("cardID in on j " + sharedpreferences.getString(cardID[j], null));
                                System.out.println("cardholder in on j " + sharedpreferences.getString(CardHolder[j], null));

                                newcard[j] = newcard[j + 1];
                                remove[j] = remove[j + 1];
                                cardholder[j] = cardholder[j + 1];
                                editcardholder[j] = editcardholder[j + 1];
                                cardnumber[j] = cardnumber[j + 1];
                                editcardnumber[j] = editcardnumber[j + 1];
                                System.out.println("cardUID[0] = " + cardUID[0]);


                            }
                        }
                        else
                        {
                            for (int j = i; j < usercardnumber; j++) {
                                ischecked[j] = ischecked[j + 1];
                                cardUID[j] = cardUID[j + 1];
                                editor.putString(CardNumber[j], sharedpreferences.getString(CardNumber[j + 1], null));
                                editor.putString(CardHolder[j], sharedpreferences.getString(CardHolder[j + 1], null));
                                editor.putString(cardID[j], sharedpreferences.getString(cardID[j + 1], null));
                                editor.apply();

                                System.out.println("cardID in on j " + sharedpreferences.getString(cardID[j], null));
                                System.out.println("cardholder in on j " + sharedpreferences.getString(CardHolder[j], null));

                                newcard[j] = newcard[j + 1];
                                remove[j] = remove[j + 1];
                                cardholder[j] = cardholder[j + 1];
                                editcardholder[j] = editcardholder[j + 1];
                                cardnumber[j] = cardnumber[j + 1];
                                editcardnumber[j] = editcardnumber[j + 1];
                                System.out.println("cardUID[0] = " + cardUID[0]);


                            }
                        }

                        if(usercardnumber==0)
                        {
                            removecard.setVisibility(View.INVISIBLE);
                        }
                    }
                    remove[i].setVisibility(View.INVISIBLE);
                }
                remove_selected_card.setVisibility(View.INVISIBLE);


            }
        });

//        savecarddetails.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(getApplicationContext(), "saved changes", Toast.LENGTH_SHORT).show();
//                addcard.setVisibility(View.INVISIBLE);
//                removecard.setVisibility(View.INVISIBLE);
//                editcarddetails.setVisibility(View.VISIBLE);
//                savecarddetails.setVisibility(View.INVISIBLE);
//                addcard.setVisibility(View.INVISIBLE);
//
//            }
//
//
//        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SELECT_IMAGE:

                if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                    selectedImage = data.getData();
                    BitmapFactory.Options o = new BitmapFactory.Options();
                    o.inJustDecodeBounds = true;
                    try {
                        BitmapFactory.decodeStream(getApplicationContext().getContentResolver().openInputStream(selectedImage), null, o);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    int width_tmp = o.outWidth, height_tmp = o.outHeight;
                    int scale = 1;

                    while (true) {
                        if (width_tmp / 2 < 600 || height_tmp / 2 < 600)
                            break;
                        width_tmp /= 2;
                        height_tmp /= 2;
                        scale *= 2;
                    }

                    BitmapFactory.Options o2 = new BitmapFactory.Options();
                    o2.inSampleSize = scale;
//                    try {
//                        profilepic.setImageBitmap(BitmapFactory.decodeStream(getApplicationContext().getContentResolver().openInputStream(selectedImage), null, o2));
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
                    // Create file metadata including the content type
//                    StorageMetadata metadata = new StorageMetadata.Builder()
//                            .setContentType("image/jpg")
//                            .setCustomMetadata(firebaseuser.getUid(), firebaseuser.getEmail())
//                            .build();
//                    profileimage = storageRef.child("profileic/" + firebaseuser.getUid());
//                    UploadTask uploadTask = profileimage.putFile(selectedImage);

// Register observers to listen for when the download is done or if it fails
//                    uploadTask.addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            // Handle unsuccessful uploads
//                        }
//                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                        }
//                    });
//                    // Observe state change events such as progress, pause, and resume
//                    uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            double progress = 100.0 * (taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                            System.out.println("Upload is " + progress + "% done");
//                        }
//                    }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
//                            System.out.println("Upload is paused");
//                        }
//                    });


                }

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // If there's an upload in progress, save the reference so you can query it later
        if (storageRef != null) {
            outState.putString("reference", storageRef.toString());
        }
//        outState.putInt("usercardnumber", usercardnumber);
//        for(int i=0; i<usercardnumber; i++)
//        {
//            outState.putString("cardUID["+i+"]", cardUID[i]);
//
//        }
//        SharedPreferences.Editor editor = sharedpreferences.edit();
//        editor.putInt(usercardno, usercardnumber);
//        editor.commit();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // If there was an upload in progress, get its reference and create a new StorageReference
        final String stringRef = savedInstanceState.getString("reference");
        if (stringRef == null) {
            return;
        }
//        usercardnumber = savedInstanceState.getInt("usercardnumber");
//        for(int i=0; i<usercardnumber; i++)
//        {
//            cardUID[i] = savedInstanceState.getString("cardUID["+i+"]");
//
//        }
        storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(stringRef);

        // Find all UploadTasks under this StorageReference (in this example, there should be one)
        List tasks = storageRef.getActiveUploadTasks();
        if (tasks.size() > 0) {
            // Get the task monitoring the upload
            UploadTask task = (UploadTask) tasks.get(0);

            // Add new listeners to the task using an Activity scope
            task.addOnSuccessListener(this, new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
//                    handleSuccess(state); //call a user defined function to handle the event.

                }

            });
        }
    }


    public void initialize() {

        container = (LinearLayout) findViewById(R.id.container);
        editpassword = (EditText) findViewById(R.id.editablepassword);
        editemailid = (EditText) findViewById(R.id.editableemailid);
        editfirstname = (EditText) findViewById(R.id.editablefirstname);
        editlastname = (EditText) findViewById(R.id.editablelastname);
        editphnumber = (EditText) findViewById(R.id.editablephnumber);
        editusername = (EditText) findViewById(R.id.editableusername);
        editemailid.setEnabled(false);
        editusername.setEnabled(false);
        editpassword.setEnabled(false);
        editfirstname.setEnabled(false);
        editlastname.setEnabled(false);
        editphnumber.setEnabled(false);
        addcardholder = (EditText) findViewById(R.id.addnameoncard);
        addcardnumber = (EditText) findViewById(R.id.addeditcardnumber);
        addexpiry = (EditText) findViewById(R.id.addexpiry);
        addcvv = (EditText) findViewById(R.id.addcvv);
        addcardholder.setVisibility(View.GONE);
        addcardnumber.setVisibility(View.GONE);
        addcvv.setVisibility(View.GONE);
        addexpiry.setVisibility(View.GONE);
        nameoncard = (TextView) findViewById(R.id.nameoncard);
        expiry = (TextView) findViewById(R.id.expiry);
        numberoncard = (TextView) findViewById(R.id.addcardnumber);
        cvv = (TextView) findViewById(R.id.cvv);
        exactwarning = (TextView) findViewById(R.id.exactwarning);
        expiryformat = (TextView) findViewById(R.id.expiryformat);
        cardtype = (Spinner) findViewById(R.id.cardtype);
//        savecarddetails = (Button) findViewById(R.id.savecarddetails);
//        savecarddetails.setVisibility(View.GONE);
        addcard = (Button) findViewById(R.id.addcard);
        editpasswordbutton = (ImageView) findViewById(R.id.editpassword);
        editpasswordbutton.setVisibility(View.GONE);
        canceladdnewcard = (Button) findViewById(R.id.canceladdnewcard);
        canceladdnewcard.setVisibility(View.INVISIBLE);

//        addcard.setVisibility(View.VISIBLE);
        removecard = (Button) findViewById(R.id.removecard);

//        editcarddetails = (Button) findViewById(R.id.editcarddetails);
//        editcarddetails.setVisibility(View.VISIBLE);
        savenewcard = (Button) findViewById(R.id.savenewcard);
        savenewcard.setVisibility(View.INVISIBLE);
//        profilepic = (ImageView) findViewById(R.id.profilepicture);
//        profilepic.setClickable(false);
//        profilepic.setEnabled(false);
        cvvdefinition = (TextView) findViewById(R.id.cvvdefinition);
        cvvdefinition.setVisibility(View.INVISIBLE);
        newcard = new View[10];
        remove = new CheckBox[10];
        cardholder = new TextView[10];
        cardnumber = new TextView[10];
        editcardholder = new EditText[10];
        editcardnumber = new EditText[10];
        nameoncard.setVisibility(View.GONE);
        expiry.setVisibility(View.GONE);
        numberoncard.setVisibility(View.GONE);
        cvv.setVisibility(View.GONE);
        expiryformat.setVisibility(View.GONE);
        exactwarning.setVisibility(View.GONE);
        cardtype.setVisibility(View.GONE);
        saveprofile = (ImageView) findViewById(R.id.saveprofile);
        saveprofile.setVisibility(View.GONE);
        editprofile = (ImageView) findViewById(R.id.editprofile);
        remove_selected_card = (Button) findViewById(R.id.removeselectedcard);
        remove_selected_card.setVisibility(View.INVISIBLE);
        mInflater = (LayoutInflater) getApplicationContext()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        storage = FirebaseStorage.getInstance();
//        deletepropic = (Button) findViewById(R.id.deletepropic);
//        deletepropic.setVisibility(View.GONE);
        // Create a storage reference from our app
        storageRef = storage.getReferenceFromUrl("gs://garcondatabase.appspot.com");
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if(sharedpreferences.getString(UserName, null)==null)
        {
            Reinstallation reinstall = new Reinstallation();
            reinstall.execute();

        }
        else {
            editemailid.setText(sharedpreferences.getString(eMailkey, null));
            editusername.setText(sharedpreferences.getString(UserName, null));
            editfirstname.setText(sharedpreferences.getString(FirstName, null));
            editlastname.setText(sharedpreferences.getString(LastName, null));
            editphnumber.setText(sharedpreferences.getString(Phone, null));
            editpassword.setText("*************");
        }
        usercardnumber = sharedpreferences.getInt(usercardno, 0);
        for(int i = 0; i<usercardnumber ; i++)
        {
            cardUID[i] = sharedpreferences.getString(cardID[i], null);
            System.out.println("cardID in on create "+sharedpreferences.getString(cardID[i], null));
        }
        if(cardUID[0] !=null)
        {
            removecard.setVisibility(View.VISIBLE);
        }
        else {
            removecard.setVisibility(View.INVISIBLE);
        }
        System.out.println("usercardnumber in oncreate" +usercardnumber);
        for(int i = 0 ; i<usercardnumber ; i++) {
            newcard[i] = mInflater.inflate(R.layout.newcard, container, false);
            remove[i] = (CheckBox) newcard[i].findViewById(R.id.remove);
            cardholder[i] = (TextView) newcard[i].findViewById(R.id.cardholder);
            cardnumber[i] = (TextView) newcard[i].findViewById(R.id.cardnumber);
            editcardholder[i] = (EditText) newcard[i].findViewById(R.id.editablecardholder);
//                editcardholder[usercardnumber].setText(card.getCardHolder());
            editcardnumber[i] = (EditText) newcard[i].findViewById(R.id.editablecardnumber);
//                editcardnumber[usercardnumber].setText(card.getCardNumber().toString());
            remove[i].setVisibility(View.INVISIBLE);
            editcardholder[i].setEnabled(false);
            editcardnumber[i].setEnabled(false);
            editcardholder[i].setText(sharedpreferences.getString(CardHolder[i], null));
            editcardnumber[i].setText(sharedpreferences.getString(CardNumber[i], null));
            container.addView(newcard[i]);
        }

    }




    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ProfileSettings Page", // TODO: Define a title for the content shown.
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

    @Override
    protected void onStart() {
        super.onStart();  // Always call the superclass method first

        // The activity is either being restarted or started for the first time
        // so this is where we should make sure that GPS is enabled
        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
            // Create a dialog here that requests the user to enable GPS, and use an intent
            // with the android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS action
            // to take the user to the Settings screen to enable GPS when they click "OK"
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first

        // Activity being restarted from stopped state
    }


    public void cancelling(Boolean cancel)
    {
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
    }

    public Boolean iscardnumbervalid(String c )
    {
        int sum = 0;
        boolean alternate = false;
        for (int i = c.length() - 1; i >= 0; i--)
        {
            int n = Integer.parseInt(c.substring(i, i + 1));
            if (alternate)
            {
                n *= 2;
                if (n > 9)
                {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    public Boolean isexpiryvalid(String expiry)
    {
        Pattern phonePattern = Pattern.compile("[0-1]{1}[1-9]{1}-[0-9]{2}");
        Matcher phoneMatcher = phonePattern.matcher(expiry);
        return phoneMatcher.matches();

    }
    public Boolean iscvvvalid(String expiry)
    {
        Pattern phonePattern = Pattern.compile("[0-9]{3}");
        Matcher phoneMatcher = phonePattern.matcher(expiry);
        return phoneMatcher.matches();

    }
    private  boolean isPhoneNumberValid(String phoneNumber){

        Pattern phonePattern = Pattern.compile("[0-9]{3}-[0-9]{3}-[0-9]{4}");
        Matcher phoneMatcher = phonePattern.matcher(phoneNumber);

        return phoneMatcher.matches();
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
                                    editusername.setText(user.getUserName());
                                    editphnumber.setText(user.getPhoneNumber());
                                    editfirstname.setText(user.getFirstName());
                                    editlastname.setText(user.getLastName());
                                    editemailid.setText(user.geteMail());
                                    editpassword.setText("************");
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString(Phone, editphnumber.getText().toString());
                                    editor.putString(UserName, editusername.getText().toString());
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

