package com.example.asus.taskbookslife;

/**
 * Created by WILANDA on 16/8/2018.
 */
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BukuActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks  {

    TextView judul,penulis;
    ImageView gambar;
    boolean isImageFitToScreen;
    Button btnDownload;
    private static final int WRITE_REQUEST_CODE = 300;
    private static final String TAG = BukuActivity.class.getSimpleName();
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buku);

        judul = (TextView) findViewById(R.id.txtjudul);
        penulis = (TextView) findViewById(R.id.writer);
        gambar = (ImageView) findViewById(R.id.cover);

        gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isImageFitToScreen) {
                    isImageFitToScreen = false;
                    gambar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    gambar.setAdjustViewBounds(true);
                } else {
                    isImageFitToScreen = true;
                    gambar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    gambar.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
            }
        });


        Intent intent = getIntent();
        final String name = getIntent().getStringExtra("name");
        String guid = getIntent().getStringExtra("guid");

        String id = getIntent().getStringExtra("id");
        final String writer = getIntent().getStringExtra("writer");

        final String pdf = getIntent().getStringExtra("pdf");

        final int image = intent.getExtras().getInt("cover");

        // URL = pdf;

        btnDownload = (Button) findViewById(R.id.btnunduh);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            //Check if SD card is present or not
            public void onClick(View v) {
                //Check if SD card is present or not
                if (CheckPenyimpanan.isSDCardPresent()) {

                    //check if app has permission to write to the external storage.
                    if (EasyPermissions.hasPermissions(BukuActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        //Get the URL entered
                        url = pdf;
                        new DownloadFile().execute(url);

                    } else {
                        //If permission is not present request for the same.
                        EasyPermissions.requestPermissions(BukuActivity.this, getString(R.string.write_file), WRITE_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
                    }


                } else {
                    Toast.makeText(getApplicationContext(),
                            "SD Card not found", Toast.LENGTH_LONG).show();

                }
            }

        });




        Gson gson = new GsonBuilder().create();



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://codecult.tech/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        api_bookslife data = retrofit.create(api_bookslife.class);
        Call<List<ClassBuku>> tampil = data.getAll();
        tampil.enqueue(new Callback<List<ClassBuku>>() {
            @Override
            public void onResponse(Call<List<ClassBuku>> call, Response<List<ClassBuku>> response) {
                judul.setText(name);

                penulis.setText("PENULIS : " + writer);

                final List<ClassBuku> data = response.body();
                for(ClassBuku m : data){
                    Picasso.with(BukuActivity.this).load(m.getCover()).noFade().into(gambar);
                }


            }

            @Override
            public void onFailure(Call<List<ClassBuku>> call, Throwable t) {
                Toast.makeText(getBaseContext(),String.valueOf("Tidak ada koneksi"),Toast.LENGTH_SHORT).show();
                btnDownload.setVisibility(View.INVISIBLE);

            }
        });






    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, BukuActivity.this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //Download the file once permission is granted
        final String pdf = getIntent().getStringExtra("pdf");
        url = pdf;
        new DownloadFile().execute(url);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been denied");
    }

    /**
     * Async Task to download file from URL
     */
    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(BukuActivity.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                //Append timestamp to file name
                fileName = timestamp + "_" + fileName;

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "Bookslife/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d(TAG, "Proses Download: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Berhasil didownload: " + folder + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Koneksi Terputus";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            // Display File path after downloading
            Toast.makeText(getApplicationContext(),
                    message, Toast.LENGTH_LONG).show();
        }
    }
}

