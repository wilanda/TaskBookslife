package com.example.asus.taskbookslife;

/**
 * Created by WILANDA on 16/8/2018.
 */
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String URL = "http://codecult.tech/";
    private ClassBuku m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Gson gson = new GsonBuilder().create();



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        api_bookslife data = retrofit.create(api_bookslife.class);
        Call<List<ClassBuku>> tampil = data.getAll();
        tampil.enqueue(new Callback<List<ClassBuku>>() {
            @Override
            public void onResponse(Call<List<ClassBuku>> call, Response<List<ClassBuku>> response) {
                final List<ClassBuku> data = response.body();


                RecyclerView tampilkan = (RecyclerView) findViewById(R.id.recyclerview_id);
                final RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(MainActivity.this,data);
                tampilkan.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
                tampilkan.setAdapter(myAdapter);




            }

            @Override
            public void onFailure(Call<List<ClassBuku>> call, Throwable t) {

                Toast.makeText(getBaseContext(),String.valueOf("Tidak ada koneksi"),Toast.LENGTH_SHORT).show();

            }
        });
    }
}
