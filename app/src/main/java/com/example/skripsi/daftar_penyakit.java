package com.example.skripsi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.example.skripsi.databinding.ActivityDaftarPenyakitBinding;

import java.util.ArrayList;

public class daftar_penyakit extends AppCompatActivity {
    ActivityDaftarPenyakitBinding Binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = ActivityDaftarPenyakitBinding.inflate(getLayoutInflater());
        setContentView(Binding.getRoot());

        final String[] description = {"1","2","3"};
        final String[] disease_name = {"1","2","3"};
        final int imageId[] = {R.drawable.brownspot, R.drawable.hispa, R.drawable.leafblast};

        ArrayList<Data> dataArrayList = new ArrayList<>();

        for(int i = 0; i < imageId.length;i++){
            Data data = new Data(disease_name[i], description[i], imageId[i]);
            dataArrayList.add(data);
        }

        List_Adapter list_adapter = new List_Adapter(daftar_penyakit.this, dataArrayList);
        Binding.listview.setAdapter(list_adapter);
        Binding.listview.setClickable(true);
        Binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(daftar_penyakit.this, View_Penyakit.class);
                i.putExtra("name", disease_name[position]);
                i.putExtra("description", description[position]);
                i.putExtra("imageId", imageId[position]);
                startActivity(i);
            }
        });
    }
}