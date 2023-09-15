package com.semapaqr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.semapaqr.R;


public class QrReader extends AppCompatActivity {

    private BottomSheetDialog bottomSheetDialog;

    private TextView TitleBusinessAssets;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_reader);

        ImageButton btnOptionDB = (ImageButton) findViewById(R.id.BtnOptionDB);

        FloatingActionButton barCodeScanner = (FloatingActionButton) findViewById(R.id.BarCodeScanner);
        TitleBusinessAssets = (TextView)findViewById(R.id.TitleBusinessAssets);

        btnOptionDB.setOnClickListener(v -> {
            bottomSheetDialog = new BottomSheetDialog(QrReader.this, R.style.BottomSheetTheme);
            View sheetview = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottommenu_layout,null);

            sheetview.findViewById(R.id.ImportDB).setOnClickListener(v1 -> {
                Toast.makeText(QrReader.this, "Click import DB", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });
            sheetview.findViewById(R.id.ExportDB).setOnClickListener(v2 -> {
                Toast.makeText(QrReader.this, "Click export DB", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });
            sheetview.findViewById(R.id.ExportDBScan).setOnClickListener(new View.OnClickListener() {

                final int a =2;
                final int b =3;
                final int c = a+b;
                @Override
                public void onClick(View v) {
                    Toast.makeText(QrReader.this, "el resultado es "+c, Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();
                }
            });

//            sheetview.findViewById(R.id.Cancel).setOnClickListener(v3 -> bottomSheetDialog.dismiss());

            bottomSheetDialog.setContentView(sheetview);
            bottomSheetDialog.show();
        });

        barCodeScanner.setOnClickListener(v -> {
            IntentIntegrator intentIntegrator = new IntentIntegrator(QrReader.this);
            intentIntegrator.setOrientationLocked(false);
            intentIntegrator.setCameraId(0);
            intentIntegrator.setBeepEnabled(false);
            intentIntegrator.setBarcodeImageEnabled(false);
            intentIntegrator.setCaptureActivity(CaptureActivityPortrait.class);
            intentIntegrator.setPrompt("ESCANEAR CODIGO");
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            intentIntegrator.initiateScan();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null){
            String contents = intentResult.getContents();
            if (contents != null){
                TitleBusinessAssets.setText(contents.replaceAll("[^0-9]", ""));
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

}