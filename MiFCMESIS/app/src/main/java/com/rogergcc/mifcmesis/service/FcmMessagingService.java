package com.rogergcc.mifcmesis.service;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rogergcc.mifcmesis.MainActivity;

import java.util.Map;

/**
 * Created by ROGERGCC on 17/02/2020.
 */
public class FcmMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FcmMessagingService";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        Log.e("newToken_ID", token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // Muestra una notificación directamente al usuario
        try {
            sendNotification(remoteMessage);
        } catch (Exception e) {
            Log.e(TAG, "Error al mostrar la notificación: " + e.getMessage());
        }
    }


    @Override
    public void handleIntent(@NonNull Intent intent) {
        super.handleIntent(intent);
        Log.i(TAG, "handleIntent: " + intent);
        Bundle bundleData = intent.getExtras();
        Log.i(TAG, "handleIntent: bundleData" + bundleData);
        RemoteMessage remoteMessage = new RemoteMessage(bundleData);
//        if (!remoteMessage.getData().toString().isEmpty()) {
//                sendNotification(remoteMessage);
//        }

        if (remoteMessage.getData() != null) {
            Map<String, String> data = remoteMessage.getData();

            // Extraer los detalles del pedido desde los datos del mensaje
            String message = data.get("message");
            String descuento = data.get("descuento");
//            String customerName = data.get("customer_name");

            // Realizar acciones adicionales, como actualizar la base de datos local
//            try {
//                updateLocalDatabase(message, descuento, customerName);
//            } catch (Exception e) {
//                Log.e(TAG, "Error al actualizar la base de datos local: " + e.getMessage());
//            }
        }
    }


    private void sendNotification(RemoteMessage remoteMessage) {

//        Intent intent = new Intent(this, MainActivity.class);
        try {
            Map<String, String> data = remoteMessage.getData();
            String titleData = data.get("title");
            String messageData = data.get("message");
            Log.i(TAG, "sendNotification: DATA getTitle " + titleData);

            String notificationTitle = remoteMessage.getNotification().getTitle();
            String notificationBody = remoteMessage.getNotification().getBody();
            Log.i(TAG, "sendNotification: NOTIFI getTitle: " + notificationTitle);


            // Muestra la notificación (puedes usar la clase NotificationUtils que mencionamos anteriormente).
            NotificationUtils.showNotification(getApplicationContext(), notificationTitle, notificationBody);

        } catch (Exception e) {
            Log.e(TAG, "sendNotification: " + e.getMessage() );
            e.printStackTrace();
        }
    }
}