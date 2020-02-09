package com.sanathbhardwaj.servicevalleydhanbad.bottom_nav_activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.sanathbhardwaj.servicevalleydhanbad.MainActivity;
import com.sanathbhardwaj.servicevalleydhanbad.R;

public class AccountActivity extends AppCompatActivity {

    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view_menu);
        bottomNavigationView.setSelectedItemId(R.id.Account);
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
                        return true;
                    case R.id.Home:
                        startActivity(new Intent(getApplicationContext()
                                , MainActivity.class));
                        finish();
                        overridePendingTransition(0,0);
                }
                return false;
            }
        });

    }
}
