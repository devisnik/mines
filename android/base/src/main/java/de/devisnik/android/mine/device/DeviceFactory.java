package de.devisnik.android.mine.device;

import android.content.Context;

public final class DeviceFactory {

	public static IDevice create(final Context context) {
		return new PostHoneycombDevice(context.getApplicationContext());
	}
}
