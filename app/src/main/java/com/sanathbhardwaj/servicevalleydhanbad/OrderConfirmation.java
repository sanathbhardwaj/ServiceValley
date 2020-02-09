package com.sanathbhardwaj.servicevalleydhanbad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderConfirmation extends AppCompatActivity {

    Button track_order;
    TextView status, order_status, status0;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        String details = getIntent().getStringExtra("details");
        System.out.println(details);
        status = findViewById(R.id.status);
        order_status = findViewById(R.id.order_status);
        img = findViewById(R.id.img);
        track_order = findViewById(R.id.track_order);


        if (details.equals("Cancelled")){
            status.setText(details);
            status.setTextColor(ContextCompat.getColor(this, R.color.quantum_googred));
            img.setBackgroundResource(R.drawable.failed);
            order_status.setText("Order Not Placed");
            track_order.setText("Close");
            track_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });


        }

        if (details.equals("Failed")){

            status.setText(details);
            status.setTextColor(ContextCompat.getColor(this, R.color.quantum_googred));
            img.setBackgroundResource(R.drawable.failed);
            order_status.setText("Order Not Placed");
            track_order.setText("Close");

            track_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });



        }
        if (details.equals("Success")){

            status.setText(details);
            status.setTextColor(ContextCompat.getColor(this, R.color.quantum_googred));
            img.setBackgroundResource(R.drawable.tick);
            order_status.setText("Order Placed Successfully");
            track_order.setText("Track Order");

            track_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(OrderConfirmation.this, TrackOrder.class);
                    startActivity(intent);
                    finish();
                }
            });



        }

    }
}
