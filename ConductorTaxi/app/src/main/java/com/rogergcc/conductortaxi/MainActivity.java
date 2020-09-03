package com.rogergcc.conductortaxi;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.rogergcc.conductortaxi.model.Carrera;
import com.rogergcc.conductortaxi.model.Productos;
import com.rogergcc.conductortaxi.retrofit.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.socket.client.Ack;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    LatLng pos;
    GoogleMap mapa;

    Socket mSocket;


    Double latcli;
    Double loncli;
    String idcli;

    String miId;

    //region Runtime Permissions
    Button btncerca;
    private void openSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Required Permissions");
        builder.setMessage("This app require permission to use awesome feature. Grant them in app settings.");
        builder.setPositiveButton("Take Me To SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void requestMultiplePermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.ACCESS_COARSE_LOCATION

                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();

                            init();
                            initSocket();

                        }

                        // check for permanent denial of any permission
                        else if (report.isAnyPermissionPermanentlyDenied()) {
                            // check for permanent denial of any permission show alert dialog
                            // navigating to Settings
                            openSettingsDialog();
                        } else {

                            Toast.makeText(getApplicationContext(), "All permissions are not granted..",
                                    Toast.LENGTH_SHORT).show();
                        }

//                        if (report.isAnyPermissionPermanentlyDenied()) {
//                            // show alert dialog navigating to Settings
//                            openSettingsDialog();
//                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();

    }


    public void init() {
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);


    }

    public void initSocket() {
        mSocket = App.getSocket();
        mSocket.on("solicitudtaxi", solicitudtaxi);

        //mSocket.on("new_connetion", nuevaconnexion);


        mSocket.connect();

//        obtenerMiId();


        if (mSocket.connected()){
            btncerca = findViewById(R.id.btncerca);
            btncerca.setEnabled(true);
        }

    }

    //endregion

    public void obtenerMiId() {
        JSONObject misdatos = new JSONObject();
        try {
            misdatos.put("cod", "-");

        } catch (JSONException e) {
            Log.e("JSONExceptionPresenter", e.toString());
        }
        mSocket.emit("misdatosconeccion", misdatos, new Ack() {
            @Override
            public void call(Object... args) {
                String res = (String) args[0];
                if (res.equals("OK")) Log.i("mi Datos Socket", "Se envio correctamente");
                else Log.i("mimensaje", "Hubo error en el envio");
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestMultiplePermission();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        pos = new LatLng(-18.011737, -70.253529);
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 17));
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mapa.setMyLocationEnabled(true);
            mapa.getUiSettings().setZoomControlsEnabled(false);
            mapa.getUiSettings().setCompassEnabled(true);
        }
    }

    public void mifinalizar(View view) {
        Button btnfinalizar = findViewById(R.id.btnfinalizar);
        btnfinalizar.setVisibility(View.INVISIBLE);

        Button btncerca = findViewById(R.id.btncerca);
        btncerca.setEnabled(false);

        EditText miedt = findViewById(R.id.miedt);

        Log.e("mi codigo conductor", mSocket.id());
        String socket_id_propio = mSocket.id();
        String socket_id_cliente = App.getidcliente();
        String nombre_conductor= miedt.getText().toString().trim();

        Log.e("socket_Id_Conductor", socket_id_propio);
        Log.e("socket_Id_Cliente", socket_id_cliente);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

//        Calendar cal = Calendar.getInstance();
        Date date = new Date();

        String fecha_finalizada = dateFormat.format((date));
        String hora_finalizada = dateFormat.format(date).toString().split(" ")[1];

        Carrera carrerafinalizada;
        carrerafinalizada = new Carrera(socket_id_propio,socket_id_cliente,nombre_conductor,hora_finalizada,fecha_finalizada);
        Call<String> call = Utils.getApi().newcarrera(carrerafinalizada);


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("carrera_finalizada: ","check:"+response.body());
                Toast.makeText(MainActivity.this, response.body(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("carrera_finalizada_f",t.toString());
            }
        });

        //Detiene el Servicio de Localizacion (OnDestroy) del ServicioLocalizacion
        stopService(new Intent(MainActivity.this,
                ServicioLocalizacion.class));

        JSONObject misdatos = new JSONObject();
        try {
            misdatos.put("id", App.getidcliente());
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

    private Emitter.Listener nuevaconnexion = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e("nueva conexion conduc", mSocket.id());
            miId=mSocket.id();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("r_nueva conexion conduc", mSocket.id());
                }
            });
        }
    };

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
                                                mapa.addMarker(new MarkerOptions().position(new LatLng(latcli, loncli)).title("Ubicacion Cliente"));

                                                JSONObject misdatos = new JSONObject();
                                                EditText miedt = findViewById(R.id.miedt);
                                                Button btnfinalizar = findViewById(R.id.btnfinalizar);
                                                btnfinalizar.setVisibility(View.VISIBLE);

                                                Button btncerca = findViewById(R.id.btncerca);
                                                btncerca.setEnabled(true);

                                                App.setidcliente(idcli);
                                                try {
                                                    misdatos.put("datotaxi", miedt.getText());
                                                    misdatos.put("id", idcli);
                                                } catch (JSONException e) {
                                                    Log.e("JSONExceptionPresenter", e.toString());
                                                }
                                                mSocket.emit("accept", misdatos, new Ack() {
                                                    @Override
                                                    public void call(Object... args) {
                                                        String res = (String) args[0];
                                                        if (res.equals("OK"))
                                                            Log.i("mimensaje", "Se envio correctamente");
                                                        else
                                                            Log.i("mimensaje", "Hubo error en el envio");
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
                        //AlertDialog alert = builder.create();

                        builder.create().show();
                        //alert.show();
                    }
                });
            } catch (JSONException e) {
                Log.e("JSONException", e.toString());
            }
        }
    };

    public void micerca(View view) {
        JSONObject misdatos = new JSONObject();
        EditText miedt = findViewById(R.id.miedt);
        if (mapa.getMyLocation() == null) {
            Toast.makeText(this, "no se ha encontrado su ubicación", Toast.LENGTH_SHORT).show();
            return;
        }

        App.setidcliente(idcli);
        try {
            misdatos.put("id", idcli);
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
