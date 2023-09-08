package com.semapaqr.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.semapaqr.MainActivity;
import com.semapaqr.R;

public class OptionsDataApp extends AppCompatActivity {

    private ImageButton BackToHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_data_app);

        BackToHome = (ImageButton)findViewById(R.id.BackToHome);

        BackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}