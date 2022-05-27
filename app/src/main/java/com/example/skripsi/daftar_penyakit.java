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

        final String[] description = {"Bercak daun coklat disebabkan oleh jamur Cochliobolus miyabeanus. Jamur ini dapat bertahan hidup dalam benih selama lebih dari empat tahun dan menyebar dari satu tanaman ke tanaman lainnya melalui spora yang ada di udara. Sisa-sisa tanaman terinfeksi yang tertinggal di lahan dan gulma adalah penyebab umum lainnya penyebaran penyakit ini. Bercak coklat dapat terjadi pada semua tahap panen, tetapi infeksi paling kritis dimulai dari tahapan akhir anakan hingga pematangan. Penyakit ini sering terjadi di lahan dengan kesalahan pengelolaan kesuburan tanah, terutama dalam hal unsur hara mikro. Pengendalian bercak coklat yang memuaskan dapat dicapai dengan menggunakan pupuk silikon.",
                "Hispa disebabkan oleh serangga dewasa dan larva hispa padi, Dicladispa armigera. Serangga dewasa mengikis permukaan atas helai daun dan hanya menyisakan epidermis bawah. Telur diletakkan di dalam celah kecil pada daun yang lembut, umumnya ke arah ujung. Ulatnya berwarna kuning keputihan dan datar. Ulat ini makan di dalam jaringan daun dengan menggali sepanjang sumbu daun, dan kemudian menjadi kepompong. Serangga dewasa berbentuk agak persegi, panjang dan lebarnya sekitar 3-5 mm.",
                "Penyakit blas merupakan penyakit yang disebabkan oleh jamur pylicularia grisea. Jamur ini dapat menginfeksi pada semua fase pertumbuhan tanaman padi, mulai dari fase pembibitan sampai pada fase generatif. Tanaman yang terserang penyakit blas memiliki ciri bercak coklat berbentuk belah ketupat pada daun tanaman padi. Perkembangan parah penyakit blas pada tanaman padi dapat mencapai bagian gabah dan patogennya dapat terbawa gabah sebagai patogen tular benih."};
        final String[] disease_name = {"Bercak Daun Coklat","Hispa","Blas"};
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