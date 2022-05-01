package de.devisnik.android.mine.device;

import android.os.Build;

public abstract class BaseDevice implements Device {

	@Override
	public int getBuildVersion() {
		return Build.VERSION.SDK_INT;
	}
}
