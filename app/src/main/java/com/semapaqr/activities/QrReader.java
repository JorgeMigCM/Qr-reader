package com.semapaqr.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.semapaqr.R;
import com.semapaqr.db.Constants;
import com.semapaqr.db.MyDbHelper;



public class QrReader extends AppCompatActivity {

    private BottomSheetDialog bottomSheetDialog;

    private RecyclerView businessAssetRv;


    private MyDbHelper dbHelper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_reader);


        //Boton para mas opciones
        ImageButton btnOptionDB = (ImageButton) findViewById(R.id.BtnOptionDB);
        //Boton para escanear codigo de barras y QR
        FloatingActionButton barCodeScanner = (FloatingActionButton) findViewById(R.id.BarCodeScanner);
        //ReciclerView para los activos fijos
        businessAssetRv =(RecyclerView)findViewById(R.id.businessAssetRv);
        //
        SearchView SearchBusinessAssets = (SearchView)findViewById(R.id.SearchBusinessAssets);

        //Busqueda de los activos
        SearchBusinessAssets.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchBusinessAsset(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchBusinessAsset(newText);
                return true;
            }
        });


        //llamado a db helper
        dbHelper = new MyDbHelper(this);

        loadBusinessAssets();

        //Acciones del menu de opciones

        btnOptionDB.setOnClickListener(v -> {
            bottomSheetDialog = new BottomSheetDialog(QrReader.this, R.style.BottomSheetTheme);
            View sheetview = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottommenu_layout,null);
            //Boton para agregar activo
            sheetview.findViewById(R.id.AddBusinessAssets).setOnClickListener(v1 -> {
                startActivity(new Intent(QrReader.this, AddBusinessAssetActivity.class));
                bottomSheetDialog.dismiss();
            });
            //Boton dialog para borrar todos los activos
            sheetview.findViewById(R.id.MoreCleanDB).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMoreCleanDBDialog();
                }
            });
            //Boton para importar datos
            sheetview.findViewById(R.id.ImportDB).setOnClickListener(v1 -> {
                Toast.makeText(QrReader.this, "Click import DB", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });
            //Boton para exportar datos
            sheetview.findViewById(R.id.ExportDB).setOnClickListener(v1 -> {
                Toast.makeText(QrReader.this, "Click export DB", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });
            //Boton del manual
            sheetview.findViewById(R.id.ManualPdf).setOnClickListener(v1 -> {
                startActivity(new Intent(QrReader.this, ManualPDFActivity.class));
                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(sheetview);
            bottomSheetDialog.show();
        });


        barCodeScanner.setOnClickListener(v -> {
            IntentIntegrator intentIntegrator = new IntentIntegrator(QrReader.this);
            intentIntegrator.setOrientationLocked(true);
            intentIntegrator.setCameraId(0);
            intentIntegrator.setBeepEnabled(false);
            intentIntegrator.setBarcodeImageEnabled(true);
            intentIntegrator.setCaptureActivity(CaptureActivityPortrait.class);
            intentIntegrator.setPrompt("Escane un codigo de barras o QR");
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            intentIntegrator.initiateScan();
        });
    }

    private void showMoreCleanDBDialog() {
        ConstraintLayout deleteConstraintLayout = findViewById(R.id.deleteConstraintLayout);
        View view = LayoutInflater.from(QrReader.this).inflate(R.layout.delete_database_dialog, deleteConstraintLayout);
        Button CleanDB = view.findViewById(R.id.CleanDB);
        Button cancelBtn = view.findViewById(R.id.cancelBtn);

        AlertDialog.Builder builder = new AlertDialog.Builder(QrReader.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        CleanDB.findViewById(R.id.CleanDB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteAllData();
                Toast.makeText(QrReader.this, "Datos eliminados", Toast.LENGTH_SHORT).show();
                onResume();
                alertDialog.dismiss();
                bottomSheetDialog.dismiss();
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

    private void loadBusinessAssets() {
        AdapterBusinessAssets adapterBusinessAssets = new AdapterBusinessAssets(QrReader.this,
                dbHelper.getAllBusinessAssets(Constants.C_ADDED_TIMESTAMP + " DESC"));

        businessAssetRv.setAdapter(adapterBusinessAssets);
    }

    private void searchBusinessAsset(String query){
        AdapterBusinessAssets adapterBusinessAssets = new AdapterBusinessAssets(QrReader.this,
                dbHelper.searchBusinessAsset(query));

        businessAssetRv.setAdapter(adapterBusinessAssets);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBusinessAssets();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            String contents = result.getContents();
            if (contents != null){
                String BarCodeSet = contents.replaceAll("[^0-9]", "");
                buscarRegistroEnBaseDeDatos(BarCodeSet);
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void buscarRegistroEnBaseDeDatos(String BarCodeSet) {

        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.C_CODIGO_ACTIVO + " =\"" + BarCodeSet + "\"";

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //keep checking in whole db for that record
        if (cursor.moveToFirst()){

            // Abre una nueva actividad y pasa los datos recuperados
            Intent intent = new Intent(this, BusinessAssetDetailActivity.class);
            intent.putExtra("RECORD_BARCODE", BarCodeSet);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Activo no encontrado: " + BarCodeSet, Toast.LENGTH_SHORT).show();
        }

    }

}