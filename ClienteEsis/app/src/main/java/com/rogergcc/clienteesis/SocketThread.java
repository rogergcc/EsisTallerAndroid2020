package com.rogergcc.clienteesis;

/**
 * Created by rogergcc on 11/02/2020.
 * Copyright Ⓒ 2020 . All rights reserved.
 */
public class SocketThread extends Thread {

    private static SocketThread mSocketThread;
    private SocketClient mSocketClient;

    private SocketThread() {
    }

    // create single instance of socket thread class
    public static SocketThread getInstance() {
        if (mSocketThread == null)//you can use synchronized also
        {
            mSocketThread = new SocketThread();
        }
        return mSocketThread;
    }


    public boolean startThread() {
        mSocketClient=new SocketClient();
        if (mSocketClient.isConnected()) {
            mSocketThread.start();
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        super.run();
        while (mSocketClient.isConnected()) {
            // continue listen
        }
        // otherwise remove socketClient instance and stop thread
    }

    public class SocketClient {
        //write all code here regarding opening, closing sockets
        //create constructor
        public SocketClient() {
            // open socket connection here
        }

        public boolean isConnected() {
            return true;
        }
    }
}