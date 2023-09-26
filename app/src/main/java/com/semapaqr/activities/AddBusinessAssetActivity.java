package com.semapaqr.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.semapaqr.R;
import com.semapaqr.db.MyDbHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddBusinessAssetActivity extends AppCompatActivity {

    private EditText codeBAEt, nameBAEt, typeBAEt, descriptionBAEt,
            sectorBAEt, attendantBAEt;
    private String codigo;
    private String recordBARCODE;

    Date date = new Date();
    private MyDbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business_asset);

        Intent intent = getIntent();
        recordBARCODE = intent.getStringExtra("RECORD_BARCODE");


        codeBAEt = findViewById(R.id.codeBAEt);
        codeBAEt.setText(recordBARCODE);
        nameBAEt = findViewById(R.id.nameBAEt);
        typeBAEt = findViewById(R.id.typeBAEt);
        descriptionBAEt = findViewById(R.id.descriptionBAEt);
        sectorBAEt = findViewById(R.id.sectorBAEt);
        attendantBAEt = findViewById(R.id.attendantBAEt);

        ImageButton saveBtn = findViewById(R.id.saveBtn);
        ImageButton cancelBtn = findViewById(R.id.cancelBtn);


        dbHelper = new MyDbHelper(this);

        saveBtn.setOnClickListener(v -> inputData());

        cancelBtn.setOnClickListener(v -> finish());



    }
    private void inputData() {
        SimpleDateFormat fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());


        String codigo_activo = ""+codeBAEt.getText().toString().trim();
        String nombre_activo = ""+nameBAEt.getText().toString().trim();
        String tipo_activo = ""+typeBAEt.getText().toString().trim();
        String descripcion_activo = ""+descriptionBAEt.getText().toString().trim();
        String sector_activo = ""+sectorBAEt.getText().toString().trim();
        String encargado_activo = ""+attendantBAEt.getText().toString().trim();

        String timestamp = fecha.format(date);

        long id = dbHelper.insertBusinessAssets(
                ""+codigo_activo,
                ""+nombre_activo,
                ""+tipo_activo,
                ""+descripcion_activo,
                ""+sector_activo,
                ""+encargado_activo,
                ""+timestamp,
                ""+timestamp
        );
        Toast.makeText(this, "Activo: "+ codigo_activo +" registrado", Toast.LENGTH_SHORT).show();
        finish();
        }

}