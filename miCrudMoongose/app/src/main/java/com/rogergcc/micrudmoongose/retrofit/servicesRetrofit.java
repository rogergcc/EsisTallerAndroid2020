package com.rogergcc.micrudmoongose.retrofit;

import com.rogergcc.micrudmoongose.model.Productos;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by rogergcc on 11/02/2020.
 * Copyright Ⓒ 2020 . All rights reserved.
 */

public interface servicesRetrofit {
    @GET("products/test")
    Call<String> getcheck();

    @POST("products/create")
    Call<String> newproducto(@Body Productos producto);

    @GET("products/list")
    Call<List<Productos>> getlist();

    @GET("/products/buscar/{name}")
    Call < Productos > getproducto(@Path("name") String name);

    @PUT("/products/{id}/update")
    Call<String> updateproducto(@Path("id") String id, @Body Productos producto);

    @DELETE("/products/{id}/delete")
    Call<String> deleteproducto(@Path("id") String id);
}