package com.sanathbhardwaj.servicevalleydhanbad;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.sanathbhardwaj.servicevalleydhanbad.AllServices.Vegetables;
import com.sanathbhardwaj.servicevalleydhanbad.AllServices.services;
import com.sanathbhardwaj.servicevalleydhanbad.Authentication.PhoneEntry;
import com.sanathbhardwaj.servicevalleydhanbad.GoogleApi.LocationEnable;
import com.sanathbhardwaj.servicevalleydhanbad.bottom_nav_activities.AccountActivity;
import com.sanathbhardwaj.servicevalleydhanbad.bottom_nav_activities.SVPartnerActivity;
import com.sanathbhardwaj.servicevalleydhanbad.bottom_nav_activities.SearchActivity;


public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    String uid = "";


    RelativeLayout locationPicker;
    TextView daily_grocery_tv, plumber_tv, photographer_tv, electrician_tv, vegetables_tv, fruits_tv, meat_tv;
    TextView medicine_tv, books_tv, doctor_tv;
    TextView address;
    ImageView daily_grocery_img, plumber_img, photographer_img, electrician_img, vegetables_img, fruits_img, meat_img;
    ImageView medicine_img, books_img, doctor_img;
    ImageView post1, post2, post3, post4, post5;

    RelativeLayout daily_grocery, plumber, photographer, electrician, vegetables, fruits, meat;
    RelativeLayout medicine, books, doctor;

    RelativeLayout refresh;

    ImageView imageView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference top_posters = db.collection("All Posters").document("Image Sliders");
    DocumentReference all_services = db.collection("All Services").document("services_list");
    DocumentReference all_services_icons = db.collection("All Services").document("service items icon list");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isNetworkAvailable()){
            setContentView(R.layout.activity_main);

            if (FirebaseAuth.getInstance().getCurrentUser()==null){
                Intent intent = new Intent(MainActivity.this, LoginSignUp.class);
                startActivity(intent);
                finish();
                uid = "";
            }
            else {
                uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            }

            mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("user");
            address = findViewById(R.id.address);
            address.setText("Locating...");
            // Read from the database
            mDatabase.child("Address").child("Locality").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    if (value!=null){
                        address.setText(value);
                    }
                    else {
                        address.setText("Select Location");
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value

                }
            });

//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                String value = dataSnapshot.getValue().toString();

