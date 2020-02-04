package com.rogergcc.mychat;


import android.app.Application;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by rogergcc on 4/02/2020.
 * Copyright Ⓒ 2020 . All rights reserved.
 */
public class App extends Application {
    private static Socket mSocket;
    @Override
    public void onCreate() {
        super.onCreate();
//Se crea un socket que apunta a la ruta que se muestra
        try {
//            mSocket = IO.socket("http://wiltestchat123.eu-4.evennode.com/");
            mSocket = IO.socket("http://esischatrogercc.eu-4.evennode.com/");
        }
        catch (Exception e){
        }
    }
    public static Socket getSocket() {
        return mSocket;
    }

}
