package com.rogergcc.conductortaxi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    LatLng pos;
    GoogleMap mapa;

    Socket mSocket;


    Double latcli;
    Double loncli;
    String idcli;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
        mSocket = App.getSocket();
        mSocket.on("solicitudtaxi", solicitudtaxi);
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

    private Emitter.Listener solicitudtaxi = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
// Se crea un JSONObjet
            JSONObject paramsRequest = (JSONObject) args[0];
            try {
                latcli = paramsRequest.getDouble("latitude");
                loncli = paramsRequest.getDouble("longitude");
                idcli = paramsRequest.getString("socket");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Nueva solicitud de servicio desea aceptar")
                                .setTitle("SOLICITUD DE SERVICIO")
                                .setCancelable(true)
                                .setNeutralButton("Aceptar",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                mapa.addMarker(new MarkerOptions().position(new LatLng(latcli,loncli)).title("Ubicacion Cliente"));
                                            }
                                        })
                                .setNegativeButton("Rechazar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
            } catch (JSONException e) {
                Log.e("JSONException", e.toString());
            }
        }
    };
}
