package com.example.fieldbuzz;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APILogin {
    @POST("login")
    Call<User>login(@Body User user);

}
