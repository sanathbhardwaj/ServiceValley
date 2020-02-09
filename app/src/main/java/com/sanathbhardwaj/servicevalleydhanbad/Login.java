package com.sanathbhardwaj.servicevalleydhanbad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    ImageView back;
    DatabaseReference mRef;
    EditText editTextPhone, password;
    TextView editTextCountryCode;
    Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextPhone = findViewById(R.id.editTextPhone);
        editTextCountryCode = findViewById(R.id.editTextCountryCode);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        final String key = password.getText().toString().trim();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = editTextCountryCode.getText().toString().trim();
                String number = editTextPhone.getText().toString().trim();

                String phoneNumber = code + number;



                Intent intent = new Intent(Login.this, OTP.class);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.putExtra("password", key);
                startActivity(intent);
            }
        });



//        else{
//
//            Toast.makeText(this, "You are not registered.", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(Login.this, LoginSignUp.class);
//            startActivity(intent);
//
//        }



    }
}
