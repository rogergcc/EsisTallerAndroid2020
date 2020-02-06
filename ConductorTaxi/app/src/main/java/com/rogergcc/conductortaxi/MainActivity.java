package com.rogergcc.conductortaxi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Ack;
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

    public void mifinalizar(View view) {
        Button btnfinalizar=findViewById(R.id.btnfinalizar);
        btnfinalizar.setVisibility(View.INVISIBLE);

        //Detiene el Servicio de Localizacion (OnDestroy) del ServicioLocalizacion
        stopService(new Intent(MainActivity.this,
                ServicioLocalizacion.class));

        JSONObject misdatos = new JSONObject();
        try {
            misdatos.put("id",App.getidcliente());
        } catch (JSONException e) {
            Log.e("JSONExceptionPresenter", e.toString());
        }
        mSocket.emit("abordo", misdatos, new Ack() {
            @Override
            public void call(Object... args) {
                String res = (String) args[0];
                if (res.equals("OK")) Log.i("mimensaje", "Se envio correctamente");
                else Log.i("mimensaje", "Hubo error en el envio");
            }
        });
    }

    //Emitter Listener Recibe los datos
    private Emitter.Listener solicitudtaxi = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
// Se crea un JSONObjet
            JSONObject paramsRequest = (JSONObject) args[0];
            try {
                latcli = paramsRequest.getDouble("latitude");
                loncli = paramsRequest.getDouble("longitude");

                //socket id del cliente
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

                                                JSONObject misdatos = new JSONObject();
                                                EditText miedt=findViewById(R.id.miedt);
                                                Button btnfinalizar=findViewById(R.id.btnfinalizar);
                                                btnfinalizar.setVisibility(View.VISIBLE);

                                                Button btncerca=findViewById(R.id.btncerca);
                                                btncerca.setEnabled(true);


                                                App.setidcliente(idcli);
                                                try {
                                                    misdatos.put("datotaxi", miedt.getText());
                                                    misdatos.put("id",idcli);
                                                } catch (JSONException e) {
                                                    Log.e("JSONExceptionPresenter", e.toString());
                                                }
                                                mSocket.emit("accept", misdatos, new Ack() {
                                                    @Override
                                                    public void call(Object... args) {
                                                        String res = (String) args[0];
                                                        if (res.equals("OK")) Log.i("mimensaje", "Se envio correctamente");
                                                        else Log.i("mimensaje", "Hubo error en el envio");
                                                    }
                                                });


                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                    startForegroundService(new Intent(MainActivity.this, ServicioLocalizacion.class));
                                                } else {
                                                    startService(new Intent(MainActivity.this,
                                                            ServicioLocalizacion.class));
                                                }

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

    public void micerca(View view) {



        JSONObject misdatos = new JSONObject();
//        try {
//            misdatos.put("id",App.getidcliente());
//        } catch (JSONException e) {
//            Log.e("JSONExceptionPresenter", e.toString());
//        }
//        mSocket.emit("cerca", misdatos, new Ack() {
//            @Override
//            public void call(Object... args) {
//                String res = (String) args[0];
//                if (res.equals("OK")) Log.i("mimensaje", "Se envio correctamente");
//                else Log.i("mimensaje", "Hubo error en el envio");
//            }
//        });
        EditText miedt=findViewById(R.id.miedt);


        if (mapa.getMyLocation() ==null){
            Toast.makeText(this, "no se ha encontrado su ubicación", Toast.LENGTH_SHORT).show();
            return;
        }

        App.setidcliente(idcli);
        try {
            misdatos.put("id",idcli);
            misdatos.put("nombre_conductor", miedt.getText());
            misdatos.put("latitude", mapa.getMyLocation().getLatitude());
            misdatos.put("longitude", mapa.getMyLocation().getLongitude());
        } catch (JSONException e) {
            Log.e("JSONExceptionPresenter", e.toString());
        }
        mSocket.emit("cerca", misdatos, new Ack() {
            @Override
            public void call(Object... args) {
                String res = (String) args[0];
                if (res.equals("OK")) Log.i("mimensaje", "Se envio correctamente");
                else Log.i("mimensaje", "Hubo error en el envio");
            }
        });
    }
}
