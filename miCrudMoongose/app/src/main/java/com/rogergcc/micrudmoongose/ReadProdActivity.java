package com.rogergcc.micrudmoongose;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rogergcc.micrudmoongose.model.Productos;
import com.rogergcc.micrudmoongose.retrofit.servicesRetrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ReadProdActivity extends AppCompatActivity {

    Retrofit retrofit; servicesRetrofit miserviceretrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_prod);
        final String url = "http://esiscrud2020.eu-4.evennode.com/";
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        miserviceretrofit = retrofit.create(servicesRetrofit.class);
        listar();
    }
    private void listar() {
        Call<List<Productos>> call = miserviceretrofit.getlist();
        call.enqueue(new Callback<List<Productos>>() {
            @Override
            public void onResponse(Call<List<Productos>> call, Response<List<Productos>> response) {
                Log.e("mirespuesta: ", response.toString());
                for(Productos res : response.body()) {
                    Log.e("mirespuesta: ","id="+res.getId()+" prod="+res.getName() +" precio"+res.getPrice());
                }
            }
            @Override
            public void onFailure(Call<List<Productos>> call, Throwable t) {
                Log.e("onFailure", t.toString());// mostrmos el error
            }
        });
    }
}

