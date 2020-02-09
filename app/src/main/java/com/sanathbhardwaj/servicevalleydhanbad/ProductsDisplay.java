package com.sanathbhardwaj.servicevalleydhanbad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sanathbhardwaj.servicevalleydhanbad.Adapters.ProductAdapter;
import com.sanathbhardwaj.servicevalleydhanbad.Models.ProductModel;

public class ProductsDisplay extends AppCompatActivity {

    private RecyclerView product_display;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    TextView no_items, bill, t, priceCounter;
    RelativeLayout go_to_cart;
    ImageView backarrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_products_display);
        String services = getIntent().getStringExtra("services");
        String key = getIntent().getStringExtra("key");
        String parent = getIntent().getStringExtra("parent");

        no_items = findViewById(R.id.no_items);
        bill = findViewById(R.id.priceCounter);
        go_to_cart = findViewById(R.id.go_to_cart);
        t = findViewById(R.id.t);
        backarrow = findViewById(R.id.backarrow);
        priceCounter = findViewById(R.id.priceCounter);

        product_display = findViewById(R.id.product_display);
        product_display.hasFixedSize();
        product_display.setLayoutManager(new LinearLayoutManager(this));

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RecyclerView.ItemDecoration divider = new RecyclerView.ItemDecoration(){
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.setEmpty();
            }
        };
        product_display.addItemDecoration(divider);
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("shops").child("category").child(services).child(key).child("category").child(parent).child("products");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ProductModel> options =
                new FirebaseRecyclerOptions.Builder<ProductModel>()
                        .setQuery(mDatabaseReference, ProductModel.class)
                        .build();

        final FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ProductModel, ProductAdapter>(options) {
            @Override
            public ProductAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_product_display, parent, false);

                return new ProductAdapter(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull final ProductAdapter holder, final int position, @NonNull final ProductModel model) {

                holder.setDetails(getApplicationContext(), model.getProduct_name(), model.getProduct_price(), model.getProduct_image());
                final String key = getIntent().getStringExtra("key");
                String services = getIntent().getStringExtra("services");
                String parent = getIntent().getStringExtra("parent");
                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                Query query = rootRef.child("shops").child("category").child(services).child(key).child("category").child(parent).child("products").orderByChild("product_name").equalTo(model.getProduct_name());
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            final String productID = ds.getKey();
//                            Toast.makeText(ProductsDisplay.this, ""+productID, Toast.LENGTH_SHORT).show();


                            final Button add = holder.itemView.findViewById(R.id.add);
                            final LinearLayout add_sub = holder.itemView.findViewById(R.id.add_sub);
                            final TextView price = holder.itemView.findViewById(R.id.price);
                            final TextView name = holder.itemView.findViewById(R.id.name);



                            final TextView reduce = holder.itemView.findViewById(R.id.reduce);
                            final TextView quantity = holder.itemView.findViewById(R.id.quantity);
                            final TextView increase = holder.itemView.findViewById(R.id.increase);
                            final int[] counter = {0};
                            final int[] price_product = {0};
                            final int[] price_per_unit = {0};
                            final DatabaseReference mCartReference = FirebaseDatabase.getInstance().getReference("users");
                            mCartReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("user");
                            price_per_unit[0] = Integer.parseInt(price.getText().toString());



//
//                    }
//                    private void goToCart() {
//
                            go_to_cart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ProductsDisplay.this, CartActivity.class);
                                    String key = getIntent().getStringExtra("key");
                                    intent.putExtra("key",key);
                                    intent.putExtra("PID", productID);
                                    startActivity(intent);
                                }
                            });

                            add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    counter[0]++;

                                    price_product[0] = counter[0]*price_per_unit[0];
                                    add.setVisibility(View.INVISIBLE);
                                    add_sub.setVisibility(View.VISIBLE);
                                    quantity.setText(String.valueOf(counter[0]));
                                    priceCounter.setText(String.valueOf(price_product[0]));
                                    mCartReference.child(uid).child("user").child("cart").child("products").child(productID).child("Product_quantity").setValue(String.valueOf(counter[0]));
                                    mCartReference.child(uid).child("user").child("cart").child("products").child(productID).child("Product_price").setValue(price.getText().toString());
                                    mCartReference.child(uid).child("user").child("cart").child("products").child(productID).child("Product_name").setValue(name.getText().toString());
                                }
                            });

                            increase.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (counter[0]==0){
                                        add.setVisibility(View.VISIBLE);
                                        add_sub.setVisibility(View.INVISIBLE);

                                    }
                                    else if(counter[0]<=0){
                                        add.setVisibility(View.VISIBLE);
                                        add_sub.setVisibility(View.INVISIBLE);
                                        counter[0]=counter[0];
                                        price_product[0] = counter[0]*price_per_unit[0];
                                    }
                                    else {
                                        add.setVisibility(View.INVISIBLE);
                                        add_sub.setVisibility(View.VISIBLE);
                                        counter[0]++;
                                        price_product[0] = counter[0]*price_per_unit[0];
                                    }
                                    quantity.setText(String.valueOf(counter[0]));
                                    priceCounter.setText(String.valueOf(price_product[0]));

                                    mCartReference.child(uid).child("user").child("cart").child("products").child(productID).child("Product_quantity").setValue(String.valueOf(counter[0]));
                                    mCartReference.child(uid).child("user").child("cart").child("products").child(productID).child("Product_price").setValue(price.getText().toString());
                                    mCartReference.child(uid).child("user").child("cart").child("products").child(productID).child("Product_name").setValue(name.getText().toString());
                                    mCartReference.child(uid).child("user").child("cart").child("current_status").setValue("added to cart");

                                }
                            });
                            reduce.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (counter[0]==1){
                                        counter[0]--;
                                        price_product[0] = counter[0]*price_per_unit[0];
                                        add.setVisibility(View.VISIBLE);
                                        add_sub.setVisibility(View.INVISIBLE);
                                        priceCounter.setText(String.valueOf(price_product[0]));

                                    }
                                    else if(counter[0]<=0){
                                        add.setVisibility(View.VISIBLE);
                                        add_sub.setVisibility(View.INVISIBLE);
                                        counter[0]=counter[0];
                                        price_product[0] = counter[0]*price_per_unit[0];
                                    }
                                    else {
                                        add.setVisibility(View.INVISIBLE);
                                        add_sub.setVisibility(View.VISIBLE);
                                        counter[0]--;
                                        price_product[0] = counter[0]*price_per_unit[0];
                                    }
                                    if (counter[0]==0){
                                        System.out.println("called");
                                        DatabaseReference clearcart = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("user");
                                        clearcart.child("cart").child("products").child(productID).removeValue();
                                    }
                                    if (counter[0]!=0) {

                                        quantity.setText(String.valueOf(counter[0]));
                                        priceCounter.setText(String.valueOf(price_product[0]));


                                        mCartReference.child(uid).child("user").child("cart").child("products").child(productID).child("Product_quantity").setValue(String.valueOf(counter[0]));
                                        mCartReference.child(uid).child("user").child("cart").child("products").child(productID).child("Product_price").setValue(price.getText().toString());
                                        mCartReference.child(uid).child("user").child("cart").child("products").child(productID).child("Product_name").setValue(name.getText().toString());
                                        mCartReference.child(uid).child("user").child("cart").child("current_status").setValue("added to cart");

                                    }

                                }

                            });

                            DatabaseReference mcart = FirebaseDatabase.getInstance().getReference("users");

                            mcart.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("user").child("cart").child("products").child(productID).child("Product_quantity").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                    if (dataSnapshot.getValue()!=null){

                                        if (dataSnapshot.getValue().toString().equals("0")) {

                                            add.setVisibility(View.VISIBLE);
                                            add_sub.setVisibility(View.INVISIBLE);


                                         }
                                        else {

                                        add.setVisibility(View.INVISIBLE);
                                        add_sub.setVisibility(View.VISIBLE);
                                        counter[0] = Integer.parseInt(dataSnapshot.getValue().toString());
                                        price_product[0] = counter[0]*price_per_unit[0];
                                        quantity.setText(dataSnapshot.getValue().toString());
                                        priceCounter.setText(String.valueOf(price_product[0]));

                                        }

                                    }

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


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        return false;
                    }
                });



