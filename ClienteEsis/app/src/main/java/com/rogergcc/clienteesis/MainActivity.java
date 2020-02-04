package com.rogergcc.clienteesis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Ack;
import io.socket.client.Socket;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    LatLng pos;
    GoogleMap mapa;

    Socket mSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
        mSocket = App.getSocket();
//mSocket.on("taxiencontrado", taxiencontrado);
//mSocket.on("localizacion",localizacion);
//mSocket.on("Abordo",abordo);
        mSocket.connect();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        pos = new LatLng(-18.011737, -70.253529);
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(pos,17));
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mapa.setMyLocationEnabled(true);
            mapa.getUiSettings().setZoomControlsEnabled(false);
            mapa.getUiSettings().setCompassEnabled(true);
        }
    }

    public void pedir(View view) {
        if (mapa.getMyLocation() != null) {
            JSONObject miubicacion = new JSONObject();
            try {
                miubicacion.put("latitude", mapa.getMyLocation().getLatitude());
                miubicacion.put("longitude", mapa.getMyLocation().getLongitude());
            } catch (JSONException e) {
                Log.e("JSONExceptionPresenter", e.toString());
            }
            mSocket.emit("pedirtaxi", miubicacion, new Ack() {
                @Override
                public void call(Object... args) {
                    String res = (String) args[0];
                    if (res.equals("OK")) Log.i("mimensaje", "Se envio correctamente");
                    else Log.i("mimensaje", "Hubo error en el envio");
                }
            });
        }
        else
            Toast.makeText(this,"no se ha encontrado su ubicación",Toast.LENGTH_SHORT).show();
    }


}