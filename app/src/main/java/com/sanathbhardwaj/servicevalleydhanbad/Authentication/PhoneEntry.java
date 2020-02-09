package com.sanathbhardwaj.servicevalleydhanbad.Authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sanathbhardwaj.servicevalleydhanbad.R;

public class PhoneEntry extends AppCompatActivity {

    EditText editTextCountryCode, editTextPhone;
    Button buttonContinue;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_entry);
        editTextCountryCode = findViewById(R.id.editTextCountryCode);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonContinue = findViewById(R.id.buttonContinue);
        back = findViewById(R.id.back);

        buttonContinue.setEnabled(false);
        buttonContinue.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryLight, null));
        buttonContinue.setBackgroundResource(R.drawable.button_drwable_light);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editTextPhone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if (s.toString().length()>9) {
                    buttonContinue.setEnabled(true);
                    buttonContinue.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null));
                    buttonContinue.setBackgroundResource(R.drawable.button_drwable);
                } else if(s.toString().length()<10) {
                    buttonContinue.setEnabled(false);
                    buttonContinue.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryLight, null));
                    buttonContinue.setBackgroundResource(R.drawable.button_drwable_light);
                } else{
                    buttonContinue.setEnabled(false);
                    buttonContinue.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryLight, null));
                    buttonContinue.setBackgroundResource(R.drawable.button_drwable_light);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextCountryCode.getText().toString().trim();
                String number = editTextPhone.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    editTextPhone.setError("Valid number is required");
                    editTextPhone.requestFocus();
                    return;
                }

                String phoneNumber = code + number;

                Intent intent = new Intent(PhoneEntry.this, PhoneVerification.class);
                intent.putExtra("phoneNumber", phoneNumber);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                startActivity(intent);

            }
        });
    }
}
