package com.semapaqr.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.semapaqr.R;
import com.semapaqr.db.Constants;
import com.semapaqr.db.MyDbHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BusinessAssetDetailActivity extends AppCompatActivity {

    private TextView TitleBA, addedTimeTv, updateTimeTv;
    private EditText codeBATv, nameBATv, typeBATv, descriptionBATv,
            sectorBATv, attendantBATv;

    private String id, codigo, nombre, tipo, descripcion, sector, encargado, addedTime, updateTime;
    MyDbHelper dbHelper;
    Date date = new Date();
    private String recordBARCODE, recordID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_asset_detail);

        ImageButton BackHome = findViewById(R.id.BackHome);
        BackHome.setOnClickListener(v -> finish());

        ImageButton editBtn = findViewById(R.id.editBtn);
        ImageButton deleteBtn = findViewById(R.id.deleteBtn);

        editBtn.setOnClickListener(v -> editBusinessAsset());
        deleteBtn.setOnClickListener(v -> showDeleteBADialog());

        Intent intent = getIntent();
        recordBARCODE = intent.getStringExtra("RECORD_BARCODE");
        recordID = intent.getStringExtra("RECORD_ID");

        //init db helper class
        dbHelper = new MyDbHelper(this);

        TitleBA = findViewById(R.id.TitleBA);
        codeBATv = findViewById(R.id.codeBATv);
        nameBATv = findViewById(R.id.nameBATv);
        typeBATv = findViewById(R.id.typeBATv);
        descriptionBATv = findViewById(R.id.descriptionBATv);
        sectorBATv = findViewById(R.id.sectorBATv);
        attendantBATv = findViewById(R.id.attendantBATv);
        addedTimeTv = findViewById(R.id.addedTimeTv);
        updateTimeTv = findViewById(R.id.updateTimeTv);

        showBusinessAssetDetails();
    }

    private void showBusinessAssetDetails() {

        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.C_CODIGO_ACTIVO + " =\"" + recordBARCODE + "\"";

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //keep checking in whole db for that record
        if (cursor.moveToFirst()){
            do {

                id = "" + cursor.getString(cursor.getColumnIndex(Constants.C_ID));
                codigo = ""+cursor.getString(cursor.getColumnIndex(Constants.C_CODIGO_ACTIVO));
                nombre = ""+cursor.getString(cursor.getColumnIndex(Constants.C_NOMBRE_ACTIVO));
                tipo = ""+cursor.getString(cursor.getColumnIndex(Constants.C_TIPO_ACTIVO));
                descripcion = ""+cursor.getString(cursor.getColumnIndex(Constants.C_DESCRIPCION_ACTIVO));
                sector = ""+cursor.getString(cursor.getColumnIndex(Constants.C_SECTOR_ACTIVO));
                encargado = ""+cursor.getString(cursor.getColumnIndex(Constants.C_ENCARGADO_ACTIVO));
                addedTime = ""+cursor.getString(cursor.getColumnIndex(Constants.C_ADDED_TIMESTAMP));
                updateTime = ""+cursor.getString(cursor.getColumnIndex(Constants.C_UPDATED_TIMESTAMP));


                //set data
                TitleBA.setText(codigo);
                codeBATv.setText(codigo);
                nameBATv.setText(nombre);
                typeBATv.setText(tipo);
                descriptionBATv.setText(descripcion);
                sectorBATv.setText(sector);
                attendantBATv.setText(encargado);
                addedTimeTv.setText(addedTime);
                updateTimeTv.setText(updateTime);


            }while (cursor.moveToNext());
        }

        //close db connection
        db.close();

    }

    private void editBusinessAsset(){
        SimpleDateFormat fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

        String codigo_activo = ""+codeBATv.getText().toString().trim();
        String nombre_activo = ""+nameBATv.getText().toString().trim();
        String tipo_activo = ""+typeBATv.getText().toString().trim();
        String descripcion_activo = ""+descriptionBATv.getText().toString().trim();
        String sector_activo = ""+sectorBATv.getText().toString().trim();
        String encargado_activo = ""+attendantBATv.getText().toString().trim();

        String timestamp = fecha.format(date);

        dbHelper.updateBusinessAsset(
                ""+id,
                ""+codigo_activo,
                ""+nombre_activo,
                ""+tipo_activo,
                ""+descripcion_activo,
                ""+sector_activo,
                ""+encargado_activo,
                ""+addedTime,
                ""+timestamp
        );
        Toast.makeText(this, "Activo " + codigo + " actualizado", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void deleteBusinessAsset() {
        String codigo_activo = ""+codeBATv.getText().toString().trim();


        String timestamp = "" + System.currentTimeMillis();

        dbHelper.deleteData(
                ""+id,
                ""+codigo_activo
        );
        //refresh record by calling activiti'es onResume method
        Toast.makeText(this, "Activo " + codigo_activo+" eliminado", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void showDeleteBADialog() {
        ConstraintLayout deleteConstraintLayoutDetail = findViewById(R.id.deleteConstraintLayoutDetail);
        View view = LayoutInflater.from(BusinessAssetDetailActivity.this).inflate(R.layout.delete_business_asset_dialog, deleteConstraintLayoutDetail);
        Button CleanDB = view.findViewById(R.id.CleanDB);
        Button cancelBtn = view.findViewById(R.id.cancelBtn);

        AlertDialog.Builder builder = new AlertDialog.Builder(BusinessAssetDetailActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        CleanDB.findViewById(R.id.CleanDB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBusinessAsset();
                alertDialog.dismiss();
            }
        });
        cancelBtn.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

}