package de.devisnik.android.mine;

import android.util.Log;

public class Logger {

	private static boolean ENABLED = true;
	private String itsTag;

	public Logger(Class<?> clazz) {
		itsTag = clazz.getSimpleName();
	}

	public int d(String msg) {
		if (ENABLED)
			return Log.d(itsTag, msg);
		return 0;
	}

	public int d(String msg, Throwable tr) {
		if (ENABLED)
			return Log.d(itsTag, msg, tr);
		return 0;
	}

	public int i(String msg) {
		if (ENABLED)
			return Log.i(itsTag, msg);
		return 0;
	}

	public int i(String msg, Throwable tr) {
		if (ENABLED)
			return Log.i(itsTag, msg, tr);
		return 0;
	}

	public int w(String msg) {
		if (ENABLED)
			return Log.w(itsTag, msg);
		return 0;
	}

	public int w(String msg, Throwable tr) {
		if (ENABLED)
			return Log.w(itsTag, msg, tr);
		return 0;
	}

	public int e(String msg) {
		if (ENABLED)
			return Log.e(itsTag, msg);
		return 0;
	}

	public int e(String msg, Throwable tr) {
		if (ENABLED)
			return Log.e(itsTag, msg, tr);
		return 0;
	}
}
