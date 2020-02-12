package com.rogergcc.micrudmoongose;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rogergcc.micrudmoongose.retrofit.Utils;
import com.rogergcc.micrudmoongose.retrofit.servicesRetrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Retrofit retrofit;
    servicesRetrofit miserviceretrofit;
    Button btn_create, btn_read, btn_update,btn_delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String url = "http://esiscrud2020.eu-4.evennode.com/";
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        miserviceretrofit = retrofit.create(servicesRetrofit.class);

        btn_create = findViewById(R.id.btn_create);
        btn_read   = findViewById(R.id.btn_read);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);


        btn_create.setOnClickListener(this);
        btn_read.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        btn_delete.setOnClickListener(this);

        checkserver();
    }
    public void checkserver() {

//        Call<String> call = Utils.getApi().getcheck();

        Call<String> call = miserviceretrofit.getcheck();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("mirespuesta: ", response.toString());
                Log.e("mirespuesta: ", response.body());
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("onFailure", t.toString());
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()){

            case R.id.btn_create:
                 intent = new Intent(this, CreateProdActivity.class);
                this.startActivity(intent);
                break;

            case R.id.btn_read:
                 intent = new Intent(this, ReadProdActivity.class);
                this.startActivity(intent);
                break;

            case R.id.btn_update:
                intent = new Intent(this, UpdateProdActivity.class);
                this.startActivity(intent);
                break;

            case R.id.btn_delete:
                intent = new Intent(this, DeleteProdActivity.class);
                this.startActivity(intent);
                break;

            default:
        }

    }
}
