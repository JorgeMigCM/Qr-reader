package com.semapaqr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.semapaqr.activities.QrReader;
public class MainActivity extends AppCompatActivity{

    private Button btnQrReader, btnOptionsDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnQrReader = (Button)findViewById(R.id.btnQrReader);
        btnOptionsDb = (Button)findViewById(R.id.btnOptionsDb);

        btnQrReader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QrReader.class);
                startActivity(intent);
            }
        });
        btnOptionsDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QrReader.class);
                startActivity(intent);
            }
        });

    }

}