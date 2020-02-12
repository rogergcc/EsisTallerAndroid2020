package com.rogergcc.micrudmoongose;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rogergcc.micrudmoongose.model.Productos;
import com.rogergcc.micrudmoongose.retrofit.servicesRetrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CreateProdActivity extends AppCompatActivity {

    Retrofit retrofit; servicesRetrofit miserviceretrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_prod);
        final String url = "http://esiscrud2020.eu-4.evennode.com/";
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        miserviceretrofit = retrofit.create(servicesRetrofit.class);
    }
    public void nuevoproducto(View view) {
        EditText edtnombre; EditText edtprecio;
        edtnombre=findViewById(R.id.edtnomprod); edtprecio=findViewById(R.id.edtprecio);
        final Productos producto= new Productos(edtnombre.getText().toString(),
                Double.parseDouble(edtprecio.getText().toString()));
        Call<String> call = miserviceretrofit.newproducto(producto);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("newproducto: ","check:"+response.body());
                Toast.makeText(CreateProdActivity.this, response.body(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("newproducto",t.toString());
            }
        });
    }

}
