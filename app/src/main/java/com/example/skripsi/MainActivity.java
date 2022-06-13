package com.example.skripsi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button DaftarPenyakit, Deteksi, AboutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaftarPenyakit = (Button)findViewById(R.id.button_deteksi);
        Deteksi = (Button)findViewById(R.id.button_daftar);
        AboutButton = (Button)findViewById(R.id.button_about);
        DaftarPenyakit.setOnClickListener(this);
        Deteksi.setOnClickListener(this);
        AboutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.button_deteksi:
               startActivity(new Intent(this, Deteksi.class));
               break;
           case R.id.button_daftar:
               startActivity(new Intent(this, daftar_penyakit.class));
               break;
           case R.id.button_about:
               startActivity(new Intent(this, About.class));
               break;
       }
    }
}