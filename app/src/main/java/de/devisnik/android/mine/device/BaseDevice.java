package de.devisnik.android.mine.device;

import android.os.Build;

public abstract class BaseDevice implements IDevice {

	private final int mBuildVersion;

	public BaseDevice() {
		mBuildVersion = Integer.parseInt(Build.VERSION.SDK);
	}

	@Override
	public int getBuildVersion() {
		return mBuildVersion;
	}

}