//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        Query query = reference
//                .child("User IDs")
//                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.getChildrenCount()>0) {
//                    //username found
//
//                }else{
//                    Intent intent = new Intent(MainActivity.this, CurrentLocation.class);
//                    startActivity(intent);
//                    finish();
//                }
//
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


            //all services text view
            daily_grocery_tv = findViewById(R.id.daily_grocery_tv);
            plumber_tv = findViewById(R.id.plumber_tv);
            photographer_tv = findViewById(R.id.photographer_tv);
            electrician_tv = findViewById(R.id.electrician_tv);
            vegetables_tv = findViewById(R.id.vegetables_tv);
            fruits_tv = findViewById(R.id.fruits_tv);
            meat_tv = findViewById(R.id.meat_tv);
            medicine_tv = findViewById(R.id.medicine_tv);
            books_tv = findViewById(R.id.books_tv);
            doctor_tv = findViewById(R.id.doctor_tv);

            //all services image view
            daily_grocery_img = findViewById(R.id.daily_grocery_img);
            plumber_img = findViewById(R.id.plumber_img);
            photographer_img = findViewById(R.id.photographer_img);
            electrician_img = findViewById(R.id.electrician_img);
            vegetables_img = findViewById(R.id.vegetables_img);
            fruits_img = findViewById(R.id.fruits_img);
            meat_img = findViewById(R.id.meat_img);
            medicine_img = findViewById(R.id.medicine_img);
            books_img = findViewById(R.id.books_img);
            doctor_img = findViewById(R.id.doctor_img);

            daily_grocery = findViewById(R.id.daily_grocery);
            plumber = findViewById(R.id.plumber);
            photographer = findViewById(R.id.photographer);
            electrician = findViewById(R.id.electrician);
            vegetables = findViewById(R.id.vegetables);
            fruits = findViewById(R.id.fruits);
            meat = findViewById(R.id.meat);
            medicine = findViewById(R.id.medicine);
            books = findViewById(R.id.books);
            doctor = findViewById(R.id.doctor);

            //top posters
            post1 = findViewById(R.id.post1);
            post2 = findViewById(R.id.post2);
            post3 = findViewById(R.id.post3);
            post4 = findViewById(R.id.post4);
            post5 = findViewById(R.id.post5);

            top_posters.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@androidx.annotation.Nullable DocumentSnapshot snapshot,
                                    @androidx.annotation.Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("FirestoreDemo", "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null && snapshot.exists()) {
                        Log.d("FirestoreDemo", "Current data: " + snapshot.getData());


                        Glide.with(getApplicationContext()).load(snapshot.getData().get("post1").toString()).into(post1);
                        Glide.with(getApplicationContext()).load(snapshot.getData().get("post1").toString()).into(post2);
                        Glide.with(getApplicationContext()).load(snapshot.getData().get("post1").toString()).into(post3);
                        Glide.with(getApplicationContext()).load(snapshot.getData().get("post1").toString()).into(post4);
                        Glide.with(getApplicationContext()).load(snapshot.getData().get("post1").toString()).into(post5);



                    } else {
                        Log.d("FirestoreDemo", "Current data: null");
                    }
                }
            });

            all_services.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@androidx.annotation.Nullable DocumentSnapshot snapshot,
                                    @androidx.annotation.Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("FirestoreDemo", "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null && snapshot.exists()) {
                        Log.d("FirestoreDemo", "Current data: " + snapshot.getData());

                        final String DailyGrocery = snapshot.getData().get("Daily Grocery").toString();
                        final String Plumber = snapshot.getData().get("Plumber").toString();
                        final String Photographer = snapshot.getData().get("Photographer").toString();
                        final String Electrician = snapshot.getData().get("Electrician").toString();
                        final String Vegetables = snapshot.getData().get("Vegetables").toString();
                        final String Fruits = snapshot.getData().get("Fruits").toString();
                        final String MeatAndFish = snapshot.getData().get("Meat and Fish").toString();
                        final String MedicineSupplies = snapshot.getData().get("Medicine Supplies").toString();
                        final String BooksAndStationary = snapshot.getData().get("Books and Stationary").toString();
                        final String Doctor = snapshot.getData().get("Doctor").toString();


                        daily_grocery_tv.setText(DailyGrocery);
                        plumber_tv.setText(Plumber);
                        photographer_tv.setText(Photographer);
                        electrician_tv.setText(Electrician);
                        vegetables_tv.setText(Vegetables);
                        fruits_tv.setText(Fruits);
                        meat_tv.setText(MeatAndFish);
                        medicine_tv.setText(MedicineSupplies);
                        books_tv.setText(BooksAndStationary);
                        doctor_tv.setText(Doctor);

                        final String City = getIntent().getStringExtra("City");


                        daily_grocery.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(MainActivity.this, services.class);
                                intent.putExtra("services",DailyGrocery);
                                intent.putExtra("city", City);
                                startActivity(intent);

                            }
                        });


                        plumber.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

//                                Intent intent = new Intent(MainActivity.this, services.class);
//                                intent.putExtra("services",Plumber);
//                                startActivity(intent);

                            }
                        });
                        photographer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

//                                Intent intent = new Intent(MainActivity.this, services.class);
//                                intent.putExtra("services",Photographer);
//                                startActivity(intent);

                            }
                        });
                        electrician.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

