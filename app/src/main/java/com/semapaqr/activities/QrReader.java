package com.semapaqr.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
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

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class QrReader extends AppCompatActivity {
    private BottomSheetDialog bottomSheetDialog;
    private RecyclerView businessAssetRv;
    private MyDbHelper dbHelper;
    Date date = new Date();
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_reader);

        context = this;

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
            sheetview.findViewById(R.id.ImportDB).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadExcel(QrReader.this);
                    onResume();
                    Toast.makeText(QrReader.this, "Datos cargados", Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();
                }
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

        //boton del escaner

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

    private void showMoreAddCodeScan(String BarCodeSet) {
        ConstraintLayout addConstraintLayout = findViewById(R.id.addConstraintLayout);
        View view = LayoutInflater.from(QrReader.this).inflate(R.layout.add_ba_database_dialog, addConstraintLayout);
        Button addScanDBbtn = view.findViewById(R.id.addScanDBbtn);
        Button cancelBtn = view.findViewById(R.id.cancelBtn);
        TextView codeScan = view.findViewById(R.id.codeScan);

        codeScan.setText(BarCodeSet);

        AlertDialog.Builder builder = new AlertDialog.Builder(QrReader.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        addScanDBbtn.findViewById(R.id.addScanDBbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(QrReader.this, AddBusinessAssetActivity.class);
                    intent.putExtra("RECORD_BARCODE", BarCodeSet);
                    startActivity(intent);
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

    @Override
    protected void onResume() {
        super.onResume();
        loadBusinessAssets();
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

            showMoreAddCodeScan(BarCodeSet);
        }

    }

    private void cargardatosSQL(Context context){

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            InputStream inputStream = context.getAssets().open("");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // Ejecuta cada sentencia SQL en el archivo
                db.execSQL(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadExcel(Context context){
        try {
            // Abre el archivo Excel desde los recursos (puedes cargarlo desde donde quieras)
            InputStream inputStream = context.getAssets().open("rep1.xlsx");
            // Crea un libro de trabajo (workbook) a partir del flujo de entrada
            Workbook workbook = new XSSFWorkbook(inputStream);
            // Obtén la hoja de trabajo (worksheet) que deseas leer
            Sheet sheet = workbook.getSheetAt(0);
            // Supongamos que la primera fila de tu archivo Excel contiene los nombres de los campos
            Row headerRow = sheet.getRow(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    // Saltar la primera fila que contiene encabezados
                    continue;
                }
                String codigo_activo = row.getCell(1).getStringCellValue();
                String nombre_activo = row.getCell(2).getStringCellValue();
                String tipo_activo = row.getCell(3).getStringCellValue();
                String descripcion_activo = row.getCell(4).getStringCellValue();
                String sector_activo = row.getCell(5).getStringCellValue();
                String encargado_activo = row.getCell(6).getStringCellValue();
                String addedTime = getCurrentTimestamp(); // Puedes obtener la fecha actual como desees
                String updateTime = getCurrentTimestamp(); // Puedes obtener la fecha actual como desees

                // Inserta los datos en la base de datos SQLite utilizando tu MyDbHelper
                MyDbHelper dbHelper = new MyDbHelper(this);
                long id = dbHelper.insertBusinessAssets(
                        ""+codigo_activo,
                        ""+nombre_activo,
                        ""+tipo_activo,
                        ""+descripcion_activo,
                        ""+sector_activo,
                        ""+encargado_activo,
                        ""+addedTime,
                        ""+updateTime);
                dbHelper.close();
            }

            // Cierra el flujo de entrada
            inputStream.close();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCurrentTimestamp() {
        SimpleDateFormat fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        String timestamp = fecha.format(date);
        // Implementa la lógica para obtener la fecha y hora actual según tus necesidades
        return  ""+timestamp; // Ejemplo: devuelve una cadena con la fecha y hora actual
    }
}
