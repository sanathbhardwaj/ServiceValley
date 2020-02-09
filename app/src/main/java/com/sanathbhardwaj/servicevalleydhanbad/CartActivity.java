package com.sanathbhardwaj.servicevalleydhanbad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sanathbhardwaj.servicevalleydhanbad.Adapters.CartAdapter;
import com.sanathbhardwaj.servicevalleydhanbad.Adapters.categoryAdapter;
import com.sanathbhardwaj.servicevalleydhanbad.Models.CartModel;
import com.sanathbhardwaj.servicevalleydhanbad.Models.categoryModel;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements PaymentStatusListener {

    Button place_order;
    BottomSheetDialog bottomSheetDialog;
    DatabaseReference databaseReference;
    private RecyclerView cart;
    RelativeLayout add_payment;
    RadioGroup rg;
    TextView payment_method, quantity, priceperunit, price;
    Dialog myDialog;

    final String currentTime = new SimpleDateFormat("HHMMSS", Locale.getDefault()).format(new Date());

    RadioButton upi, paytm, cod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        myDialog = new Dialog(this);

        bottomSheetDialog = new BottomSheetDialog(this);
        place_order = findViewById(R.id.place_order);
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("user");
        add_payment = findViewById(R.id.add_payment);
        payment_method = findViewById(R.id.payment_method);
        price = findViewById(R.id.price);

        cart = findViewById(R.id.cart);
        cart.hasFixedSize();
        cart.setLayoutManager(new LinearLayoutManager(this));

        add_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPayment();
            }
        });
        payment_method.setText("COD");

        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String key = getIntent().getStringExtra("key");
                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final String user = uid.substring(0, Math.min(uid.length(), 3));
                final String partner = key.substring(0, Math.min(key.length(), 3));
                final DatabaseReference m = FirebaseDatabase.getInstance().getReference("users");

                m.child(uid).child("user").child("cart").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        m.child(key).child("user").child("orders").child("orderID").child(user+partner+currentTime).setValue(dataSnapshot.getValue());
                        m.child(key).child("user").child("orders").child("orderID").child(user+partner+currentTime).child("current_status").setValue("order placed");
                        m.child(key).child("partner").child("orders").child("orderID").child(user+partner+currentTime).setValue(dataSnapshot.getValue());
                        m.child(key).child("partner").child("orders").child("orderID").child(user+partner+currentTime).child("current_status").setValue("order placed");
                        m.child(key).child("partner").child("orders").child("orderID").child(user+partner+currentTime).child("payment").setValue("cod");
                        m.child(key).child("partner").child("orders").child("orderID").child(user+partner+currentTime).child("payment status").setValue("not paid");

                        Intent intent = new Intent(CartActivity.this, OrderConfirmation.class);
                        intent.putExtra("details", "Success");
                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


