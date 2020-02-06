package com.rogergcc.imagenesesis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_cargar_imagen_picasso,btn_cargar_imagen_loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_cargar_imagen_picasso = findViewById(R.id.btn_cargar_imagen_picasso);
        btn_cargar_imagen_loader = findViewById(R.id.btn_cargar_imagen_loader);

        btn_cargar_imagen_picasso.setOnClickListener(this);
        btn_cargar_imagen_loader.setOnClickListener(this);
        Log.i("fasf","main");
    }



    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.btn_cargar_imagen_loader: /** Start a new Activity MyCards.java */
                Intent intent = new Intent(this, MiImageLoader.class);
                this.startActivity(intent);
                break;

            case R.id.btn_cargar_imagen_picasso: /** AlerDialog when click on Exit */
                Intent intent2 = new Intent(this, MiPicasso.class);
                this.startActivity(intent2);
                break;

                default:
        }
    }
}
