/*-----------------------------------------------------------------------------
 - Developed by Haerul Muttaqin                                               -
 - Last modified 3/17/19 5:24 AM                                              -
 - Subscribe : https://www.youtube.com/haerulmuttaqin                         -
 - Copyright (c) 2019. All rights reserved                                    -
 -----------------------------------------------------------------------------*/
package com.rogergcc.conductortaxi.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

//import okhttp3.logging.HttpLoggingInterceptor;

public class CrudClient {

    //https://www.toptal.com/android/android-apps-mvvm-with-clean-architecture

    private static final String BASE_URL = "http://esiscrud2020.eu-4.evennode.com/";
//    private static final String BASE_URL = "http://192.168.0.23:5000/";



    public static Retrofit getFoodClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder().baseUrl(BASE_URL)
                .client(provideOkHttp())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addConverterFactory(new NullOnEmptyConverterFactory())
//                .addConverterFactory(GsonConverterFactory.create(getGsonViaBuilder()))
                .build();
    }

    private static Gson getGsonViaBuilder() {
        return new GsonBuilder()
                    .serializeNulls()

                    .create();
    }

//    private static Interceptor provideLoggingInterceptor() {
//        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
//    }

    private static OkHttpClient provideOkHttp() {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
//                .addNetworkInterceptor(provideLoggingInterceptor())
                .build();
    }
}
