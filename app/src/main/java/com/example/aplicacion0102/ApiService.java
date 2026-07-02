package com.example.aplicacion0102;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @GET("objects")
    Call<List<Device>> getObjects();

    @POST("objects")
    Call<Device> createObject(@Body Device newDevice);
}