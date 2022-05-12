package com.example.skripsi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.skripsi.databinding.ActivityViewPenyakitBinding;

public class View_Penyakit extends AppCompatActivity {
    ActivityViewPenyakitBinding Binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = ActivityViewPenyakitBinding.inflate(getLayoutInflater());
        setContentView(Binding.getRoot());

        Intent intent = this.getIntent();

        if(intent != null){
            String name = intent.getStringExtra("name");
            String description = intent.getStringExtra("description");
            int imageId = intent.getIntExtra("imageId", R.drawable.hispa);

            Binding.detailName.setText(name);
            Binding.detailDesc.setText(description);
            Binding.detailImage.setImageResource(imageId);
        }

    }
}