package com.semapaqr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.semapaqr.activities.QrReader;
public class MainActivity extends AppCompatActivity{

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Iniciar la actividad principal aquí (por ejemplo, la actividad que contiene QrReader)
                Intent intent = new Intent(MainActivity.this, QrReader.class);
                startActivity(intent);
                finish(); // Opcional: cierra la pantalla de inicio
            }
        }, 100);

    }

}