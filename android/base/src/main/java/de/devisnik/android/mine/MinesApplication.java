package de.devisnik.android.mine;

import android.app.Application;
import android.preference.PreferenceManager;

import de.devisnik.android.mine.device.DeviceFactory;
import de.devisnik.android.mine.device.IDevice;

public class MinesApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
	}

	public IDevice getDevice() {
		return DeviceFactory.get(this);
	}
}
