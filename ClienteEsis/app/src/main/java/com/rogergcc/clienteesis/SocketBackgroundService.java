package com.rogergcc.clienteesis;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * Created by rogergcc on 11/02/2020.
 * Copyright Ⓒ 2020 . All rights reserved.
 */
public class SocketBackgroundService extends Service {

    private SocketThread mSocketThread;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        mSocketThread = SocketThread.getInstance();
    }


    @Override
    public void onDestroy() {
        //stop thread and socket connection here
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mSocketThread.startThread()) {
        } else {
            stopSelf();
        }

        return START_STICKY;
    }
}

