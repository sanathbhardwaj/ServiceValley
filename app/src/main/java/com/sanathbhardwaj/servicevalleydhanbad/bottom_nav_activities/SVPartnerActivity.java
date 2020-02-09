package com.sanathbhardwaj.servicevalleydhanbad.bottom_nav_activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sanathbhardwaj.servicevalleydhanbad.MainActivity;
import com.sanathbhardwaj.servicevalleydhanbad.R;

public class SVPartnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svpartner);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view_menu);
        bottomNavigationView.setSelectedItemId(R.id.SVPartner);
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
                        return true;
                    case R.id.Account:
                        startActivity(new Intent(getApplicationContext()
                                , AccountActivity.class));
                        finish();
                        overridePendingTransition(0,0);
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
