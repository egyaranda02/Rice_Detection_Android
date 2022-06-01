package com.example.skripsi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.skripsi.databinding.ActivityDeteksiBinding;
import com.example.skripsi.ml.Resnet;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;

import android.util.Log;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class Deteksi extends AppCompatActivity implements View.OnClickListener {
    ActivityDeteksiBinding Binding;
    private Button button_kamera, button_galeri;
    private TextView title, description;
    private ImageView imageCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = ActivityDeteksiBinding.inflate(getLayoutInflater());
        setContentView(Binding.getRoot());


        button_kamera = (Button)findViewById(R.id.button_kamera);
        button_galeri = (Button)findViewById(R.id.button_galeri);
        title = (TextView)findViewById(R.id.text_deteksi);
        description = (TextView)findViewById(R.id.text_deteksi_deskripsi);

        button_kamera.setOnClickListener(this);
        button_galeri.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_galeri:
                pickImageFromGallery();
                break;
            case R.id.button_kamera:
                pickImageFromKamera();
                break;
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 101);
    }

    private void pickImageFromKamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            int dimension = Math.min(captureImage.getWidth(), captureImage.getHeight());
            captureImage = ThumbnailUtils.extractThumbnail(captureImage, dimension, dimension);
            Binding.viewImageDeteksi.setImageBitmap(captureImage);
            Bitmap resized = Bitmap.createScaledBitmap(captureImage, 224, 224, true);
            Bitmap newResized = resized.copy(Bitmap.Config.ARGB_8888,true);
            MLModel(newResized);
        }
        if (requestCode == 101) {

            Uri galleryImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), galleryImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int dimension = Math.min(bitmap.getWidth(), bitmap.getHeight());
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension);
            Binding.viewImageDeteksi.setImageBitmap(bitmap);
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
            MLModel(resized);
        }
    }

    private void MLModel(Bitmap resized){
        try {
            Bitmap image = resized;
            Resnet model = Resnet.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
            tensorImage.load(image);
            ByteBuffer byteBuffer = tensorImage.getBuffer();


            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Resnet.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();

            // Releases model resources if no longer used.
            model.close();

            int index = 0;
            float max = 0;

            for(int i =0; i <confidences.length; i++){
                if(confidences[i]>max){
                    max = confidences[i];
                    index = i;
                }
            }

            Log.d("message", "Array:" + Arrays.toString(confidences));


            if(index==0){
                Log.d("message", "Brown spot");
                Binding.textDeteksi.setText("Bercak Daun Coklat");
                Binding.textDeteksiDeskripsi.setText("Bercak daun coklat disebabkan oleh jamur Cochliobolus miyabeanus. Jamur ini dapat bertahan hidup dalam benih selama lebih dari empat tahun dan menyebar dari satu tanaman ke tanaman lainnya melalui spora yang ada di udara. Sisa-sisa tanaman terinfeksi yang tertinggal di lahan dan gulma adalah penyebab umum lainnya penyebaran penyakit ini. Pengendalian bercak coklat yang memuaskan dapat dicapai dengan menggunakan pupuk silikon.");
                Binding.textDeteksiDeskripsi.setVisibility(View.VISIBLE);
            }else if(index==1){
                Log.d("message", "Sehat");
                Binding.textDeteksi.setText("Sehat");
                Binding.textDeteksiDeskripsi.setText("Tanaman ini sehat");
                Binding.textDeteksiDeskripsi.setVisibility(View.VISIBLE);
            }else if(index == 2){
                Log.d("message", "Hispa");
                Binding.textDeteksi.setText("Hispa");
                Binding.textDeteksiDeskripsi.setText("Hispa disebabkan oleh serangga dewasa dan larva hispa padi, Dicladispa armigera. Serangga dewasa mengikis permukaan atas helai daun dan hanya menyisakan epidermis bawah. Telur diletakkan di dalam celah kecil pada daun yang lembut, umumnya ke arah ujung. Ulat ini makan di dalam jaringan daun dengan menggali sepanjang sumbu daun, dan kemudian menjadi kepompong.");
                Binding.textDeteksiDeskripsi.setVisibility(View.VISIBLE);
            }else if(index == 3){
                Log.d("message", "Blas");
                Binding.textDeteksi.setText("Blas");
                Binding.textDeteksiDeskripsi.setText("Penyakit blas merupakan penyakit yang disebabkan oleh jamur pylicularia grisea. Jamur ini dapat menginfeksi pada semua fase pertumbuhan tanaman padi, mulai dari fase pembibitan sampai pada fase generatif. Tanaman yang terserang penyakit blas memiliki ciri bercak coklat berbentuk belah ketupat pada daun tanaman padi. Perkembangan parah penyakit blas pada tanaman padi dapat mencapai bagian gabah dan patogennya dapat terbawa gabah sebagai patogen tular benih.");
                Binding.textDeteksiDeskripsi.setVisibility(View.VISIBLE);
            }
        } catch (IOException e) {
            // TODO Handle the
            System.out.print("Error");
        }
    }
}