package com.simonguest.btxfr;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

class DataTransferThread extends Thread {
    private final String TAG = "android-btxfr/DataTransferThread";
    private final BluetoothSocket socket;
    private Handler handler;

    public DataTransferThread(BluetoothSocket socket, Handler handler) {
        this.socket = socket;
        this.handler = handler;
    }

    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();

            byte[] payload = new byte[1];

            while (true) {
                try {
                    int bytesRead = inputStream.read(payload, 0, 1);
                } catch (IOException e){
                    Log.d(TAG, "error reading data", e);
                    break;
                }



                Log.v(TAG, "Digest matches OK.");
                Message message = new Message();
                message.obj = payload;
                message.what = MessageType.DATA_RECEIVED;
                handler.sendMessage(message);
            }

            Log.v(TAG, "Closing server socket");
            socket.close();

        } catch (Exception ex) {
            Log.d(TAG, ex.toString());
        }
    }
}
