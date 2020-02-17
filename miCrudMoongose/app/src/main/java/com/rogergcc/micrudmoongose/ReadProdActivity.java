package com.rogergcc.micrudmoongose;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rogergcc.micrudmoongose.adapter.DataAdapter;
import com.rogergcc.micrudmoongose.model.Productos;
import com.rogergcc.micrudmoongose.retrofit.servicesRetrofit;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ReadProdActivity extends AppCompatActivity {

    Retrofit retrofit;
    servicesRetrofit miserviceretrofit;

    RecyclerView recyclerView;
    ArrayList<Productos> productosList;
    DataAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_prod);

        if(getActionBar()==null){
            String title = getResources().getString(R.string.title_list);
            getSupportActionBar().setTitle(title);
        }

        final String url = "http://esiscrud2020.eu-4.evennode.com/";
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        miserviceretrofit = retrofit.create(servicesRetrofit.class);



        productosList = new ArrayList<>();

        recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        listar();

    }

    private void listar() {

        productosList.clear();
        Call<List<Productos>> call = miserviceretrofit.getlist();
        call.enqueue(new Callback<List<Productos>>() {
            @Override
            public void onResponse(Call<List<Productos>> call, Response<List<Productos>> response) {
                Log.e("mirespuesta: ", response.toString());


                    for (Productos res : response.body()) {
                        Log.e("mirespuesta: ", "id=" + res.getId() + " prod=" + res.getName() + " precio" + res.getPrice());

                    }
                    productosList.addAll(response.body());
                    dataAdapter = new DataAdapter(ReadProdActivity.this, productosList);
                    recyclerView.setAdapter(dataAdapter);

            }

            @Override
            public void onFailure(Call<List<Productos>> call, Throwable t) {
                Log.e("onFailure", t.toString());// mostrmos el error
            }
        });
    }
}

