package de.devisnik.android.mine.device;

import android.os.Build;

public abstract class BaseDevice implements IDevice {

	@Override
	public int getBuildVersion() {
		return Build.VERSION.SDK_INT;
	}
}