//                final ElegantNumberButton button = holder.itemView.findViewById(R.id.button);
//                button.setRange(0,10);
//
//                button.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
//                    @Override
//                    public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
//                        counter=counter+newValue-oldValue;
//                        int number = Integer.parseInt(price.getText().toString());
//                        priceCounter = priceCounter+((newValue-oldValue)*number);
//                        no_items.setText(String.valueOf(counter));
//                        bill.setText(String.valueOf(priceCounter));
//
//
//
//
//

//
//
//
//                    }
//                });







                // Bind the image_details object to the BlogViewHolder

//                TextView tv = holder.itemView.findViewById(R.id.name);
//                final TextView p = holder.itemView.findViewById(R.id.price);
//                tv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(ProductsDisplay.this, "Badhai ho bhaiya"+position, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(ProductsDisplay.this, "Badhai ho bhaiya"+position, Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//               holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });
//                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View view) {
//                        return false;
//                    }
//                });

            }
        };
        firebaseRecyclerAdapter.startListening();
        product_display.setAdapter(firebaseRecyclerAdapter);
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        Parcelable recyclerViewState;
//        recyclerViewState = product_display.getLayoutManager().onSaveInstanceState();//save
//        product_display.getLayoutManager().onRestoreInstanceState(recyclerViewState);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        Parcelable recyclerViewState;
//        recyclerViewState = product_display.getLayoutManager().onSaveInstanceState();//save
//        product_display.getLayoutManager().onRestoreInstanceState(recyclerViewState);
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//
//        Parcelable recyclerViewState;
//        recyclerViewState = product_display.getLayoutManager().onSaveInstanceState();//save
//        product_display.getLayoutManager().onRestoreInstanceState(recyclerViewState);
//    }


    private void goToCart() {
        Intent intent = new Intent(ProductsDisplay.this, CartActivity.class);

        startActivity(intent);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}

