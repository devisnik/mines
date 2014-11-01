package de.devisnik.android.mine.input;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.simonguest.btxfr.ClientThread;
import com.simonguest.btxfr.MessageType;
import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.scanner.ScanActivity;



public class MyoInputActivity extends Activity implements View.OnClickListener, Handler.Callback{

    private static final String TAG = "MyoInputActivity";
    private Handler handler = new Handler(this);
    private ClientThread clientThread;

    private DeviceListener listener = new AbstractDeviceListener() {
        @Override
        public void onConnect(Myo myo, long timestamp) {
            Log.d(TAG, "connected");
        }

        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            Log.d(TAG, "disconnected");
        }

        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {
            Log.d(TAG, "pose received " + pose);
            byte event = (byte) getEventFromPose(pose).getCode();
            if (event > 0) {
                Message message = new Message();
                message.obj = new byte[]{event};
                clientThread.incomingHandler.sendMessage(message);
            }
        }
    };
    private Pose lastEvent;

    private InputDevice.Event getEventFromPose(Pose pose) {
        InputDevice.Event event = InputDevice.Event.UNKNOWN;
        switch (pose){
            case WAVE_IN:
                event = InputDevice.Event.LEFT;
                lastEvent = pose;
                break;
            case WAVE_OUT:
                event = InputDevice.Event.RIGHT;
                lastEvent = pose;
                break;
            case FINGERS_SPREAD:
                event = InputDevice.Event.UP;
                lastEvent = pose;
                break;
            case FIST:
                if (lastEvent == Pose.THUMB_TO_PINKY){
                    lastEvent = Pose.UNKNOWN;
                    event = InputDevice.Event.CLICK;
                } else {
                    lastEvent = pose;
                    event = InputDevice.Event.DOWN;
                }
                break;
            case THUMB_TO_PINKY:
                if (lastEvent == Pose.THUMB_TO_PINKY) {
                    lastEvent = Pose.UNKNOWN;
                    event = InputDevice.Event.CLICK;
                } else {
                    lastEvent = pose;
                    event = InputDevice.Event.DOUBLE_CLICK;
                }
                break;

        }
        return event;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myo_input);
        findViewById(R.id.button_connect).setOnClickListener(this);
        findViewById(R.id.button_setup).setOnClickListener(this);
        findViewById(R.id.button_start).setOnClickListener(this);
        findViewById(R.id.button_remote_consumer).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_connect:
                Hub hub = Hub.getInstance();
                if (!hub.init(this)) {
                    Log.e(TAG, "Could not initialize the Hub.");
                    finish();
                    return;
                }
                Intent intent = new Intent(this, ScanActivity.class);
                startActivity(intent);
                break;
            case R.id.button_setup:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://thalmiclabs.com/setupgesture.html"));
                startActivity(intent);
                break;
            case R.id.button_start:
                if (clientThread == null) {
                    startClient();
                    ((TextView) findViewById(R.id.button_start)).setText(R.string.stop);
                } else {
                    stopClient();
                    ((TextView) findViewById(R.id.button_start)).setText(R.string.start);
                }
                break;
            case R.id.button_remote_consumer:
                new RemoteMyoInputDevice().addListener(new InputDevice.Listener() {
                    @Override
                    public void onLeft() {
                        Log.d(TAG, "left");
                    }

                    @Override
                    public void onRight() {
                        Log.d(TAG, "right");
                    }

                    @Override
                    public void onUp() {
                        Log.d(TAG, "up");
                    }

                    @Override
                    public void onDown() {
                        Log.d(TAG, "down");
                    }

                    @Override
                    public void onClick() {
                        Log.d(TAG, "click");
                    }

                    @Override
                    public void onDoubleClick() {
                        Log.d(TAG, "doubleClick");
                    }
                });
        }
    }

    private void stopClient() {
        if (clientThread != null){
            clientThread.cancel();
            clientThread = null;
        }
        Hub.getInstance().removeListener(listener);
    }

    private void startClient() {
        clientThread = new ClientThread(getBluetoothInputConsumer(), handler);
        clientThread.start();
    }

    private BluetoothDevice getBluetoothInputConsumer() {
        for (BluetoothDevice device : BluetoothAdapter.getDefaultAdapter().getBondedDevices()){
            if (device.getName().contains("BT-200")){
                return device;
            }
        }
        throw new RuntimeException("Moverio not found");
    }

    @Override
    public boolean handleMessage(Message message) {
        Log.d(TAG, "msg received: " + message);

        switch (message.what) {
            case MessageType.READY_FOR_DATA:
                Hub.getInstance().addListener(listener);
                return true;

        }
        return false;
    }


}
