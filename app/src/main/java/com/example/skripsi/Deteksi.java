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
    private Button button_kamera, button_galeri, button_deteksi;
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
            MLModel(resized);
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
//            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3);
//            byteBuffer.order(ByteOrder.nativeOrder());
//
//            int [] intValues = new int[224 * 224];
//            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
//
//            int pixel = 0;
//            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
//            for(int i = 0; i < 224; i ++){
//                for(int j = 0; j < 224; j++){
//                    int val = intValues[pixel++]; // RGB
//                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
//                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
//                    byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
//                }
//            }


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
                Binding.textDeteksi.setText("Brown Spot");
                Binding.textDeteksiDeskripsi.setText("Ini Brownspot");
                Binding.textDeteksiDeskripsi.setVisibility(View.VISIBLE);
            }else if(index==1){
                Log.d("message", "Sehat");
                Binding.textDeteksi.setText("Sehat");
                Binding.textDeteksiDeskripsi.setText("Tanaman ini sehat");
                Binding.textDeteksiDeskripsi.setVisibility(View.VISIBLE);
            }else if(index == 2){
                Log.d("message", "Hispa");
                Binding.textDeteksi.setText("Hispa");
                Binding.textDeteksiDeskripsi.setText("Tanaman ini terjangkit Hispa");
                Binding.textDeteksiDeskripsi.setVisibility(View.VISIBLE);
            }else if(index == 3){
                Log.d("message", "Blas");
                Binding.textDeteksi.setText("Leaf Blast");
                Binding.textDeteksiDeskripsi.setText("Tanaman ini terjangkit blas");
                Binding.textDeteksiDeskripsi.setVisibility(View.VISIBLE);
            }
        } catch (IOException e) {
            // TODO Handle the
            System.out.print("Error");
        }
    }
}