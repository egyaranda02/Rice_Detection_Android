package com.example.skripsi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.skripsi.Data;
import com.example.skripsi.R;

import java.util.ArrayList;


public class List_Adapter extends ArrayAdapter<Data> {

        public List_Adapter(Context context, ArrayList<Data> dataArrayList){

                super(context, R.layout.list_item, dataArrayList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                Data data = getItem(position);

                if (convertView == null){
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);

                }

                ImageView imageView = convertView.findViewById(R.id.disease_image);
                TextView disseaseName = convertView.findViewById(R.id.disease_name);
                TextView description = convertView.findViewById(R.id.description);

                imageView.setImageResource(data.imageId);
                disseaseName.setText(data.disease_name);
                description.setText(data.description);


                return convertView;
        }
}
