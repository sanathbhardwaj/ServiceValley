package com.sanathbhardwaj.servicevalleydhanbad.AllServices;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sanathbhardwaj.servicevalleydhanbad.category;
import com.sanathbhardwaj.servicevalleydhanbad.Adapters.MyAdapter;
import com.sanathbhardwaj.servicevalleydhanbad.Models.Profile;
import com.sanathbhardwaj.servicevalleydhanbad.R;

public class services extends AppCompatActivity {

    private RecyclerView shops, InActive;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    TextView title, l, distant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        ImageView imageView = findViewById(R.id.backarrow);

        title = findViewById(R.id.title);

        shops = findViewById(R.id.shops);
        shops.hasFixedSize();
        shops.setLayoutManager(new LinearLayoutManager(this));

        InActive = findViewById(R.id.InActive);
        InActive.hasFixedSize();
        InActive.setLayoutManager(new LinearLayoutManager(this));
        l = findViewById(R.id.l);
        distant = findViewById(R.id.distant);



        String services = getIntent().getStringExtra("services");
        title.setText(services);



        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("shops").child("category").child(services);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
        @Override
        protected void onStart() {
            super.onStart();


            DatabaseReference m = FirebaseDatabase.getInstance().getReference("users");

            m.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("user").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String city = dataSnapshot.child("Address").child("City").getValue().toString();

                    final FirebaseRecyclerOptions<Profile> options =
                            new FirebaseRecyclerOptions.Builder<Profile>()
                                    .setQuery(mDatabaseReference.orderByChild("status_location").equalTo("Active_"+city), Profile.class)
                                    .build();



                    final FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Profile, MyAdapter>(options) {
                        @Override
                        public MyAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.shop_display, parent, false);

                            return new MyAdapter(view);
                        }

                        @Override
                        protected void onBindViewHolder(@NonNull final MyAdapter holder, final int position, @NonNull final Profile model) {


                            final String services = getIntent().getStringExtra("services");
                            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                            Query query = rootRef.child("shops").child("category").child(services).orderByChild("shop_name").equalTo(model.getShop_name());
                            ValueEventListener valueEventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                        final String key = ds.getKey();
                                        DatabaseReference m;

                                        m = FirebaseDatabase.getInstance().getReference("users");
                                        m.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                Double lat1 = (Double) dataSnapshot.child(key).child("partner").child("LatLog").child("latitude").getValue();
                                                Double log1 = (Double) dataSnapshot.child(key).child("partner").child("LatLog").child("longitude").getValue();
                                                Double lat2 = (Double) dataSnapshot.child(uid).child("user").child("LatLog").child("latitude").getValue();
                                                Double log2 = (Double) dataSnapshot.child(uid).child("user").child("LatLog").child("longitude").getValue();
                                                holder.findDist(String.valueOf((int)getKmFromLatLong(lat1, log1, lat2, log2)));


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                                   Log.d(TAG, databaseError.getMessage());
                                }
                            };
                            query.addListenerForSingleValueEvent(valueEventListener);

                            // Bind the image_details object to the BlogViewHolder
//                   holder.itemView.setEnabled(false);
//                   holder.itemView.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryLight, null));


//                    query.orderByChild("status").equalTo("InActive").addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                            holder.itemView.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryLight, null));
//                            holder.itemView.setEnabled(false);
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    final String services = getIntent().getStringExtra("services");

                                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                    Query query = rootRef.child("shops").child("category").child(services).orderByChild("shop_name").equalTo(model.getShop_name());
                                    ValueEventListener valueEventListener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                                final String key = ds.getKey();
                                                Intent intent = new Intent(com.sanathbhardwaj.servicevalleydhanbad.AllServices.services.this, category.class);
                                                intent.putExtra("key",key);
                                                intent.putExtra("services",services);
                                                System.out.println(key);
                                                startActivity(intent);

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                                   Log.d(TAG, databaseError.getMessage());
                                        }
                                    };
                                    query.addListenerForSingleValueEvent(valueEventListener);


                                }
                            });
                            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View view) {
                                    return false;
                                }
                            });

                            holder.setDetails(getApplicationContext(), model.getShop_name(), model.getSlogan(), model.getBanner());
                        }
                    };


                    firebaseRecyclerAdapter.startListening();
                    shops.setAdapter(firebaseRecyclerAdapter);




                    FirebaseRecyclerOptions<Profile> Inactive =
                            new FirebaseRecyclerOptions.Builder<Profile>()
                                    .setQuery(mDatabaseReference.orderByChild("status_location").equalTo("InActive_"+city), Profile.class)
                                    .build();

                    FirebaseRecyclerAdapter FirebaseRecyclerAdapterInActive = new FirebaseRecyclerAdapter<Profile, MyAdapter>(Inactive) {
                        @Override
                        public MyAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.in_active_shop, parent, false);
                            return new MyAdapter(view);
                        }

                        @Override
                        protected void onBindViewHolder(@NonNull final MyAdapter holder, final int position, @NonNull final Profile model) {




                            // Bind the image_details object to the BlogViewHolder
                            holder.itemView.setEnabled(false);
                            holder.itemView.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryLight, null));


//                    query.orderByChild("status").equalTo("InActive").addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                            holder.itemView.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryLight, null));
//                            holder.itemView.setEnabled(false);
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });

                            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View view) {
                                    return false;
                                }
                            });

                            holder.setDetails(getApplicationContext(), model.getShop_name(), model.getSlogan(), model.getBanner());
                        }
                    };

                    FirebaseRecyclerAdapterInActive.startListening();
                    InActive.setAdapter(FirebaseRecyclerAdapterInActive);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

    }


    public static float getKmFromLatLong(Double lat1, Double lng1, Double lat2, Double lng2){
        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lng1);
        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lng2);
        float distanceInMeters = loc1.distanceTo(loc2);
        return distanceInMeters/1000;
    }

    public float getTime(LatLng user, LatLng shop){

        float time=0;


        return time;
    }
}
