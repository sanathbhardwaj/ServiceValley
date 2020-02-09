package com.sanathbhardwaj.servicevalleydhanbad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sanathbhardwaj.servicevalleydhanbad.GoogleApi.CurrentLocation;
import com.sanathbhardwaj.servicevalleydhanbad.GoogleApi.LocationEnable;

public class GpsSelector extends AppCompatActivity {

    Button location_choose;
    private DatabaseReference mDatabase;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_selector);
        location_choose = findViewById(R.id.location_choose);
        password = findViewById(R.id.password);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        location_choose.setEnabled(false);
        location_choose.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryLight, null));
        location_choose.setBackgroundResource(R.drawable.button_drwable_light);

        password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if (s.toString().length()>5) {
                    location_choose.setEnabled(true);
                    location_choose.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null));
                    location_choose.setBackgroundResource(R.drawable.button_drwable);
                } else if(s.toString().length()<6) {
                    location_choose.setEnabled(false);
                    location_choose.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryLight, null));
                    location_choose.setBackgroundResource(R.drawable.button_drwable_light);
                } else{
                    location_choose.setEnabled(false);
                    location_choose.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryLight, null));
                    location_choose.setBackgroundResource(R.drawable.button_drwable_light);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        location_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(GpsSelector.this)
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("user").child("password").setValue(password.getText().toString());
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("user").child("Name").setValue(null);
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("user").child("Address").setValue(null);
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("user").child("LatLog").setValue(null);
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("user").child("phone number").setValue(1234567890);
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("user").child("Orders Active").setValue(null);
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("user").child("Orders Cancelled").setValue(null);
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("user").child("All Orders").setValue(null);
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("user").child("Total Amount Spent").setValue(null);
                                Intent intent = new Intent(GpsSelector.this, CurrentLocation.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                                if (response.isPermanentlyDenied()) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(GpsSelector.this);
                                    builder.setTitle("Permission is Required")
                                            .setMessage("Permission is not granted. You need to go to settings and allow the required permission")
                                            .setNegativeButton("Cancel", null)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent();
                                                    intent.setData(Uri.fromParts("package", getPackageName(), null));

                                                }
                                            })
                                            .show();
                                }

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                                token.continuePermissionRequest();

                            }
                        })
                        .check();

            }
        });


    }
}
