package com.semapaqr.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.acutecoder.pdf.OnActionListener;
import com.acutecoder.pdf.PdfScrollBar;
import com.acutecoder.pdf.PdfView;
import com.acutecoder.pdf.TemporaryFile;
import com.semapaqr.R;

public class ManualPDFActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_pdfactivity);

        PdfView pdfView = findViewById(R.id.pdfview);
        PdfScrollBar scrollBar = findViewById(R.id.pdfscroll);
        ProgressBar progressBar = findViewById(R.id.pg);

        scrollBar.attachTo(pdfView);
        pdfView.setZoomEnabled(true);
        pdfView.setMaxZoomScale(1);
        pdfView.setPath(new TemporaryFile("ManualPDF.pdf"));
        pdfView.addOnActionListener(new OnActionListener() {
            @Override
            public void onLoaded() {
                progressBar.setVisibility(View.GONE);
            }
        });
        pdfView.load();
    }
}