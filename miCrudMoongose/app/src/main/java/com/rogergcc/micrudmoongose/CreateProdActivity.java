package com.rogergcc.micrudmoongose;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
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

    TextInputLayout pro_nombre, pro_precio;
    FloatingActionButton fab_addproduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_prod);

        if(getActionBar()==null){
            String title = getResources().getString(R.string.title_create);
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

        fab_addproduct = findViewById(R.id.add);


    }

    public void nuevoproducto(View view) {

        EditText edtnombre, edtprecio;
        edtnombre=findViewById(R.id.edtnomprod);
        edtprecio=findViewById(R.id.edtprecio);
        pro_nombre = findViewById(R.id.title_product);
        pro_precio = findViewById(R.id.title_product_precio);

        pro_nombre.setError(null);
        pro_precio.setError(null);

        if (edtnombre.getText().toString().equals("")) {
            pro_nombre.setError("Ingrese producto!");
            return;
        }
        else if (edtprecio.getText().toString().equals("")) {
            pro_precio.setError("Ingrese precio!");
            return;
        }

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

    public void add_product(View view) {
        EditText edtnombre,edtprecio;
        edtnombre=findViewById(R.id.edtnomprod);
        edtprecio=findViewById(R.id.edtprecio);
        pro_nombre.setError(null);
        pro_precio.setError(null);

        if (edtnombre.getText().toString().equals("")) {
            pro_nombre.setError("Title cannot be empty!");
        }
        else if (edtprecio.getText().toString().equals("")) {
            pro_precio.setError("Agenda cannot be empty!");
        }

        else {
//            addEvent();
        }
    }
}