//                                Intent intent = new Intent(MainActivity.this, services.class);
//                                intent.putExtra("services",Electrician);
//                                startActivity(intent);

                            }
                        });
                        vegetables.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(MainActivity.this, services.class);
                                intent.putExtra("services",Vegetables);
                                intent.putExtra("city", City);
                                startActivity(intent);

                            }
                        });
                        fruits.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(MainActivity.this, services.class);
                                intent.putExtra("services",Fruits);
                                startActivity(intent);

                            }
                        });
                        meat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(MainActivity.this, services.class);
                                intent.putExtra("services",MeatAndFish);
                                intent.putExtra("city", City);
                                startActivity(intent);

                            }
                        });
                        medicine.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(MainActivity.this, services.class);
                                intent.putExtra("services",MedicineSupplies);
                                intent.putExtra("city", City);
                                startActivity(intent);

                            }
                        });
                        books.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(MainActivity.this, services.class);
                                intent.putExtra("services",BooksAndStationary);
                                intent.putExtra("city", City);
                                startActivity(intent);

                            }
                        });
                        doctor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

//                                Intent intent = new Intent(MainActivity.this, services.class);
//                                intent.putExtra("services",Doctor);
//                                startActivity(intent);

                            }
                        });



                    } else {
                        Log.d("FirestoreDemo", "Current data: null");
                    }
                }
            });

            all_services_icons.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@androidx.annotation.Nullable DocumentSnapshot snapshot,
                                    @androidx.annotation.Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("FirestoreDemo", "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null && snapshot.exists()) {
                        Log.d("FirestoreDemo", "Current data: " + snapshot.getData());

                        Glide.with(getApplicationContext()).load(snapshot.getData().get("Daily Grocery").toString()).into(daily_grocery_img);
                        Glide.with(getApplicationContext()).load(snapshot.getData().get("Plumber").toString()).into(plumber_img);
                        Glide.with(getApplicationContext()).load(snapshot.getData().get("Photographer").toString()).into(photographer_img);
                        Glide.with(getApplicationContext()).load(snapshot.getData().get("Electrician").toString()).into(electrician_img);
                        Glide.with(getApplicationContext()).load(snapshot.getData().get("Vegetables").toString()).into(vegetables_img);
                        Glide.with(getApplicationContext()).load(snapshot.getData().get("Fruits").toString()).into(fruits_img);
                        Glide.with(getApplicationContext()).load(snapshot.getData().get("Meat and Fish").toString()).into(meat_img);
                        Glide.with(getApplicationContext()).load(snapshot.getData().get("Medicine Supplies").toString()).into(medicine_img);
                        Glide.with(getApplicationContext()).load(snapshot.getData().get("Books and Stationary").toString()).into(books_img);
                        Glide.with(getApplicationContext()).load(snapshot.getData().get("Doctor").toString()).into(doctor_img);

                    } else {
                        Log.d("FirestoreDemo", "Current data: null");
                    }
                }
            });

            imageView = findViewById(R.id.help);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, HelpCentre.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view_menu);
            bottomNavigationView.setSelectedItemId(R.id.Home);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.search:
                            startActivity(new Intent(getApplicationContext()
                                    , SearchActivity.class));
                            finish();
                            overridePendingTransition(0,0);
                            return true;
                        case R.id.SVPartner:
                            startActivity(new Intent(getApplicationContext()
                                    , SVPartnerActivity.class));
                            finish();
                            overridePendingTransition(0,0);
                            return true;
                        case R.id.Account:
                            startActivity(new Intent(getApplicationContext()
                                    , AccountActivity.class));
                            finish();
                            overridePendingTransition(0,0);
                            return true;
                        case R.id.Home:
                            return true;
                    }
                    return false;
                }
            });

            locationPicker = findViewById(R.id.location_picker);
            locationPicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(MainActivity.this, LocationEnable.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                }
            });

        }
        else {
            setContentView(R.layout.no_internet);
            refresh = findViewById(R.id.refresh);
            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, MainActivity.class);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                }
            });
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}