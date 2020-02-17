package com.rogergcc.clienteesis;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by rogergcc on 11/02/2020.
 * Copyright Ⓒ 2020 . All rights reserved.
 */
public class SocketBackgroundService extends Service {

    private SocketThread mSocketThread;

    Socket mSocket; {
        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }

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

