package com.sanathbhardwaj.servicevalleydhanbad;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sanathbhardwaj.servicevalleydhanbad.Adapters.categoryAdapter;
import com.sanathbhardwaj.servicevalleydhanbad.Models.categoryModel;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class category extends AppCompatActivity {

    private RecyclerView category;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    BottomSheetDialog bottomSheetDialog;
    ImageView backarrow, list;
    private Uri filePath;
    Button place_order;
    private static final int PICK_IMAGE_REQUEST = 234;

    final String currentTime = new SimpleDateFormat("HHMMSS", Locale.getDefault()).format(new Date());
    final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    final String user = uid.substring(0, Math.min(uid.length(), 3));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        bottomSheetDialog = new BottomSheetDialog(category.this);
        String key = getIntent().getStringExtra("key");
        String services = getIntent().getStringExtra("services");
        category = findViewById(R.id.category);
        backarrow = findViewById(R.id.backarrow);
        category.hasFixedSize();
        list = findViewById(R.id.list);
        place_order = findViewById(R.id.place_order);
        category.setLayoutManager(new GridLayoutManager(this,3));

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("shops").child("category").child(services).child(key).child("category");
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String key = getIntent().getStringExtra("key");
                final String user = uid.substring(0, Math.min(uid.length(), 3));
                final String partner = key.substring(0, Math.min(key.length(), 3));
                final DatabaseReference m = FirebaseDatabase.getInstance().getReference("users");

                if (filePath!=null){


                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    storageRef.child("orders/"+uid+"/"+user+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {


                            m.child(key).child("partner").child("orders").child("orderID").child(user+partner+currentTime).child("image").setValue(uri.toString());
                            m.child(key).child("partner").child("orders").child("orderID").child(user+partner+currentTime).child("current_status").setValue("order placed");
                            m.child(key).child("partner").child("orders").child("orderID").child(user+partner+currentTime).child("payment").setValue("cod");
                            m.child(key).child("partner").child("orders").child("orderID").child(user+partner+currentTime).child("payment status").setValue("not paid");


//                productDatabase.child("category").child(my_spinner).child("products").child(productname).child("product_image").setValue(uri.toString());
                            // Got the download URL for 'users/me/profile.png'

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
//                Toast.makeText(AddProduct.this, "Some error occurred. Try Again!", Toast.LENGTH_SHORT).show();
//                System.out.println(productname);

                        }
                    });
                }


//                m.child(uid).child("user").child("cart").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        m.child(key).child("partner").child("orders").child("orderID").child(user+partner+currentTime).setValue(dataSnapshot.getValue());
//                        m.child(key).child("partner").child("orders").child("orderID").child(user+partner+currentTime).child("current_status").setValue("order placed");
//                        m.child(key).child("partner").child("orders").child("orderID").child(user+partner+currentTime).child("payment").setValue("cod");
//                        m.child(key).child("partner").child("orders").child("orderID").child(user+partner+currentTime).child("payment status").setValue("not paid");
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });

            }
        });
    }

    private void getDownloadUrl() {

    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                list.setImageBitmap(bitmap);
                uploadFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
           final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference storageRef = storage.getReference();
            StorageReference riversRef = storageRef.child("orders/"+uid+"/"+user+".jpg");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            //if the upload is successfull
//                            //hiding the progress dialog
                            progressDialog.dismiss();
//
//                            getDownloadUrl();
//
//                            //and displaying a success toast
//                            Toast.makeText(getApplicationContext(), "Product added successfully ! ", Toast.LENGTH_LONG).show();
//                            product_name.setText("");
//                            product_price.setText("");
//                            product_stock.setText("");
//                            product_description.setText("");
//                            product_image.setImageResource(R.drawable.rename);
//
                        }
//
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }


    public void onBackPressed(){
        DatabaseReference mCart = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("user");
        mCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("cart")&& !(category.this).isFinishing()){
                    showPopUp();
                }
                else {
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void showPopUp() {
        Button clearCart;
        ImageView cancel;
        bottomSheetDialog.setContentView(R.layout.clear_cart);
        clearCart = bottomSheetDialog.findViewById(R.id.clear);
        cancel = bottomSheetDialog.findViewById(R.id.cancel);
        bottomSheetDialog.show();
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(true);

        clearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference clearcart = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("user");
                clearcart.child("cart").removeValue();
                bottomSheetDialog.dismiss();
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<categoryModel> options =
                new FirebaseRecyclerOptions.Builder<categoryModel>()
                .setQuery(mDatabaseReference, categoryModel.class)
                .build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<categoryModel, categoryAdapter>(options) {
            @Override
            public categoryAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())

                        .inflate(R.layout.single_category_display, parent, false);

                return new categoryAdapter(view);
            }


            @Override
            protected void onBindViewHolder(@NonNull categoryAdapter holder, final int position, @NonNull final categoryModel model) { ;
                // Bind the image_details object to the BlogViewHolder

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        final String services = getIntent().getStringExtra("services");
                        String key = getIntent().getStringExtra("key");

                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                        Query query = rootRef.child("shops").child("category").child(services).child(key).child("category").orderByChild("name").equalTo(model.getName());
                        ValueEventListener valueEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                    String parent = ds.getKey();
                                    String key = getIntent().getStringExtra("key");
                                    Intent intent = new Intent(category.this, ProductsDisplay.class);
                                    intent.putExtra("parent",parent);
                                    intent.putExtra("key", key);
                                    intent.putExtra("services",services);
                                    startActivity(intent);
//                                    Toast.makeText(category.this, key, Toast.LENGTH_SHORT).show();
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

                holder.setDetails(getApplicationContext(), model.getName(), model.getImage());
            }
        };

        firebaseRecyclerAdapter.startListening();
        category.setAdapter(firebaseRecyclerAdapter);
    }
}

