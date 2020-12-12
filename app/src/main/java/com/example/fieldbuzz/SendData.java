package com.example.fieldbuzz;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface SendData {
    @POST("recruiting-entities")
    Call<Data> getData(@Header("Authorization") String token, @Body Data data);
}
