package com.garcon.garcon;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garcon.Constants.Constants;
import com.garcon.Models.Receipt;
import com.garcon.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReceiptActivity extends Activity {
    ArrayList<Integer> values;
    String billingCard;
    static LinearLayout layout;
    static Bitmap map;
    static String currentDateandTime;
    uploadReceipt upload = new uploadReceipt();
    StorageMetadata metadata;
    String card[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        values = getIntent().getIntegerArrayListExtra("values");
        billingCard = getIntent().getStringExtra("billingCard");
        layout=(LinearLayout)findViewById(R.id.layout);


        for(int i = 0; i < values.size(); i++)
            Log.i("information ",i+" "+values.get(i));

        TextView subtotal = (TextView) findViewById(R.id.subtotal_final);
        subtotal.setText(String.valueOf(values.get(0)));

        TextView tip = (TextView) findViewById(R.id.tip_final);
        tip.setText(String.valueOf(values.get(1)));

        TextView total = (TextView) findViewById(R.id.total_final);
        total.setText(String.valueOf(values.get(2)));

        TextView datetime = (TextView) findViewById(R.id.datetime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        currentDateandTime = sdf.format(new Date());
        datetime.setText(currentDateandTime);
        System.out.println("billling card" +billingCard);
        card = billingCard.split("\\,");
        TextView cardType = (TextView) findViewById(R.id.cardtypeused);
        cardType.setText(card[0]);

        TextView cardNumber = (TextView) findViewById(R.id.cardnoused);
        cardNumber.setText(card[2]);

        TextView cardHolder = (TextView) findViewById(R.id.cardholderused);
        cardHolder.setText(card[4]);

        Button ok = (Button) findViewById(R.id.okay);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConvertToBitmap(layout);
                upload.execute();
                Intent intent = new Intent(getApplicationContext(), homeactivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    class uploadReceipt extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            final FirebaseAuth mAuth = FirebaseAuth.getInstance();
//            FirebaseStorage storage;
//            StorageReference storageRef;
//            StorageReference ReceiptsRef;
//            final String[] dwndurl = new String[1];

            final DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL_USERS);

            try {
//                storage = FirebaseStorage.getInstance();
//                // Create the file metadata
//                metadata = new StorageMetadata.Builder()
//                        .setContentType("image/jpeg")
//                        .build();
//                storageRef = storage.getReferenceFromUrl("gs://garcondatabase.appspot.com");
//                ReceiptsRef = storageRef.child("UserReceipts").child(mAuth.getCurrentUser().getUid());
//
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                map.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                byte[] data = baos.toByteArray();
//
//                UploadTask uploadTask = ReceiptsRef.child(currentDateandTime).putBytes(data, metadata);
//                uploadTask.addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Handle unsuccessful uploads
//                    }
//                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                        dwndurl[0] = downloadUrl.toString();
                Receipt receipt = new Receipt(String.valueOf(values.get(0)), String.valueOf(values.get(1)), String.valueOf(values.get(2)), currentDateandTime, card[0], card[2], card[4]);
                ref.child(mAuth.getCurrentUser().getUid()).child("Receipt").child(currentDateandTime).setValue(receipt);
            }
//                });

//            }
            catch(Exception e)
            {

            }
            return null;
        }
    }
    protected void ConvertToBitmap(LinearLayout layout) {


        layout.setDrawingCacheEnabled(true);

        layout.buildDrawingCache();

        map=layout.getDrawingCache();


    }
}
