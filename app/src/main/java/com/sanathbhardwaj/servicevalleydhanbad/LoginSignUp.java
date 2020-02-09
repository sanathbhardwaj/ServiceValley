package com.sanathbhardwaj.servicevalleydhanbad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.sanathbhardwaj.servicevalleydhanbad.Adapters.SliderAdapter;
import com.sanathbhardwaj.servicevalleydhanbad.Authentication.PhoneEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LoginSignUp extends AppCompatActivity {

    List<Integer> icon;
    List<String> iconName;

    ViewPager viewPager;
    TabLayout indicator;
    Button location;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_signup);

        viewPager = findViewById(R.id.viewPager);
        indicator = findViewById(R.id.indicator);
        location = findViewById(R.id.location);
        login = findViewById(R.id.login);

        icon = new ArrayList<>();
        icon.add(R.drawable.slider1);
        icon.add(R.drawable.slider2);
        icon.add(R.drawable.slider3);

        iconName = new ArrayList<>();
        iconName.add("Grocery delivery is free now");
        iconName.add("Free Delivery with Slot Delivery");
        iconName.add("All payment methods available");

        viewPager.setAdapter(new SliderAdapter(LoginSignUp.this, icon, iconName));
        indicator.setupWithViewPager(viewPager, true);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginSignUp.this, PhoneEntry.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginSignUp.this, Login.class);
                startActivity(intent);

            }
        });
    }
    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            LoginSignUp.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < icon.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }
}
