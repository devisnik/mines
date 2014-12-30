package de.devisnik.android.mine;

import de.devisnik.android.mine.device.DeviceFactory;
import de.devisnik.android.mine.device.IDevice;
import android.app.Application;
import android.preference.PreferenceManager;

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
