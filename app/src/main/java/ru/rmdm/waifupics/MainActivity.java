package ru.rmdm.waifupics;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import android.Manifest;
import android.app.AlertDialog;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import androidx.core.content.ContextCompat;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import dagger.hilt.android.AndroidEntryPoint;
import ru.rmdm.waifupics.adapter.RecyclerViewAdapter;
import ru.rmdm.waifupics.listener.OnImageItemLongClickListener;
import ru.rmdm.waifupics.model.WaifuImage;
import ru.rmdm.waifupics.viewmodel.WaifuViewModel;


@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements OnImageItemLongClickListener {

    RecyclerView rv_main;
    WaifuViewModel viewModel;

    RecyclerViewAdapter adapter;
    FloatingActionButton floatingActionButton;




    private static final int STORAGE_PERMISSION_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
        rv_main = findViewById(R.id.rv_main);
        checkPermission();
        viewModel = new ViewModelProvider(this).get(WaifuViewModel.class);
        adapter = new RecyclerViewAdapter();
        adapter.setLongClickListener(this);
        rv_main.setAdapter(adapter);
        rv_main.setLayoutManager(new LinearLayoutManager(this));
        floatingActionButton = findViewById(R.id.fab);
        Switch nsfw_switch = findViewById(R.id.nsfw_switch);

        viewModel.getLiveData().observe(this, new Observer<List<WaifuImage>>() {
            @Override
            public void onChanged(List<WaifuImage> waifuImages) {
                if (waifuImages != null) {
                    adapter.setListItems(waifuImages);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        if (activeNetwork!=null && activeNetwork.isConnected()){

            viewModel.makeApiCall();
        }

        floatingActionButton.setOnClickListener(v-> {
            viewModel.getLiveData().observe(this, new Observer<List<WaifuImage>>() {
                @Override
                public void onChanged(List<WaifuImage> waifuImages) {
                    if (waifuImages != null) {
                        adapter.setListItems(waifuImages);
                        adapter.notifyDataSetChanged();
                    }
                }
            });

            if (activeNetwork!=null && activeNetwork.isConnected()){
                if (nsfw_switch.isChecked()){
                    viewModel.makeApiSwitch("true");
                    rv_main.smoothScrollToPosition(0);
                } else {
                viewModel.makeApiCall();
                rv_main.smoothScrollToPosition(0);
                }
            }

        });


    }

    @Override
    public void onImageLongClick(WaifuImage image) {
        new AlertDialog.Builder(this)
                .setTitle("Save Image")
                .setIcon(R.mipmap.waifupics)
                .setMessage("Are you want save the image?")
                .setPositiveButton("yes",(dialog,which) -> downloadImageWithGlide(image.getUrl()))
                .setNegativeButton("Cancel",null)
                .show();

    }
    private void downloadImageWithGlide(String imageUrl){

        new Thread(()->{
            String fileName;
            try {
                Context context = getApplicationContext();
                FutureTarget<Bitmap> futureTarget = Glide.with(context).asBitmap()
                        .load(imageUrl)
                        .submit();

                Bitmap bitmap = futureTarget.get();

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                if(imageUrl.endsWith(".gif")){
                    fileName = "WaifuPics_" + timeStamp + ".gif";
                }
                else
                    fileName = "WaifuPics_" + timeStamp + ".jpg";


                File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"WaifuPics");
                if (!directory.exists()){
                    directory.mkdirs();
                }
                File file = new File(directory,fileName);
                FileOutputStream outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                outputStream.flush();
                outputStream.close();

                MediaScannerConnection.scanFile(this,
                        new String[]{file.getAbsolutePath()},
                        new String[]{"image/jpeg"},null);

                runOnUiThread(()->
                        Toast.makeText(this,"Image Saved",Toast.LENGTH_SHORT).show());
            } catch (InterruptedException | ExecutionException | IOException e ){
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this,"Image  Not Saved",Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
    public void checkPermission(){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_CODE);
            }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show();
            }   else
                Toast.makeText(this,"Need Permission for save images",Toast.LENGTH_SHORT).show();
        }
    }

}