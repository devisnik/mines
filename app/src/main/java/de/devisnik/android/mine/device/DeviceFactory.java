package de.devisnik.android.mine.device;

import android.content.Context;
import android.os.Build;

public final class DeviceFactory {

	private static IDevice INSTANCE;

	private static IDevice create(final Context context) {
		int versionNumber = Integer.parseInt(Build.VERSION.SDK);
		if (versionNumber < 11)
			return new PreHoneycombDevice();
		return new PostHoneycombDevice(context.getApplicationContext());
	}

	public static IDevice get(final Context context) {
		if (INSTANCE == null)
			INSTANCE = create(context);
		return INSTANCE;
	}
}
