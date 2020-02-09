package com.rogergcc.clienteesis;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import io.socket.client.Ack;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback
        , LocationListener {
    LatLng pos;
    GoogleMap mapa;

    Socket mSocket;
    Button btn_pedir_taxi;

    // Variables
    private String[] multiple_permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private String miconductor="";
    private Marker mimarker;
    private String TAG ="MainAct";


    Double latcli;
    Double loncli;
    DecimalFormat df = new DecimalFormat("#.00");
    private Uri notification;
    private Ringtone r;


    private void locationEnabled() {
        LocationManager lm = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            if (lm != null) {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (lm != null) {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (!gps_enabled && !network_enabled) {
//            new AlertDialog.Builder(MainActivity. this )
//                    .setMessage( "GPS Enable" )
//                    .setPositiveButton( "Settings" , new
//                            DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick (DialogInterface paramDialogInterface , int paramInt) {
//                                    startActivity( new Intent(Settings. ACTION_LOCATION_SOURCE_SETTINGS )) ;
//                                }
//                            })
//                    .setNegativeButton( "Cancel" , null )
//                    .show() ;
//        }

        if (!gps_enabled) {
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("GPS Enable")
                    .setPositiveButton("Settings", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

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

    private void requestSinglePermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // permission is granted
                        Toast.makeText(MainActivity.this, "permission granted..",
                                Toast.LENGTH_SHORT).show();

//                        turnGPSOn();
                        //turnGpsOn(MainActivity.this);
                        locationEnabled();
                        init();
                        initSocket();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            openSettingsDialog();
                        }
//                        else {
//                            Toast.makeText(MainActivity.this, "permission not granted..",
//                                    Toast.LENGTH_SHORT).show();
//
//                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission,
                                                                   PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
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

//                            turnGPSOn();
//                            turnGpsOn(MainActivity.this);
//                            locationEnabled();
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


    private void turnGPSOn() {
        Log.e("Tag", "turnGPSOn method ");
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Toolbar :: Transparent
        toolbar.setBackgroundColor(Color.TRANSPARENT);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("EsisUber");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Status bar :: Transparent
        Window window = this.getWindow();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        btn_pedir_taxi = findViewById(R.id.btn_pedir_taxi);
        requestMultiplePermission();
//        requestSinglePermission();


    }

    public void initSocket() {
        mSocket = App.getSocket();

        mSocket.on("taxiencontrado", taxiencontrado);
        mSocket.on("localizacion", localizacion);
        mSocket.on("Abordo", abordo);
        mSocket.on("taxiCerca", taxiCerca);

        mSocket.connect();
    }

    public void init() {
        notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        btn_pedir_taxi.setEnabled(true);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mapa.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.uber_style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        pos = new LatLng(-18.011737, -70.253529);
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 17));
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

            mapa.setMyLocationEnabled(true);



            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            if (locationManager != null) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);
            }
            //Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {

//            double myCurrentLatitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
//
//            double myCurrentLongitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
                locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                List<String> providers = locationManager.getProviders(true);
                Location bestLocation = null;
                for (String provider : providers) {
                    Location l = locationManager.getLastKnownLocation(provider);
                    if (l == null) {
                        continue;
                    }
                    if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                        // Found best last known location: %s", l);
                        bestLocation = l;
                    }
                }
                CameraPosition myCurrentLocation = CameraPosition.builder()
                        .target(new LatLng(bestLocation.getLatitude(), bestLocation.getLongitude()))
                        .zoom(17)
                        .bearing(0)
                        .tilt(2)
                        .build();
                mapa.moveCamera(CameraUpdateFactory.newCameraPosition(myCurrentLocation));
            }

            mapa.getUiSettings().setZoomControlsEnabled(false);
//            mapa.getUiSettings().setCompassEnabled(true);

//            mapaRGCC.getUiSettings().setScrollGesturesEnabled(true);
        }
    }

    public void pedir(View view) {

        locationEnabled();

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
        } else
            Toast.makeText(this, "no se ha encontrado su ubicación", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        String hasta = latLng.toString().substring(9);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private Emitter.Listener taxiencontrado = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i("misdatos","taxiencontrado");
            final JSONObject paramsRequest = (JSONObject) args[0];
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.i("misdatos","check:"+paramsRequest);
                        miconductor = paramsRequest.getString("datotaxi");
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Felicidades el señor " + miconductor + " atendera su pedido")
                                .setTitle("Conductor encontrado")
                                .setCancelable(false)
                                .setNeutralButton("Aceptar",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } catch(
                            JSONException e)
                    {
                        Log.e("JSONException", e.toString());
                    }
                }
            });
        }
    };

    private Emitter.Listener localizacion = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i("localizacion","nuevalocalizacion");
            JSONObject paramsRequest = (JSONObject) args[0];
            final Double latcond,loncond;
            try {
                Log.i("localizacion","nuevalocalizacion:"+paramsRequest.toString());
                latcond = paramsRequest.getDouble("lat");
                loncond = paramsRequest.getDouble("lon");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mimarker!=null)
                        {
                            mimarker.remove();
                        }
                        mimarker= mapa.addMarker(new MarkerOptions().position(new LatLng(latcond, loncond))
                                .icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).title(miconductor));
                    }
                });
            } catch (JSONException e) {
                Log.e("JSONException", e.toString());
            }
        }
    };

    private Emitter.Listener abordo = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Que tenga un buen viaje")
                            .setTitle("Gracias por su preferencia")
                            .setCancelable(false)
                            .setNeutralButton("Aceptar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }
    };

    private Emitter.Listener taxiCerca = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            final float[] distance = new float[2];
//            Location.distanceBetween(userLocation.latitude, userLocation.longitude,
//                    latituCirculo, loongCirculo, distance);
//            radioDeZonaTrabajo[0] = circle.getRadius();
//
//            userObtenerID = zonasList.get(i).getUsuarioID();
//
//            if ((distance[0] < radio) && codigoUsuario.equals(userObtenerID)) {
//                habilitarAcceso = true;
//                codigoZonaTrabajo=zonasList.get(i).getZonaTrabajoId();
//            } else {
//
//                habilitarAcceso = (habilitarAcceso) ? habilitarAcceso : false;
//            }

            Log.i("misdatostaxicerca","el taxi esta cerca");
            final JSONObject paramsRequest = (JSONObject) args[0];
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // alarmSound = ringtoneMgr.getRingtoneUri(alarmChosen);
//                    Ringtone r = RingtoneManager.getRingtone(getActivity(), alarmSound);
//                    r.play();

                    try {

                        r.play();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        Log.e("misdatos taxicerca","check:"+paramsRequest);

                        miconductor = paramsRequest.getString("nombre_conductor");

                        latcli = paramsRequest.getDouble("latitude");
                        loncli = paramsRequest.getDouble("longitude");

                        if (mapa.getMyLocation() ==null){
                            Toast.makeText(getApplicationContext(), "No se encontro su Ubicación", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Location.distanceBetween(
                                mapa.getMyLocation().getLatitude(), mapa.getMyLocation().getLongitude(),
                                latcli, loncli, distance);


                        float ditancetwodecimal = 0.0f;
                        ditancetwodecimal = Float.valueOf(df.format(distance[0]));
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("El Taxista " + miconductor + " esta cerca a :" +ditancetwodecimal+" mts")
                                .setTitle("Conductor cerca")
                                .setCancelable(false)
                                .setNeutralButton(" OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } catch(
                            JSONException e)
                    {
                        Log.e("JSONException", e.toString());
                    }
                }
            });
        }
    };

}