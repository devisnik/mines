package de.devisnik.android.mine.input;

import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.simonguest.btxfr.MessageType;
import com.simonguest.btxfr.ServerThread;

import java.util.HashSet;
import java.util.Set;

public class RemoteMyoInputDevice implements InputDevice {

    private static final String TAG = "RemoteMyoInputDevice";

    private Set<Listener> listeners = new HashSet<Listener>();
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessageType.DATA_RECEIVED: {
                    Log.d(TAG, "data received");
                    handleEvent(((byte[])msg.obj)[0]);
                    break;
                }

                case MessageType.DIGEST_DID_NOT_MATCH: {
                    Log.d(TAG, "didn't match");
                    break;
                }

                case MessageType.DATA_PROGRESS_UPDATE: {
                    Log.d(TAG, "progress update");
                    break;
                }

                case MessageType.INVALID_HEADER: {
                    Log.d(TAG, "invalid header");
                    break;
                }

            }
        }
    };

    private void handleEvent(int eventCode) {
        Event event = Event.from(eventCode);
        for (Listener listener : listeners) {
            switch (event) {
                case LEFT: listener.onLeft();
                    break;
                case RIGHT: listener.onRight();
                    break;
                case UP: listener.onUp();
                    break;
                case DOWN: listener.onDown();
                    break;
                case CLICK: listener.onClick();
                    break;
                case DOUBLE_CLICK: listener.onDoubleClick();
            }
        }
    }

    private ServerThread serverThread;

    @Override
    public void addListener(Listener listener) {
        listeners.add(listener);
        if (listeners.size() == 1){
            startServer();
        }
    }


    @Override
    public void removeListener(Listener listener) {
        listeners.remove(listener);
        if (listeners.size() == 0){
            stopServer();
        }
    }

    private void startServer() {
        serverThread  = new ServerThread(BluetoothAdapter.getDefaultAdapter(), handler);
        serverThread.start();
    }

    private void stopServer() {
        if (serverThread != null) {
            serverThread.cancel();
        }
    }



}