//                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        if (!dataSnapshot.hasChild("Complete Address")&&!(CartActivity.this).isFinishing()){
//                            showPopUp();
//                        }
//                        else{
//                            goForPayment();
//                        }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });

    }


    private void selectPayment() {

        bottomSheetDialog.setContentView(R.layout.payment_method);
        bottomSheetDialog.show();
        bottomSheetDialog.setCanceledOnTouchOutside(true);

        rg = bottomSheetDialog.findViewById(R.id.rg);

        upi = bottomSheetDialog.findViewById(R.id.upi);
        paytm = bottomSheetDialog.findViewById(R.id.payment);
        cod = bottomSheetDialog.findViewById(R.id.cod);


//        if (cod.isChecked()){
//            System.out.println("cod");
//        }
//        else if (paytm.isChecked()){
//            System.out.println("paytm");
//        }
//        else {
//            upi.isChecked();
//            System.out.println("upi");
//        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<CartModel> options =
                new FirebaseRecyclerOptions.Builder<CartModel>()
                        .setQuery(databaseReference.child("cart").child("products"), CartModel.class)
                        .build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CartModel, CartAdapter>(options) {
            @Override
            public CartAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())

                        .inflate(R.layout.single_cart_layout, parent, false);

                return new CartAdapter(view);
            }


            @Override
            protected void onBindViewHolder(@NonNull CartAdapter holder, final int position, @NonNull final CartModel model) {

                holder.setDetails(getApplicationContext(), model.getProduct_name(), model.getProduct_price(), model.getProduct_quantity());


                // Bind the image_details object to the BlogViewHolder


                final String services = getIntent().getStringExtra("services");
                final String key = getIntent().getStringExtra("key");
                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                quantity = holder.itemView.findViewById(R.id.quantity);
                priceperunit = holder.itemView.findViewById(R.id.priceperunit);
                price = holder.itemView.findViewById(R.id.price);


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


//                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
//                        Query query = rootRef.child("shops").child("category").child(services).child(key).child("category").orderByChild("name").equalTo(model.getName());
//                        ValueEventListener valueEventListener = new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                for(DataSnapshot ds : dataSnapshot.getChildren()) {
//                                    String parent = ds.getKey();
//                                    String key = getIntent().getStringExtra("key");
//                                    Intent intent = new Intent(category.this, ProductsDisplay.class);
//                                    intent.putExtra("parent",parent);
//                                    intent.putExtra("key", key);
//                                    intent.putExtra("services",services);
//                                    startActivity(intent);
//                                    Toast.makeText(category.this, parent, Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
////                                   Log.d(TAG, databaseError.getMessage());
//                            }
//                        };
//                        query.addListenerForSingleValueEvent(valueEventListener);


                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        return false;
                    }
                });
            }
        };

        firebaseRecyclerAdapter.startListening();
        cart.setAdapter(firebaseRecyclerAdapter);

    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        String TransactionRefId = FirebaseAuth.getInstance().getUid();

        long timeSeed = System.nanoTime();
        double randSeed = Math.random() * 1000;
        long midSeed = (long) (timeSeed * randSeed);
        String s = midSeed + "";
        String TransactionID = s.substring(0, 9);


        final EasyUpiPayment easyUpiPayment = new EasyUpiPayment.Builder()
                .with(CartActivity.this)
                .setPayeeVpa("braja6313@okhdfcbank")
                .setPayeeName("Service Valley")
                .setTransactionId(TransactionID)
                .setTransactionRefId(TransactionRefId+TransactionID)
                .setDescription("Paying to Service Valley")
                .setAmount("1.00")
                .build();



        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.cod:

                if (checked)
                    System.out.println("cod");
                payment_method.setText("cod");
                place_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final String key = getIntent().getStringExtra("key");
                        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        final String user = uid.substring(0, Math.min(uid.length(), 3));
                        final String partner = key.substring(0, Math.min(key.length(), 3));
                        final DatabaseReference m = FirebaseDatabase.getInstance().getReference("users");



                        m.child(uid).child("user").child("cart").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                m.child(key).child("partner").child("orders").child("orderID").child(user+partner+currentTime).setValue(dataSnapshot.getValue());
                                m.child(key).child("partner").child("orders").child("orderID").child(user+partner+currentTime).child("current_status").setValue("order placed");
                                m.child(key).child("partner").child("orders").child("orderID").child(user+partner+currentTime).child("payment").setValue("cod");
                                m.child(key).child("partner").child("orders").child("orderID").child(user+partner+currentTime).child("payment status").setValue("not paid");
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });

                    }
                });
                break;

            case R.id.paytm:

                if (checked)
                    System.out.println("paytm");
                payment_method.setText("paytm");

                place_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        System.out.println("paytm");
//                        m.child(key).child("partner").child("orders").child("orderID").child(uid+currentTime).child("status").setValue("order placed");
//
                    }
                });

                break;

            case R.id.upi:

                if (checked)
                    System.out.println("upi");
                payment_method.setText("upi");
                place_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        System.out.println("upi");
//                      m.child(key).child("partner").child("orders").child("orderID").child(uid+currentTime).child("status").setValue("order placed");
                        easyUpiPayment.startPayment();
                    }
                });
                break;
        }
        bottomSheetDialog.dismiss();
        easyUpiPayment.setPaymentStatusListener(this);
    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {

//        Intent intent = new Intent(CartActivity.this, OrderConfirmation.class);
//        startActivity(intent);
//        intent.putExtra("details", transactionDetails.toString());
//        finish();

    }

    @Override
    public void onTransactionSuccess() {

        final String key = getIntent().getStringExtra("key");
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String user = uid.substring(0, Math.min(uid.length(), 3));
        final String partner = key.substring(0, Math.min(key.length(), 3));
        final DatabaseReference m = FirebaseDatabase.getInstance().getReference("users");


        m.child(uid).child("user").child("cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                m.child(key).child("partner").child("orders").child("orderID").child(user+partner+currentTime).setValue(dataSnapshot.getValue());
                m.child(key).child("partner").child("orders").child("orderID").child(user+partner+currentTime).child("current_status").setValue("order placed");
                m.child(key).child("partner").child("orders").child("orderID").child(user+partner+currentTime).child("payment").setValue("upi");
                m.child(key).child("partner").child("orders").child("orderID").child(user+partner+currentTime).child("payment status").setValue("paid");

                Intent intent = new Intent(CartActivity.this, OrderConfirmation.class);
                intent.putExtra("details", "Success");
                startActivity(intent);
                finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onTransactionSubmitted() {

//        Intent intent = new Intent(CartActivity.this, OrderConfirmation.class);
//        startActivity(intent);
//        finish();
    }

    @Override
    public void onTransactionFailed() {

        Intent intent = new Intent(CartActivity.this, OrderConfirmation.class);
        startActivity(intent);
        intent.putExtra("details", "Failed");
        finish();
    }

    @Override
    public void onTransactionCancelled() {

        Intent intent = new Intent(CartActivity.this, OrderConfirmation.class);
        intent.putExtra("details", "Cancelled");
        startActivity(intent);
        finish();
    }

    @Override
    public void onAppNotFound() {

    }
}

