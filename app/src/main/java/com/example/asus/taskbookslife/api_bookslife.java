package com.example.asus.taskbookslife;

/**
 * Created by Wilanda on 16/8/2018.
 */
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface api_bookslife {

    @GET("bookslife/")
    Call<List<ClassBuku>> getAll();
}
