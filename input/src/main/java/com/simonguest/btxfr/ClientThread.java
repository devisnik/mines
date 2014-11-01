package com.simonguest.btxfr;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ClientThread extends Thread {
    private final String TAG = "android-btxfr/ClientThread";
    private final BluetoothSocket socket;
    private final Handler handler;
    public Handler incomingHandler;

    public ClientThread(BluetoothDevice device, Handler handler) {
        BluetoothSocket tempSocket = null;
        this.handler = handler;

        try {
            tempSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(Constants.UUID_STRING));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        this.socket = tempSocket;
    }

    public void run() {
        try {
            Log.v(TAG, "Opening client socket");
            socket.connect();
            Log.v(TAG, "Connection established");
            handler.sendEmptyMessage(MessageType.READY_FOR_DATA);

        } catch (IOException ioe) {
            handler.sendEmptyMessage(MessageType.COULD_NOT_CONNECT);
            Log.e(TAG, ioe.toString());
            try {
                socket.close();
            } catch (IOException ce) {
                Log.e(TAG, "Socket close exception: " + ce.toString());
            }
        }

        Looper.prepare();

        incomingHandler = new Handler(){
            @Override
            public void handleMessage(Message message)
            {
                if (message.obj != null)
                {
                    Log.v(TAG, "Handle received data to send");
                    byte[] payload = (byte[])message.obj;

                    try {
                        handler.sendEmptyMessage(MessageType.SENDING_DATA);
                        OutputStream outputStream = socket.getOutputStream();

                        // now write the data
                        outputStream.write(payload);
                        outputStream.flush();

                        handler.sendEmptyMessage(MessageType.DATA_SENT_OK);


                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }

                }
            }
        };

        Looper.loop();
    }

    public void cancel() {
        try {
            if (socket.isConnected()) {
                socket.close();
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}